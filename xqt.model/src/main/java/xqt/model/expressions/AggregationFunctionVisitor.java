/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.model.expressions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import xqt.model.functions.AggregationCallInfo;
import xqt.model.functions.FunctionInfo;
import xqt.model.functions.FunctionInfoContainer;

/**
 *
 * @author Javad Chamanara <chamanara@gmail.com>
 */
public class AggregationFunctionVisitor implements ExpressionVisitor{
    private final List<AggregationCallInfo> aggregattionCallInfo = new ArrayList<>(); 
    private final String namingPrefix;
    
    public AggregationFunctionVisitor(String namingPrefix){
        this.namingPrefix = namingPrefix;
    }

    public List<AggregationCallInfo> getAggregattionCallInfo() {
        return aggregattionCallInfo;
    }
        
    @Override
    public void reset() {
    }

    @Override
    public void visit(BinaryExpression expr) {
    }

    @Override
    public void visit(FunctionExpression expr) {
        // check whether the function is an aggregate, add it to list, etc...
        // do not change the function itself, it will be done by the adapters
        FunctionInfoContainer functionContainer = FunctionInfoContainer.getDefaultInstance();
        Optional<FunctionInfo> fInfo = functionContainer.getRegisteredFunctions().stream()
                .filter(p-> p.getPackageName().equalsIgnoreCase(expr.getPackageId()) 
                        && p.getName().equalsIgnoreCase(expr.getId())
                        && p.isAggregate()).findFirst();
        if(fInfo.isPresent()){
            AggregationCallInfo aggInfo = new AggregationCallInfo();
            aggInfo.setAliasName(namingPrefix + "_Aggregate_" + aggregattionCallInfo.size());
            aggInfo.setFunction(expr);
            aggInfo.setFunctionName(expr.getPackageId() + "." + expr.getId());
            aggInfo.setParameterName(aggInfo.getAliasName() + "_" + "P0"); // only one parameter is considered for the aggregate functions
            if(expr.getParameters().size() > 0){
                aggInfo.setParameter(expr.getParameters().get(0));
            }
            aggregattionCallInfo.add(aggInfo);
        }        
    }

    @Override
    public void visit(InvalidExpression expr) {
    }

    @Override
    public void visit(MemberExpression expr) {
    }

    @Override
    public void visit(ParameterExpression expr) {
    }

    @Override
    public void visit(UnaryExpression expr) {
    }

    @Override
    public void visit(ValueExpression expr) {
    }
    
}
