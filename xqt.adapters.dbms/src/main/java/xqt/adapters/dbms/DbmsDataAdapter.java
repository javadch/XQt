/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.adapters.dbms;

import com.vaiona.commons.data.AttributeInfo;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import xqt.model.adapters.AdapterInfo;
import xqt.model.adapters.DataAdapter;
import xqt.model.containers.DataContainer;
import xqt.model.containers.JoinedContainer;
import xqt.model.containers.SingleContainer;
import xqt.model.conversion.ConvertSelectElement;
import xqt.model.data.Resultset;
import xqt.model.data.Variable;
import xqt.model.declarations.PerspectiveAttributeDescriptor;
import xqt.model.declarations.PerspectiveDescriptor;
import xqt.model.exceptions.LanguageExceptionBuilder;
import xqt.model.expressions.AggregationFunctionVisitor;
import xqt.model.expressions.Expression;
import xqt.model.functions.AggregationCallInfo;
import xqt.model.statements.query.GroupEntry;
import xqt.model.statements.query.SelectDescriptor;

/**
 *
 * @author Javad Chamanara <chamanara@gmail.com>
 */
public class DbmsDataAdapter implements DataAdapter{
    private DbmsDataReaderBuilder builder = null;
    private DbmsDataAdapterHelper helper = null;
    private ConvertSelectElement convertSelect = null;
    private Map<JoinedContainer.JoinOperator, String> runtimeJoinOperators = new HashMap<>();
    private HashMap<String, Boolean> capabilities = new HashMap<>();
    private AdapterInfo adapterInfo;
    private String dialect = "default";
        
    public DbmsDataAdapter(){
        convertSelect = new ConvertSelectElement();
        runtimeJoinOperators.put(JoinedContainer.JoinOperator.EQ, "==");
        runtimeJoinOperators.put(JoinedContainer.JoinOperator.NotEQ, "!=");
        runtimeJoinOperators.put(JoinedContainer.JoinOperator.GT, ">");
        runtimeJoinOperators.put(JoinedContainer.JoinOperator.GTEQ, ">=");
        runtimeJoinOperators.put(JoinedContainer.JoinOperator.LT, "<");
        runtimeJoinOperators.put(JoinedContainer.JoinOperator.LTEQ, "<=");        
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
    public boolean needsMemory() {
        return false;
    }

    @Override
    public void setup(Map<String, Object> config) {
        registerCapability("select.qualifier", true);
        registerCapability("function", true);
        registerCapability("function.default.max", true);
        registerCapability("expression", true);
        registerCapability("select.projection.perspective", true);
        registerCapability("select.projection.perspective.implicit", true);
        registerCapability("select.projection.perspective.explicit", true);
        registerCapability("select.projection.perspective.inline", true);
        registerCapability("select.source.single", true);
        registerCapability("select.source.joined", true);
        registerCapability("select.source.variable", false);
        registerCapability("select.target.variable", true);
        registerCapability("select.target.persist", false);
        registerCapability("select.target.plot", false);
        registerCapability("select.anchor", false);
        registerCapability("select.filter", true);
        registerCapability("select.orderby", true);
        registerCapability("select.groupby", true);
        registerCapability("select.limit", true);
        registerCapability("select.limit.take", true);
        registerCapability("select.limit.skip", true);
    }

    @Override
    public Resultset run(SelectDescriptor select, Object context) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//        switch (select.getSourceClause().getContainer().getDataContainerType()) {
//            case Single:
//                return runForSingleContainer(select, context);
//            case Joined:
//                return runForJoinedContainer(select, context);
//            case Variable:
//                if(select.getTargetClause().getContainer().getDataContainerType() == DataContainer.DataContainerType.Single){
//                    try {
//                        return runForVariable_SingleContainer(select, context);
//                    } catch (IOException ex) {
//                        return null;
//                    }
//                }
//                return null;
//            default:
//                return null;
//        }
    }

    @Override
    public Resultset complement(SelectDescriptor select, Variable variable) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void prepare(SelectDescriptor select, Object context) {
        try{
            builder = new DbmsDataReaderBuilder();
            builder
                //.baseClassName("GeneratedX") // let the builder name the classes automatically
                .dateFormat("yyyy-MM-dd'T'HH:mm:ssX") //check the timezone formatting
                .namespace("xqt.adapters.dbms")
                .entityResourceName("Entity")
            ;
            switch (select.getSourceClause().getContainer().getDataContainerType()) {
                case Single:
                    prepareSingle(select);
                    break;
                case Joined:
                    //prepareJoined(select);
                    break;
                case Variable:
                    // Its one of these cases: 
                    // 1: the result set of a previous statement is going to be persisted
                    // 2: a statement with target clause of type container is broken into multiple parts because of the adapter capabilities. This is the last part that writes the final result into a container
                    if(select.getTargetClause().getContainer().getDataContainerType() == DataContainer.DataContainerType.Single){
                        try {
                            //prepareVariable(select);
                        } catch (Exception ex) {
                            select.getLanguageExceptions().add(
                                LanguageExceptionBuilder.builder()
                                    .setMessageTemplate(ex.getMessage())
                                    .setContextInfo1(select.getId())
                                    .setLineNumber(select.getParserContext().getStart().getLine())
                                    .setColumnNumber(select.getParserContext().getStop().getCharPositionInLine())
                                    .build()
                            );                        
                        }
                    }
                    break;
                default:
                    break;
            }
          
        } catch (ParseException ex){
            select.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate(ex.getMessage())
                    .setContextInfo1(select.getId())
                    .setLineNumber(select.getParserContext().getStart().getLine())
                    .setColumnNumber(select.getParserContext().getStop().getCharPositionInLine())
                    .build()
            );                        
        }    
    }

    @Override
    public boolean isSupported(String capability) {
        if(capabilities.containsKey(capability) && capabilities.get(capability) == true)
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
    public AdapterInfo getAdapterInfo() {
        return adapterInfo;
    }

    @Override
    public void setAdapterInfo(AdapterInfo value) {
        adapterInfo = value;
    }
    
    Map<String, AttributeInfo>  attributeInfos = new LinkedHashMap<>();
    private final List<AggregationCallInfo> aggregattionCallInfo = new ArrayList<>(); 
    private final PerspectiveDescriptor aggregatePerspective = new PerspectiveDescriptor(PerspectiveDescriptor.PerspectiveType.Implicit);
    private final List<AttributeInfo> groupByAttributes = new ArrayList<>();      
    private final List<String> groupByImplicitAttributes = new ArrayList<>();

    private void prepareSingle(SelectDescriptor select) {
        SingleContainer container =((SingleContainer)select.getSourceClause().getContainer());
        try{
            helper = DbmsDataAdapterHelper.getConcreteHelper(container);
            builder.containerName(container.getContainerName())
                   .addFields(helper.getContinerSchema(container));
            builder.registerQueryHelper(helper);
            if(select.getProjectionClause().isPresent() == false 
                    && select.getProjectionClause().getPerspective().getPerspectiveType() == PerspectiveDescriptor.PerspectiveType.Implicit) {
                select.getProjectionClause().setPerspective(
                        helper.createPhysicalPerspective(builder.getFields(), select.getProjectionClause().getPerspective(), select.getId()));
                select.getProjectionClause().setPresent(true);
                select.validate();
                if(select.hasError())
                    return;
            } else { // see whether there exists any attribute of unkown type!
                select.getProjectionClause().setPerspective(
                    helper.improvePerspective(builder.getFields(), select.getProjectionClause().getPerspective()));
            }
            builder.connectionString(helper.getConnectionString(container))
                   .username(helper.getContainerUsername(container))
                   .password(helper.getContainerUsername(container))
                   .dbProvider(DBMSDialect.getEnum(helper.getContainerDialectId(container)));
            // aggregate functions in the perspective should be be handled here. also other prepare functions and adapters should do it properly
            Boolean hasAggregates = prepareAggregates(builder, select);
            if(hasAggregates){
                builder.readerResourceName("AggregateReader");
                builder.addAggregates(aggregattionCallInfo);
                // send the aggregate perspective
                // check whether all the field references in the mappings, are valid by making sure they are in the Fields list.
                Map<String, AttributeInfo> rowEntityattributeInfos = convertSelect.prepareAttributes(aggregatePerspective, this, false);            
                // set the resultset perspective. 
                // check whether all the field references in the mappings, are valid by making sure they are in the Fields list.
                // maybe pareparation is not needed!!!!!!
                attributeInfos = convertSelect.prepareAttributes(select.getProjectionClause().getPerspective(), this, false);            
                for(AttributeInfo attInfo: attributeInfos.values()){
                    attInfo.forwardMap = attInfo.forwardMap.replaceAll("DONOTCHANGE.([^\\s]*).NOCALL\\s*\\(\\s*([^\\s]*)\\s*\\)", "functions.get(\"$1\").move(rowEntity.$2)");
                    //attInfo.forwardMap = attInfo.forwardMap.replaceAll("move ( ([^<]*) )", "move(rowEntity.$1 ) ");
                }
                prepareGroupBy(builder, select);
                if(isSupported("select.groupby")) {
                    if(groupByAttributes.size() > 0){
                        builder.groupBy(groupByAttributes);
                    }
                }

                // check if there are groupby attributes, add them to the row entity and replace the access method of the result entity
                if(groupByAttributes != null && groupByAttributes.size() > 0){ // the groupby attributes hsould be added to the row entity to be used in the group constrcution keys
                    //replace the forward map of the resultentity to point to the same attribute in the row entity
                    for(AttributeInfo attInfo: attributeInfos.values()){
                        if(groupByAttributes.stream().anyMatch(p-> p.name.equals(attInfo.name))){
                            AttributeInfo tobeAddedToTheRowEntity = new AttributeInfo(attInfo);
                            rowEntityattributeInfos.put(tobeAddedToTheRowEntity.name, tobeAddedToTheRowEntity);
                            attInfo.forwardMap = "rowEntity." + attInfo.name; // pointing to a veraible of same name in the row entity//attInfo.forwardMap.replaceAll("DONOTCHANGE.([^\\s]*).NOCALL\\s*\\(\\s*([^\\s]*)\\s*\\)", "functions.get(\"$1\").move(rowEntity.$2)");
                        }
                    }
                } 
                builder.addRowAttributes(rowEntityattributeInfos);
                builder.getRowAttributes().values().stream().forEach(at -> {
                    at.internalDataType = helper.getPhysicalType(at.conceptualDataType);
                });

                builder.addResultAttributes(attributeInfos);
                builder.getResultAttributes().values().stream().forEach(at -> {
                    at.internalDataType = helper.getPhysicalType(at.conceptualDataType);
                });
                
                if(isSupported("select.orderby")) {
                    Map<AttributeInfo, String> orderItems = new LinkedHashMap<>();        
                    for (Map.Entry<String, String> entry : convertSelect.prepareOrdering(select.getOrderClause()).entrySet()) {
                        if(attributeInfos.containsKey(entry.getKey())){
                            orderItems.put(attributeInfos.get(entry.getKey()), entry.getValue());
                        }            
                    }
                    builder.orderBy(orderItems);
                }else {
                    builder.orderBy(null);
                }
                
            } else { // no aggregate is present
                builder.readerResourceName("Reader");
                // check whether all the field references in the mappings, are valid by making sure they are in the Fields list.
                attributeInfos = convertSelect.prepareAttributes(select.getProjectionClause().getPerspective(), this, false);            
                builder.addResultAttributes(attributeInfos);
                builder.getResultAttributes().values().stream().forEach(at -> {
                    at.internalDataType = helper.getPhysicalType(at.conceptualDataType);
                });
                prepareGroupBy(builder, select);
                if(isSupported("select.groupby")) {
                    if(groupByAttributes.size() > 0){
                        builder.groupBy(groupByAttributes);
                    }
                }                
            
                if(isSupported("select.orderby")) {
                    Map<AttributeInfo, String> orderItems = new LinkedHashMap<>();        
                    for (Map.Entry<String, String> entry : convertSelect.prepareOrdering(select.getOrderClause()).entrySet()) {
                        if(attributeInfos.containsKey(entry.getKey())){
                            orderItems.put(attributeInfos.get(entry.getKey()), entry.getValue());
                        }            
                    }
                    builder.orderBy(orderItems);
                }else {
                    builder.orderBy(null);
                }
            }
            
            try{
                if(isSupported("select.filter")) 
                    builder.where(convertSelect.prepareWhere(select.getFilterClause(), this), false);
                else 
                    builder.where("", false);
            } catch(Exception ex){
                select.getLanguageExceptions().add(
                    LanguageExceptionBuilder.builder()
                        .setMessageTemplate(ex.getMessage())
                        .setContextInfo1(select.getId())
                        .setLineNumber(select.getParserContext().getStart().getLine())
                        .setColumnNumber(-1)
                        .build()
                    );                
            }
            
            prepareLimit(builder, select);
            //all attributes refered to from the group by plus, 
            //if the perspective contains aggregate functions, all non aggregate attributes should be added to the goup by list
            // 

            builder.writeResultsToFile(convertSelect.shouldResultBeWrittenIntoFile(select.getTargetClause()));
            select.getExecutionInfo().setSources(builder.createSources());
        } catch (IOException ex){
            select.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate(ex.getMessage())
                    .setContextInfo1(select.getId())
                    .setLineNumber(select.getParserContext().getStart().getLine())
                    .setColumnNumber(-1)
                    .build()
                );
        }
    }

    // these methods appear in multiple adapters, subject to be factored out.
    private Boolean prepareAggregates(DbmsDataReaderBuilder builder, SelectDescriptor select) {
        // adopt for other types of queries, variable, join, etc
        for(PerspectiveAttributeDescriptor attribute: select.getProjectionClause().getPerspective().getAttributes().values()){
            AggregationFunctionVisitor visitor = new AggregationFunctionVisitor(attribute.getId());
            attribute.getForwardExpression().accept(visitor);
            if(visitor.getAggregattionCallInfo().size() > 0){
                aggregattionCallInfo.addAll(visitor.getAggregattionCallInfo());                
            } else {// the attribute does not contain any aggregate, it should be considered as a group by item. preserve and merge it with group by list, later
                groupByImplicitAttributes.add(attribute.getId());
            }
        }
        // if there is no aggregate function discovered, there is no need to do anything else, 
        // also the group by items found above, are not needed anymore
        if(aggregattionCallInfo.size() <= 0){
            // remove the group by list items, too
            groupByAttributes.clear();
            groupByImplicitAttributes.clear();
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

    private void prepareGroupBy(DbmsDataReaderBuilder builder, SelectDescriptor select) {
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
        }
    }

    private void prepareLimit(DbmsDataReaderBuilder builder, SelectDescriptor select) {
        if(isSupported("select.limit")){
            builder.skip(select.getLimitClause().getSkip())
                   .take(select.getLimitClause().getTake());
        }
        else{
            builder.skip(-1)
                   .take(-1);
        }
    }    
}
