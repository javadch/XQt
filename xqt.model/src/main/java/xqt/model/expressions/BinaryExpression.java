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
public class BinaryExpression extends Expression{
    private Expression left;
    private Expression right;

    public BinaryExpression(Expression left, Expression right, ExpressionType operator){
        this.left = left;
        this.right = right;
        this.expressionType = operator;
    }
    
    @Override
    public void accept(ExpressionVisitor visitor){
        this.left.accept(visitor);
        visitor.visit(this);
        this.right.accept(visitor);
    }

    public Expression getLeft() {
        return left;
    }

    public void setLeft(Expression left) {
        this.left = left;
    }

    public Expression getRight() {
        return right;
    }

    public void setRight(Expression right) {
        this.right = right;
    }

    public ExpressionType getOperator() {
        return expressionType;
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
