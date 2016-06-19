/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.adapters.dbms;

import com.vaiona.commons.data.AttributeInfo;
import com.vaiona.commons.data.DataReaderBuilderBase;
import com.vaiona.commons.logging.LoggerHelper;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import xqt.model.adapters.AdapterInfo;
import xqt.model.adapters.BaseDataAdapter;
import xqt.model.containers.DataContainer;
import xqt.model.containers.JoinedContainer;
import xqt.model.containers.SingleContainer;
import xqt.model.conversion.ConvertSelectElement;
import xqt.model.data.Resultset;
import xqt.model.data.ResultsetType;
import xqt.model.data.Variable;
import xqt.model.declarations.PerspectiveAttributeDescriptor;
import xqt.model.declarations.PerspectiveDescriptor;
import xqt.model.exceptions.LanguageExceptionBuilder;
import xqt.model.expressions.AggregationFunctionVisitor;
import xqt.model.expressions.Expression;
import xqt.model.expressions.ExpressionType;
import xqt.model.functions.AggregationCallInfo;
import xqt.model.statements.query.GroupEntry;
import xqt.model.statements.query.SelectDescriptor;

/**
 *
 * @author Javad Chamanara <chamanara@gmail.com>
 */
public class DbmsDataAdapter extends BaseDataAdapter { //implements DataAdapter{
    private DbmsDataReaderBuilder builder = null;
    private DbmsDataAdapterHelper helper = null;
        
    public DbmsDataAdapter(){
    	dialect = "default";
    	needsMemory = false;
        convertSelect = new ConvertSelectElement();
        runtimeJoinOperators.put(JoinedContainer.JoinOperator.EQ, "==");
        runtimeJoinOperators.put(JoinedContainer.JoinOperator.NotEQ, "!=");
        runtimeJoinOperators.put(JoinedContainer.JoinOperator.GT, ">");
        runtimeJoinOperators.put(JoinedContainer.JoinOperator.GTEQ, ">=");
        runtimeJoinOperators.put(JoinedContainer.JoinOperator.LT, "<");
        runtimeJoinOperators.put(JoinedContainer.JoinOperator.LTEQ, "<="); 
        LoggerHelper.logDebug(MessageFormat.format("The DBMS adapter encapsulated in the class: {0} was successfully instantiated.", DbmsDataAdapter.class.getName()));        
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
        registerCapability("select.orderby", false);
        registerCapability("select.groupby", true);
        registerCapability("select.limit", false);
        registerCapability("select.limit.take", false);
        registerCapability("select.limit.skip", false);
    }

    @Override
    public Resultset run(SelectDescriptor select, Object context) {
        LoggerHelper.logDebug(MessageFormat.format("The DBMS adapter started running statement {0}.",select.getId()));        
        Resultset resultset = null;
        switch (select.getSourceClause().getContainer().getDataContainerType()) {
            case Single:
                resultset = runForSingleContainer(select, context);
                break;
            case Joined:
//                resultset = runForJoinedContainer(select, context);
                break;
            case Variable:
                if(select.getTargetClause().getContainer().getDataContainerType() == DataContainer.DataContainerType.Single){
//                    try {
//                        resultset = runForVariable_SingleContainer(select, context);
//                    } catch (IOException ex) {
//                        resultset = null;
//                    }
                }
                break;
            default:
                resultset = null;
        }
        LoggerHelper.logDebug(MessageFormat.format("The DBMS adapter finished running statement {0}.",select.getId()));
        LoggerHelper.logDebug(MessageFormat.format("statement {0} execution had {1} result.", select.getId(), resultset == null? "no": "a"));        
        return resultset;
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
                .dateFormat("yyyy-MM-dd") // 'T'HH:mm:ssX  check the timezone formatting
                .namespace("xqt.adapters.dbms")
                .statementId(select.getId())
                .dialect(dialect)
                .namesCaseSensitive(false) // maybe need to be true for PgS
            ;
            switch (select.getSourceClause().getContainer().getDataContainerType()) {
                case Single:
                    prepareSingle(select);
                    break;
                case Joined:
                    //prepareJoined(select);
                // do not forget to set this inside the function - > .entityResourceName(helper.getEntityResourceName())

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
    
    // if needed develop an adapter specific implementation of prepareAggregates
    
    private void prepareSingle(SelectDescriptor select) {
        SingleContainer container =((SingleContainer)select.getSourceClause().getContainer());
        try{
            helper = DbmsDataAdapterHelper.getConcreteHelper(container);
            builder.registerQueryHelper(helper)                   
                   .containerName(container.getContainerName())
                   .addFields(helper.getContinerSchema(container))
                   .entityResourceName(helper.getEntityResourceName())
                   .sourceOfData("container") 
                    ;
        
            if(select.getProjectionClause().isPresent() == false 
                    && select.getProjectionClause().getPerspective().getPerspectiveType() == PerspectiveDescriptor.PerspectiveType.Implicit) {
                select.getProjectionClause().setPerspective(
                        helper.createPhysicalPerspective(builder.getFields(), select.getProjectionClause().getPerspective(), select.getId()));
                select.getProjectionClause().setPresent(true);
                select.validate();
                if(select.hasError())
                    return;
            } else { // see whether there exists any attribute of unknown type!
                select.getProjectionClause().setPerspective(
                    helper.improvePerspective(builder.getFields(), select.getProjectionClause().getPerspective()));
                // extract referenced fields from the perspective and other clauses, then remove the not-used ones from the field list
            }
            builder.connectionString(helper.getConnectionString(container))
                   .username(helper.getContainerUsername(container))
                   .password(helper.getContainerPassword(container))
                   .dbProvider(DBMSDialect.getEnum(helper.getContainerDialectId(container)));
            // aggregate functions in the perspective should be be handled here. also other prepare functions and adapters should do it properly
            Boolean hasAggregates = prepareAggregates(builder, select);
            if(hasAggregates){
                builder.readerResourceName(helper.getAggregateReaderResourceName());
                builder.addAggregates(aggregattionCallInfo);
                // send the aggregate perspective
                // check whether all the field references in the mappings, are valid by making sure they are in the Fields list.
                Map<String, AttributeInfo> rowEntityattributeInfos = new HashMap<String, AttributeInfo>();//convertSelect.prepareAttributes(aggregatePerspective, this, false);            
                // set the resultset perspective. 
                // check whether all the field references in the mappings, are valid by making sure they are in the Fields list.
                // maybe preparation is not needed!!!!!!
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

//                // check if there are groupby attributes, add them to the row entity and replace the access method of the result entity
//                if(groupByAttributes != null && groupByAttributes.size() > 0){ // the groupby attributes should be added to the row entity to be used in the group constrcution keys
//                    //replace the forward map of the resultentity to point to the same attribute in the row entity
//                    for(AttributeInfo attInfo: attributeInfos.values()){
//                        if(groupByAttributes.stream().anyMatch(p-> p.name.equals(attInfo.name))){
//                            AttributeInfo tobeAddedToTheRowEntity = new AttributeInfo(attInfo);
//                            rowEntityattributeInfos.put(tobeAddedToTheRowEntity.name, tobeAddedToTheRowEntity);
//                            attInfo.forwardMap = "rowEntity." + attInfo.name; // pointing to a variable of same name in the row entity//attInfo.forwardMap.replaceAll("DONOTCHANGE.([^\\s]*).NOCALL\\s*\\(\\s*([^\\s]*)\\s*\\)", "functions.get(\"$1\").move(rowEntity.$2)");
//                        }
//                    }
//                } 
//                builder.addRowAttributes(rowEntityattributeInfos);
//                builder.getRowAttributes().values().stream().forEach(at -> {
//                    at.internalDataType = helper.getPhysicalType(at.conceptualDataType);
//                });

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
                builder.readerResourceName(helper.getReaderResourceName());
                // check whether all the field references in the mappings, are valid by making sure they are in the Fields list.
                attributeInfos = convertSelect.prepareAttributes(select.getProjectionClause().getPerspective(), this, false);            
                builder.addResultAttributes(attributeInfos);
                for (AttributeInfo at : builder.getResultAttributes().values()) {
                    at.internalDataType = helper.getPhysicalType(at.conceptualDataType);
				}
//                builder.getResultAttributes().values().stream().forEach(at -> {
//                    at.internalDataType = helper.getPhysicalType(at.conceptualDataType);
//                    at.forwardMap = at.name; // experimental code: all the mapping expressions are done by the DB.
//                });
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
            //all attributes referred to from the group by plus, 
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

    @Override
    protected Boolean prepareAggregates(DataReaderBuilderBase builder, SelectDescriptor select) {
    	//Boolean result = super.prepareAggregates(builder, select);
    	// ------------------------------------
        // adopt for other types of queries, variable, join, etc
        for(PerspectiveAttributeDescriptor attribute: select.getProjectionClause().getPerspective().getAttributes().values()){
            AggregationFunctionVisitor visitor = new AggregationFunctionVisitor(attribute.getId(), this);
            attribute.getForwardExpression().accept(visitor);
            if(visitor.getAggregattionCallInfo().size() > 0){
                aggregattionCallInfo.addAll(visitor.getAggregattionCallInfo());                
            } else {// the attribute is not containing aggregate, it should be considered as a group by item. preserve and check it with group by list, later
                if(!attribute.isAuxiliary())
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
        return true;
    	
    	// ------------------------------------
    	// prepare for DBMS

    }
    
    @Override
    protected void prepareGroupBy(DataReaderBuilderBase builder, SelectDescriptor select) {
    	//super.prepareGroupBy(builder, select);
    	// prepare group by for DBMS
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
    
    private Resultset runForSingleContainer(SelectDescriptor select, Object context) {
        try{
            //SingleContainer container =((SingleContainer)select.getSourceClause().getContainer());
            Class<?> entryPoint = select.getExecutionInfo().getExecutionSource().getCompiledClass();
            DbmsDataReader<Object, Object, Object> reader = builder.build(entryPoint);
            if(reader != null){
                List<Object> result = reader
                        .fields(builder.getFields().values().stream().collect(Collectors.toList()))
                        .attributes(builder.getResultAttributes().values().stream().collect(Collectors.toList()))
                        .connectionString(builder.connectionString) //(helper.getConnectionString(container))
                        .userName(builder.username)//(helper.getContainerUsername(container))
                        .password(builder.password)//(helper.getContainerPassword(container))
                        .read(null, null);
                
                //System.out.println("The result set contains " + result.stream().count() + " records.");
                if(result != null){
                    Resultset resultSet = new Resultset(ResultsetType.Tabular); 
                    resultSet.setData(result);
                    resultSet.setSchema(select.getProjectionClause().getPerspective().createSchema());
                    return resultSet;
                }else {
                    return null;
                }
            }
        } catch (Exception ex) {
            select.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate("Statement could not be translated. Technical details: " + ex.getMessage())
                    .setContextInfo1(select.getId())
                    .setLineNumber(select.getParserContext().getStart().getLine())
                    .setColumnNumber(select.getParserContext().getStop().getCharPositionInLine())
                    .build()
            );            
        }
        return null;    
    }

    public Map<ExpressionType, String> getExpressionPatterns(){
    	Map<ExpressionType, String> patterns = new HashMap<>();
    	// check for supported dialects and return proper patterns accordingly,
    	// current set is PostgreSQL specific!
        patterns.put(ExpressionType.Add, "(( {0} ) + ( {1} ))");
        patterns.put(ExpressionType.And, "(( {0} ) AND ( {1} ))");
        patterns.put(ExpressionType.ArithmeticAnd, "(( {0} ) & ( {1} ))"); //?
        patterns.put(ExpressionType.ArithmeticOr, "(( {0} ) | ( {1} ))");
        //patterns.put(ExpressionType.ArithmeticXor, "(( {0} ) + ( {1} ))");
        patterns.put(ExpressionType.Attribute, "( {0} )");
        patterns.put(ExpressionType.Constant, " {0} ");
        patterns.put(ExpressionType.Divide, "(( {0} ) / ( {1} ))");
        patterns.put(ExpressionType.Equal, "(( {0} ) = ( {1} ))");
        patterns.put(ExpressionType.StringEqual, "(( {0} ) = ( {1} ))"); 
        patterns.put(ExpressionType.Function, "( {0} ( {1} ) )"); // the second arg is the parameters' source
        patterns.put(ExpressionType.GreaterThan, "(( {0} ) > ( {1} ))");
        patterns.put(ExpressionType.GreaterThanOrEqual, "(( {0} ) >= ( {1} ))");
        patterns.put(ExpressionType.LessThan, "(( {0} ) < ( {1} ))");
        patterns.put(ExpressionType.LessThanOrEqual, "(( {0} ) <= ( {1} ))");
        patterns.put(ExpressionType.Member, " {0} "); //maybe type conversion is needed too!
        patterns.put(ExpressionType.Modulo, "(( {0} ) % ( {1} ))");
        patterns.put(ExpressionType.Multiply, "(( {0} ) * ( {1} ))");
        patterns.put(ExpressionType.Negate, "( - ( {0} ))");
        patterns.put(ExpressionType.Not, "( NOT ( {0} ))");
        patterns.put(ExpressionType.NotEqual, "(( {0} ) != ( {1} ))");
        patterns.put(ExpressionType.StringNotEqual, "(( {0} ) != ( {1} ))"); 
        patterns.put(ExpressionType.Or, "(( {0} ) OR ( {1} ))");
        patterns.put(ExpressionType.Parameter, " {0} ");
        patterns.put(ExpressionType.Power, "(power( {0} , {1} ))");
        patterns.put(ExpressionType.Subtract, "(( {0} ) - ( {1} ))");
        patterns.put(ExpressionType.IsNull, "(( {0} ) IS NULL)");
        patterns.put(ExpressionType.IsNumber, "({0} SIMILAR TO ''-?\\d+(\\.\\d+)?'')");
        // <DataType>.isNaN(x) not supported yet
        patterns.put(ExpressionType.IsDate, "(( {0} ) == null)"); // not supported yet
        patterns.put(ExpressionType.IsEmpty, "((( {0} ) IS NULL) OR (length({0}) <= 0))");
       
    	return patterns;
    }
}
