/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.expressions;

/**
 *
 * @author standard
 */
public class ParameterExpression extends Expression{

    private Expression internalExpression;

    public ParameterExpression(Expression expression){
        this.internalExpression = expression;
        this.expressionType = ExpressionType.Parameter;
        this.returnType = expression.getReturnType();
    }
    
    public Expression getInternalExpression() {
        return internalExpression;
    }

    public void setInternalExpression(Expression internalExpression) {
        this.internalExpression = internalExpression;
    }
    
   @Override
    public void accept(ExpressionVisitor visitor){
        internalExpression.accept(visitor);
        visitor.visit(this);
    }
    
    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
