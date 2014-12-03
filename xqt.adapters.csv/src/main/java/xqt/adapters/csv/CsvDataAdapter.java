/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.adapters.csv;

import com.vaiona.commons.data.FieldInfo;
import com.vaiona.commons.types.TypeSystem;
import com.vaiona.csv.reader.DataReader;
import com.vaiona.csv.reader.DataReaderBuilder;
import com.vaiona.csv.reader.HeaderBuilder;
import com.vaiona.csv.reader.TestEntity;
import com.vaiona.csv.reader.TestEntityJoin;
import com.vaiona.csv.reader.TestReader;
import com.vaiona.csv.reader.TestReaderJoin;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.rythmengine.internal.parser.Patterns;
import xqt.model.adapters.DataAdapter;
import xqt.model.containers.DataContainer;
import xqt.model.containers.JoinedContainer;
import xqt.model.containers.SingleContainer;
import xqt.model.conversion.ConvertSelectElement;
import xqt.model.data.Resultset;
import xqt.model.data.ResultsetType;
import xqt.model.data.SchemaItem;
import xqt.model.data.Variable;
import xqt.model.declarations.PerspectiveAttributeDescriptor;
import xqt.model.declarations.PerspectiveDescriptor;
import xqt.model.exceptions.LanguageExceptionBuilder;
import xqt.model.expressions.Expression;
import xqt.model.expressions.MemberExpression;
import xqt.model.statements.query.SelectDescriptor;

/**
 *
 * @author standard
 */
public class CsvDataAdapter implements DataAdapter {

    private ConvertSelectElement convertSelect = null;
    private CsvDataAdapterHelper helper = null;
    private DataReaderBuilder builder = null;
    
    public CsvDataAdapter(){
        convertSelect = new ConvertSelectElement();
        helper = new CsvDataAdapterHelper();
    }

    @Override
    public Resultset run(SelectDescriptor select, Object context) {
        switch (select.getSourceClause().getContainer().getDataContainerType()) {
            case Single:
                return runForSingleContainer(select, context);
            case Joined:
                TestReaderJoin joinedReader = new TestReaderJoin();
                {
                    try {
                        List<TestEntityJoin> result = joinedReader.read();
                    } catch (IOException ex) {
                        Logger.getLogger(CsvDataAdapter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                return null;
            default:
                return null;
        }
    }

    @Override
    public Resultset complement(SelectDescriptor select, Variable variable){
        return null;
    }
    
    @Override
    public void prepare(SelectDescriptor select) {
        // check whether the source is a simple or a joined one!
        try{
            builder = new DataReaderBuilder();
            builder
                //.baseClassName("GeneratedX") // let the builder name the classes automatically
                .dateFormat("yyyy-MM-dd'T'HH:mm:ssX") //check the timezone formatting
                //.addProjection("MAX", "SN")// MIN, SUM, COUNT, AVG, 
            ;
            switch (select.getSourceClause().getContainer().getDataContainerType()) {
                case Single:
                    prepareSingle(select);
                    break;
                case Joined:
                    prepareJoined(select);
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

////@Override
//    public Resultset run2(SelectDescriptor select) {
//        try{
//            TestReader reader = new TestReader();
//            if(reader != null){
//                List<TestEntity> result = reader
//                    .source(helper.getCompleteSourceName(select.getSourceClause()))
//                    .target(helper.getCompleteTargetName(select.getTargetClause()))
//                    .bypassFirstRow(firstRowIsHeader)
//                    .trimTokens(true) // default is true
//                    .read();
//                
//                if(result != null){
//                    Resultset resultSet = new Resultset(ResultsetType.Tabular);
//                    resultSet.setData(result);
//                    resultSet.setSchema(helper.prepareSchema(select.getProjectionClause().getPerspective()));
//                    return resultSet;
//                }else {
//                    return null;
//                }
//            }
//        } catch (Exception ex1) {
//            Logger.getLogger(CsvDataAdapter.class.getName()).log(Level.SEVERE, null, ex1);
//            // throw a proper exception
//        }
//        return null;        
//    }  

    private void prepareLimit(DataReaderBuilder builder, SelectDescriptor select) {
        if(isSupported("select.limit")){
            builder.skip(select.getLimitClause().getSkip())
                   .take(select.getLimitClause().getTake());
        }
        else{
            builder.skip(-1)
                   .take(-1);
        }
    }

    @Override
    public boolean needsMemory() {
        return false;
    }

    private final HashMap<String, Boolean> capabilities = new HashMap<>();
    
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
    public void setup(Map<String, Object> config) {
        registerCapability("select.qualifier", false);
        registerCapability("select.projection.perspective", true);
        registerCapability("select.projection.perspective.explicit", true);
        registerCapability("select.projection.perspective.implicit", true);
//        registerCapability("select.projection.perspective.inline", true);
        registerCapability("function", true);
        registerCapability("function.default.max", false); // add other functions, too.
        registerCapability("expression", true);
        
        registerCapability("select.source.single", true);
        registerCapability("select.source.joined", true);
        registerCapability("select.source.variable", true);

        registerCapability("select.target.simplecontainer", true);
        registerCapability("select.target.joinedcontainer", false);
        registerCapability("select.target.variable", true);
        registerCapability("select.target.plot", false);
        
        registerCapability("select.anchor", false);
        registerCapability("select.filter", true);
        registerCapability("select.orderby", true);
        registerCapability("select.groupby", false);
        registerCapability("select.limit", false);
        
    }

    @Override
    public boolean hasRequiredCapabilities(SelectDescriptor select) {
        boolean allmatched = select.getRequiredCapabilities().stream().allMatch(p-> this.isSupported(p));
        return allmatched;
    }

    private void prepareSingle(SelectDescriptor select) {
        SingleContainer container =((SingleContainer)select.getSourceClause().getContainer());
        try {
            String columnDelimiter = container.getBinding().getConnection().getParameters().get("delimiter").getValue();
            builder.columnDelimiter(determineDeleimiter(columnDelimiter));
        } catch(Exception ex){
            builder.columnDelimiter(",");
        }

        try{
            builder.addFields(helper.prepareFields(container, builder.getColumnDelimiter(), builder.getTypeDelimiter(), builder.getUnitDelimiter()));
            if(select.getProjectionClause().isPresent() == false 
                    && select.getProjectionClause().getPerspective().getPerspectiveType() == PerspectiveDescriptor.PerspectiveType.Implicit) {
                select.getProjectionClause().setPerspective(helper.createPhysicalPerspective(builder.getFields(), select.getProjectionClause().getPerspective(), select.getId()));
                select.getProjectionClause().setPresent(true);
                select.validate();
                if(select.hasError())
                    return;
            }
            builder.setAttributes(convertSelect.prepareAttributes(select.getProjectionClause().getPerspective()));
            builder.getAttributes().values().stream().forEach(at -> {
                at.internalDataType = helper.getPhysicalType(at.conceptualDataType);
            });
            builder.where(convertSelect.prepareWhere(select.getFilterClause()));            
            builder.setOrdering(convertSelect.prepareOrdering(select.getOrderClause()));
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
   
    private void prepareJoined(SelectDescriptor select) {
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
            builder.addLeftFields(helper.prepareFields(leftContainer, builder.getColumnDelimiter(), builder.getTypeDelimiter(), builder.getUnitDelimiter()));
            builder.addRightFields(helper.prepareFields(rightContainer, builder.getColumnDelimiter(), builder.getTypeDelimiter(), builder.getUnitDelimiter()));
           
            // it is sopposed that the perspective oject is set to null during the gramar visitation, if not appreaed in the join statement
            if(leftContainer.getPerspective() == null) {
                leftContainer.setPerspective(helper.createPhysicalPerspective(builder.getLeftFields(), null, "left_" + select.getId()));
            }
            if(rightContainer.getPerspective() == null) {
                rightContainer.setPerspective(helper.createPhysicalPerspective(builder.getRightFields(), null, "right_" + select.getId()));
            }
            
            // compile an implicit perspective for the whole select statement
            select.getProjectionClause().setPerspective(
                    helper.combinePerspective(
                            select.getProjectionClause().getPerspective(), leftContainer.getPerspective(), rightContainer.getPerspective(), "joined_" + select.getId()
                    ));
            select.getProjectionClause().setPresent(true);
            // filter, ordering, and grouping may face attribute rename issues because of the combined attributes of the left and right.
            // they should be renamed accordingly
            // select.repair();
            select.validate();
            if(select.hasError())
                return;

            builder.setAttributes(convertSelect.prepareAttributes(select.getProjectionClause().getPerspective()));
            builder.getAttributes().values().stream().forEach(at -> {
                at.internalDataType = helper.getPhysicalType(at.conceptualDataType);
            });
            builder.where(convertSelect.prepareWhere(select.getFilterClause()));            
            builder.setOrdering(convertSelect.prepareOrdering(select.getOrderClause()));
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
            Class entryPoint = select.getExecutionInfo().getSources().values().stream()
                    .filter(p-> p.isEntryPoint() == true).findFirst().get().getCompiledClass();
            DataReader<Object> reader = builder.build(entryPoint);
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
                        .read();
                
                //System.out.println("The result set contains " + result.stream().count() + " records.");
                if(result != null){
                    Resultset resultSet = new Resultset(ResultsetType.Tabular); 
                    resultSet.setData(result);
                    resultSet.setSchema(helper.prepareSchema(select.getProjectionClause().getPerspective()));
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
