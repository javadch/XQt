/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.model.statements;

import java.util.List;
import xqt.model.ElementDescriptor;
import xqt.model.execution.ExecutionInfo;

/**
 *
 * @author jfd
 */
public abstract class StatementDescriptor extends ElementDescriptor {    
    protected ExecutionInfo executionInfo = null;
    protected StatementDescriptor dependsUpon;

    public StatementDescriptor getDependsUpon() {
        return dependsUpon;
    }

    public void setDependsUpon(StatementDescriptor dependsUpon) {
        this.dependsUpon = dependsUpon;
    }

    public Boolean isExecuted(){
        return executionInfo != null;
    }

    public void setExecutionInfo(ExecutionInfo executionInfo) {
        this.executionInfo = executionInfo;
        executionInfo.setStatement(this);
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
    public abstract void accept(StatementVisitor visitor);

    // implementations should get and load a proper adapter, make the adapter to generate sources (to be compiled and executed later)
    public abstract void prepare(StatementVisitor visitor);

    public abstract void pass2(StatementVisitor visitor);

    public abstract void checkDependencies(List<StatementDescriptor> stmts);
}
