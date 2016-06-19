/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.conversion;

import com.vaiona.commons.types.TypeSystem;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import xqt.model.adapters.DataAdapter;
import xqt.model.expressions.BinaryExpression;
import xqt.model.expressions.Expression;
import xqt.model.expressions.ExpressionType;
import xqt.model.expressions.FunctionExpression;
import xqt.model.expressions.MemberExpression;
import xqt.model.expressions.ParameterExpression;
import xqt.model.expressions.UnaryExpression;
import xqt.model.expressions.ValueExpression;
import xqt.model.functions.FunctionImplementation;
import xqt.model.functions.FunctionInfo;

/**
 *
 * @author standard
 */
public class ExpressionLocalizer { //implements ExpressionVisitor{
    private String source;
    private List<String> memeberNames = new ArrayList<>();
    private final DataAdapter adapter;
    private static Map<ExpressionType, String> patterns = new HashMap<>();
    
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
        patterns.put(ExpressionType.StringEqual, "(( {0} ) .equals ( {1} ))"); 
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
        patterns.put(ExpressionType.StringNotEqual, "(!(( {0} ) .equals ( {1} )))"); 
        patterns.put(ExpressionType.Or, "(( {0} ) || ( {1} ))");
        patterns.put(ExpressionType.Parameter, " {0} ");
        patterns.put(ExpressionType.Power, "(java.lang.Math.pow( {0} , {1} ))");
        patterns.put(ExpressionType.Subtract, "(( {0} ) - ( {1} ))");
        patterns.put(ExpressionType.IsNull, "(( {0} ) == null)");
        patterns.put(ExpressionType.IsNumber, "({0}.matches(\"-?\\\\d+(\\\\.\\\\d+)?\"))"); // <DataType>.isNaN(x) not supported yet
        patterns.put(ExpressionType.IsDate, "(( {0} ) == null)"); // not supported yet
        patterns.put(ExpressionType.IsEmpty, "((( {0} ) == null) || ({0} .length() <= 0))");
    }

    public ExpressionLocalizer(DataAdapter value){
        adapter = value;
        Map<ExpressionType, String> adapterPatterns = adapter.getExpressionPatterns();
        // Overriding the expression patterns with adapter specific ones, if adapters exposed them.
        if(adapterPatterns!= null){
	        for (Entry<ExpressionType, String> item : adapterPatterns.entrySet()) {
	        	if(patterns.containsKey(item.getKey())){
	        		patterns.put(item.getKey(),item.getValue());
	        	}
			}
        }
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
            return localizeBinaryExpression(exp, left, right);
        } else if(expression instanceof FunctionExpression){
            FunctionExpression exp = (FunctionExpression)expression;
            String parameterPart = exp.getParameters().stream().map(p->visitAll(p)).collect(Collectors.joining(","));
            return localizeFunctionExpression(exp, parameterPart);
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
            return localizeMemberExpression(exp);
        } else if(expression instanceof UnaryExpression){
            UnaryExpression exp = (UnaryExpression)expression;
            String operand = visitAll(exp.getOperand());
            return localizeUnaryExpression(operand, exp);
            
        } else if(expression instanceof ValueExpression){
            ValueExpression exp = (ValueExpression)expression;
            return localizeValueExpression(exp);
        }
        return "";
    }   
    
    //@Override
    public void reset(){
        source = "";
        memeberNames = new ArrayList<>();
    }

    // make this public abstract
    private String localizeBinaryExpression(BinaryExpression exp, String left, String right) {
        String pattern = patterns.get(exp.getExpressionType());
        if(exp.getLeft().getReturnType().equalsIgnoreCase(TypeSystem.TypeName.String) || exp.getRight().getReturnType().equalsIgnoreCase(TypeSystem.TypeName.String)){
            if(exp.getExpressionType() == ExpressionType.Equal || exp.getExpressionType() == ExpressionType.StringEqual){
                pattern = patterns.get(ExpressionType.StringEqual);
            } else if (exp.getExpressionType() == ExpressionType.NotEqual | exp.getExpressionType() == ExpressionType.StringNotEqual){
                pattern = patterns.get(ExpressionType.StringNotEqual);
            }                
        }
        return MessageFormat.format(pattern, left, right); 
    }

    private String localizeUnaryExpression(String operand, UnaryExpression exp){
        String pattern = patterns.get(exp.getExpressionType());
        return MessageFormat.format(pattern, operand); 
    }
    
    private String localizeFunctionExpression(FunctionExpression exp, String localizedParameters) {
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
            String runtimeType = TypeSystem.getTypes().get(exp.getReturnType()).getRuntimeType();
            // enhancing the aggregate calls with explicit returntype casting. because the aggrgate functions are
            // called via the interface AggregateFunction which returns an Object.
            funcPattern = MessageFormat.format("(({0}){1})", runtimeType, funcPattern);
            // the funcPattern still has placeholders for the function name and the parameter list.
            return MessageFormat.format(funcPattern, functionPart, localizedParameters);
        }
        Optional <FunctionInfo> funcSpec =adapter.getAdapterInfo().getFunctionInfoContainer(adapter.getConfigPaths()).getRegisteredFunctions().stream()
                .filter(p->p.getName().equals(exp.getFunctionSpecification().getName())).findFirst();
        // if there is no such a funtion, use the default one
        // if there is one, try find the dialect specific implementation, if not use the default fallback one!
        List<FunctionImplementation> impls;
        if(!funcSpec.isPresent()){ // no adapter specific funtion! try use the fallback adapter
            impls = exp.getFunctionSpecification().getImplementations();
        } else {
            impls = funcSpec.get().getImplementations();
        }
        if(impls != null && impls.size() >0){ // there are some implementations in one of the adapters!
            Optional<FunctionImplementation> impl = impls.stream().filter(p->p.getDialect().equalsIgnoreCase(adapter.getDialect())).findFirst();
            if(!impl.isPresent()){ // no dialcet specific implemntation, look for a generic one
                impl = impls.stream().filter(p->p.getDialect().equalsIgnoreCase("default")).findFirst();
            }
            if(!impl.isPresent()){ // the function has no proper implementation neither in its nor in the fallback adapters
                //throw new LanguageException
                return "noop()";
            }
            // the impl is the desired implementation
            if(impl.get().getNativeCode().isEmpty()){
                functionPart = MessageFormat.format("{0}.{1}.{2}", 
                impl.get().getNamespace(), 
                impl.get().getClassName(),
                impl.get().getMethodName());                                    
            } else {
                functionPart = MessageFormat.format("{0}", 
                        impl.get().getNativeCode());
            }
        }
        return MessageFormat.format(funcPattern, functionPart, localizedParameters);
    }

    private String localizeMemberExpression(MemberExpression exp) {
        String pattern = patterns.get(exp.getExpressionType());
        if(exp.getMemberType() == MemberExpression.MemberType.Compound)
            return MessageFormat.format(pattern, exp.getComponents().get(0).toLowerCase() + "_" + exp.getComponents().get(1).toLowerCase());
        return MessageFormat.format(pattern, exp.getId());            
    }
    
    private String localizeValueExpression(ValueExpression exp){
        String pattern = patterns.get(exp.getExpressionType());
        return MessageFormat.format(pattern, exp.getValue()); // type conversion to be considered          
    }
}
