
package xqt.adapters.builtin;

import com.vaiona.commons.compilation.*;
import com.vaiona.commons.data.AttributeInfo;
import com.vaiona.commons.data.DataReaderBuilderBase;
import com.vaiona.commons.data.FieldInfo;
import com.vaiona.commons.types.TypeSystem;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import xqt.model.conversion.ConvertSelectElement;
import xqt.model.declarations.PerspectiveAttributeDescriptor;
import xqt.model.declarations.PerspectiveDescriptor;
import xqt.model.expressions.MemberExpression;
import xqt.model.functions.AggregationCallInfo;
import xqt.model.statements.query.SelectDescriptor;

/**
 *
 * @author standard
 */
public class DataReaderBuilder extends DataReaderBuilderBase {
    ConvertSelectElement convertSelect = null;
    PerspectiveDescriptor recordPerspective;
    
    public DataReaderBuilder(){        
        convertSelect = new ConvertSelectElement();
    }

    DataReader build(Class classObject) throws NoSuchMethodException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, Exception  {
        DataReader<Object, Object, Object> instance = (DataReader)ObjectCreator.createInstance(classObject);
        return instance;
    }

    @Override
    protected String translate(AttributeInfo attributeInfo, boolean rightSide){
        // the method should be called not before the recordPerspective is set
        String translated = "";
        if(sourceOfData.equalsIgnoreCase("variable")){ 
            if(recordPerspective == null){
                return ""; // it is an error
            }
            PerspectiveAttributeDescriptor referenceAttribute = (PerspectiveAttributeDescriptor)attributeInfo.reference;
            if(((PerspectiveAttributeDescriptor)attributeInfo.reference).getForwardExpression() instanceof MemberExpression){
                MemberExpression member = (MemberExpression)((PerspectiveAttributeDescriptor)attributeInfo.reference).getForwardExpression();
                if(attributeInfo.internalDataType.equalsIgnoreCase(TypeSystem.TypeName.Unknown)
                   && member.getMemberType() == MemberExpression.MemberType.Simple
                   && recordPerspective.getAttributes().containsKey(member.getId().toLowerCase())
                    ){
                    PerspectiveAttributeDescriptor att = recordPerspective.getAttributes().get(member.getId().toLowerCase());
                    attributeInfo.internalDataType = att.getDataType();
                    attributeInfo.runtimeType = TypeSystem.getTypes().get(att.getDataType()).getRuntimeType();
                    attributeInfo.conceptualDataType = TypeSystem.getTypes().get(att.getDataType()).getName();
                    // it was the reference attribute to have Unknown type, that caused the attributeInfo to be of type Unknwon!
                    // here is the chance to apply the inferred type on the reference attribute too
                    referenceAttribute.setDataType(TypeSystem.getTypes().get(att.getDataType()).getName());
                }
            }
            // the data is read from a varibale, so the target entity's attributes should properly point to the source entity
            // the source entity is called record, so all the pointers to the source entity attributes should be prefixed by the 'record' keyword            
            for (StringTokenizer stringTokenizer = new StringTokenizer(attributeInfo.forwardMap, " "); stringTokenizer.hasMoreTokens();) {
                String token = stringTokenizer.nextToken();
                String properCaseToken = token;
                if(!namesCaseSensitive)
                    properCaseToken = token.toLowerCase();
                if(recordPerspective.getAttributes().containsKey(properCaseToken)){
                    String temp = "rowEntity." + token;
                    translated = translated + " " + temp;
                } else {
                    translated = translated + " " + token;
                }            
            }
            return translated;
        }
        
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
        this.namespace("xqt.adapters.builtin");
        super.buildSharedSegments();
        
        readerContext.put("ConsiderAggregates", false);
        if(hasAggregate()){
            entityContext.put("ConsiderAggregates", true);
            readerContext.put("RecordClassName", this.leftClassName);
        }
        else
            entityContext.put("ConsiderAggregates", false);
        readerContext.put("LeftClassName", this.leftClassName); // used as both left and right sides' type.
        readerContext.put("RightClassName", this.leftClassName); // in the single container it is not used by the reader, but shold be provided for compilation purposes.
        readerContext.put("AggregationCallInfos", this.aggregationCallInfo);
        // do not move these items to the base class
    }
    
    @Override
    protected void buildSingleSourceSegments(){
        super.buildSingleSourceSegments();
        if (sourceOfData.equalsIgnoreCase("variable")){
            // genarete a new entity class name
            entityContext.put("RecordClassName", leftClassName );
//            if(hasAggregate()){
//                entityContext.put("RecordClassName", leftClassName );
//            }
//            readerContext.put("LeftClassName", this.leftClassName); // used as both left and right sides' type.
//            readerContext.put("RightClassName", this.leftClassName); // in the single container it is not used by the reader, but shold be provided for compilation purposes.
//            readerContext.put("TargetRowType", entityClassName);    
            readerContext.put("TargetRowType", this.baseClassName + "Entity"); 

        } else {
            readerContext.put("TargetRowType", this.leftClassName);        
        }
        // the memory adapter does not create intermedite records.
        recordContext.clear();
    }

    @Override
    protected void buildJoinedSourceSegments(){
        // maybe it is needed to call buildSingleSourceSegments too.
        super.buildJoinedSourceSegments();
        entityContext.put("LeftClassName", this.leftClassName);
        entityContext.put("RightClassName", this.rightClassName);
        readerContext.put("LeftClassName", this.leftClassName); 
        readerContext.put("RightClassName", this.rightClassName);
        readerContext.put("TargetRowType", (namespace + "." + baseClassName + "Entity"));
    }
        
    public PerspectiveDescriptor getRecordPerspective(){ return recordPerspective;}

    public DataReaderBuilder recordPerspective(PerspectiveDescriptor value){
        this.recordPerspective = value;
        return this;
    }
    
    List<AggregationCallInfo> aggregationCallInfo = new ArrayList<>();
    public DataReaderBuilder addAggregates(List<AggregationCallInfo> value) {
        aggregationCallInfo = value;
        return this;
    }
}
