/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.engines.builtin;

import com.vaiona.commons.logging.LoggerHelper;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import xqt.adapters.builtin.DefaultDataAdapter;
import xqt.engine.QueryEngine;
import xqt.model.adapters.AdapterInfo;
import xqt.model.adapters.AdapterInfoContainer;
import xqt.model.adapters.DataAdapter;
import xqt.model.configurations.ConnectionParameterDescriptor;
import xqt.model.containers.DataContainer;
import xqt.model.containers.JoinedContainer;
import xqt.model.containers.SingleContainer;
import xqt.model.containers.VariableContainer;
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

    StatementExecuter(DefaultQueryEngine engine, Map<String, Variable> memory) throws Exception {
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
        LoggerHelper.logDebug(MessageFormat.format("Executing statement {0} on the {1} adapter.", select.getId(), adapter.getAdapterInfo().getId()));

        result = adapter.run(select, memory);
//        if(adapter.needsMemory()){
//            result = adapter.run(select, memory);
//        } else {
//            result = adapter.run(select, null);
//        }
        if(select.getComplementingStatement() != null){
            Variable var = new Variable();
            var.setExecutionInfo(select.getExecutionInfo());
            //because of the compensation query, the target of the main one is a variable
            var.setName(((VariableContainer)select.getTargetClause().getContainer()).getVariableName());
            var.setResult(result);

            DataAdapter compelementingAdapter = select.getComplementingStatement().getExecutionInfo().getAdapter();
            result = compelementingAdapter.complement(select.getComplementingStatement(), var);
            if(select.getComplementingStatement().getComplementingStatement() != null) { // there is a target clause complementing statement
                DataAdapter adp = select.getComplementingStatement().getComplementingStatement().getExecutionInfo().getAdapter();
                Variable var2 = new Variable();
                var2.setExecutionInfo(select.getComplementingStatement().getExecutionInfo());
                //because of the compensation query, the target of the main one is a variable
                var2.setName(((VariableContainer)select.getComplementingStatement().getTargetClause().getContainer()).getVariableName());
                var2.setResult(result);
                result = compelementingAdapter.complement(select.getComplementingStatement().getComplementingStatement(), var2);
                var2.setExecutionInfo(null);
                select.getClauses().remove(select.getTargetClause().getType());
                select.addClause(select.getComplementingStatement().getComplementingStatement().getTargetClause());
            } else {
                select.getClauses().remove(select.getTargetClause().getType());
                select.addClause(select.getComplementingStatement().getTargetClause());                
            }
            var.setExecutionInfo(null); // remove the variable, its temporary and not needed anymore
        }
        LoggerHelper.logDebug(MessageFormat.format("Statement {0} was run successfully on the {1} adapter .", select.getId(), adapter.getAdapterInfo().getId()));        
        return result;
    }

    @Override
    public void prepare(SelectDescriptor select) {
        if(select.hasError())
            return;
        ExecutionInfo eix = new ExecutionInfo();
        select.setExecutionInfo(eix);
        eix.setExecuted(false);
        DataAdapter adapter = chooseAdapter(select); // create the adapter based on its registration info and the statement's bindinf info
        eix.setAdapter(adapter);
        if(!adapter.hasRequiredCapabilities(select)){
            SelectDescriptor comp = buildComplementingStatement(select);
            // check if the orginal query's target clause is a persistent data source, and the origianl adapter supports wirting to data containers
            // create another complementing query over the first complementing query to delegate the write to the original adapter
            
        }
        select.getExecutionInfo().getAdapter().prepare(select, null); // creates the source files but does not compile them 
        LoggerHelper.logDebug(MessageFormat.format("Checkpoint: StatementExecuter.prepare {0}.", 1));                
        if(select.hasError()) // check after lazy construction and validations
            return;
        if(select.getComplementingStatement() != null){
            SelectDescriptor comp = select.getComplementingStatement();
            comp.getExecutionInfo().getAdapter().prepare(comp, null);      
            if(comp.getComplementingStatement() != null){
                comp.getComplementingStatement().getExecutionInfo().getAdapter().prepare(comp.getComplementingStatement(), comp);
            }
        }
    }
    
    public void prepare_del(SelectDescriptor select) {
        if(select.hasError())
            return;
        ExecutionInfo eix = new ExecutionInfo();
        select.setExecutionInfo(eix);
        eix.setExecuted(false);
        DataAdapter adapter = chooseAdapter(select); // create the adapter based on its registration info and the statement's bindinf info
        eix.setAdapter(adapter);
        adapter.prepare(select, null); // creates the source files but does not compile them 
        if(select.hasError()) // check after lazy construction and validations
            return;
        if(!adapter.hasRequiredCapabilities(select)){
            SelectDescriptor comp = buildComplementingStatement(select);
            comp.getExecutionInfo().getAdapter().prepare(comp, null);      
            if(comp.getComplementingStatement() != null){
                comp.getComplementingStatement().getExecutionInfo().getAdapter().prepare(comp.getComplementingStatement(), comp);
            }
            // check if the orginal query's target clause is a persistent data source, and the origianl adapter supports wirting to data containers
            // create another complementing query over the first complementing query to delegate the write to the original adapter
            
        }
    }
    
    //private static HashMap<String, DataAdapter> loadedAdapters = new HashMap<>();
    
    private DataAdapter chooseAdapter(SelectDescriptor select) {
        DataAdapter adapter = null;
        // use the AdpaterInfo.getRegisteredAdapterInfos to access the information about the registered adapters
        switch (select.getSourceClause().getContainer().getDataContainerType()) {
            case Variable:
            {
                 switch (select.getTargetClause().getContainer().getDataContainerType()) {
                     case Single: // in this case a memory variable should be written to a persistent container, hence the associated adapter should take the statement
                     {
                        String adapterType = ((SingleContainer)select.getTargetClause().getContainer()).getBinding().getConnection().getAdapterName();
                        String adapterDialect = getConnectionDialect((SingleContainer)select.getTargetClause().getContainer());
                        try {
                            AdapterInfo adapterInfo = adapterInfoContainer.getAdapterInfo(adapterType); // hande not found exception
                            adapter = adapterInfo.load(adapterDialect);
                            adapter.setup(null); // pass the configuration information. they are in the connection object associated to the select
                            adapter.setAdapterInfo(adapterInfo);
                            return adapter;
                        }
                        catch (Exception ex) {
                            select.getLanguageExceptions().add(
                                LanguageExceptionBuilder.builder()
                                    .setMessageTemplate("Could not load the adapter for '" + adapterType + "'. " + ex.getMessage())
                                    .setLineNumber(select.getSourceClause().getParserContext().getStart().getLine())
                                    .setColumnNumber(-1)
                                    .build()
                            );                        
                        }
                     }
                     default:
                        adapter = new DefaultDataAdapter();  
                        adapter.setup(null);
                        adapter.setAdapterInfo(adapterInfoContainer.getDefultAdapter());
                        return adapter; // when caching is enabled, remove this line
                 }
            }
            case Single:
            {
                String adapterType = ((SingleContainer)select.getSourceClause().getContainer()).getBinding().getConnection().getAdapterName();
                try {
                    AdapterInfo adapterInfo = adapterInfoContainer.getAdapterInfo(adapterType); // hande not found exception
                    String adapterDialect = getConnectionDialect((SingleContainer)select.getSourceClause().getContainer());
                    adapter = adapterInfo.load(adapterDialect);
                    adapter.setup(null); // pass the configuration information. they are in the connection object associated to the select
                    adapter.setAdapterInfo(adapterInfo);
                    return adapter;
                }
                catch (Exception ex) {
                    select.getLanguageExceptions().add(
                        LanguageExceptionBuilder.builder()
                            .setMessageTemplate("Could not load the adapter for '" + adapterType + "'. " + ex.getMessage())
                            //.setContextInfo1(select.getId())
                            .setLineNumber(select.getSourceClause().getParserContext().getStart().getLine())
                            .setColumnNumber(-1)
                            .build()
                    );                        
                }
            }
            case Joined:
            {
                JoinedContainer joinedSource = (JoinedContainer)select.getSourceClause().getContainer();
                if(joinedSource.getLeftContainer().getDataContainerType() != joinedSource.getRightContainer().getDataContainerType()){
                    select.getLanguageExceptions().add(
                        LanguageExceptionBuilder.builder()
                            .setMessageTemplate("Left and right containers of the JOIN should be of same type.")
                            .setLineNumber(select.getSourceClause().getParserContext().getStart().getLine())
                            .setColumnNumber(-1)
                            .build()
                    ); 
                } else if(joinedSource.getLeftContainer().getDataContainerType() == DataContainer.DataContainerType.Variable){ // both sides are variable
                    adapter = new DefaultDataAdapter();  
                    adapter.setup(null);
                    adapter.setAdapterInfo(adapterInfoContainer.getDefultAdapter());
                    return adapter;
                } else if(joinedSource.getLeftContainer().getDataContainerType() == DataContainer.DataContainerType.Single){ // both are single containers
                    // the single containers should use a same adapter.
                    String leftAdapterCode = ((SingleContainer)joinedSource.getLeftContainer()).getBinding().getConnection().getAdapterName();
                    String rightAdapterCode = ((SingleContainer)joinedSource.getRightContainer()).getBinding().getConnection().getAdapterName();
                    if(!leftAdapterCode.equalsIgnoreCase(rightAdapterCode)) {
                        select.getLanguageExceptions().add(
                            LanguageExceptionBuilder.builder()
                                .setMessageTemplate("Left and right containers of the JOIN should use a same connection/ adapter.")
                                .setLineNumber(select.getSourceClause().getParserContext().getStart().getLine())
                                .setColumnNumber(-1)
                                .build()
                        );                         
                    } else { // can get the adapter info now and instantiate it.
                        try {
                            AdapterInfo adapterInfo = adapterInfoContainer.getAdapterInfo(leftAdapterCode);
                            String adapterDialect = getConnectionDialect((SingleContainer)joinedSource.getLeftContainer());//.getBinding().getConnection().getParameters().getOrDefault("dialect", ConnectionParameterDescriptor.createEmpty()).getValue();                    
                            adapter = adapterInfo.load(adapterDialect);
                            adapter.setup(null); // pass the configuration information. they are in the connection object associated to the select
                            adapter.setAdapterInfo(adapterInfo);
                            return adapter;
                        }
                        catch (Exception ex) {
                            select.getLanguageExceptions().add(
                                LanguageExceptionBuilder.builder()
                                    .setMessageTemplate("Could not load the adapter for '" + leftAdapterCode + "'. " + ex.getMessage())
                                    .setLineNumber(select.getSourceClause().getParserContext().getStart().getLine())
                                    .setColumnNumber(-1)
                                    .build()
                            );                        
                        }                        
                    }
                }
            }
            default: // joined is not yet supported
                return null;
        }
    }    

    private String getConnectionDialect(SingleContainer container){
        return container.getBinding().getConnection().getParameters().getOrDefault("dialect", ConnectionParameterDescriptor.createEmpty()).getValue();
    }
    
    private SelectDescriptor buildComplementingStatement(SelectDescriptor select) {
        // check which capabilities are missing and check whether they are suppotrted by the completing adapter?
        // check the dependecies between the missing capabilities
        // build a completing query
        // adopt the main query to the changes.
        boolean specialCase = false;
        if(select.getSourceClause().getContainer().getDataContainerType() == DataContainer.DataContainerType.Variable
                && select.getTargetClause().getContainer().getDataContainerType() == DataContainer.DataContainerType.Single){
            specialCase = true;
        }        
        
        SelectDescriptor comp = new SelectDescriptor();
        comp.setDependsUpon(select);
        select.setComplementingStatement(comp);
        
        comp.addClause(new SetQualifierClause());
        comp.addClause(select.getProjectionClause()); //the comp. query uses the main's projection
        
        // create a source of type variable and name it as "Tempvar"+select.id+ time.ticks
        String variableName = "TempVar_" + select.getId() +  "_" + System.currentTimeMillis();
        SourceClause source = new SourceClause();
        source.setContainer(new VariableContainer(variableName));
        
        comp.addClause(source);
        comp.addClause(select.getTargetClause());
        //replace the main's target clause with the temp var, so that the main query puts the result in the tempvar.
        // the temp var should be deleted after the query is executed.
        
        // the target clause will be compensated anyway! this includes the case when only the target clause 
        // is not supported.
        // special care is needed for the target clauses that persist resultsets into an external media! the default adapter may not know how to perform it.
        select.getClauses().remove(select.getTargetClause().getType());
        TargetClause target = new TargetClause();
        target.setContainer(new VariableContainer(variableName));
        select.addClause(target);

        ExecutionInfo executionInfo = new ExecutionInfo();
        comp.setExecutionInfo(executionInfo);
        executionInfo.setExecuted(false);
        DataAdapter adapter = chooseAdapter(comp); 
        comp.getExecutionInfo().setAdapter(adapter);
        
        if(specialCase){
            // also check for joins with both side variable
            // the comp adapter takes the adapter of the main, because it will run the write operation into the container
            
            // the main adapter now is totally working on memeory, so needs the fallback adapter
            // its hould also be possible to interchange the adpaters between the main and the comp!
            select.getExecutionInfo().setAdapter(chooseAdapter(select));
            
            // when the code reachs here, it means that the main query is now totally runing in memory using the fallback adpater.
            // the fallback adapter is suposed wo support all the clauses (except writing to adapater specific containers). 
            // by chaning the main's adapter to the fallback, it will run without issues
            // the comp query simply acepts the main's output and writes it to the container, using the original adapter that was
            // attached to the main statement.
            return comp;
        }
        
    
        if(select.getAnchorClause().isPresent() && !select.getExecutionInfo().getAdapter().isSupported("select.anchor")){
            if(comp.getExecutionInfo().getAdapter().isSupported("select.anchor")){                
                comp.addClause(select.getAnchorClause());
                select.getClauses().remove(select.getAnchorClause().getType());
                select.addClause(new AnchorClause());  // added an empty/neutral clause              
            }
        } else { // add default clauses to the compensation query
            comp.addClause(new AnchorClause());
        }

        if(select.getFilterClause().isPresent() && !select.getExecutionInfo().getAdapter().isSupported("select.filter")){
            if(comp.getExecutionInfo().getAdapter().isSupported("select.filter")){
                comp.addClause(select.getFilterClause());
                select.getClauses().remove(select.getFilterClause().getType());
                select.addClause(new FilterClause());  // added an empty/neutral clause              
            }
        } else {
            comp.addClause(new FilterClause());
        }
        
        if(select.getOrderClause().isPresent() && !select.getExecutionInfo().getAdapter().isSupported("select.orderby")){
            if(comp.getExecutionInfo().getAdapter().isSupported("select.orderby")){
                comp.addClause(select.getOrderClause());
                select.getClauses().remove(select.getOrderClause().getType());
                select.addClause(new OrderClause());  // added an empty/neutral clause              
            }
        } else {
            comp.addClause(new OrderClause());
        }

        if(select.getGroupClause().isPresent() && !select.getExecutionInfo().getAdapter().isSupported("select.groupby")){
            if(comp.getExecutionInfo().getAdapter().isSupported("select.groupby")){
                comp.addClause(select.getGroupClause());
                select.getClauses().remove(select.getGroupClause().getType());
                select.addClause(new GroupClause());  // added an empty/neutral clause              
            }
        } else {
            comp.addClause(new GroupClause());
        }
                
        if(select.getLimitClause().isPresent() && !select.getExecutionInfo().getAdapter().isSupported("select.limit")){
            if(comp.getExecutionInfo().getAdapter().isSupported("select.limit")){
                comp.addClause(select.getLimitClause());
                select.getClauses().remove(select.getLimitClause().getType());
                select.addClause(new LimitClause());  // added an empty/neutral clause              
            }
        } else {
            comp.addClause(new LimitClause());
        }
        
        return comp;
    }

    private SelectDescriptor buildComplementingStatement_del(SelectDescriptor select) {
        // check which capabilities are missing and check whether they are suppotrted by the completing adapter?
        // check the dependecies between the missing capabilities
        // build a completing query
        // adopt the main query to the changes.
        
        SelectDescriptor comp = new SelectDescriptor();
        comp.setDependsUpon(select);
        select.setComplementingStatement(comp);
        
        comp.addClause(new SetQualifierClause());
        comp.addClause(select.getProjectionClause()); //the comp. query uses the main's projection
        
        // create a source of type variable and name it as "Tempvar"+select.id+ time.ticks
        String variableName = "TempVar_" + select.getId() +  "_" + System.currentTimeMillis();
        SourceClause source = new SourceClause();
        source.setContainer(new VariableContainer(variableName));
        
        comp.addClause(source);
        if(select.getTargetClause().getContainer().getDataContainerType() != DataContainer.DataContainerType.Single){
            comp.addClause(select.getTargetClause());
        } else { 
            // the target is a single continer. the fallback adapter is not able/allowed to write into a container
            // so the fallback mechanism, should create another inner statement to write the query result into the container
            // using the original select's adapter.
            TargetClause target2 = new TargetClause();
            target2.setContainer(new VariableContainer(variableName + "_2"));
            comp.addClause(target2);

            SelectDescriptor targetComp = new SelectDescriptor();
            targetComp.setDependsUpon(comp);
            comp.setComplementingStatement(targetComp);

            targetComp.addClause(new SetQualifierClause());
            targetComp.addClause(comp.getProjectionClause());            
            targetComp.addClause(new AnchorClause());
            targetComp.addClause(new FilterClause());
            targetComp.addClause(new OrderClause());
            targetComp.addClause(new GroupClause());
            targetComp.addClause(new LimitClause());
        
            SourceClause source2 = new SourceClause();
            source2.setContainer(new VariableContainer(variableName + "_2"));
            targetComp.addClause(source2);
            
            targetComp.addClause(select.getTargetClause());
            
            ExecutionInfo executionInfo = new ExecutionInfo();
            targetComp.setExecutionInfo(executionInfo);
            executionInfo.setExecuted(false);
            executionInfo.setAdapter(select.getExecutionInfo().getAdapter());            
        }
        //replace the main's target clause with the temp var, so that the main query puts the result in the tempvar.
        // the temp var should be deleted after the query is executed.
        
        // if there is any non supprted capability, the target clause will be compensated automatically. this includes the case when only the target clause 
        // is not supported.
        // speciall care is needed for the target clauses that persist resultsets into an external media! the default adapter may not know how to perform it.
        select.getClauses().remove(select.getTargetClause().getType());
        TargetClause target = new TargetClause();
        target.setContainer(new VariableContainer(variableName));
        select.addClause(target);
        
        ExecutionInfo executionInfo = new ExecutionInfo();
        comp.setExecutionInfo(executionInfo);
        executionInfo.setExecuted(false);
        DataAdapter adapter = chooseAdapter(comp); // this call must be made after setting the source clause, because the choose adpater function needs to know the source clause
        executionInfo.setAdapter(adapter);

        if(select.getAnchorClause().isPresent() && !select.getExecutionInfo().getAdapter().isSupported("select.anchor")){
            if(comp.getExecutionInfo().getAdapter().isSupported("select.anchor")){                
                comp.addClause(select.getAnchorClause());
                select.getClauses().remove(select.getAnchorClause().getType());
                select.addClause(new AnchorClause());  // added an empty/neutral clause              
            }
        } else { // add default clauses to the compensation query
            comp.addClause(new AnchorClause());
        }

        if(select.getFilterClause().isPresent() && !select.getExecutionInfo().getAdapter().isSupported("select.filter")){
            if(comp.getExecutionInfo().getAdapter().isSupported("select.filter")){
                comp.addClause(select.getFilterClause());
                select.getClauses().remove(select.getFilterClause().getType());
                select.addClause(new FilterClause());  // added an empty/neutral clause              
            }
        } else {
            comp.addClause(new FilterClause());
        }
        
        if(select.getOrderClause().isPresent() && !select.getExecutionInfo().getAdapter().isSupported("select.orderby")){
            if(comp.getExecutionInfo().getAdapter().isSupported("select.orderby")){
                comp.addClause(select.getOrderClause());
                select.getClauses().remove(select.getOrderClause().getType());
                select.addClause(new OrderClause());  // added an empty/neutral clause              
            }
        } else {
            comp.addClause(new OrderClause());
        }

        if(select.getGroupClause().isPresent() && !select.getExecutionInfo().getAdapter().isSupported("select.groupby")){
            if(comp.getExecutionInfo().getAdapter().isSupported("select.groupby")){
                comp.addClause(select.getGroupClause());
                select.getClauses().remove(select.getGroupClause().getType());
                select.addClause(new GroupClause());  // added an empty/neutral clause              
            }
        } else {
            comp.addClause(new GroupClause());
        }
                
        if(select.getLimitClause().isPresent() && !select.getExecutionInfo().getAdapter().isSupported("select.limit")){
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
