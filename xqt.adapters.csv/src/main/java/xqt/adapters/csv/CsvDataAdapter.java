/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.adapters.csv;

import com.vaiona.commons.data.AttributeInfo;
import com.vaiona.commons.logging.LoggerHelper;
import com.vaiona.csv.reader.DataReader;
import com.vaiona.csv.reader.DataReaderBuilder;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import xqt.model.adapters.AdapterInfo;
import xqt.model.adapters.BaseDataAdapter;
import xqt.model.adapters.DataAdapter;
import xqt.model.containers.DataContainer;
import xqt.model.containers.JoinedContainer;
import xqt.model.containers.JoinedContainer.JoinOperator;
import xqt.model.containers.SingleContainer;
import xqt.model.containers.VariableContainer;
import xqt.model.conversion.ConvertSelectElement;
import xqt.model.data.Resultset;
import xqt.model.data.ResultsetType;
import xqt.model.data.Variable;
import xqt.model.declarations.PerspectiveAttributeDescriptor;
import xqt.model.declarations.PerspectiveDescriptor;
import xqt.model.exceptions.LanguageException;
import xqt.model.exceptions.LanguageExceptionBuilder;
import xqt.model.expressions.AggregationFunctionVisitor;
import xqt.model.expressions.Expression;
import xqt.model.functions.AggregationCallInfo;
import xqt.model.functions.aggregates.Average;
import xqt.model.functions.aggregates.Minimum;
import xqt.model.functions.aggregates.Sum;
import xqt.model.statements.query.GroupEntry;
import xqt.model.statements.query.SelectDescriptor;

/**
 *
 * @author standard
 */
public class CsvDataAdapter extends BaseDataAdapter {//implements DataAdapter {

    private CsvDataAdapterHelper helper = null;
    private DataReaderBuilder builder = null;
    
    public CsvDataAdapter(){
        needsMemory = false; // default is false, too.
        helper = new CsvDataAdapterHelper();
        runtimeJoinOperators.put(JoinOperator.EQ, "==");
        runtimeJoinOperators.put(JoinOperator.NotEQ, "!=");
        runtimeJoinOperators.put(JoinOperator.GT, ">");
        runtimeJoinOperators.put(JoinOperator.GTEQ, ">=");
        runtimeJoinOperators.put(JoinOperator.LT, "<");
        runtimeJoinOperators.put(JoinOperator.LTEQ, "<=");
        LoggerHelper.logDebug(MessageFormat.format("The CSV adapter encapsulated in the class: {0} was successfully instantiated.", CsvDataAdapter.class.getName()));        
    }

    @Override
    public Resultset run(SelectDescriptor select, Object context) {
        LoggerHelper.logDebug(MessageFormat.format("The CSV adapter started running the statement {0}.",select.getId()));        
        Resultset resultset = null;
        switch (select.getSourceClause().getContainer().getDataContainerType()) {
            case Single:
                resultset = runForSingleContainer(select, context);
                break;
            case Joined:
                resultset = runForJoinedContainer(select, context);
                break;
            case Variable:
                if(select.getTargetClause().getContainer().getDataContainerType() == DataContainer.DataContainerType.Single){
                    try {
                        resultset = runForVariable_SingleContainer(select, context);
                    } catch (IOException ex) {
                        resultset = null;
                    }
                }
                break;
            default:
                resultset = null;
        }
        LoggerHelper.logDebug(MessageFormat.format("The CSV adapter finished running statement {0}.",select.getId()));
        LoggerHelper.logDebug(MessageFormat.format("statement {0} execution had {1} result.", select.getId(), resultset == null? "no": "a"));        
        return resultset;
    }

    @Override
    public Resultset complement(SelectDescriptor select, Variable sourceVariable){
        Resultset resultSet = new Resultset(ResultsetType.Tabular); 
        List<Object> source = (List<Object>)sourceVariable.getResult().getTabularData(); // for testing purpose, it just returns the source
        if(source == null || source.stream().count() <= 0){  
            return null;
//                        resultSet.setData(null);
//                        resultSet.setSchema(sourceVariable.getResult().getSchema());
        } else {
            try{
                Class entryPoint = select.getExecutionInfo().getExecutionSource().getCompiledClass();            
                DataReader reader = builder.build(entryPoint);
                List<Object> result = reader
                    .target(helper.getCompleteTargetName(select.getTargetClause()))
                    .read(source, null);
                if(result == null)
                    return null;
                resultSet.setData(result);
                resultSet.setSchema(sourceVariable.getResult().getSchema());                               
            } catch (ClassNotFoundException | IOException | IllegalAccessException | IllegalArgumentException 
                    | InstantiationException | NoSuchMethodException | InvocationTargetException ex) {
                // do something here!!  
                return null;
            } catch (Exception ex){
                return null;
            }
        }
        //resultSet.setSchema(prepareSchema(select));
        return resultSet;
    }
    
    @Override
    public void prepare(SelectDescriptor select, Object context) {
        // check whether the source is a simple or a joined one!
        LoggerHelper.logDebug(MessageFormat.format("The CSV adapter started preparing statement {0}",select.getId()));        
        try{
            builder = new DataReaderBuilder();
            builder
                //.baseClassName("GeneratedX") // let the builder name the classes automatically
                .dateFormat("yyyy-MM-dd'T'HH:mm:ssX") //check the timezone formatting
                //.addProjection("MAX", "SN")// MIN, SUM, COUNT, AVG, 
                .namespace("com.vaiona.csv.reader")
                .entityResourceName("Entity")
                .dialect(dialect)
                .namesCaseSensitive(false)
            ;
            switch (select.getSourceClause().getContainer().getDataContainerType()) {
                case Single:
                    prepareSingle(select);
                    break;
                case Joined:
                    prepareJoined(select);
                    break;
                case Variable:
                    // Its one of these cases: 
                    // 1: the result set of a previous statement is going to be persisted
                    // 2: a statement with target clause of type container is broken into multiple parts because of the adapter capabilities. This is the last part that writes the final result into a container
                    if(select.getTargetClause().getContainer().getDataContainerType() == DataContainer.DataContainerType.Single){
                        try {
                            prepareVariable(select);
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
            LanguageException lex=    LanguageExceptionBuilder.builder()
                    .setMessageTemplate(ex.getMessage())
                    .setContextInfo1(select.getId())
                    .setLineNumber(select.getParserContext().getStart().getLine())
                    .setColumnNumber(select.getParserContext().getStop().getCharPositionInLine())
                    .build();
            select.getLanguageExceptions().add(lex);
            LoggerHelper.logError(MessageFormat.format("The CSV adapter was not able to prepare statement {0}. Cause: {1}",select.getId(), lex.getMessage()));                    
        }      
        LoggerHelper.logDebug(MessageFormat.format("The CSV adapter prepared statement {0}",select.getId()));                
    }

    @Override
    public void setup(Map<String, Object> config) {
        registerCapability("select.qualifier", false);
        registerCapability("select.projection.perspective", true);
        registerCapability("select.projection.perspective.explicit", true);
        registerCapability("select.projection.perspective.implicit", true);
        registerCapability("select.projection.perspective.inline", true);
        registerCapability("function", true);
        registerCapability("function.default.max", true); // add other functions, too.
        registerCapability("expression", true);
        
        registerCapability("select.source.single", true);
        registerCapability("select.source.joined", true);
        registerCapability("select.source.variable", true);

        registerCapability("select.target.single", true);
        registerCapability("select.target.joined", false);
        registerCapability("select.target.variable", true);
        registerCapability("select.target.plot", false);
        
        registerCapability("select.anchor", false);
        registerCapability("select.filter", true);
        registerCapability("select.orderby", true);
        registerCapability("select.groupby", true);
        registerCapability("select.limit", true);
        registerCapability("select.limit.take", true);
        registerCapability("select.limit.skip", true);        
    }

    private void prepareSingle(SelectDescriptor select) {
        SingleContainer container =((SingleContainer)select.getSourceClause().getContainer());
        try {
            String columnDelimiter = container.getBinding().getConnection().getParameters().get("delimiter").getValue();
            builder.columnDelimiter(determineDeleimiter(columnDelimiter));
        } catch(Exception ex){
            builder.columnDelimiter(",");
        }
        builder.sourceOfData("container");
        try{
            builder.addFields(helper.prepareFields(container, builder.getColumnDelimiter(), builder.getTypeDelimiter(), builder.getUnitDelimiter()));
            if(select.getProjectionClause().isPresent() == false 
                    && select.getProjectionClause().getPerspective().getPerspectiveType() == 
                    PerspectiveDescriptor.PerspectiveType.Implicit) {
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
                
                prepareOrderBy(builder, select);
                
            } else {
                builder.readerResourceName("Reader");
                // check whether all the field references in the mappings, are valid by making sure they are in the Fields list.
                attributeInfos = convertSelect.prepareAttributes(select.getProjectionClause().getPerspective(), this, false);            
                builder.addResultAttributes(attributeInfos);
                builder.getResultAttributes().values().stream().forEach(at -> {
                    at.internalDataType = helper.getPhysicalType(at.conceptualDataType);
                });
                prepareGroupBy(builder, select);
                prepareOrderBy(builder, select);
            
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

    private void prepareJoined(SelectDescriptor select) {
        builder.readerResourceName("JoinReader");
        JoinedContainer join = ((JoinedContainer)select.getSourceClause().getContainer());
        
        if(join.getLeftContainer().getDataContainerType() != DataContainer.DataContainerType.Single){
            select.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate("A single (persistent) container is expected on the left side of the JOIN.")
                    .setContextInfo1(select.getId())
                    .setLineNumber(select.getParserContext().getStart().getLine())
                    .setColumnNumber(-1)
                    .build()
                );    
            return;
        }
        SingleContainer leftContainer = (SingleContainer)join.getLeftContainer();
        
        if(join.getRightContainer().getDataContainerType() != DataContainer.DataContainerType.Single){
            select.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate("A single (persistent) container is expected on the right side of the JOIN.")
                    .setContextInfo1(select.getId())
                    .setLineNumber(select.getParserContext().getStart().getLine())
                    .setColumnNumber(-1)
                    .build()
                );    
            return;
        }
        SingleContainer rightContainer = (SingleContainer)join.getRightContainer();

        try {
            String columnDelimiter = leftContainer.getBinding().getConnection().getParameters().get("delimiter").getValue();
            builder.leftColumnDelimiter(determineDeleimiter(columnDelimiter));                
            columnDelimiter = rightContainer.getBinding().getConnection().getParameters().get("delimiter").getValue();
            builder.rightColumnDelimiter(determineDeleimiter(columnDelimiter));                
        } catch(Exception ex){
            builder.leftColumnDelimiter(",");
            builder.rightColumnDelimiter(",");
        }

        try{
            builder.addLeftFields(helper.prepareFields(leftContainer, builder.getLeftColumnDelimiter(), builder.getTypeDelimiter(), builder.getUnitDelimiter()));
            builder.addRightFields(helper.prepareFields(rightContainer, builder.getRightColumnDelimiter(), builder.getTypeDelimiter(), builder.getUnitDelimiter()));
           
            // it is sopposed that the perspective oject is set to null during the gramar visitation, if not appreaed in the join statement
            if(leftContainer.getPerspective() == null) {
                leftContainer.setPerspective(helper.createPhysicalPerspective(builder.getLeftFields(), null, "left_" + select.getId()));
            }
            if(rightContainer.getPerspective() == null) {
                rightContainer.setPerspective(helper.createPhysicalPerspective(builder.getRightFields(), null, "right_" + select.getId()));
            }
            
            // compile an implicit perspective for the whole select statement
            select.getProjectionClause().setPerspective(
                    PerspectiveDescriptor.combinePerspective(
                            select.getProjectionClause().getPerspective(), leftContainer.getPerspective(), rightContainer.getPerspective(), "joined_" + select.getId()
                    ));
            select.getProjectionClause().setPresent(true);
            // filter, ordering, and grouping may face attribute rename issues because of the combined attributes of the left and right.
            // they should be renamed accordingly
            // select.repair();
            select.validate();
            if(select.hasError())
                return;
            // check whether all the field references in the mappings, are valid by making sure they are in the Fields list.
            
            //builder.leftClassName(select.getDependsUpon().getEntityType().getFullName());
            //builder.rightClassName(select.getDependsUpon2().getEntityType().getFullName());

            Map<String, AttributeInfo>  attributes = convertSelect.prepareAttributes(select.getProjectionClause().getPerspective(), this, false);            
            builder.addResultAttributes(attributes);
            builder.getResultAttributes().values().stream().forEach(at -> {
                at.internalDataType = helper.getPhysicalType(at.conceptualDataType);
            });

            try{
                if(isSupported("select.filter")) 
                builder.where(convertSelect.prepareWhere(select.getFilterClause(), this), true);
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
            
            Map<AttributeInfo, String> orderItems = new LinkedHashMap<>();        
            for (Map.Entry<String, String> entry : convertSelect.prepareOrdering(select.getOrderClause()).entrySet()) {
                    if(attributes.containsKey(entry.getKey())){
                        orderItems.put(attributes.get(entry.getKey()), entry.getValue());
                    }            
            }

            if(isSupported("select.orderby")) builder.orderBy(orderItems);
            else builder.orderBy(null);
            
            prepareLimit(builder, select);
            builder.writeResultsToFile(convertSelect.shouldResultBeWrittenIntoFile(select.getTargetClause()));

            builder.joinType(join.getJoinType().toString())
                    .joinOperator(runtimeJoinOperators.get(join.getJoinOperator()))
                    .leftJoinKey(join.getLeftKey().getId())
                    .rightJoinKey(join.getRightKey().getId());
            
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

    private String determineDeleimiter(String delimiter){
        switch (delimiter){ // register these cases as a map
            case "comma": 
                return(",");
            case "tab": 
                return("\t");
            case "blank":
                return(" ");
            case "semicolon":
                return(";");
            default:
                return(delimiter);
        }                                                
    }
   
    private void prepareVariable(SelectDescriptor select) throws Exception {
        // the source is a variable and the target is single container
        SingleContainer container =((SingleContainer)select.getTargetClause().getContainer());
        try {
            String columnDelimiter = container.getBinding().getConnection().getParameters().get("delimiter").getValue();
            builder.columnDelimiter(determineDeleimiter(columnDelimiter));
        } catch(Exception ex){
            builder.columnDelimiter(",");
        }
        builder.entityResourceName("");
        builder.readerResourceName("Reader");
        builder.sourceOfData("variable");
        
        String sourceRowType = select.getEntityType().getFullName();                    
        if(sourceRowType.isEmpty())
            throw new Exception("No dependecy trace is found in statement %s"); // is caught by the next catch block
        builder.sourceRowType(sourceRowType);
        try{
            //builder.addFields();
            // check whether all the field references in the mappings, are valid by making sure they are in the Fields list.
            Map<String, AttributeInfo>  attributes = convertSelect.prepareAttributes(select.getProjectionClause().getPerspective(), this, false);            
            builder.addResultAttributes(attributes);
            builder.getResultAttributes().values().stream().forEach(at -> {
                at.internalDataType = helper.getPhysicalType(at.conceptualDataType);
            });

            if(isSupported("select.filter")) builder.where(convertSelect.prepareWhere(select.getFilterClause(), this), true);
            else builder.where("", true);
            
            Map<AttributeInfo, String> orderItems = new LinkedHashMap<>();        
            for (Map.Entry<String, String> entry : convertSelect.prepareOrdering(select.getOrderClause()).entrySet()) {
                    if(attributes.containsKey(entry.getKey())){
                        orderItems.put(attributes.get(entry.getKey()), entry.getValue());
                    }            
            }

            if(isSupported("select.orderby")) builder.orderBy(orderItems);
            else builder.orderBy(null);

            prepareLimit(builder, select);
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

    private Resultset runForSingleContainer(SelectDescriptor select, Object context) {
        try{
            Class entryPoint = select.getExecutionInfo().getExecutionSource().getCompiledClass();
            DataReader<Object, Object, Object> reader = builder.build(entryPoint);
            if(reader != null){
                // when the reader is built, it can be used nutiple time having different CSV settings
                // as long as the query has not changed. means the reader can read/ query different files the share the same column info
                // but maybe different delimiter, etc.
                List<Object> result = reader
                        // in XQt usage scenarios these methods should not be called. instead they are called on the builder
                        // ====================================================>
                        //.columnDelimiter(",") // set during build
                        //.quoteDelimiter("\"")
                        //.unitDelimiter("::")
                        // <====================================================
                        .source(helper.getCompleteSourceName(((SingleContainer)select.getSourceClause().getContainer())))
                        .target(helper.getCompleteTargetName(select.getTargetClause()))
                        // pass th target file
                        .bypassFirstRow(helper.isFirstRowHeader(((SingleContainer)select.getSourceClause().getContainer())))
                        .trimTokens(true) // default is true
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
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException ex) {
            select.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate("Statement could not be translated. Technical details: " + ex.getMessage())
                    .setContextInfo1(select.getId())
                    .setLineNumber(select.getParserContext().getStart().getLine())
                    .setColumnNumber(select.getParserContext().getStop().getCharPositionInLine())
                    .build()
            );            
        }
        catch (IOException ex){
            select.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate(ex.getMessage())
                    .setContextInfo1(select.getId())
                    .setLineNumber(select.getParserContext().getStart().getLine())
                    .setColumnNumber(-1)
                    .build()
            );                        
        }
        return null;    
    }

    private Resultset runForVariable_SingleContainer(SelectDescriptor select, Object context) throws IOException {
        try{
            Class entryPoint = select.getExecutionInfo().getExecutionSource().getCompiledClass();
            DataReader<Object, Object, Object> reader = builder.build(entryPoint);
            if(reader != null){
                Map<String, Variable> memory = (Map<String, Variable>)context;
                Variable sourceVariable = (Variable)memory.get(((VariableContainer)select.getSourceClause().getContainer()).getVariableName());
                List<Object> source = (List<Object>)sourceVariable.getResult().getTabularData(); // for testing purpose, it just returns the source
                Resultset resultSet = new Resultset(ResultsetType.Tabular); 
                List<Object> result = reader // do not check for source == null, and do not bypass this case. the adapter needs to do somethinf even when the source is null
                    .target(helper.getCompleteTargetName(select.getTargetClause()))
                    .read(source, null);
                if(result == null)
                    return null;
                resultSet.setData(result);
                resultSet.setSchema(sourceVariable.getResult().getSchema());                               
                return resultSet;
            }else {
                return null;
            }
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | 
                InstantiationException | NoSuchMethodException | InvocationTargetException ex) {
            select.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate("Statement could not be translated. Technical details: " + ex.getMessage())
                    .setContextInfo1(select.getId())
                    .setLineNumber(select.getParserContext().getStart().getLine())
                    .setColumnNumber(select.getParserContext().getStop().getCharPositionInLine())
                    .build()
            );            
        }
        catch (IOException ex){
            select.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate(ex.getMessage())
                    .setContextInfo1(select.getId())
                    .setLineNumber(select.getParserContext().getStart().getLine())
                    .setColumnNumber(-1)
                    .build()
            );                        
        }
        return null;    
    }
    
    private Resultset runForJoinedContainer(SelectDescriptor select, Object context) {
        try{
            Class entryPoint = select.getExecutionInfo().getExecutionSource().getCompiledClass();
            JoinedContainer join = ((JoinedContainer)select.getSourceClause().getContainer());
            DataReader<Object, Object, Object> reader = builder.build(entryPoint);
            if(reader != null){
                List<Object> result = reader
                    .source(helper.getCompleteSourceName((SingleContainer)join.getLeftContainer()))
                    .sourceRight(helper.getCompleteSourceName((SingleContainer)join.getRightContainer()))    
                    .target(helper.getCompleteTargetName(select.getTargetClause()))
                    // pass th target file
                    .bypassFirstRow(helper.isFirstRowHeader((SingleContainer)join.getLeftContainer()))
                    .bypassFirstRowRight(helper.isFirstRowHeader((SingleContainer)join.getRightContainer()))
                    .trimTokens(true) // default is true
                    .read(null, null);
                
                if(result != null){
                    Resultset resultSet = new Resultset(ResultsetType.Tabular); 
                    resultSet.setData(result);
                    resultSet.setSchema(select.getProjectionClause().getPerspective().createSchema());
                    return resultSet;
                }else {
                    return null;
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException ex) {
            select.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate("Statement could not be translated. Technical details: " + ex.getMessage())
                    .setContextInfo1(select.getId())
                    .setLineNumber(select.getParserContext().getStart().getLine())
                    .setColumnNumber(select.getParserContext().getStop().getCharPositionInLine())
                    .build()
            );            
        }
        catch (IOException ex){
            select.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate(ex.getMessage())
                    .setContextInfo1(select.getId())
                    .setLineNumber(select.getParserContext().getStart().getLine())
                    .setColumnNumber(-1)
                    .build()
            );                        
        }
        return null;    
    }

}
