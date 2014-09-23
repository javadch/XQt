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
    protected String returnType = "Unknown"; // link it the conceptual types

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }
    
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
        BinaryExpression ex = new BinaryExpression(left, right, ExpressionType.Add);
        // check the types of the left and right expressiosns and merge them
        ex.getLanguageExceptions().addAll(left.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(right.getLanguageExceptions());
        return ex;
    }

    public static BinaryExpression Subtract(Expression left, Expression right){
        BinaryExpression ex = new BinaryExpression(left, right, ExpressionType.Subtract);
        // check the types of the left and right expressiosns and merge them
        ex.getLanguageExceptions().addAll(left.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(right.getLanguageExceptions());
        return ex;
    }

    public static BinaryExpression Multiply(Expression left, Expression right){
        BinaryExpression ex = new BinaryExpression(left, right, ExpressionType.Multiply);
        // check the types of the left and right expressiosns and merge them
        ex.getLanguageExceptions().addAll(left.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(right.getLanguageExceptions());
        return ex;
    }

    public static BinaryExpression Divide(Expression left, Expression right){
        BinaryExpression ex = new BinaryExpression(left, right, ExpressionType.Divide);
        // check the types of the left and right expressiosns and merge them
        ex.getLanguageExceptions().addAll(left.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(right.getLanguageExceptions());
        return ex;
    }

    public static BinaryExpression Modulo(Expression left, Expression right){
        BinaryExpression ex = new BinaryExpression(left, right, ExpressionType.Modulo);
        // check the types of the left and right expressiosns and merge them
        ex.getLanguageExceptions().addAll(left.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(right.getLanguageExceptions());
        return ex;
    }

    public static BinaryExpression And(Expression left, Expression right){
        BinaryExpression ex = new BinaryExpression(left, right, ExpressionType.And);
        ex.returnType = "Boolean";
        ex.getLanguageExceptions().addAll(left.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(right.getLanguageExceptions());
        return ex;
    }

    public static BinaryExpression Or(Expression left, Expression right){
        BinaryExpression ex = new BinaryExpression(left, right, ExpressionType.Or);
        ex.returnType = "Boolean";
        ex.getLanguageExceptions().addAll(left.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(right.getLanguageExceptions());
        return ex;
    }

    public static BinaryExpression ArithmeticAnd(Expression left, Expression right){
        BinaryExpression ex = new BinaryExpression(left, right, ExpressionType.ArithmeticAnd);
        // check the types of the left and right expressiosns and merge them
        // it may be always INT
        ex.getLanguageExceptions().addAll(left.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(right.getLanguageExceptions());
        return ex;
    }

    public static BinaryExpression ArithmeticOr(Expression left, Expression right){
        BinaryExpression ex = new BinaryExpression(left, right, ExpressionType.ArithmeticOr);
        // check the types of the left and right expressiosns and merge them
        // it may be always INT
        ex.getLanguageExceptions().addAll(left.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(right.getLanguageExceptions());
        return ex;
    }

    public static BinaryExpression ArithmeticXor(Expression left, Expression right){
        BinaryExpression ex = new BinaryExpression(left, right, ExpressionType.ArithmeticXor);
        // check the types of the left and right expressiosns and merge them
        // it may be always INT
        ex.getLanguageExceptions().addAll(left.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(right.getLanguageExceptions());
        return ex;
    }

    public static BinaryExpression GreaterThan(Expression left, Expression right){
        BinaryExpression ex = new BinaryExpression(left, right, ExpressionType.GreaterThan);
        ex.returnType = "Boolean";
        ex.getLanguageExceptions().addAll(left.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(right.getLanguageExceptions());
        return ex;
    }

    public static BinaryExpression GreaterThanOrEqual(Expression left, Expression right){
        BinaryExpression ex = new BinaryExpression(left, right, ExpressionType.GreaterThanOrEqual);
        ex.returnType = "Boolean";
        ex.getLanguageExceptions().addAll(left.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(right.getLanguageExceptions());
        return ex;
    }

    public static BinaryExpression LessThan(Expression left, Expression right){
        BinaryExpression ex = new BinaryExpression(left, right, ExpressionType.LessThan);
        ex.returnType = "Boolean";
        ex.getLanguageExceptions().addAll(left.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(right.getLanguageExceptions());
        return ex;
    }
    
    public static BinaryExpression LessThanOrEqual(Expression left, Expression right){
        BinaryExpression ex = new BinaryExpression(left, right, ExpressionType.LessThanOrEqual);
        ex.returnType = "Boolean";
        ex.getLanguageExceptions().addAll(left.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(right.getLanguageExceptions());
        return ex;
    }

    public static BinaryExpression Equal(Expression left, Expression right){
        BinaryExpression ex = new BinaryExpression(left, right, ExpressionType.Equal);
        ex.returnType = "Boolean";
        ex.getLanguageExceptions().addAll(left.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(right.getLanguageExceptions());
        return ex;
    }

    public static BinaryExpression Like(Expression left, Expression right){
        BinaryExpression ex = new BinaryExpression(left, right, ExpressionType.Like);
        ex.returnType = "Boolean";
        ex.getLanguageExceptions().addAll(left.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(right.getLanguageExceptions());
        return ex;
    }

    public static BinaryExpression NotEqual(Expression left, Expression right){
        BinaryExpression ex = new BinaryExpression(left, right, ExpressionType.NotEqual);
        ex.returnType = "Boolean";
        ex.getLanguageExceptions().addAll(left.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(right.getLanguageExceptions());
        return ex;
    }

    public static BinaryExpression Power(Expression x, Expression y) {
        BinaryExpression ex = new BinaryExpression(x, y, ExpressionType.Power);
        // check the types of the left and right expressiosns and merge them
        ex.getLanguageExceptions().addAll(x.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(y.getLanguageExceptions());
        return ex;
    }

    public static UnaryExpression Not(Expression operand){
        UnaryExpression ex = new UnaryExpression(operand, ExpressionType.Not);
        ex.returnType = "Boolean";
        ex.getLanguageExceptions().addAll(operand.getLanguageExceptions());
        return ex;
    }

    public static UnaryExpression Negate(Expression operand){
        UnaryExpression ex = new UnaryExpression(operand, ExpressionType.Negate);
        ex.returnType = operand.getReturnType();        
        ex.getLanguageExceptions().addAll(operand.getLanguageExceptions());
        return ex;
    }

    public static UnaryExpression IsNull(Expression operand){
        UnaryExpression ex = new UnaryExpression(operand, ExpressionType.IsNull);
        ex.returnType = "Boolean";
        return ex;
    }
    
    public static UnaryExpression IsNumber(Expression operand){
        UnaryExpression ex = new UnaryExpression(operand, ExpressionType.IsNumber);
        ex.returnType = "Boolean";
        ex.getLanguageExceptions().addAll(operand.getLanguageExceptions());
        return ex;
    }

    public static UnaryExpression IsDate(Expression operand){
        UnaryExpression ex = new UnaryExpression(operand, ExpressionType.IsDate);
        ex.returnType = "Boolean";
        ex.getLanguageExceptions().addAll(operand.getLanguageExceptions());
        return ex;
    }
    
    public static UnaryExpression IsEmpty(Expression operand){
        UnaryExpression ex = new UnaryExpression(operand, ExpressionType.IsEmpty);
        ex.returnType = "Boolean";
        ex.getLanguageExceptions().addAll(operand.getLanguageExceptions());
        return ex;
    }
    
    public static MemberExpression Member(String name) {        
        MemberExpression ex = new MemberExpression(name);
        // the caller must merge it with the effective surrounding expression elements.
        // i.e. if its a parameter to a function, its type would be of the type of that parameter, ...
        ex.returnType = "Unknown"; 
        return ex;
    }
    
    public static MemberExpression CompoundMember(List<String> nameComponents) {        
        MemberExpression ex = new MemberExpression(nameComponents);
        ex.returnType = "Unknown";
        return ex;
    }
  
    public static ValueExpression Value(String value, String type) {        
        ValueExpression ex = new ValueExpression(value, type);
        ex.returnType = type; //check if the type is of conceptual type
        return ex;
    }

    public static FunctionExpression Function(String packageId, String id, List<Expression> parameters) {
        FunctionExpression ex = new FunctionExpression(packageId, id, ExpressionType.Function, parameters);
        parameters.stream().forEach(p-> ex.getLanguageExceptions().addAll(p.getLanguageExceptions()));
        // check the function specifications, loaded by the language service provider, and determine the return type.
        return ex;
    }

    public static InvalidExpression Invalid() {        
        InvalidExpression ex = new InvalidExpression();
        ex.returnType = "Unknown";
        return ex;
    }
  
}
