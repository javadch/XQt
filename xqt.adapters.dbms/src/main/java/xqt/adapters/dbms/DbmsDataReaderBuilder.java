/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.adapters.dbms;

import com.vaiona.commons.compilation.ObjectCreator;
import com.vaiona.commons.data.AttributeInfo;
import com.vaiona.commons.data.DataReaderBuilderBase;
import com.vaiona.commons.data.FieldInfo;
import com.vaiona.commons.types.TypeSystem;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import xqt.model.conversion.ConvertSelectElement;
import xqt.model.declarations.PerspectiveAttributeDescriptor;
import xqt.model.functions.AggregationCallInfo;

/**
 *
 * @author Javad Chamanara <chamanara@gmail.com>
 */
public class DbmsDataReaderBuilder extends DataReaderBuilderBase {
    ConvertSelectElement convertSelect = null;
    String connectionString;
    String username;
    String password;
    DBMSDialect dbProvider; // postgre, mysql, etc.
    String containerName; // table or view name. the left one in the JOIN statements
    String rightContainerName;
    DbmsDataAdapterHelper queryHelper;
    
    public DbmsDataReaderBuilder(){        
        convertSelect = new ConvertSelectElement();
    }

    public DbmsDataReaderBuilder connectionString(String value){
        this.connectionString = value;
        return this;
    }    

    public DbmsDataReaderBuilder username(String value){
        this.username = value;
        return this;
    }    

    public DbmsDataReaderBuilder password(String value){
        this.password = value;
        return this;
    }    

    public DbmsDataReaderBuilder dbProvider(DBMSDialect value){
        this.dbProvider = value;
        return this;
    }    
    
    public DbmsDataReaderBuilder registerQueryHelper(DbmsDataAdapterHelper value) {
        this.queryHelper = value;
        return this;
    }
    
    public DbmsDataReaderBuilder containerName(String value){
        this.containerName = value;
        return this;
    }    

    public DbmsDataReaderBuilder rightContainerName(String value){
        this.rightContainerName = value;
        return this;
    }    

    List<AggregationCallInfo> aggregationCallInfo = new ArrayList<>();
    public DbmsDataReaderBuilder addAggregates(List<AggregationCallInfo> value) {
        aggregationCallInfo = value;
        return this;
    }
    
    DbmsDataReader build(Class classObject) {
        try{
            DbmsDataReader<Object, Object, Object> instance = (DbmsDataReader)ObjectCreator.createInstance(classObject);
            return instance;
        } catch (Exception ex){
            return null;
        }
    }

    @Override
    protected String translate(AttributeInfo attribute, boolean rightSide){
        String translated = "";
        String properCaseToken = attribute.name;
        if(!namesCaseSensitive)
            properCaseToken = properCaseToken.toLowerCase();
        if(!rightSide){
            // need for a type check
            // the following statement, sets a default format for the date, if the field is of type Date
            String temp = TypeSystem.getTypes().get(attribute.conceptualDataType).getCastPattern().replace("$data$", "row[" + attribute.index + "]");
            if(attribute.conceptualDataType.equalsIgnoreCase(TypeSystem.TypeName.Date)){
                // check whether the field has date format, if yes, apply it
                if(attribute.unit!= null && !attribute.unit.isEmpty() && !attribute.unit.equalsIgnoreCase(TypeSystem.TypeName.Unknown)){
                    temp = TypeSystem.getTypes().get(attribute.conceptualDataType).makeDateCastPattern(attribute.unit).replace("$data$", "row[" + attribute.index + "]");
                // check whether the attribute has date format, if yes, apply it
                }
            }
            translated = translated + " " + temp;
        }
        else if(rightSide){
        	// add table name to the field names! but maybe later in the query!
        }
        // enclose the translated attribute in a data conversion based on the attributes type
        //translated = dataTypes.get(type).replace("$data$", translated);
        // consider Date!!!
        if(attribute.conceptualDataType.equals("Date") || attribute.conceptualDataType.equals("String")){
            translated = "(" + translated + ")";
        }
        else { // cast the translated expression to the attribute type
            translated = "(" + TypeSystem.getTypes().get(attribute.conceptualDataType).getRuntimeType().toLowerCase() + ")(" + translated + ")";
        }
        
        return translated;
    }    
        
    @Override
    protected void buildSharedSegments(){
        //entityResourceName = "";
        this.namespace("xqt.adapters.dbms");
        super.buildSharedSegments();
        readerContext.put("sourceOfData", sourceOfData);     
        readerContext.put("LeftClassName", this.leftClassName); // used as both left and right sides' type.
        readerContext.put("RightClassName", this.leftClassName); // in the single container it is not used by the reader, but shold be provided for compilation purposes.
        // do not move these items to the base class
        recordContext.put("Attributes", rowEntityAttributes.values().stream().collect(Collectors.toList()));           
    }
    
    @Override
    protected void buildSingleSourceSegments(){
        super.buildSingleSourceSegments();
        if(sourceOfData.equalsIgnoreCase("container")){
            String otherCalssNames = (namespace + "." + baseClassName + "Entity");
            readerContext.put("LeftClassName", "Object"); // used as both left and right sides' type.
            readerContext.put("RightClassName", "Object"); // in the single container it is not used by the reader, but shold be provided for compilation purposes.
            readerContext.put("TargetRowType", otherCalssNames);
            readerContext.put("ContainerName", this.containerName);            
            String query = queryHelper.assembleQuery(readerContext);
            readerContext.put("NativeQuery", query);
        } else if (sourceOfData.equalsIgnoreCase("variable")){
            readerContext.put("LeftClassName", this.leftClassName); // used as both left and right sides' type.
            readerContext.put("RightClassName", this.leftClassName); // in the single container it is not used by the reader, but shold be provided for compilation purposes.
            readerContext.put("TargetRowType", this.leftClassName);            
        }
    }

    @Override
    protected void buildJoinedSourceSegments(){
        super.buildJoinedSourceSegments();
        readerContext.put("LeftClassName", "Object"); 
        readerContext.put("RightClassName", "Object");
        readerContext.put("TargetRowType", (namespace + "." + baseClassName + "Entity"));
        readerContext.put("ContainerName", this.containerName);            
        readerContext.put("RightContainerName", this.rightContainerName);            
        readerContext.put("LeftFieldsNo", this.fields.size());                    
        readerContext.put("RightFieldsNo", this.rightFields.size());                    
    }    
}
