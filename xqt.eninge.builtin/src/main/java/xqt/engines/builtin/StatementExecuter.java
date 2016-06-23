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
import xqt.model.adapters.AdapterSelector;
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
import xqt.model.statements.StatementDescriptor;
import xqt.model.statements.StatementVisitor;
import xqt.model.statements.query.AnchorFeature;
import xqt.model.statements.query.SelectionFeature;
import xqt.model.statements.query.GroupFeature;
import xqt.model.statements.query.JoinedSelectDescriptor;
import xqt.model.statements.query.LimitFeature;
import xqt.model.statements.query.OrderFeature;
import xqt.model.statements.query.ProjectionFeature;
import xqt.model.statements.query.SelectDescriptor;
import xqt.model.statements.query.SetQualifierFeature;
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
    private HashMap<String, Object> auxiliaryData = new HashMap<>();
    private DefaultAdapterSelector adapterSelector;
    
    StatementExecuter(DefaultQueryEngine engine, Map<String, Variable> memory, HashMap<String, Object> auxiliaryData) throws Exception {
        this.engine = engine;
        this.memory = memory;
        this.auxiliaryData= auxiliaryData;
        adapterInfoContainer = AdapterInfoContainer.getInstance(engine.getConfigPaths());
        adapterSelector = new DefaultAdapterSelector(engine);
    }

    public QueryEngine getQueryEngine(){
    	return engine;
    }

    public AdapterSelector getAdapterSelector(){
    	return adapterSelector;
    }
    
    public HashMap<String, Object> getAuxiliaryData(){
    	return auxiliaryData;
    }
    
    public Resultset visit(StatementDescriptor statement) {
        // get the adapter object
        // call its run statement with the statement object, currently there is no capability matching
        // if the adapter had reported any lack of functionality, there is a compensation query attached to it.
        // in this case, first execute the master statement and then pass its result to the compensation query. The correct result
        // is the return value from the compensation query.
        //this.engine.setVariableValue("test", new Object());
        // the adapter has been chosen in the prepare phase and is assigned to the statement's execution info.
        //String adapterId = selectStatement.getSourceClause().getBinding().getConnection().getAdapterName();
    	Resultset result = null;
        LoggerHelper.logDebug(MessageFormat.format("Executing statement {0} on the {1} adapter.", statement.getId(), statement.getExecutionInfo().getAdapter().getAdapterInfo().getId()));

        long startExecutionTime = System.nanoTime();
        if(statement instanceof JoinedSelectDescriptor){
        	result = execute((JoinedSelectDescriptor)statement);
        }  else if(statement instanceof SelectDescriptor) {
        	result = execute((SelectDescriptor)statement);
        } 
        statement.getExecutionInfo().setExecutionTime(System.nanoTime() - startExecutionTime);
        
        LoggerHelper.logDebug(MessageFormat.format("Statement {0} was run successfully on the {1} adapter .", statement.getId(), statement.getExecutionInfo().getAdapter().getAdapterInfo().getId()));        
        return result;
    }

    private Resultset execute(SelectDescriptor statement) {
        DataAdapter adapter = statement.getExecutionInfo().getAdapter();
        Resultset result;
        result = adapter.run(statement, memory);
        
        if(statement.getComplementingStatement() != null){
            Variable var = new Variable();
            var.setExecutionInfo(statement.getExecutionInfo());
            //because of the compensation query, the target of the main one is a variable
            var.setName(((VariableContainer)statement.getTargetClause().getContainer()).getVariableName());
            var.setResult(result);

            DataAdapter compelementingAdapter = statement.getComplementingStatement().getExecutionInfo().getAdapter();
            result = compelementingAdapter.complement(statement.getComplementingStatement(), var);
            if(statement.getComplementingStatement().getComplementingStatement() != null) { // there is a target clause complementing statement
                DataAdapter adp = statement.getComplementingStatement().getComplementingStatement().getExecutionInfo().getAdapter();
                Variable var2 = new Variable();
                var2.setExecutionInfo(statement.getComplementingStatement().getExecutionInfo());
                //because of the compensation query, the target of the main one is a variable
                var2.setName(((VariableContainer)statement.getComplementingStatement().getTargetClause().getContainer()).getVariableName());
                var2.setResult(result);
                result = compelementingAdapter.complement(statement.getComplementingStatement().getComplementingStatement(), var2);
                var2.setExecutionInfo(null);
                statement.getClauses().remove(statement.getTargetClause().getType());
                statement.addClause(statement.getComplementingStatement().getComplementingStatement().getTargetClause());
            } else {
            	statement.getClauses().remove(statement.getTargetClause().getType());
            	statement.addClause(statement.getComplementingStatement().getTargetClause());                
            }
            var.setExecutionInfo(null); // remove the variable, its temporary and not needed anymore
        }
        return result;
	}

    private Resultset execute(JoinedSelectDescriptor statement) {
    	// The side queries are already executed, by the JoinedSelectDescriptor.accpet method.
       	// execute the main join query
    	DataAdapter adapter = statement.getExecutionInfo().getAdapter();
        Resultset result;
        result = adapter.run(statement, memory);
        return result;
    }
    
    
	//@Override
    public void prepare_delete(SelectDescriptor select) { // to be deleted
        if(select.hasError())
            return;
        ExecutionInfo eix = new ExecutionInfo();
        select.setExecutionInfo(eix);
        eix.setExecuted(false);

        HashMap<String, Object> context = new HashMap<>();
        context.put("processFolder", engine.getProcessPath());
        context.put("applicationFolder", engine.getApplicationPath());

        if(numberOfAdaptersInvolved_delete(select) > 1){
        	// its a heterogeneous join. check if left adapter accepts join, choose the left adapter, otherwise
        	// break the query to a composition join and two side queries, left and right.
        	rewriteJoinedSelect(select);
        } else {
            DataAdapter adapter = chooseAdapter_delete(select); // create the adapter based on its registration info and the statement's binding info
            eix.setAdapter(adapter);
            if(!adapter.hasRequiredCapabilities(select)){
                buildComplementingStatement_delete(select);
            }
            // check if the original query's target clause is a persistent data source, and the original adapter supports writing to data containers
            // create another complementing query over the first complementing query to delegate the write to the original adapter
            select.getExecutionInfo().getAdapter().prepare(select, context); // creates the source files but does not compile them 
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
        LoggerHelper.logDebug(MessageFormat.format("Statement '{0}' passed the preparation phase.", select.getId()));                
    }
    
    private void rewriteJoinedSelect(SelectDescriptor select) {
		// rewrite the select query to a composition join and two side queries.
    	// the composition join query depends upon the left and right side queries.
    	// set proper adapters for each query
        boolean specialCase = false;
        if(select.getTargetClause().getContainer().getDataContainerType() == DataContainer.DataContainerType.Single){
            specialCase = true;
        }        
    	JoinedContainer joinedSource = (JoinedContainer)select.getSourceClause().getContainer();

		
	}

	private int numberOfAdaptersInvolved_delete(SelectDescriptor select) {
    	JoinedContainer joinedSource = null;
    	try{
    		joinedSource = (JoinedContainer)select.getSourceClause().getContainer();
    	} catch(Exception ex){
    		return 1; // it should be a single container select
    	}
        if(joinedSource.getLeftContainer().getDataContainerType() == DataContainer.DataContainerType.Variable){ // both sides are variable
        	return 1;
        } else if(joinedSource.getLeftContainer().getDataContainerType() == DataContainer.DataContainerType.Single){ // both are single containers
            // the single containers should use a same adapter.
            String leftAdapterCode = ((SingleContainer)joinedSource.getLeftContainer()).getBinding().getConnection().getAdapterName();
            String rightAdapterCode = ((SingleContainer)joinedSource.getRightContainer()).getBinding().getConnection().getAdapterName();
            if(!leftAdapterCode.equalsIgnoreCase(rightAdapterCode)) {
            	// get the left adapter and ask whether it supports JOINS            	
            	return 2;
            } else { // check the dialects
                String leftAdapterDialect = getConnectionDialect((SingleContainer)joinedSource.getLeftContainer());                    
                String rightAdapterDialect = getConnectionDialect((SingleContainer)joinedSource.getRightContainer());
                if(!leftAdapterDialect.equalsIgnoreCase(rightAdapterDialect)) {
                	return 2;
                } else{
                	return 1;
                }
            }
        }
        return 2; // two containers one variable one single
	}

	private DataAdapter chooseAdapter_delete(SelectDescriptor select) {
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
                            AdapterInfo adapterInfo = adapterInfoContainer.getAdapterInfo(adapterType); // handle not found exception
                            adapter = adapterInfo.load(adapterDialect, engine.getClassLoader(), engine.getConfigPaths());
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
                try {
                    String adapterType = ((SingleContainer)select.getSourceClause().getContainer()).getBinding().getConnection().getAdapterName();
                    AdapterInfo adapterInfo = adapterInfoContainer.getAdapterInfo(adapterType); // Handle not found exception
                    String adapterDialect = getConnectionDialect((SingleContainer)select.getSourceClause().getContainer());
                    adapter = adapterInfo.load(adapterDialect, engine.getClassLoader(), engine.getConfigPaths());
                    adapter.setup(null); // pass the configuration information. they are in the connection object associated to the select
                    adapter.setAdapterInfo(adapterInfo);
                    return adapter;
                }
                catch (Exception ex) {
                    select.getLanguageExceptions().add(
                        LanguageExceptionBuilder.builder()
                            .setMessageTemplate("Could not load an adapter for statement '" + select.getId() + "'. " + ex.getMessage())
                            //.setContextInfo1(select.getId())
                            .setLineNumber(select.getSourceClause().getParserContext().getStart().getLine())
                            .setColumnNumber(-1)
                            .build()
                    );                        
                }
            }
            // this function must not see this part of the code! joins cause multiple adapters for their side containers!
            // Adapters that support JOINS should return the adapter of the left side, otherwise the query should have been rewritten.
            case Joined: 
            {
            	try {
                JoinedContainer joinedSource = (JoinedContainer)select.getSourceClause().getContainer();
                // both sides are variable, so the default variable is returned
                if(joinedSource.getLeftContainer().getDataContainerType() == DataContainer.DataContainerType.Variable){
                    adapter = new DefaultDataAdapter();  
                    adapter.setup(null);
                    adapter.setAdapterInfo(adapterInfoContainer.getDefultAdapter());
                    return adapter;
                }
                
                String leftAdapterCode = ((SingleContainer)joinedSource.getLeftContainer()).getBinding().getConnection().getAdapterName();
                AdapterInfo adapterInfo = adapterInfoContainer.getAdapterInfo(leftAdapterCode);
                String adapterDialect = getConnectionDialect((SingleContainer)joinedSource.getLeftContainer());//.getBinding().getConnection().getParameters().getOrDefault("dialect", ConnectionParameterDescriptor.createEmpty()).getValue();                    
                adapter = adapterInfo.load(adapterDialect, engine.getClassLoader(), engine.getConfigPaths());
                adapter.setup(null); // pass the configuration information. they are in the connection object associated to the select
                adapter.setAdapterInfo(adapterInfo);
                if(adapter.isSupported("select.source.joined")){
                	return adapter;
                } else {                
                    select.getLanguageExceptions().add(
                        LanguageExceptionBuilder.builder()
                            .setMessageTemplate("Not supported Heterogeneous JOIN. Left and right containers of the JOIN could not be rewritten.")
                            .setLineNumber(select.getSourceClause().getParserContext().getStart().getLine())
                            .setColumnNumber(-1)
                            .build()
                    );                                            
                }
            }
            catch (Exception ex) {
                select.getLanguageExceptions().add(
                    LanguageExceptionBuilder.builder()
                        .setMessageTemplate("Could not load an adapter for statement '" + select.getId() + "'. " + ex.getMessage())
                        //.setContextInfo1(select.getId())
                        .setLineNumber(select.getSourceClause().getParserContext().getStart().getLine())
                        .setColumnNumber(-1)
                        .build()
                );                        
            }

            }
            default:
                return null;
        }
    }    

    private String getConnectionDialect(SingleContainer container){
        return container.getBinding().getConnection().getParameterValue("dialect", "default").getValue();
    }
    
    private SelectDescriptor buildComplementingStatement_delete(SelectDescriptor select) {
        // check which capabilities are missing and check whether they are supported by the completing adapter?
        // check the dependencies between the missing capabilities
        // build a completing query
        // adopt the main query to the changes.
        boolean specialCase = false;
        if(select.getSourceClause().getContainer().getDataContainerType() == DataContainer.DataContainerType.Variable
                && select.getTargetClause().getContainer().getDataContainerType() == DataContainer.DataContainerType.Single){
            specialCase = true;
        }        
        
        SelectDescriptor comp = new SelectDescriptor();
        comp.setId(select.getId() + "_CMPL1");
        comp.setDependsUpon(select);
        select.setComplementingStatement(comp);
        
        comp.addClause(new SetQualifierFeature());
        // commented on 15.06.15 because I think a canonic perspective made form the select's perspective would do the job better
        //comp.addClause(select.getProjectionClause()); //the comp. query uses the main's projection
        ProjectionFeature   projection       = new ProjectionFeature();
        projection.setPerspective(select.getProjectionClause().getPerspective().createCanonicPerspective());
        projection.setPresent(true);
        comp.addClause(projection);
        
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
        DataAdapter adapter = chooseAdapter_delete(comp); 
        comp.getExecutionInfo().setAdapter(adapter);
        
        if(specialCase){
            // also check for joins with both side variable
            // the comp adapter takes the adapter of the main, because it will run the write operation into the container
            
            // the main adapter now is totally working on memory, so needs the fallback adapter
            // its should also be possible to interchange the adapters between the main and the comp!
            select.getExecutionInfo().setAdapter(chooseAdapter_delete(select));
            
            // when the code reaches here, it means that the main query is now totally running in memory using the fallback adapter.
            // the fallback adapter is supposed to support all the clauses (except writing to adapter specific containers). 
            // by changing the main's adapter to the fallback, it will run without issues
            // the comp query simply accepts the main's output and writes it to the container, using the original adapter that was
            // attached to the main statement.
            return comp;
        }
        
    
        if(select.getAnchorClause().isPresent() && !select.getExecutionInfo().getAdapter().isSupported("select.anchor")){
            if(comp.getExecutionInfo().getAdapter().isSupported("select.anchor")){                
                comp.addClause(select.getAnchorClause());
                select.getClauses().remove(select.getAnchorClause().getType());
                select.addClause(new AnchorFeature());  // added an empty/neutral clause              
            }
        } else { // add default clauses to the compensation query
            comp.addClause(new AnchorFeature());
        }

        if(select.getFilterClause().isPresent() && !select.getExecutionInfo().getAdapter().isSupported("select.filter")){
            if(comp.getExecutionInfo().getAdapter().isSupported("select.filter")){
                comp.addClause(select.getFilterClause());
                select.getClauses().remove(select.getFilterClause().getType());
                select.addClause(new SelectionFeature());  // added an empty/neutral clause              
            }
        } else {
            comp.addClause(new SelectionFeature());
        }
        
        if(select.getOrderClause().isPresent() && !select.getExecutionInfo().getAdapter().isSupported("select.orderby")){
            if(comp.getExecutionInfo().getAdapter().isSupported("select.orderby")){
                comp.addClause(select.getOrderClause());
                select.getClauses().remove(select.getOrderClause().getType());
                select.addClause(new OrderFeature());  // added an empty/neutral clause              
            }
        } else {
            comp.addClause(new OrderFeature());
        }

        if(select.getGroupClause().isPresent() && !select.getExecutionInfo().getAdapter().isSupported("select.groupby")){
            if(comp.getExecutionInfo().getAdapter().isSupported("select.groupby")){
                comp.addClause(select.getGroupClause());
                select.getClauses().remove(select.getGroupClause().getType());
                select.addClause(new GroupFeature());  // added an empty/neutral clause              
            }
        } else {
            comp.addClause(new GroupFeature());
        }
                
        if(select.getLimitClause().isPresent() && !select.getExecutionInfo().getAdapter().isSupported("select.limit")){
            if(comp.getExecutionInfo().getAdapter().isSupported("select.limit")){
                comp.addClause(select.getLimitClause());
                select.getClauses().remove(select.getLimitClause().getType());
                select.addClause(new LimitFeature());  // added an empty/neutral clause              
            }
        } else {
            comp.addClause(new LimitFeature());
        }
        
        return comp;
    }

}
