/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.model.adapters;

import com.vaiona.commons.data.AttributeInfo;
import com.vaiona.commons.data.DataReaderBuilderBase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import xqt.model.containers.JoinedContainer;
import xqt.model.conversion.ConvertSelectElement;
import xqt.model.data.Resultset;
import xqt.model.data.Variable;
import xqt.model.declarations.PerspectiveAttributeDescriptor;
import xqt.model.declarations.PerspectiveDescriptor;
import xqt.model.expressions.AggregationFunctionVisitor;
import xqt.model.expressions.Expression;
import xqt.model.functions.AggregationCallInfo;
import xqt.model.statements.query.GroupEntry;
import xqt.model.statements.query.SelectDescriptor;

/**
 *
 * @author jfd
 */
public abstract class BaseDataAdapter implements DataAdapter {

    protected final HashMap<String, Boolean> capabilities = new HashMap<>();
    protected AdapterInfo adapterInfo;
    protected final Map<JoinedContainer.JoinOperator, String> runtimeJoinOperators = new HashMap<>();
    protected Map<String, AttributeInfo>  attributeInfos = new LinkedHashMap<>();
    protected String dialect = "";
    protected boolean needsMemory = false;
    protected ConvertSelectElement convertSelect = new ConvertSelectElement();
    protected String configPaths = ".";

    
    @Override
    public boolean needsMemory() {
        return needsMemory;
    }

    @Override
    public abstract void setup(Map<String, Object> config);

    @Override
    public abstract Resultset run(SelectDescriptor select, Object context);

    @Override
    public abstract Resultset complement(SelectDescriptor select, Variable variable);

    @Override
    public abstract void prepare(SelectDescriptor select, Object context);

    @Override
    public boolean isSupported(String capability) {
        if(capabilities.containsKey(capability.toLowerCase()) && capabilities.get(capability.toLowerCase()) == true)
            return true;
        return false;
    }

    @Override
    public void registerCapability(String capabilityKey, boolean isSupported) {
        capabilities.put(capabilityKey, isSupported);
    }

    @Override
    public boolean hasRequiredCapabilities(SelectDescriptor select) {
        boolean allmatched = select.getRequiredCapabilities().stream().allMatch(p-> this.isSupported(p));
        return allmatched;
    }

    @Override
    public AdapterInfo getAdapterInfo(){
        return adapterInfo;
    }
    
    @Override
    public void setAdapterInfo(AdapterInfo value){
        adapterInfo = value;
    }

    @Override
    public String getDialect() {
        return dialect;
    }

    @Override
    public void setDialect(String dialect) {
        this.dialect = dialect;
    }
 
    @Override
    public String getConfigPaths() {
        return configPaths;
    }
    
    @Override
    public void setConfigPaths(String value){
        configPaths = value;
    }

    // holds the information about aggregate functions as they were found in the perspective attributes.
    // the agg. functions are substituted with a pointer in the aggregattionCallInfo, so that the adapater, calls them later
    // the whole argument passed to an aggregate function is moved to here and replaced with an automatically generaated name.
    // these items, are used to create an intermediate perspective for retreiving data.
    // the original perspective is used for the aggregated/ grouped resultset.
    
    protected final List<AggregationCallInfo> aggregattionCallInfo = new ArrayList<>(); 
    protected final PerspectiveDescriptor aggregatePerspective = new PerspectiveDescriptor(PerspectiveDescriptor.PerspectiveType.Implicit);
    protected final List<AttributeInfo> groupByAttributes = new ArrayList<>();      
    protected final List<String> groupByImplicitAttributes = new ArrayList<>();
    
    public boolean hasAggregate(){
       return (aggregattionCallInfo.size() > 0 );
    }
    
    protected Boolean prepareAggregates(DataReaderBuilderBase builder, SelectDescriptor select) {
        // adopt for other types of queries, variable, join, etc
        for(PerspectiveAttributeDescriptor attribute: select.getProjectionClause().getPerspective().getAttributes().values()){
            AggregationFunctionVisitor visitor = new AggregationFunctionVisitor(attribute.getId(), this.configPaths);
            attribute.getForwardExpression().accept(visitor);
            if(visitor.getAggregattionCallInfo().size() > 0){
                aggregattionCallInfo.addAll(visitor.getAggregattionCallInfo());                
            } else {// the attribute is not containing aggregate, it should be considered as a group by item. preserve and check it withe group by list, later
                if(!attribute.isAuxiliary())
                    groupByImplicitAttributes.add(attribute.getId());
            }
        }
        // if there is no aggregate function discovered, there is no need to do anything else, 
        // also the group by items found above, are not needed anymore
        if(aggregattionCallInfo.size() <= 0){
            // remove the group by list items, too
            groupByAttributes.clear();
            return false;
        }
//        aggregatePerspective.setPerspectiveType(PerspectiveDescriptor.PerspectiveType.Implicit);
        aggregatePerspective.setId("aggregate_Perspective_for" + select.getProjectionClause().getPerspective().getId());

        // replace the original aggregate calls
        for(AggregationCallInfo callInfo: aggregattionCallInfo){
                // I should find a way to replace this function with a specific wrapper call!!
                callInfo.getFunction().setId(callInfo.getAliasName()); //.setId(callInfo.getAliasName());
                callInfo.getFunction().setPackageId("DONOTCHANGE");
                callInfo.getFunction().getParameters().clear();
                callInfo.getFunction().getParameters()
                        .add(Expression.Parameter(
                                Expression.Member(callInfo.getParameterName(), callInfo.getParameter().getReturnType())));
                
                // also add the callinfo parameters to the row entity perpspective ...
                PerspectiveAttributeDescriptor attribute = new PerspectiveAttributeDescriptor();
                attribute.setId(callInfo.getParameterName());
                attribute.setDataType(callInfo.getParameter().getReturnType());

                attribute.setForwardExpression(callInfo.getParameter());
                attribute.setReverseExpression(null);
                aggregatePerspective.addAttribute(attribute);
        }
        // if there is any aggregate function present in the perspective (aggregattionCallInfo)
        // construct a row entity perpective to be used for reading the data. The current perspective is
        // used for the result entities.
        return true;
    }
    
    protected void prepareLimit(DataReaderBuilderBase builder, SelectDescriptor select) {
        if(isSupported("select.limit")){
            builder.skip(select.getLimitClause().getSkip())
                   .take(select.getLimitClause().getTake());
        }
        else{
            builder.skip(-1)
                   .take(-1);
        }
    }
    
    protected void prepareGroupBy(DataReaderBuilderBase builder, SelectDescriptor select) {
        if(isSupported("select.groupby")) {
            for(String implicitGroupByItem: groupByImplicitAttributes){
                if(attributeInfos.containsKey(implicitGroupByItem)){
                    groupByAttributes.add(attributeInfos.get(implicitGroupByItem));
                }                 
            }
            for (Map.Entry<String, GroupEntry> entry : select.getGroupClause().getGroupIds().entrySet()) {
                if(attributeInfos.containsKey(entry.getKey())){
                    groupByAttributes.add(attributeInfos.get(entry.getKey()));
                }            
            }
            if(groupByAttributes.size() > 0){
                builder.groupBy(groupByAttributes);
            }
        }
    }

    protected void prepareOrderBy(DataReaderBuilderBase builder, SelectDescriptor select) {
        if(isSupported("select.orderby")) {
            Map<AttributeInfo, String> orderItems = new LinkedHashMap<>();    
            for (Map.Entry<String, String> entry : convertSelect.prepareOrdering(select.getOrderClause()).entrySet()) {
                if(builder.getResultAttributes().containsKey(entry.getKey())){
                    orderItems.put(builder.getResultAttributes().get(entry.getKey()), entry.getValue());
                }                                
            }
            builder.orderBy(orderItems);
        }else {
            builder.orderBy(null);
        }
    }

}
