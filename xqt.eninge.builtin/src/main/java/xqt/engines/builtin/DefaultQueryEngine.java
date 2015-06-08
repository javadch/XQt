/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.engines.builtin;

import com.vaiona.commons.compilation.ClassCompiler;
import com.vaiona.commons.compilation.InMemoryCompiledObject;
import com.vaiona.commons.compilation.InMemorySourceFile;
import com.vaiona.commons.logging.LoggerHelper;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import xqt.engine.QueryEngine;
import xqt.model.ProcessModel;
import xqt.model.data.Variable;
import xqt.model.exceptions.LanguageExceptionBuilder;
import xqt.model.execution.ExecutionInfo;
import xqt.model.statements.StatementDescriptor;
import xqt.model.statements.StatementVisitor;
import xqt.model.statements.query.SelectDescriptor;

/**
 *
 * @author Javad Chamanara
 */
public class DefaultQueryEngine  implements QueryEngine{

    /** "memory" for variable/value pairs go here */
    private final Map<String, Variable> memory;    
    private ProcessModel model = null;
    ClassLoader classLoader = null;
    
    public DefaultQueryEngine(ProcessModel processModel){
        this.memory = new HashMap<>();
        model = processModel;
        // register connections, bindings and perspectives
        // create adapter objects, better to do it in the visit function of the executer class
        // set versioning schemes
        // try to see whether adapters are able to connect to data sources
        // pre-fetch adapters' capabilities
    }

    @Override
    public void setClassLoader(ClassLoader classLoader){
        this.classLoader = classLoader;
    }

    @Override
    public ClassLoader getClassLoader(){
        return classLoader != null? classLoader: this.getClass().getClassLoader();
    }
    
    @Override
    public Object getVariableValue(String variableName){
        if(this.memory.containsKey(variableName)){
            return (memory.get(variableName));
        }
        return (null);
    }

    @Override
    public Set<String> getVariableNames(){
        return this.memory.keySet();
    }
    
    @Override
    public List<Variable> getVariables(){
        return this.memory.values().stream().collect(Collectors.toList());
    }

    @Override
    public void deleteVariable(String variableName){
        this.memory.remove(variableName);
    }
    
    @Override
    public void addVariable(Variable variable){
        this.memory.put(variable.getName(), variable);
    }
    
    @Override
    public Boolean isCapableOf(String capabilityName){
        return (true);
    }

    @Override
    public Boolean isValid(String capabilityName){
        return(true);
    }

    @Override
    public ProcessModel getProcessModel(){
        return model;
    }
    
   /*
    * Executes each statement in the model. Every statement should hold its result and
    * set a flag showing it is executed.
    * Assignments statements are put into the memory of the query engine
    * There will be a CLEAR and CLEAR ID statements in the grammar which clear the whole memory or a single entry
    * In a more general sense, sme of the statements are executed against the query engine itself not the data source. adapter
    * for example, a statement may try to use a variable already in the memory to run a function on it!
    * All these information should be passed to the execute method of the statement in question!
    * Model objects can serve as the CONTRACT between different layers i.e., Grammar, Engine and Adapter.
    */

    @Override
    public ExecutionInfo execute(Integer statementDescriptorId){
        // check all the dependencies first and execute them if required, 
        // also invalidate the followers
        try{
            StatementDescriptor sd;
            sd = model.getStatement(statementDescriptorId);
            StatementVisitor visitor = new StatementExecuter(this); // 
            sd.accept(visitor);
            if(sd.getExecutionInfo().isExecuted() && sd.getExecutionInfo().getVariable() != null)
                this.memory.put(sd.getExecutionInfo().getVariable().getName(), sd.getExecutionInfo().getVariable());
            return sd.getExecutionInfo(); //sd.getResult();
        }
        catch(Exception ex){
            // record, report error
            // update the statement result
            return null; // replace with a proper exception
        }        
    }

    @Override
    public void execute() {
        try {
            // the statements should be executed in the process order,
            // but the map does not guarantee the order!
            // TAKE CARE and change the code // <<<<<<<<<<<<<<<<<<<<<<<!!!!!!!!!!!!!!!!!
            // take a look at linked map, SortedMap
            // as the statementIds are ascending, unique and key of the map, maybe sortedmap solves the issue
            // but LinkedHashMap guarantees the insertion order without relying on the meaningfulness of the Ids
            StatementVisitor visitor = new StatementExecuter(this, memory);
            LinkedHashMap<String, InMemorySourceFile> sourcesToBeCompiled = new LinkedHashMap<>();
            List<StatementDescriptor> erroneousStatements = new ArrayList<>();
            for(StatementDescriptor sm: model.getStatements().values()){
                // pass the required information without binding too much to the structure of the statement
                // or needing too much knowledge about the statement!
                if(sm.hasError()) {
                    erroneousStatements.add(sm); // mark and avoid executing the statements with errors
                } else {
                    if(erroneousStatements.contains(sm.getDependsUpon())) {
                        erroneousStatements.add(sm); // avoid executing statements that are dependent upon the errorenous statements and also mark them
                    } else {
                        sm.prepare(visitor); // after this step, the sources are ready, if any. so add them to a collection and comiple all of them at once
                        if(sm.getDependsUpon() != null && sm instanceof SelectDescriptor) {
                            // if the statement is depending upon a statement that its perspective was lazily created, it should be double checked
                            // to see whether it is still valid (attributes used in various clauses should be in the perspective)
                            SelectDescriptor select = (SelectDescriptor)sm;
                            // the perspective should be linked to the master one, so just set the present status
                            select.getProjectionClause().setPresent(((SelectDescriptor)select.getDependsUpon()).getProjectionClause().isPresent());
                            select.validate();
                        }
                        // in case of implicit perspectives, the prepare method goes through the data source to consruct a perspective and checks the validity of the select statement
                        // so there is a chance to find some semantic errors here! and avoid executing the statement.
                        if(sm.hasError()){
                            erroneousStatements.add(sm); 
                            LoggerHelper.logError(MessageFormat.format("The statement {0} has some errors.", sm.getId()));
                        } else {
                            // add the sources to the compilation unit
                            LoggerHelper.logDebug(MessageFormat.format("Checkpoint {0}: DefaultQueryEngine.execute. Adding the sources to the compilation unit...", 1));                
                            if(sm.getExecutionInfo().getSources().values().stream().count() > 0){
                                sourcesToBeCompiled.putAll(sm.getExecutionInfo().getSources());                                
                                if(sm instanceof SelectDescriptor){ // do it also for the other types
                                    SelectDescriptor complementingStatement = ((SelectDescriptor)sm).getComplementingStatement();
                                    if(complementingStatement != null && complementingStatement.getExecutionInfo().getSources().values().stream().count() > 0){
                                        sourcesToBeCompiled.putAll(complementingStatement.getExecutionInfo().getSources());
                                        SelectDescriptor complementingStatement2 = ((SelectDescriptor)sm).getComplementingStatement().getComplementingStatement();
                                        if(complementingStatement2 != null && complementingStatement2.getExecutionInfo().getSources().values().stream().count() > 0){
                                            sourcesToBeCompiled.putAll(complementingStatement2.getExecutionInfo().getSources());
                                        }
                                    }
                                }
                            }
                            LoggerHelper.logDebug(MessageFormat.format("Checkpoint {0}: DefaultQueryEngine.execute. Added {1} sources to the compilation unit.", 2, sourcesToBeCompiled.size()));                                            
                        }
                    }
                }
            }
            JavaFileManager fileManager = null;
            if(sourcesToBeCompiled.size() > 0){
                // intentionally used the class loader obtained from the API caller to load the compiler class
                // so that the generated classes are in the same loader as the API caller.
                ClassCompiler compiler = (ClassCompiler) classLoader.loadClass("com.vaiona.commons.compilation.ClassCompiler")
                                            .getConstructor(ClassLoader.class)
                                            .newInstance(classLoader);
                LoggerHelper.logDebug(MessageFormat.format("Checkpoint {0}: DefaultQueryEngine.execute. preparing to compile {1} sources.", 3, sourcesToBeCompiled.size()));
                for (Map.Entry<String, InMemorySourceFile> entry : sourcesToBeCompiled.entrySet()) {
                    LoggerHelper.logDebug(MessageFormat.format("The source file {0} was added to the compilation queue.", entry.getKey()));
                    InMemorySourceFile source = entry.getValue();
                    compiler.addSource(source);
                }
                fileManager = compiler.compile(null);
                //fileManager.getClassLoader
            } else {
                LoggerHelper.logDebug(MessageFormat.format("There are {0} statements submitted but no source is generated for them!", model.getStatements().size()));                
            }
            for(StatementDescriptor sm: model.getStatements().values()){     
                if(!sm.hasError() && !erroneousStatements.contains(sm)){ // the statement has no error and is not dependent upon an errorenous one.
                    if(sm.getExecutionInfo()!= null) {
                        for(InMemorySourceFile source : sm.getExecutionInfo().getSources().values()){
                            if(fileManager != null){
                                try{
                                    // some of the adapters may use no sources, or they may prepare in a non dynamic way for specific scenario
                                    // default adapater for example may use a predefined class(s), or ...
                                    source.setCompiledClass(fileManager.getClassLoader(null).loadClass(source.getFullName()));
                                    if(source.getCompiledClass() != null)
                                        LoggerHelper.logDebug(MessageFormat.format("Compiled class is set for source {0}.", source.getFullName()));                
                                    else
                                        LoggerHelper.logError(MessageFormat.format("Compiled class is NOT set for source {0}.", source.getFullName()));                

                                    if(sm instanceof SelectDescriptor){ // do it also for the other types
                                        SelectDescriptor compensationStatement = ((SelectDescriptor)sm).getComplementingStatement();
                                        if(compensationStatement != null && compensationStatement.getExecutionInfo().getSources().values().stream().count() > 0){
                                            for(InMemorySourceFile compSource : compensationStatement.getExecutionInfo().getSources().values()){
                                                compSource.setCompiledClass(fileManager.getClassLoader(null).loadClass(compSource.getFullName()));                                
                                            }
                                            
                                            SelectDescriptor compensationStatement2 = ((SelectDescriptor)sm).getComplementingStatement().getComplementingStatement();
                                            if(compensationStatement2 != null && compensationStatement2.getExecutionInfo().getSources().values().stream().count() > 0){
                                                for(InMemorySourceFile compSource2 : compensationStatement2.getExecutionInfo().getSources().values()){
                                                    compSource2.setCompiledClass(fileManager.getClassLoader(null).loadClass(compSource2.getFullName()));                                
                                                }
                                            }
                                        }
                                    }
                                } catch (ClassNotFoundException ex) {
                                    // a compaliation error has happened, but a proper error message should be communicated to the user.
                                    LoggerHelper.logError(MessageFormat.format("No class was comipled for the source {0}. The actual error: {1}.", source.getFullName(), ex.getMessage()));
                                }
                            }
                        }
                        sm.accept(visitor);
                        // get the result set back and assign it to the variable named in the target caluse
                        // put the result set in the engine's memory
                        // return a pointer to the variable!
                        if(sm.getExecutionInfo().isExecuted() && sm.getExecutionInfo().getVariable() != null)
                            this.memory.put(sm.getExecutionInfo().getVariable().getName(), sm.getExecutionInfo().getVariable());
                    }
                }
            }

        } catch (Exception ex) {
            LoggerHelper.logError(MessageFormat.format("An exception has occured in the DefaultQueryEngine. execute method. Details: {0}.", ex.getMessage()));                
            model.getLanguageExceptions().add(LanguageExceptionBuilder.builder()
                            .setMessageTemplate(ex.getMessage())
                            .build()
                        );
        }
    }    
}
