/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.model.expressions;

import java.util.List;
import xqt.model.ElementDescriptor;

/**
 *
 * @author jfd
 */
public abstract class Expression extends ElementDescriptor {

    protected String body;
    protected ExpressionType expressionType;
    protected String inferredDataType;
    
    public String getBody() {
        return body;
    }

    public ExpressionType getExpressionType() {
        return expressionType;
    }

    public void setExpressionType(ExpressionType expressionType) {
        this.expressionType = expressionType;
    }

    
    @Override
    public abstract String toString();
    
    public static BinaryExpression Add(Expression left, Expression right){
        return new BinaryExpression(left, right, ExpressionType.Add);
    }

    public static BinaryExpression Subtract(Expression left, Expression right){
        return new BinaryExpression(left, right, ExpressionType.Subtract);
    }

    public static BinaryExpression Multiply(Expression left, Expression right){
        return new BinaryExpression(left, right, ExpressionType.Multiply);
    }

    public static BinaryExpression Divide(Expression left, Expression right){
        return new BinaryExpression(left, right, ExpressionType.Divide);
    }

    public static BinaryExpression Modulo(Expression left, Expression right){
        return new BinaryExpression(left, right, ExpressionType.Modulo);
    }

    public static BinaryExpression And(Expression left, Expression right){
        return new BinaryExpression(left, right, ExpressionType.And);
    }

    public static BinaryExpression Or(Expression left, Expression right){
        return new BinaryExpression(left, right, ExpressionType.Or);
    }

    public static BinaryExpression ArithmeticAnd(Expression left, Expression right){
        return new BinaryExpression(left, right, ExpressionType.ArithmeticAnd);
    }

    public static BinaryExpression ArithmeticOr(Expression left, Expression right){
        return new BinaryExpression(left, right, ExpressionType.ArithmeticOr);
    }

    public static BinaryExpression ArithmeticXor(Expression left, Expression right){
        return new BinaryExpression(left, right, ExpressionType.ArithmeticXor);
    }

    public static BinaryExpression GreaterThan(Expression left, Expression right){
        return new BinaryExpression(left, right, ExpressionType.GreaterThan);
    }

    public static BinaryExpression GreaterThanOrEqual(Expression left, Expression right){
        return new BinaryExpression(left, right, ExpressionType.GreaterThanOrEqual);
    }

    public static BinaryExpression LessThan(Expression left, Expression right){
        return new BinaryExpression(left, right, ExpressionType.LessThan);
    }
    
    public static BinaryExpression LessThanOrEqual(Expression left, Expression right){
        return new BinaryExpression(left, right, ExpressionType.LessThanOrEqual);
    }

    public static BinaryExpression Equal(Expression left, Expression right){
        return new BinaryExpression(left, right, ExpressionType.Equal);
    }

    public static BinaryExpression Like(Expression left, Expression right){
        return new BinaryExpression(left, right, ExpressionType.Like);
    }

    public static BinaryExpression NotEqual(Expression left, Expression right){
        return new BinaryExpression(left, right, ExpressionType.NotEqual);
    }

    public static BinaryExpression Power(Expression x, Expression y) {
        return new BinaryExpression(x, y, ExpressionType.Power);
    }

    public static UnaryExpression Not(Expression operand){
        return new UnaryExpression(operand, ExpressionType.Not);
    }

    public static UnaryExpression Negate(Expression operand){
        return new UnaryExpression(operand, ExpressionType.Negate);
    }

    public static UnaryExpression IsNull(Expression operand){
        return new UnaryExpression(operand, ExpressionType.IsNull);
    }
    
    public static UnaryExpression IsNumber(Expression operand){
        return new UnaryExpression(operand, ExpressionType.IsNumber);
    }

    public static UnaryExpression IsDate(Expression operand){
        return new UnaryExpression(operand, ExpressionType.IsDate);
    }
    
    public static UnaryExpression IsEmpty(Expression operand){
        return new UnaryExpression(operand, ExpressionType.IsEmpty);
    }
    
    public static MemberExpression Member(String name) {        
        return new MemberExpression(name);
    }
    
    public static ValueExpression Value(String value, String type) {        
        return new ValueExpression(value, type);
    }

    public static FunctionExpression Function(String packageId, String id, List<Expression> parameters) {
        return new FunctionExpression(packageId, id, ExpressionType.Function, parameters);
    }
}
