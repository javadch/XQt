/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.model.statements;

import xqt.model.ElementDescriptor;
import xqt.model.execution.ExecutionInfo;

/**
 *
 * @author jfd
 */
public abstract class StatementDescriptor extends ElementDescriptor {    
    protected ExecutionInfo executionInfo = null;

    public Boolean isExecuted(){
        return executionInfo != null;
    }

    public Boolean hasResult(){
        return (isExecuted() && executionInfo.getVariable() != null);
    }
    
    public ExecutionInfo getExecutionInfo() {
        return executionInfo;
    }
    
    public StatementDescriptor(){

    }

    // its good to pass an implementation instance of the query engine via an interface so that execute is able to perform its job
    // check for the adapter capability, if matched execute the rule by passing the model (with some additional control info)
    // to the adapter, otherwise go down into the children and do the same

    // in the case of an assignment statement, the result should be something like an attribute/value
    // so that the engine can put it in the process's memory
    public abstract ExecutionInfo accept(StatementVisitor visitor);
}
