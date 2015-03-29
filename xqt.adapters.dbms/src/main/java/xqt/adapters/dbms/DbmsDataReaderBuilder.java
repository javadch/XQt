/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.adapters.dbms;

import com.vaiona.commons.compilation.ObjectCreator;
import com.vaiona.commons.data.AttributeInfo;
import com.vaiona.commons.data.DataReaderBuilderBase;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import xqt.model.conversion.ConvertSelectElement;
import xqt.model.declarations.PerspectiveAttributeDescriptor;
import xqt.model.functions.AggregationCallInfo;

/**
 *
 * @author Javad Chamanara <chamanara@gmail.com>
 */
public class DbmsDataReaderBuilder extends DataReaderBuilderBase {
    ConvertSelectElement convertSelect = null;
    
    public DbmsDataReaderBuilder(){        
        convertSelect = new ConvertSelectElement();
    }

    List<AggregationCallInfo> aggregationCallInfo = new ArrayList<>();
    public DbmsDataReaderBuilder addAggregates(List<AggregationCallInfo> value) {
        aggregationCallInfo = value;
        return this;
    }
    
    DbmsDataReader build(Class classObject) throws NoSuchMethodException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException  {
        DbmsDataReader<Object, Object, Object> instance = (DbmsDataReader)ObjectCreator.load(classObject);
        return instance;
    }

    @Override
    protected String translate(AttributeInfo attributeInfo, boolean rightSide){
        String translated = "";
        if(!joinType.isEmpty()){
            if(attributeInfo.reference != null){ 
                // The attribute names should come from the deepest root
                PerspectiveAttributeDescriptor originalAttribute = (PerspectiveAttributeDescriptor)attributeInfo.reference;
                String id = originalAttribute.getId();
                do{
                    originalAttribute = originalAttribute.getReference();
                    id = originalAttribute.getId();
                }while (originalAttribute.getReference()!= null);
                String temp = rightSide? //attribute.joinSide.equalsIgnoreCase("R")
                        "rightEntity." + id
                        : "leftEntity." + id;                
                translated = translated + " " + temp;
            }  else  {
                String temp = rightSide?
                        "rightEntity." + attributeInfo.name
                        : "leftEntity." + attributeInfo.name;                
                translated = translated + " " + temp;                
            }
        }
        return translated;
    }    
        
    @Override
    protected void buildSharedSegments(){
        //entityResourceName = "";
        this.namespace("xqt.adapters.dbms");
        super.buildSharedSegments();
        
        readerContext.put("LeftClassName", this.leftClassName); // used as both left and right sides' type.
        readerContext.put("RightClassName", this.leftClassName); // in the single container it is not used by the reader, but shold be provided for compilation purposes.
        // the delimiter MUST come from the source->connection->... chain. if the direct source has no connection it should come from the upper ones
//        String header = String.join(",", attributes.values().stream().map(p-> p.name + ":" + p.internalDataType).collect(Collectors.toList()));
//        String linePattern = String.join(",", attributes.values().stream().map(p-> "String.valueOf(entity." + p.name + ")").collect(Collectors.toList()));
//        readerContext.put("linePattern", linePattern);        
//        readerContext.put("rowHeader", header);        
    }
    
    @Override
    protected void buildSingleSourceSegments(){
        //Default data adapter does not need these items! check for the join case
        //super.buildSingleSourceSegments();
        readerContext.put("TargetRowType", this.leftClassName);

    }

    @Override
    protected void buildJoinedSourceSegments(){
        // maybe it is needed to call buildSingleSourceSegments too.
        super.buildJoinedSourceSegments();
        resultEntityContext.put("LeftClassName", this.leftClassName);
        resultEntityContext.put("RightClassName", this.rightClassName);
        readerContext.put("LeftClassName", this.leftClassName); 
        readerContext.put("RightClassName", this.rightClassName);
        readerContext.put("TargetRowType", (namespace + "." + baseClassName + "Entity"));
    }    
}
