/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.data;

import xqt.model.execution.ExecutionInfo;

/**
 *
 * @author Javad Chamanara
 * @project SciQuest
 */
public class Variable {
    private String name;
    private Resultset result;
    private ExecutionInfo executionInfo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Resultset getResult() {
        return result;
    }

    public void setResult(Resultset result) {
        this.result = result;
    }

    public ExecutionInfo getExecutionInfo() {
        return executionInfo;
    }

    public void setExecutionInfo(ExecutionInfo executionInfo) {
        this.executionInfo = executionInfo;
    }


}
