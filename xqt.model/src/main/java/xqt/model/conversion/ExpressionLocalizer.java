/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.conversion;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import xqt.model.adapters.AdapterInfo;
import xqt.model.expressions.BinaryExpression;
import xqt.model.expressions.Expression;
import xqt.model.expressions.ExpressionType;
import xqt.model.expressions.ExpressionVisitor;
import xqt.model.expressions.FunctionExpression;
import xqt.model.expressions.InvalidExpression;
import xqt.model.expressions.MemberExpression;
import xqt.model.expressions.ParameterExpression;
import xqt.model.expressions.UnaryExpression;
import xqt.model.expressions.ValueExpression;
import xqt.model.functions.FunctionInfo;

/**
 *
 * @author standard
 */
public class ExpressionLocalizer { //implements ExpressionVisitor{
    private String source;
    private List<String> memeberNames = new ArrayList<>();
    private final AdapterInfo adapterInfo;
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

    public ExpressionLocalizer(AdapterInfo value){
        adapterInfo = value;
    }
    
    public String getSource() {
        return source;
    }    

    public List<String> getMemeberNames() {
        return memeberNames;
    }
    
    //@Override
    public void visit(Expression expr) {
        if(expr == null)
            source = "";
        else
            source = visitAll(expr);
    }

    private String visitAll(Expression expression){
        if(expression instanceof BinaryExpression){
            BinaryExpression exp = (BinaryExpression)expression;
            String left = visitAll(exp.getLeft());
            String right = visitAll(exp.getRight());
            String pattern = patterns.get(exp.getExpressionType());
            return MessageFormat.format(pattern, left, right); 
            
        } else if(expression instanceof FunctionExpression){
            FunctionExpression exp = (FunctionExpression)expression;
            //StringBuilder paramStringBuilder = new StringBuilder();
            String parameterPart = exp.getParameters().stream().map(p->visitAll(p)).collect(Collectors.joining(","));
//            for (Expression p: exp.getParameters()) {
//                String pa = visitAll(p);
//                String pattern = patterns.get(p.getExpressionType());
//                paramStringBuilder.append(MessageFormat.format(pattern, pa));
//                paramStringBuilder.append(",");
//            }
//            paramStringBuilder.deleteCharAt(paramStringBuilder.lastIndexOf(","));
            String funcPattern = patterns.get(exp.getExpressionType());
            String functionPart = "";
            if(exp.getPackageId().equalsIgnoreCase("DONOTCHANGE")){
                    // call a specificaly designed method as the aggregate wrapper
                    //this is the actual information of the concrete aggregate function to be called, but it should (usually) be replaced in the adapters to call a wrapper method
                    // in order to direct the call to the groupped version of the aggregates/ running aggregates.
                functionPart = MessageFormat.format("{0}.{1}.{2}", 
                    exp.getPackageId(), 
                    exp.getId(),
                    "NOCALL");
                return MessageFormat.format(funcPattern, functionPart, parameterPart);
            }
            Optional <FunctionInfo> funcSpec =adapterInfo.getFunctionInfoContainer().getRegisteredFunctions().stream()
                    .filter(p->p.getName().equals(exp.getFunctionSpecification().getName())).findFirst();
            if(!funcSpec.isPresent()) { // there should be also an option to fail on this case!      
                // function implementation should be static
                functionPart = MessageFormat.format("{0}.{1}.{2}", 
                exp.getFunctionSpecification().getImplementation().getNamespace(), 
                exp.getFunctionSpecification().getImplementation().getClassName(),
                exp.getFunctionSpecification().getImplementation().getMethodName());                
            } else { // the responsible adapter has a specification for the function. its spec should be used.
                switch (funcSpec.get().getImplementation().getProvider()){
                    case Adapter: // use the adapter specific implementation
                        functionPart = MessageFormat.format("{0}.{1}.{2}", 
                            funcSpec.get().getImplementation().getNamespace(), 
                            funcSpec.get().getImplementation().getClassName(),
                            funcSpec.get().getImplementation().getMethodName());
                        break;
                    case Container: // use the container/ data source specific impplementation
                        functionPart = MessageFormat.format("{0}", 
                            funcSpec.get().getImplementation().getProviderString());
                        break;
                    case Fallback: // use the fallback implementation provided by the fallback adapter.
                        functionPart = MessageFormat.format("{0}.{1}.{2}", 
                            exp.getFunctionSpecification().getImplementation().getNamespace(), 
                            exp.getFunctionSpecification().getImplementation().getClassName(),
                            exp.getFunctionSpecification().getImplementation().getMethodName());                        
                        break;
                }
            }
            return MessageFormat.format(funcPattern, functionPart, parameterPart);
            
        } else if(expression instanceof ParameterExpression){
            ParameterExpression exp = (ParameterExpression)expression;
            return visitAll(exp.getInternalExpression());
        }
        else if(expression instanceof MemberExpression){
            MemberExpression exp = (MemberExpression)expression;
            //put memebr names in a list for later use by the Csv Reader.It needs them for prepopulation
            if(!memeberNames.contains(exp.getId())){
                memeberNames.add(exp.getId());
            }
            String pattern = patterns.get(exp.getExpressionType());
            return MessageFormat.format(pattern, exp.getId());
            
        } else if(expression instanceof UnaryExpression){
            UnaryExpression exp = (UnaryExpression)expression;
            String operand = visitAll(exp.getOperand());
            String pattern = patterns.get(exp.getExpressionType());
            return MessageFormat.format(pattern, operand); 
            
        } else if(expression instanceof ValueExpression){
            ValueExpression exp = (ValueExpression)expression;
            String pattern = patterns.get(exp.getExpressionType());
            return MessageFormat.format(pattern, exp.getValue()); // type conversion to be considered            
        }
        return "";
    }   
    
    //@Override
    public void reset(){
        source = "";
        memeberNames = new ArrayList<>();
    }

/*
    @Override
    public void visit(BinaryExpression expr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(FunctionExpression expr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(InvalidExpression expr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(MemberExpression expr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(ParameterExpression expr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(UnaryExpression expr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(ValueExpression expr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
*/    
}
