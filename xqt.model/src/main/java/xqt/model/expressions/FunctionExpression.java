/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.expressions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import xqt.model.functions.FunctionInfo;

/**
 *
 * @author standard
 */
public class FunctionExpression extends Expression{
    private String packageId;
    private List<Expression> parameters = new ArrayList<>();

    private FunctionInfo functionSpecification = null;
    
    @Override
    public void accept(ExpressionVisitor visitor){
        parameters.stream().forEachOrdered(p-> p.accept(visitor));
        visitor.visit(this);
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }
    
    public List<Expression> getParameters() {
        return parameters;
    }

    public void setParameters(List<Expression> parameters) {
        this.parameters.clear();
        this.parameters = parameters;
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
    
    @Override
    public List<MemberExpression> getMemberExpressions(){
    	HashMap<String, MemberExpression> members = new HashMap<>();
    	for(Expression par: parameters){
    		for(MemberExpression member: par.getMemberExpressions()){
    			if(!members.containsKey(member.getId()))
    				members.put(member.getId(), member);
    		}
    	}
    	return members.values().stream().distinct().collect(Collectors.toList());
    }
}
