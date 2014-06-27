/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.adapters.csv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import xqt.model.expressions.BinaryExpression;
import xqt.model.expressions.Expression;
import xqt.model.expressions.ExpressionType;
import xqt.model.expressions.ExpressionVisitor;
import xqt.model.expressions.FunctionExpression;
import xqt.model.expressions.MemberExpression;
import xqt.model.expressions.UnaryExpression;
import xqt.model.expressions.ValueExpression;

/**
 *
 * @author standard
 */
public class ExpressionToJavaSource implements ExpressionVisitor{
    private String source;
    private List<String> memeberNames = new ArrayList<>();

    private final static Map<ExpressionType, String> patterns = new HashMap<>();
    
    static {
        patterns.put(ExpressionType.Add, "(( %s ) + ( %s ))");
        patterns.put(ExpressionType.And, "(( %s ) && ( %s ))");
        patterns.put(ExpressionType.ArithmeticAnd, "(( %s ) & ( %s ))");
        patterns.put(ExpressionType.ArithmeticOr, "(( %s ) | ( %s ))");
        //patterns.put(ExpressionType.ArithmeticXor, "(( %s ) + ( %s ))");
        patterns.put(ExpressionType.Attribute, "( %s )");
        patterns.put(ExpressionType.Constant, " %s ");
        patterns.put(ExpressionType.Divide, "(( %s ) / ( %s ))");
        patterns.put(ExpressionType.Equal, "(( %s ) == ( %s ))");
        patterns.put(ExpressionType.Function, "( %s ( %s ) )"); // the second arg is the parameters' source
        patterns.put(ExpressionType.GreaterThan, "(( %s ) > ( %s ))");
        patterns.put(ExpressionType.GreaterThanOrEqual, "(( %s ) >= ( %s ))");
        patterns.put(ExpressionType.LessThan, "(( %s ) < ( %s ))");
        patterns.put(ExpressionType.LessThanOrEqual, "(( %s ) <= ( %s ))");
        patterns.put(ExpressionType.Member, " %s "); //maybe type conversion is needed too!
        patterns.put(ExpressionType.Modulo, "(( %s ) % ( %s ))");
        patterns.put(ExpressionType.Multiply, "(( %s ) * ( %s ))");
        patterns.put(ExpressionType.Negate, "( - ( %s ))");
        patterns.put(ExpressionType.Not, "( ! ( %s ))");
        patterns.put(ExpressionType.NotEqual, "(( %s ) != ( %s ))");
        patterns.put(ExpressionType.Or, "(( %s ) || ( %s ))");
        patterns.put(ExpressionType.Parameter, " %s ");
        patterns.put(ExpressionType.Power, "(java.lang.Math.pow( %s , %s ))");
        patterns.put(ExpressionType.Subtract, "(( %s ) - ( %s ))");
        patterns.put(ExpressionType.IsNull, "(( %s ) == null)");
        patterns.put(ExpressionType.IsNaN, "( %s.isNaN ( %s ))"); // <DataType>.isNaN(x)
    }

    public String getSource() {
        return source;
    }    

    public List<String> getMemeberNames() {
        return memeberNames;
    }
    
    @Override
    public void visit(Expression expr) {
        if(expr == null)
            source = "";
        else
            source = visitAll(expr);
    }

    private String visitAll(Expression expression){
        if(expression.getClass().equals(BinaryExpression.class)){
            BinaryExpression exp = (BinaryExpression)expression;
            String left = visitAll(exp.getLeft());
            String right = visitAll(exp.getRight());
            String pattern = patterns.get(exp.getExpressionType());
            return String.format(pattern, left, right); 
            
        } else if(expression.getClass().equals(FunctionExpression.class)){
            FunctionExpression exp = (FunctionExpression)expression;
            StringBuilder paramStringBuilder = new StringBuilder();
            for (Expression p: exp.getParameters()) {
                String pa = visitAll(p);
                String pattern = patterns.get(p.getExpressionType());
                paramStringBuilder.append(String.format(pattern, pa));
                paramStringBuilder.append(",");
            }
            paramStringBuilder.deleteCharAt(paramStringBuilder.lastIndexOf(","));
            String funcPattern = patterns.get(exp.getExpressionType());
            return String.format(funcPattern, exp.getId(), paramStringBuilder.toString()); // packageId is not considered
            
        } else if(expression.getClass().equals(MemberExpression.class)){
            MemberExpression exp = (MemberExpression)expression;
            //put memebr names in a list for later use by the Csv Reader.It needs them for prepopulation
            if(!memeberNames.contains(exp.getId())){
                memeberNames.add(exp.getId());
            }
            String pattern = patterns.get(exp.getExpressionType());
            return String.format(pattern, exp.getId());
            
        } else if(expression.getClass().equals(UnaryExpression.class)){
            UnaryExpression exp = (UnaryExpression)expression;
            String operand = visitAll(exp.getOperand());
            String pattern = patterns.get(exp.getExpressionType());
            return String.format(pattern, operand); 
            
        } else if(expression.getClass().equals(ValueExpression.class)){
            ValueExpression exp = (ValueExpression)expression;
            String pattern = patterns.get(exp.getExpressionType());
            return String.format(pattern, exp.getValue()); // type conversion to be considered
            
        }
        return "";
    }   
    
    @Override
    public void reset(){
        source = "";
        memeberNames = new ArrayList<>();
    }
}
