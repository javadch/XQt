/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.expressions;

import java.util.ArrayList;
import java.util.List;
import xqt.model.functions.FunctionInfo;

/**
 *
 * @author standard
 */
public class FunctionExpression extends Expression{
    private String packageId;
    private List<Expression> parameters = new ArrayList<>();
    private FunctionInfo functionSpecification = null;
    
    public String getPackageId() {
        return packageId;
    }

    public List<Expression> getParameters() {
        return parameters;
    }

    
    public FunctionExpression(String packageId, String id, ExpressionType expressionType, List<Expression> parameters){
        this.packageId = packageId;
        this.id = id;
        this.expressionType = expressionType;
        this.parameters = parameters;
    }

    public FunctionInfo getFunctionSpecification() {
        return functionSpecification;
    }

    public void setFunctionSpecification(FunctionInfo functionSpecification) {
        this.functionSpecification = functionSpecification;
    }

    
    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
