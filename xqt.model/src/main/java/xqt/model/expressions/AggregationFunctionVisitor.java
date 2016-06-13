/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.model.expressions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import xqt.model.adapters.DataAdapter;
import xqt.model.exceptions.LanguageExceptionBuilder;
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
    private final String basePaths = ".";
    private DataAdapter adapter = null;

    public AggregationFunctionVisitor() {
        this.namingPrefix = null;
    }
    
    public AggregationFunctionVisitor(String namingPrefix, DataAdapter adapter){
        this.namingPrefix = namingPrefix;
        this.adapter = adapter;
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
///////////////////////////
       try{
            FunctionInfoContainer functionContainer = null;
            if( adapter == null)
            	functionContainer = FunctionInfoContainer.getDefaultInstance(basePaths);
            else
            	functionContainer = FunctionInfoContainer.getInstance(adapter.getAdapterInfo().getId(), adapter.getConfigPaths());
            Optional<FunctionInfo> fInfo = functionContainer.getRegisteredFunctions().stream()
                    .filter(p-> p.getPackageName().equalsIgnoreCase(expr.getPackageId()) 
                            && p.getName().equalsIgnoreCase(expr.getId())
                            && p.isAggregate()).findFirst();
            if(fInfo.isPresent()){
                AggregationCallInfo aggInfo = new AggregationCallInfo();
                aggInfo.setAliasName((namingPrefix + "_Aggregate_" + aggregattionCallInfo.size()).toLowerCase());
                aggInfo.setFunction(expr);
                aggInfo.setFunctionName(expr.getPackageId() + "." + expr.getId());
                aggInfo.setParameterName(aggInfo.getAliasName() + "_" + "p0"); // all in lower case. only one parameter is considered for the aggregate functions
                if(expr.getParameters().size() > 0){
                    aggInfo.setParameter(expr.getParameters().get(0));
                }
                aggregattionCallInfo.add(aggInfo);
            } else {            
                expr.getLanguageExceptions().add(
                            LanguageExceptionBuilder.builder()
                                .setMessageTemplate("Function \'%s\' was not found in package \'%s\'.")
                                .setContextInfo1(expr.getPackageId())
                                .setContextInfo2(expr.getPackageId())
                                .build()
                        ); 
            } 
        } catch(Exception ex){
            expr.getLanguageExceptions().add(
                        LanguageExceptionBuilder.builder()
                            .setMessageTemplate(ex.getMessage())
                            .build()
                    ); 
        }        
///////////////////////////        
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

	public DataAdapter getAdapter() {
		return adapter;
	}
    
}
