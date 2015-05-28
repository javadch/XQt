/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.api;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.binding.StringBinding;
import xqt.engine.QueryEngine;
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
    
    public void registerScript(InputStream script){        
        processScript = new StringBuilder();
        if(script != null)
            inputStream = script;
    }

    public void registerScript(String fileName){        
        processScript = new StringBuilder();
        try {
            inputStream = new FileInputStream(fileName);
        } catch (FileNotFoundException ex) {
            this.exceptions.add(ex);
            //Logger.getLogger(LanguageServicePoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public LanguageServicePoint(){
        
    }
//    public LanguageServicePoint(String processScript) {
//        try{
//            InputStream stream = new ByteArrayInputStream(processScript.getBytes("UTF-8"));
//            init(stream);
//        } catch(Exception ex){
//            this.exceptions.add(ex);
//        }
//    }
//        
//    public LanguageServicePoint(InputStream processScript) {
//        try{
//           init(processScript);
//        } catch(Exception ex){
//            this.exceptions.add(ex);
//        }
//    }
    
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
        
        this.inputStream = processScript;
        runtime = new RuntimeSystem();
        try{
            engine = runtime.createQueryEngine(processScript, exceptions); // also static method should work           
        }
        catch (Exception ex) {
            this.exceptions.add(new Exception("Could not prepare the query engine! Likely there are some errors in the process syntax.", ex));
        }       
    }
    
    public void process(){
        try{
            if(inputStream!= null){
                init(inputStream);
            } else {
                InputStream stream = new ByteArrayInputStream(processScript.toString().getBytes("UTF-8"));
                init(stream);
            }
        } catch(Exception ex){
            this.exceptions.add(ex);
        }
        
        // process all the statements and store the results, but do not return anything
        if(exceptions == null || exceptions.size() <=0)
            engine.execute();
    }
    
    public Object process(Integer statementId, Boolean forceExecution, Boolean executeIfNeeded){
        // Id is the pointer to the statement, find and process it, then store the result
        // if it is already executed, just return the result set
        // if there is a dependency on some previous statements, process them also (recursive: they may have dependencies too)
        // after the execution of the statement, invalidate resultset that are dependant upon this one
        // also return the result set
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
    
//    public StatementDescriptor getStatement(int id){
//        return getStatementDescriptor(id);
//    }

    public Object getVariable(String variableName){
        if(variableName.equals("Diana")){
            Object[] o = { new String[] { "a", "b", "c","d", "e" },
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
                    return variable.getResult().getSchema().stream().map(p->p.getName()).collect(Collectors.toList()).toArray();
                case Image:
                    break;
                default:
                    break;
            }
        }
        return null;
    }

//    public class DataObject{
//        private String s;
//        private int number;
//        private double x;
//        public DataObject(String s, int  i, double d){
//            this.s = s;
//            this.number = i;
//            this.x = d;
//        }
//        public void setS(String s) {
//            this.s = s;
//        }
//
//        public void setNumber(int number) {
//            this.number = number;
//        }
//
//        public void setX(double x) {
//            this.x = x;
//        }
//        
//        public  static DataObject[] createData(){
//            DataObject[] cs = { new DataObject("a", 1, .5),
//                                new DataObject("b", 2, 1.5),
//                                new DataObject("c", 3, 3.4),
//                                new DataObject("d", 4, 5.6),
//            };
//            return cs;
//        }
//    }
    
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
    
}
