/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.engine.builtin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import xqt.adapters.builtin.DefaultDataAdapter;
import xqt.engine.QueryEngine;
import xqt.model.DataContainerDescriptor;
import xqt.model.adapters.AdapterInfo;
import xqt.model.adapters.AdapterInfoContainer;
import xqt.model.adapters.DataAdapter;
import xqt.model.data.Resultset;
import xqt.model.data.Variable;
import xqt.model.exceptions.LanguageExceptionBuilder;
import xqt.model.execution.ExecutionInfo;
import xqt.model.statements.StatementVisitor;
import xqt.model.statements.query.AnchorClause;
import xqt.model.statements.query.FilterClause;
import xqt.model.statements.query.GroupClause;
import xqt.model.statements.query.LimitClause;
import xqt.model.statements.query.OrderClause;
import xqt.model.statements.query.SelectDescriptor;
import xqt.model.statements.query.SetQualifierClause;
import xqt.model.statements.query.SourceClause;
import xqt.model.statements.query.TargetClause;

/**
 *
 * @author standard
 */
public class StatementExecuter implements StatementVisitor{
    private QueryEngine engine;
    private Map<String, Variable> memory;
    private AdapterInfoContainer adapterInfoContainer = null;
    public StatementExecuter(QueryEngine engine){
        this.engine = engine;
    }

    StatementExecuter(DefaultQueryEngine engine, Map<String, Variable> memory) {
        this.engine = engine;
        this.memory = memory;
        adapterInfoContainer = AdapterInfoContainer.getInstance();
    }

    @Override
    public Resultset visit(SelectDescriptor select) {
        // get the adapter object
        // call its run statement with the statement object, currently there is no capability matching
        // if the adapater had reported any lack of functionlity, there is a compensation query attached to it.
        // in this case, first execute the master statement and then pass its result to the compensation query. The correct result
        // is the return value from the compensation query.
        //this.engine.setVariableValue("test", new Object());
        // the adapater has been chosen in the prepare phase and is assigned to the statement's execution info.
        //String adapterId = selectStatement.getSourceClause().getBinding().getConnection().getAdapterName();
        
        DataAdapter adapter = select.getExecutionInfo().getAdapter();
        Resultset result;
        if(adapter.needsMemory()){
            result = adapter.run(select, memory);
        } else {
            result = adapter.run(select, null);
        }
        if(select.getCompensationStatement() != null){
            Variable var = new Variable();
            var.setExecutionInfo(select.getExecutionInfo());
            var.setName(select.getTargetClause().getVariableName());
            var.setResult(result);

            DataAdapter compensationAdapter = select.getCompensationStatement().getExecutionInfo().getAdapter();
            result = compensationAdapter.compensate(select.getCompensationStatement(), var);
            var.setExecutionInfo(null); // remove the variable, its temporary and not needed anymore
            select.getClauses().remove(select.getTargetClause().getType());
            select.addClause(select.getCompensationStatement().getTargetClause());
        }
        return result;
    }

    @Override
    public void prepare(SelectDescriptor select) {
        ExecutionInfo eix = new ExecutionInfo();
        select.setExecutionInfo(eix);
        eix.setExecuted(false);
        DataAdapter adapter = chooseAdapter(select); // create the adapter based on its registration info and the statement's bindinf info
        eix.setAdapter(adapter);
        adapter.prepare(select); // creates the source files but does not compile them 
        if(!adapter.hasRequiredCapabilities(select)){
            SelectDescriptor comp = buildCompensationStatement(select);
            comp.getExecutionInfo().getAdapter().prepare(comp);
        }
    }
    
    private static HashMap<String, DataAdapter> loadedAdapters = new HashMap<>();
    
    private DataAdapter chooseAdapter(SelectDescriptor select) {
        // use the AdpaterInfo.getRegisteredAdapterInfos to access the information about the registered adapters
        if(select.getSourceClause().getDataContainerType() == DataContainerDescriptor.DataContainerType.Variable){
            //if(!loadedAdapters.containsKey("Default")) {
                // when the adapters are cached, their linked builders are also cached! which keep their previous state: attributes, where...
                // this causes the second call to generate invalid files!! solve it first and the cache the adapters
                DataAdapter adapter = new DefaultDataAdapter();  
                adapter.setup(null);
                //loadedAdapters.put("Default", adapter);
                return adapter; // when caching is enabled, remove this line
            //}
            //return loadedAdapters.get("Default"); // caching of the adapters currently works but the internal builder of the adpater is not in a proper state.
        }
        try {
            //if(!loadedAdapters.containsKey("CSV")) {// read the key from the select statement's associated connection
                // read the adapter info from the adapters config file in the applications installation folder
                AdapterInfo adapterInfo = adapterInfoContainer.getRegisteredAdapterInfos().stream().filter(p->p.getId().equals("CSV")).findFirst().get(); // hande not found exception
                ClassLoader classLoader = new URLClassLoader(new URL[]{new URL(adapterInfo.getLocationType() + ":" + adapterInfo.getLocation())});
                Class cl = classLoader.loadClass(adapterInfo.getMainNamespace() + "." + adapterInfo.getMainClassName());
                Constructor<?> ctor = cl.getConstructor();
                ctor.setAccessible(true);
                DataAdapter adapter = (DataAdapter)ctor.newInstance();
                adapter.setup(null); // pass the configuration information. they are in the connection object associated to the select
                //loadedAdapters.put("CSV", adapter);
                return adapter;
            //}
            //return loadedAdapters.get("CSV");
        }
        catch (MalformedURLException | ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            select.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate("Could not load the adapter for '" + "CSV" + "'. " + ex.getMessage())
                    //.setContextInfo1(select.getId())
                    .setLineNumber(select.getSourceClause().getParserContext().getStart().getLine())
                    .setColumnNumber(-1)
                    .build()
            );                        
        }  
        return null;
    }    

    private SelectDescriptor buildCompensationStatement(SelectDescriptor select) {
        // check which capabilities are missing and chech whether they are suppotrted by the compensation adapter?
        // check the dependecies between the missing capabilities
        // build a compensation query
        // adopt the main query to the changes.
        
        // for now just the LIMIT clause is compensated. later the PLOT should be considered and so on.
        
        SelectDescriptor comp = new SelectDescriptor();
        comp.setDependsUpon(select);
        select.setCompensationStatement(comp);
        
        comp.addClause(new SetQualifierClause());
        comp.addClause(select.getProjectionClause()); //the comp. query uses the main's projection
        
        // create a source of type variable and name it as "Tempvar"+select.id+ time.ticks
        String variableName = "TempVar_" + select.getId() +  "_" + System.currentTimeMillis();
        comp.addClause(SourceClause.createVariableSource(variableName));
        comp.addClause(select.getTargetClause());
        //replace the main's target clause with the temp var, so that the main query puts the result in the tempvar.
        // the temp var should be deleted after the query is executed.
        
        // if there is any non supprted capability, the target clause will be compensated automatically. this includes the case when only the target clause 
        // is not supported.
        // speciall care is needed for the target clauses that persist resultsets into an external media! the default adapter may not know how to perform it.
        select.getClauses().remove(select.getTargetClause().getType());
        select.addClause(TargetClause.createVariableTarget(variableName));
        
        ExecutionInfo executionInfo = new ExecutionInfo();
        comp.setExecutionInfo(executionInfo);
        executionInfo.setExecuted(false);
        DataAdapter adapter = chooseAdapter(comp); // this call must be made after setting the source clause, because the choose adpater function needs to know the source clause
        executionInfo.setAdapter(adapter);

        if(select.getAnchorClause().isIsPresent() && !select.getExecutionInfo().getAdapter().isSupported("select.anchor")){
            if(comp.getExecutionInfo().getAdapter().isSupported("select.anchor")){                
                comp.addClause(select.getAnchorClause());
                select.getClauses().remove(select.getAnchorClause().getType());
                select.addClause(new AnchorClause());  // added an empty/neutral clause              
            }
        } else { // add default clauses to the compensation query
            comp.addClause(new AnchorClause());
        }

        if(select.getFilterClause().isIsPresent() && !select.getExecutionInfo().getAdapter().isSupported("select.filter")){
            if(comp.getExecutionInfo().getAdapter().isSupported("select.filter")){
                comp.addClause(select.getFilterClause());
                select.getClauses().remove(select.getFilterClause().getType());
                select.addClause(new FilterClause());  // added an empty/neutral clause              
            }
        } else {
            comp.addClause(new FilterClause());
        }
        
        if(select.getOrderClause().isIsPresent() && !select.getExecutionInfo().getAdapter().isSupported("select.orderby")){
            if(comp.getExecutionInfo().getAdapter().isSupported("select.orderby")){
                comp.addClause(select.getOrderClause());
                select.getClauses().remove(select.getOrderClause().getType());
                select.addClause(new OrderClause());  // added an empty/neutral clause              
            }
        } else {
            comp.addClause(new OrderClause());
        }

        if(select.getGroupClause().isIsPresent() && !select.getExecutionInfo().getAdapter().isSupported("select.groupby")){
            if(comp.getExecutionInfo().getAdapter().isSupported("select.groupby")){
                comp.addClause(select.getGroupClause());
                select.getClauses().remove(select.getGroupClause().getType());
                select.addClause(new GroupClause());  // added an empty/neutral clause              
            }
        } else {
            comp.addClause(new GroupClause());
        }
                
        if(select.getLimitClause().isIsPresent() && !select.getExecutionInfo().getAdapter().isSupported("select.limit")){
            if(comp.getExecutionInfo().getAdapter().isSupported("select.limit")){
                comp.addClause(select.getLimitClause());
                select.getClauses().remove(select.getLimitClause().getType());
                select.addClause(new LimitClause());  // added an empty/neutral clause              
            }
        } else {
            comp.addClause(new LimitClause());
        }
        
        // update/ enhance MemReader.it
        return comp;
    }

}
