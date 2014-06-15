/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.api;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import xqt.engine.QueryEngine;
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

    public RuntimeSystem getRuntime() {
        return runtime;
    }

    public QueryEngine getEngine() {
        return engine;
    }

    
    public LanguageServicePoint(String processScript) throws UnsupportedEncodingException, IOException{
        //this(new ByteArrayInputStream(processScript.getBytes("UTF-8"))); // also should work, but it must be the first statement
        InputStream stream = new ByteArrayInputStream(processScript.getBytes("UTF-8"));
        init(stream);
    }
        
    public LanguageServicePoint(InputStream processScript) throws IOException {
       init(processScript);
    }
    
    // it is to keep the ctors clean
    private void init(InputStream processScript) throws IOException{
        //prepare the parser/ annotator and create the DST use the runtime system for all the functions, 
        // The API is just a facade over the runtime
        // every statement should have an ID
        // create dependencies of each element to the others especiallay statements.
        //dstNode.getDependsUponElements(ElementType.Statement ...)
        this.inputStream = processScript;
        runtime = new RuntimeSystem();
        engine = runtime.createQueryEngine(processScript); // also static method should work        
    }
    
    public void process(){
        // process all the statements and store the results, but do not return anything
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
        if(sd.isExecuted() && sd.getExecutionInfo().getVariable() != null)
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

    private StatementDescriptor getStatementDescriptor(int id){
        StatementDescriptor statement = engine.getProcessModel().getStatement(id);
        return statement;
    }
}
