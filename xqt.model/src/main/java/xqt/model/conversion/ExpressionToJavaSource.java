/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.conversion;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
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
        patterns.put(ExpressionType.Add, "(( {0} ) + ( {1} ))");
        patterns.put(ExpressionType.And, "(( {0} ) && ( {1} ))");
        patterns.put(ExpressionType.ArithmeticAnd, "(( {0} ) & ( {1} ))");
        patterns.put(ExpressionType.ArithmeticOr, "(( {0} ) | ( {1} ))");
        //patterns.put(ExpressionType.ArithmeticXor, "(( {0} ) + ( {1} ))");
        patterns.put(ExpressionType.Attribute, "( {0} )");
        patterns.put(ExpressionType.Constant, " {0} ");
        patterns.put(ExpressionType.Divide, "(( {0} ) / ( {1} ))");
        patterns.put(ExpressionType.Equal, "(( {0} ) == ( {1} ))");
        patterns.put(ExpressionType.Function, "( {0} ( {1} ) )"); // the second arg is the parameters' source
        patterns.put(ExpressionType.GreaterThan, "(( {0} ) > ( {1} ))");
        patterns.put(ExpressionType.GreaterThanOrEqual, "(( {0} ) >= ( {1} ))");
        patterns.put(ExpressionType.LessThan, "(( {0} ) < ( {1} ))");
        patterns.put(ExpressionType.LessThanOrEqual, "(( {0} ) <= ( {1} ))");
        patterns.put(ExpressionType.Member, " {0} "); //maybe type conversion is needed too!
        patterns.put(ExpressionType.Modulo, "(( {0} ) % ( {1} ))");
        patterns.put(ExpressionType.Multiply, "(( {0} ) * ( {1} ))");
        patterns.put(ExpressionType.Negate, "( - ( {0} ))");
        patterns.put(ExpressionType.Not, "( ! ( {0} ))");
        patterns.put(ExpressionType.NotEqual, "(( {0} ) != ( {1} ))");
        patterns.put(ExpressionType.Or, "(( {0} ) || ( {1} ))");
        patterns.put(ExpressionType.Parameter, " {0} ");
        patterns.put(ExpressionType.Power, "(java.lang.Math.pow( {0} , {1} ))");
        patterns.put(ExpressionType.Subtract, "(( {0} ) - ( {1} ))");
        patterns.put(ExpressionType.IsNull, "(( {0} ) == null)");
        patterns.put(ExpressionType.IsNumber, "({0}.matches(\"-?\\\\d+(\\\\.\\\\d+)?\"))"); // <DataType>.isNaN(x) not supported yet
        patterns.put(ExpressionType.IsDate, "(( {0} ) == null)"); // not supported yet
        patterns.put(ExpressionType.IsEmpty, "((( {0} ) != null) && ({0} .length() <= 0))");
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
            return MessageFormat.format(pattern, left, right); 
            
        } else if(expression.getClass().equals(FunctionExpression.class)){
            FunctionExpression exp = (FunctionExpression)expression;
            StringBuilder paramStringBuilder = new StringBuilder();
            for (Expression p: exp.getParameters()) {
                String pa = visitAll(p);
                String pattern = patterns.get(p.getExpressionType());
                paramStringBuilder.append(MessageFormat.format(pattern, pa));
                paramStringBuilder.append(",");
            }
            paramStringBuilder.deleteCharAt(paramStringBuilder.lastIndexOf(","));
            String funcPattern = patterns.get(exp.getExpressionType());
            return MessageFormat.format(funcPattern, exp.getId(), paramStringBuilder.toString()); // packageId is not considered
            
        } else if(expression.getClass().equals(MemberExpression.class)){
            MemberExpression exp = (MemberExpression)expression;
            //put memebr names in a list for later use by the Csv Reader.It needs them for prepopulation
            if(!memeberNames.contains(exp.getId())){
                memeberNames.add(exp.getId());
            }
            String pattern = patterns.get(exp.getExpressionType());
            return MessageFormat.format(pattern, exp.getId());
            
        } else if(expression.getClass().equals(UnaryExpression.class)){
            UnaryExpression exp = (UnaryExpression)expression;
            String operand = visitAll(exp.getOperand());
            String pattern = patterns.get(exp.getExpressionType());
            return MessageFormat.format(pattern, operand); 
            
        } else if(expression.getClass().equals(ValueExpression.class)){
            ValueExpression exp = (ValueExpression)expression;
            String pattern = patterns.get(exp.getExpressionType());
            return MessageFormat.format(pattern, exp.getValue()); // type conversion to be considered
            
        }
        return "";
    }   
    
    @Override
    public void reset(){
        source = "";
        memeberNames = new ArrayList<>();
    }
}
