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
    private DataReaderBuilder builder = null;
    private Boolean firstRowIsHeader = true;
    
    public CsvDataAdapter(){
        convertSelect = new ConvertSelectElement();
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

//@Override
    public Resultset run2(SelectDescriptor select) {
        try{
            Boolean firstRowIsHeader = true;            
            
            TestReader reader = new TestReader();
            if(reader != null){
                List<TestEntity> result = reader
                    .source(convertSelect.getCompleteSourceName(select))
                    .target(convertSelect.getCompleteTargetName(select))
                    .bypassFirstRow(firstRowIsHeader)
                    .trimTokens(true) // default is true
                    .read();
                
                if(result != null){
                    Resultset resultSet = new Resultset(ResultsetType.Tabular);
                    resultSet.setData(result);
                    resultSet.setSchema(prepareSchema(select));
                    return resultSet;
                }else {
                    return null;
                }
            }
        } catch (Exception ex1) {
            Logger.getLogger(CsvDataAdapter.class.getName()).log(Level.SEVERE, null, ex1);
            // throw a proper exception
        }
        return null;        
    }  

    private HashSet<SchemaItem> prepareSchema(SelectDescriptor select) {
        // pay attention to aggrgates!
        HashSet<SchemaItem> schema = new LinkedHashSet<>();
        // do not use the functional counterpart, as it uses the streaming method, which doe not guarantee to preserve the order
        for(PerspectiveAttributeDescriptor attribute: select.getProjectionClause().getPerspective().getAttributes().values()){
            SchemaItem sItem = new SchemaItem();
            sItem.setDataType(attribute.getDataType());
            sItem.setName(attribute.getId());
            sItem.setSystemType(TypeSystem.getTypes().get(attribute.getDataType()).getName());
            sItem.setIndex(schema.size());            
            schema.add(sItem); 
        }
        return schema;
    }

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

    private final HashMap<String, List<String>> typeConversion = new HashMap<>();
    
    public String getConceptualType(String physicalType){
        Optional<Entry<String, List<String>>> entry = typeConversion.entrySet().stream()
                .filter(p-> p.getValue().contains(physicalType.toLowerCase())).findFirst();
        if(entry.isPresent()){
            return entry.get().getKey();
        }
        else
            return TypeSystem.Unknown;
    }

    public String getPhysicalType(String conceptualType){
        if(typeConversion.containsKey(conceptualType)){
            return typeConversion.get(conceptualType).get(0); // returns first physical data type by default.
        }
            return TypeSystem.Unknown;
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
        registerCapability("select.orderby", false);
        registerCapability("select.groupby", false);
        registerCapability("select.limit", false);
        
        typeConversion.put(TypeSystem.Boolean,  new ArrayList<String>() {{add("boolean");}});
        typeConversion.put(TypeSystem.Byte,     new ArrayList<String>() {{add("byte");}});
        typeConversion.put(TypeSystem.Date,     new ArrayList<String>() {{add("date");}});
        typeConversion.put(TypeSystem.Integer,  new ArrayList<String>() {{add("integer");add("int");}});
        typeConversion.put(TypeSystem.Long,     new ArrayList<String>() {{add("long");}});
        typeConversion.put(TypeSystem.Real,     new ArrayList<String>() {{add("double");add("real");add("float");}});
        typeConversion.put(TypeSystem.String,   new ArrayList<String>() {{add("string");add("char");}});
    }

    @Override
    public boolean hasRequiredCapabilities(SelectDescriptor select) {
        boolean allmatched = select.getRequiredCapabilities().stream().allMatch(p-> this.isSupported(p));
        return allmatched;
    }

    private void prepareSingle(SelectDescriptor select) {
        try {
            String columnDelimiter = ((SingleContainer)select.getSourceClause().getContainer())
                                        .getBinding().getConnection().getParameters().get("delimiter").getValue();
            switch (columnDelimiter){ // register these cases as a map
                case "comma": 
                    builder.columnDelimiter(",");
                    break;
                case "tab": 
                    builder.columnDelimiter("\t");
                    break;
                case "blank":
                    builder.columnDelimiter(" ");
                    break;
                case "semicolon":
                    builder.columnDelimiter(";");
                    break;
                default:
                    builder.columnDelimiter(columnDelimiter);
                    break;
            }                                        
        } catch(Exception ex){
            builder.columnDelimiter(",");
        }

        try{
            firstRowIsHeader = prepareFields(builder, select);
            if(select.getProjectionClause().isPresent() == false 
                    && select.getProjectionClause().getPerspective().getPerspectiveType() == PerspectiveDescriptor.PerspectiveType.Implicit) {
                createPhysicalPerspective(select);
            }
            builder.setAttributes(convertSelect.prepareAttributes(select));
            builder.getAttributes().values().stream().forEach(at -> {
                at.internalDataType = getPhysicalType(at.conceptualDataType);
            });
            builder.where(convertSelect.prepareWhere(select));            
            builder.setOrdering(convertSelect.prepareOrdering(select));
            prepareLimit(builder, select);
            builder.writeResultsToFile(convertSelect.shouldResultBeWrittenIntoFile(select));
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

    //merge to single when all the called functions are container type aware! 18.11.14 Javad
    private void prepareJoined(SelectDescriptor select) {
      boolean a = true;
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
                        .source(convertSelect.getCompleteSourceName(select))
                        .target(convertSelect.getCompleteTargetName(select))
                        // pass th target file
                        .bypassFirstRow(firstRowIsHeader)
                        .trimTokens(true) // default is true
                        .read();
                
                //System.out.println("The result set contains " + result.stream().count() + " records.");
                if(result != null){
                    Resultset resultSet = new Resultset(ResultsetType.Tabular); 
                    resultSet.setData(result);
                    resultSet.setSchema(prepareSchema(select));
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

    private Boolean prepareFields(DataReaderBuilder builder, SelectDescriptor select) throws IOException {
        switch (select.getSourceClause().getContainer().getDataContainerType()) {
            case Single:
                try {
                    String fileName = convertSelect.getCompleteSourceName(select);
                    HeaderBuilder hb = new HeaderBuilder();
                    LinkedHashMap<String, FieldInfo> fields = hb.buildFromDataFile(fileName, builder.getColumnDelimiter(), builder.getTypeDelimiter(), builder.getUnitDelimiter());
                    fields.values().stream().forEach(field -> {
                        field.conceptualDataType = getConceptualType(field.internalDataType);
                    });
                    builder.addFields(fields);
                    firstRowIsHeader = Boolean.valueOf(((SingleContainer)select.getSourceClause().getContainer()).getBinding().getConnection().getParameters().get("firstRowIsHeader").getValue());
                } catch (Exception ex){}
                break;
            case Joined:
                break;
            default:
                break;
        }
                
        return firstRowIsHeader;
    }
    
    private void createPhysicalPerspective(SelectDescriptor select) {
        builder.getFields().values().stream().forEach( p-> {
            PerspectiveAttributeDescriptor attribute = new PerspectiveAttributeDescriptor();
            attribute.setId(p.name);
            attribute.setDataType(getConceptualType(p.internalDataType));
            
            MemberExpression fwd = Expression.Member(attribute.getId(), attribute.getDataType());
            MemberExpression rvs = Expression.Member(attribute.getId(), attribute.getDataType());
            
            attribute.setForwardExpression(fwd);
            attribute.setReverseExpression(rvs);
            select.getProjectionClause().getPerspective().addAttribute(attribute);
        });
        select.getProjectionClause().getPerspective().setId("generated_Perspective_"+ select.getId());
        select.getProjectionClause().setPresent(true);
    }


}
