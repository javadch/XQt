package xqt.model.expressions;

/**
 *
 * @author standard
 */
public class UnaryExpression extends Expression{
    private Expression operand;

    public UnaryExpression(Expression operand, ExpressionType operator){
        this.operand = operand;
        this.expressionType = operator;
    }
        
    public Expression getOperand() {
        return operand;
    }

    public void setOperand(Expression operand) {
        this.operand = operand;
    }

    public ExpressionType getOperator() {
        return expressionType;
    }

    @Override
    public void accept(ExpressionVisitor visitor){
        operand.accept(visitor);
        visitor.visit(this);
    }
    
    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
