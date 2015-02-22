/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.model.functions;

import xqt.model.expressions.Expression;
import xqt.model.expressions.FunctionExpression;

/**
 *
 * @author Javad Chamanara <chamanara@gmail.com>
 */

public class AggregationCallInfo{
    private String aliasName; // the replacement name in the caller
    private String functionName; // aggregate function name + package name
    private FunctionInfo functionInfo;
    private String parameterName;
    private Expression parameter;
    private FunctionExpression function;

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public FunctionInfo getFunctionInfo() {
        return functionInfo;
    }

    public void setFunctionInfo(FunctionInfo functionInfo) {
        this.functionInfo = functionInfo;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public Expression getParameter() {
        return parameter;
    }

    public void setParameter(Expression parameter) {
        this.parameter = parameter;
    }        

    public FunctionExpression getFunction() {
        return function;
    }

    public void setFunction(FunctionExpression function) {
        this.function = function;
    }
    
}
