/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.api;

import com.vaiona.commons.io.MarkableFileInputStream;
import com.vaiona.commons.logging.LoggerHelper;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.binding.StringBinding;
import xqt.engine.QueryEngine;
import xqt.model.adapters.AdapterInfoContainer;
import xqt.model.data.Resultset;
import xqt.model.data.ResultsetType;
import xqt.model.data.Variable;
import xqt.model.exceptions.LanguageException;
import xqt.model.functions.FunctionInfoContainer;
import xqt.model.statements.StatementDescriptor;
import xqt.runtime.RuntimeSystem;

/**
 *
 * @author jfd
 */
public class LanguageServicePoint {
    //private final String processScript;
    private InputStream inputStream;
    private RuntimeSystem runtime = null;
    private QueryEngine engine;
    private StringBuilder processScript = new StringBuilder();
    protected List<Exception> exceptions = new ArrayList<>();
    protected ClassLoader classLoader = null;
    
    public LanguageServicePoint(){
        
    }

    // it is to keep the ctors clean
    private void init(InputStream processScript) throws Exception{
        //prepare the parser/ annotator and create the DST use the runtime system for all the functions, 
        // The API is just a facade over the runtime
        // every statement should have an ID
        // create dependencies of each element to the others especiallay statements.
        //dstNode.getDependsUponElements(ElementType.Statement ...)
        
        // Load the function specifications from the packs
        loadFunctionSpecifications();
        // Load the jars of the adapters. maybe it can be deffered to the time they actually requested!
        classLoader = this.getClass().getClassLoader();
        
        //this.inputStream = processScript;
        runtime = new RuntimeSystem();
        try{
            engine = runtime.createQueryEngine(processScript, exceptions); // also static method should work   
            engine.setClassLoader(classLoader);
        }
        catch (Exception ex) {
            this.exceptions.add(new Exception("Could not prepare the query engine! Likely there are some errors in the process syntax.", ex));
        }       
    }
    
    public List<Exception> getExceptions() {
        return exceptions;
    }

    public RuntimeSystem getRuntime() {
        return runtime;
    }

    public QueryEngine getEngine() {
        return engine;
    }

    
    // this removes whatever in the inputstream
    public String addScript(String statement){
        inputStream = null;
        if(statement != null && !statement.isEmpty())
            processScript.append(statement).append("\r\n");
        return statement;
    }

    public String getScript(){
        if(processScript == null || processScript.length() <=0){
            StringBuilder sb=new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            
            String read;
            try {
                read = br.readLine();
                while(read != null) {
                    sb.append(read); sb.append("\n");
                    read =br.readLine();
                }
                if(inputStream.markSupported()) // if not, susequent get or process calls may fail
                    inputStream.reset();
                return sb.toString();
            } catch (IOException ex) {
                return ex.getMessage();
            }
        } else {
            return processScript.toString();
        }
    }
 
    public void registerScript(InputStream script){        
        processScript = new StringBuilder();
        if(script != null)
            inputStream = script;
    }

    public String registerScript(String fileName){        
        processScript = new StringBuilder();
        try {
            inputStream = new MarkableFileInputStream(new FileInputStream(fileName));
        } catch (FileNotFoundException ex) {
            this.exceptions.add(ex);
            return ex.getMessage();
        }
        return "OK";
    }

    public String process(){
        try{
            if(inputStream!= null){
                init(inputStream);
            } else {
                InputStream stream = new ByteArrayInputStream(processScript.toString().getBytes("UTF-8"));
                init(stream);
            }
        } catch(Exception ex){
            this.exceptions.add(ex);
            return "Could not read the input stream: " + ex.getMessage();
        }
        
        // process all the statements and store the results, but do not return anything
        if(exceptions == null || exceptions.size() <=0)
            engine.execute();
        if(engine == null || engine.getProcessModel() == null || engine.getProcessModel().hasError()){
            exceptions.stream().forEach((exx) -> {
                LoggerHelper.logError(MessageFormat.format("Error: {0}\r\n", exx.getMessage()));
            });
            return "Execution terminated by errors in the process. Call getErrors for more information.";
        }
        return "OK";
    }
    
    public Object process(Integer statementId, Boolean forceExecution, Boolean executeIfNeeded){
        // Id is the pointer to the statement, find and process it, then store the result
        // if it is already executed, just return the result set
        // if there is a dependency on some previous statements, process them also (recursive: they may have dependencies too)
        // after the execution of the statement, invalidate resultset that are dependant upon this one
        // also return the result set
        // needs init to be done first, so this method should be called after the process method is called
        if(forceExecution)
        {
            Object result = engine.execute(statementId);
            return result;
        }
        StatementDescriptor sd = getStatementDescriptor(statementId);
        if(sd.hasExecutionInfo() && sd.getExecutionInfo().getVariable() != null)
            return sd.getExecutionInfo().getVariable().getResult();
        else if(executeIfNeeded)
        {
            Object result = engine.execute(statementId);
            return result;
        }
        return null;
    }
    
    public Object getVariable(String variableName){
        if(variableName.equals("Diana")){
            Object[] o = {  new String[] { "a", "b", "c","d", "e" },
                            new int[] { 1, 2, 3, 4, 5},
                            new double[] { .5, 1.5, 2.0, 3.0, 3.5 } };
            return o;
        }
        
        Optional<StatementDescriptor> stmt = engine.getProcessModel().getStatements().values().stream()
                .filter(p-> p.hasExecutionInfo() 
                        && p.getExecutionInfo().hasVariable() 
                        && p.getExecutionInfo().getVariable().getName().equalsIgnoreCase(variableName)
                ).findFirst();
        if(stmt.isPresent()){
            Variable variable = stmt.get().getExecutionInfo().getVariable();
            switch(variable.getResult().getResultsetType()){
                case Tabular:
                    return variable.getResultAsArray();
                case Image:
                    break;
                default:
                    break;
            }
        }
        return null;
    }
    
    public Object getVariableSchema(String variableName){
        if(variableName.equals("Diana")){
            return new String[] { "v1", "v2", "v3" };
        }
        
        Optional<StatementDescriptor> stmt = engine.getProcessModel().getStatements().values().stream()
                .filter(p-> p.hasExecutionInfo() 
                        && p.getExecutionInfo().hasVariable() 
                        && p.getExecutionInfo().getVariable().getName().equalsIgnoreCase(variableName)
                ).findFirst();
        if(stmt.isPresent()){
            Variable variable = stmt.get().getExecutionInfo().getVariable();
            switch(variable.getResult().getResultsetType()){
                case Tabular:
                    String[] names = new String[variable.getResult().getSchema().size()];
                    int index =0;
                    for(String name :variable.getResult().getSchema().stream().map(p->p.getName()).collect(Collectors.toList())){
                        names[index++] = name;
                    }
                    return names;
                case Image:
                    break;
                default:
                    break;
            }
        }
        return null;
    }
  
    private StatementDescriptor getStatementDescriptor(int id){
        StatementDescriptor statement = engine.getProcessModel().getStatement(id);
        return statement;
    }
    
    public boolean hasError(){
        // go through the languageExceptions and also ask all the elements: declarations, statements, configurations etc
        if(this.exceptions.stream().count() > 0)
            return true;
        return false;
    }

    FunctionInfoContainer functionContainer = null;
    private void loadFunctionSpecifications() throws Exception {
        // use the function specification bean, read the function pack folder, list the packages, read them all, add them to the function list.
        functionContainer = FunctionInfoContainer.getDefaultInstance();
    }
    
    public Object getAdapterNames(){
        try{
            AdapterInfoContainer instance = AdapterInfoContainer.getInstance();
            return instance.getAdapterNames();
        } catch(Exception ex){
            String[] problem = new String[1];
            problem[0] = ex.getMessage();
            return problem;
        }
    }

    public String getErrors(){
        StringBuilder errors = new StringBuilder();
        int errorCount = 0;
        if(hasError()){ // lexical, syntax and ... error
            errors.append("**************************************************************************************\n");
            errors.append("************************************ Lexical Errors ***********************************\n");
            errors.append("**************************************************************************************\n");
            for(Exception p : getExceptions()){
                errors.append("Error " + ++errorCount + " : " + p.getMessage()+ "\n");
            }
        } 
        if(getEngine() != null && getEngine().getProcessModel() != null) {
            if(getEngine().getProcessModel().hasError()){ // semantic errors
                errors.append("**************************************************************************************\n");
                errors.append("******************************* Synatx and Semantic Errors *******************************\n");
                errors.append("**************************************************************************************\n");
                for(Exception p : getExceptions()){
                    errors.append("Error " + ++errorCount + " : " + p.getMessage()+ "\n");
                }
            }             
            errors.append("**************************************************************************************\n");
            errors.append("****************************** Statement Execution Results ********************************\n");
            errors.append("**************************************************************************************\n");
            getEngine().getProcessModel().getStatements().values().stream().forEachOrdered((s) -> {
                if(s.hasExecutionInfo()){
                    if(!s.getExecutionInfo().isExecuted()){
                        errors.append("Statement " + s.getId() + " was NOT executed.\n");
                    } else if(s.hasResult()){
                        Variable v = s.getExecutionInfo().getVariable();
                        switch (v.getResult().getResultsetType()){
                            case Tabular:{
                                errors.append("Statement " + s.getId() + " was executed. Its result is in the variable: '" + v.getName() + "' and contains " + v.getResult().getTabularData().size() + " records.\n");                                     
                                break;
                            }
                            case Image: {
                                errors.append("Statement " + s.getId() + " was executed.  Its result is in the variable: '" + v.getName() + "'.\n"); 
                                break;
                            }
                        }
                    } else {
                        errors.append("Statement " + s.getId() + " was executed but returned no result.\n");
                    }                      
                } else {
                    errors.append("Statement " + s.getId() + " was NOT executed.\n");
                }
            });
        }  
        return errors.toString();
    }
}
