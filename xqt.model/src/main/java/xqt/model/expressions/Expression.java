/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.model.expressions;

import com.vaiona.commons.types.TypeSystem;
import java.util.List;
import xqt.model.ElementDescriptor;

/**
 *
 * @author jfd
 */
public abstract class Expression extends ElementDescriptor implements VisitingExpressionElement{

    protected String body;
    protected ExpressionType expressionType;
    protected String returnType = TypeSystem.TypeName.Unknown; // link it the conceptual types

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
    public abstract void accept(ExpressionVisitor visitor);
    
    @Override
    public abstract String toString();
    
    public static BinaryExpression Add(Expression left, Expression right){
        BinaryExpression ex = new BinaryExpression(left, right, ExpressionType.Add);
        // check the types of the left and right expressiosns and merge them
        ex.getLanguageExceptions().addAll(left.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(right.getLanguageExceptions());
        ex.setReturnType(TypeSystem.getResultType(left.getReturnType(), right.getReturnType(), "+", false, true));
        return ex;
    }

    public static BinaryExpression Subtract(Expression left, Expression right){
        BinaryExpression ex = new BinaryExpression(left, right, ExpressionType.Subtract);
        // check the types of the left and right expressiosns and merge them
        ex.getLanguageExceptions().addAll(left.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(right.getLanguageExceptions());
        ex.setReturnType(TypeSystem.getResultType(left.getReturnType(), right.getReturnType(), "-", false, true));
        return ex;
    }

    public static BinaryExpression Multiply(Expression left, Expression right){
        BinaryExpression ex = new BinaryExpression(left, right, ExpressionType.Multiply);
        // check the types of the left and right expressiosns and merge them
        ex.getLanguageExceptions().addAll(left.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(right.getLanguageExceptions());
        ex.setReturnType(TypeSystem.getResultType(left.getReturnType(), right.getReturnType(), "*", false, true));
        return ex;
    }

    public static BinaryExpression Divide(Expression left, Expression right){
        BinaryExpression ex = new BinaryExpression(left, right, ExpressionType.Divide);
        // check the types of the left and right expressiosns and merge them
        ex.getLanguageExceptions().addAll(left.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(right.getLanguageExceptions());
        ex.setReturnType(TypeSystem.getResultType(left.getReturnType(), right.getReturnType(), "/", false, true));
        return ex;
    }

    public static BinaryExpression Modulo(Expression left, Expression right){
        BinaryExpression ex = new BinaryExpression(left, right, ExpressionType.Modulo);
        // check the types of the left and right expressiosns and merge them
        ex.getLanguageExceptions().addAll(left.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(right.getLanguageExceptions());
        ex.setReturnType(TypeSystem.getResultType(left.getReturnType(), right.getReturnType(), "%", false, false));
        return ex;
    }

    public static BinaryExpression And(Expression left, Expression right){
        BinaryExpression ex = new BinaryExpression(left, right, ExpressionType.And);
        ex.getLanguageExceptions().addAll(left.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(right.getLanguageExceptions());
        //ex.returnType = TypeSystem.Boolean;
        ex.setReturnType(TypeSystem.getResultType(left.getReturnType(), right.getReturnType(), "and", false, true));
        return ex;
    }

    public static BinaryExpression Or(Expression left, Expression right){
        BinaryExpression ex = new BinaryExpression(left, right, ExpressionType.Or);
        ex.getLanguageExceptions().addAll(left.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(right.getLanguageExceptions());
        //ex.returnType = TypeSystem.Boolean;
        ex.setReturnType(TypeSystem.getResultType(left.getReturnType(), right.getReturnType(), "or", false, true));
        return ex;
    }

    public static BinaryExpression ArithmeticAnd(Expression left, Expression right){
        BinaryExpression ex = new BinaryExpression(left, right, ExpressionType.ArithmeticAnd);
        // check the types of the left and right expressiosns and merge them
        // it may be always INT
        ex.getLanguageExceptions().addAll(left.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(right.getLanguageExceptions());
        ex.setReturnType(TypeSystem.getResultType(left.getReturnType(), right.getReturnType(), "aand", false, true));
return ex;
    }

    public static BinaryExpression ArithmeticOr(Expression left, Expression right){
        BinaryExpression ex = new BinaryExpression(left, right, ExpressionType.ArithmeticOr);
        // check the types of the left and right expressiosns and merge them
        // it may be always INT
        ex.getLanguageExceptions().addAll(left.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(right.getLanguageExceptions());
        ex.setReturnType(TypeSystem.getResultType(left.getReturnType(), right.getReturnType(), "aor", false, true));
        return ex;
    }

    public static BinaryExpression ArithmeticXor(Expression left, Expression right){
        BinaryExpression ex = new BinaryExpression(left, right, ExpressionType.ArithmeticXor);
        // check the types of the left and right expressiosns and merge them
        // it may be always INT
        ex.getLanguageExceptions().addAll(left.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(right.getLanguageExceptions());
        ex.setReturnType(TypeSystem.getResultType(left.getReturnType(), right.getReturnType(), "axor", false, true));
        return ex;
    }

    public static BinaryExpression GreaterThan(Expression left, Expression right){
        BinaryExpression ex = new BinaryExpression(left, right, ExpressionType.GreaterThan);
        ex.getLanguageExceptions().addAll(left.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(right.getLanguageExceptions());
        //ex.returnType = TypeSystem.Boolean;
        ex.setReturnType(TypeSystem.getResultType(left.getReturnType(), right.getReturnType(), "gt", false, true));
        return ex;
    }

    public static BinaryExpression GreaterThanOrEqual(Expression left, Expression right){
        BinaryExpression ex = new BinaryExpression(left, right, ExpressionType.GreaterThanOrEqual);
        ex.getLanguageExceptions().addAll(left.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(right.getLanguageExceptions());
        //ex.returnType = TypeSystem.Boolean;
        ex.setReturnType(TypeSystem.getResultType(left.getReturnType(), right.getReturnType(), "gteq", false, true));
        return ex;
    }

    public static BinaryExpression LessThan(Expression left, Expression right){
        BinaryExpression ex = new BinaryExpression(left, right, ExpressionType.LessThan);
        ex.getLanguageExceptions().addAll(left.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(right.getLanguageExceptions());
        //ex.returnType = TypeSystem.Boolean;
        ex.setReturnType(TypeSystem.getResultType(left.getReturnType(), right.getReturnType(), "lt", false, true));
        return ex;
    }
    
    public static BinaryExpression LessThanOrEqual(Expression left, Expression right){
        BinaryExpression ex = new BinaryExpression(left, right, ExpressionType.LessThanOrEqual);
        ex.getLanguageExceptions().addAll(left.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(right.getLanguageExceptions());
        //ex.returnType = TypeSystem.Boolean;
        ex.setReturnType(TypeSystem.getResultType(left.getReturnType(), right.getReturnType(), "lteq", false, true));
        return ex;
    }

    public static BinaryExpression Equal(Expression left, Expression right){
        BinaryExpression ex = new BinaryExpression(left, right, ExpressionType.Equal);
        ex.getLanguageExceptions().addAll(left.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(right.getLanguageExceptions());
        //ex.returnType = TypeSystem.Boolean;
        ex.setReturnType(TypeSystem.getResultType(left.getReturnType(), right.getReturnType(), "eq", false, true));
        return ex;
    }

    public static BinaryExpression Like(Expression left, Expression right){
        BinaryExpression ex = new BinaryExpression(left, right, ExpressionType.Like);
        ex.getLanguageExceptions().addAll(left.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(right.getLanguageExceptions());
        //ex.returnType = TypeSystem.Boolean;
        ex.setReturnType(TypeSystem.getResultType(left.getReturnType(), right.getReturnType(), "like", false, true));
        return ex;
    }

    public static BinaryExpression NotEqual(Expression left, Expression right){
        BinaryExpression ex = new BinaryExpression(left, right, ExpressionType.NotEqual);
        ex.getLanguageExceptions().addAll(left.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(right.getLanguageExceptions());
        ex.returnType = TypeSystem.TypeName.Boolean;
        ex.setReturnType(TypeSystem.getResultType(left.getReturnType(), right.getReturnType(), "noteq", false, true));
        return ex;
    }

    // should be removed and modeled as a function
    public static BinaryExpression Power(Expression x, Expression y) {
        BinaryExpression ex = new BinaryExpression(x, y, ExpressionType.Power);
        // check the types of the left and right expressiosns and merge them
        ex.getLanguageExceptions().addAll(x.getLanguageExceptions());
        ex.getLanguageExceptions().addAll(y.getLanguageExceptions());
        return ex;
    }

    public static UnaryExpression Not(Expression operand){
        UnaryExpression ex = new UnaryExpression(operand, ExpressionType.Not);
        ex.getLanguageExceptions().addAll(operand.getLanguageExceptions());
        //ex.returnType = TypeSystem.Boolean;
        ex.setReturnType(TypeSystem.getResultType(operand.getReturnType(), operand.getReturnType(), "not", true, true));
        return ex;
    }

    public static UnaryExpression Negate(Expression operand){
        UnaryExpression ex = new UnaryExpression(operand, ExpressionType.Negate);
        ex.returnType = operand.getReturnType();        
        ex.getLanguageExceptions().addAll(operand.getLanguageExceptions());
        ex.setReturnType(TypeSystem.getResultType(operand.getReturnType(), operand.getReturnType(), "-", true, true));
        return ex;
    }

    public static UnaryExpression IsNull(Expression operand){
        UnaryExpression ex = new UnaryExpression(operand, ExpressionType.IsNull);
        //ex.returnType = TypeSystem.Boolean;
        ex.setReturnType(TypeSystem.getResultType(operand.getReturnType(), operand.getReturnType(), "isnull", true, true));
        return ex;
    }
    
    public static UnaryExpression IsNumber(Expression operand){
        UnaryExpression ex = new UnaryExpression(operand, ExpressionType.IsNumber);
        ex.getLanguageExceptions().addAll(operand.getLanguageExceptions());
        //ex.returnType = TypeSystem.Boolean;
        ex.setReturnType(TypeSystem.getResultType(operand.getReturnType(), operand.getReturnType(), "isnumber", true, true));
        return ex;
    }

    public static UnaryExpression IsDate(Expression operand){
        UnaryExpression ex = new UnaryExpression(operand, ExpressionType.IsDate);
        ex.getLanguageExceptions().addAll(operand.getLanguageExceptions());
        //ex.returnType = TypeSystem.Boolean;
        ex.setReturnType(TypeSystem.getResultType(operand.getReturnType(), operand.getReturnType(), "isdate", true, true));
        return ex;
    }
    
    public static UnaryExpression IsEmpty(Expression operand){
        UnaryExpression ex = new UnaryExpression(operand, ExpressionType.IsEmpty);
        ex.getLanguageExceptions().addAll(operand.getLanguageExceptions());
        //ex.returnType = TypeSystem.Boolean;
        ex.setReturnType(TypeSystem.getResultType(operand.getReturnType(), operand.getReturnType(), "isempty", true, true));
        return ex;
    }
    
    public static MemberExpression Member(String name) {        
        // the caller must merge it with the effective surrounding expression elements.
        // i.e. if its a parameter to a function, its type would be of the type of that parameter, ...
        return Member(name, TypeSystem.TypeName.Unknown);
    }
    
    public static MemberExpression Member(String name, String type) {        
        MemberExpression ex = new MemberExpression(name);
        ex.returnType = type; 
        return ex;
    }
    
    public static MemberExpression CompoundMember(List<String> nameComponents) {        
        MemberExpression ex = new MemberExpression(nameComponents);
        ex.returnType = TypeSystem.TypeName.Unknown;
        return ex;
    }
  
    public static ValueExpression Value(String value, String type) {        
        ValueExpression ex = new ValueExpression(value, type);
        ex.returnType = type; //check if the type is of conceptual type
        return ex;
    }

    public static ParameterExpression Parameter(Expression value) {        
        ParameterExpression ex = new ParameterExpression(value);
        return ex;
    }

    public static FunctionExpression Function(String packageId, String id, String returnType, List<Expression> parameters) {
        FunctionExpression ex = new FunctionExpression(packageId, id, ExpressionType.Function, parameters);
        parameters.stream().forEach(p-> ex.getLanguageExceptions().addAll(p.getLanguageExceptions()));
        ex.setReturnType(returnType);
        return ex;
    }

    public static InvalidExpression Invalid() {        
        InvalidExpression ex = new InvalidExpression();
        ex.returnType = TypeSystem.TypeName.Unknown;
        return ex;
    }

   
}
