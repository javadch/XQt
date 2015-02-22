
package xqt.adapters.builtin;

import com.vaiona.commons.data.DataReaderBase;
import com.vaiona.commons.compilation.*;
import com.vaiona.commons.data.AttributeInfo;
import com.vaiona.commons.data.DataReaderBuilderBase;
import java.lang.reflect.InvocationTargetException;
import xqt.model.conversion.ConvertSelectElement;
import xqt.model.declarations.PerspectiveAttributeDescriptor;

/**
 *
 * @author standard
 */
public class DataReaderBuilder extends DataReaderBuilderBase {
    ConvertSelectElement convertSelect = null;
    
    public DataReaderBuilder(){        
        convertSelect = new ConvertSelectElement();
    }

    DataReader build(Class classObject) throws NoSuchMethodException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException  {
        DataReader<Object, Object, Object> instance = (DataReader)ObjectCreator.load(classObject);
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
        this.namespace("xqt.adapters.builtin");
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
        
//    LinkedHashMap<String, InMemorySourceFile> createSources1(SelectDescriptor select, String sourceRowType) throws IOException {
//        // set the entrypoint
//        // set the full name
//        String baseClassName = "C" + (new Date()).getTime(); // just to make a unique name
//        
//        Map<String, AttributeInfo>  attributes = convertSelect.prepareAttributes(select.getProjectionClause().getPerspective());
//        
//        // transform the ordering clauses to their bound equivalent, in each attribute names are linked to the attibutes objects
//        Map<AttributeInfo, String> orderItems = new LinkedHashMap<>();        
//        for (Map.Entry<String, String> entry : convertSelect.prepareOrdering(select.getOrderClause()).entrySet()) {
//                if(attributes.containsKey(entry.getKey())){
//                    orderItems.put(attributes.get(entry.getKey()), entry.getValue());
//                }            
//        }
//        
//        Map<String, Object> readerContext = new HashMap<>();
//        readerContext.put("namespace", "x.y.z");
//        readerContext.put("BaseClassName", baseClassName);
//        readerContext.put("SourceRowNamespace", sourceRowType.subSequence(0, sourceRowType.lastIndexOf(".")));
//        readerContext.put("SourceRowType", sourceRowType.subSequence(sourceRowType.lastIndexOf(".")+1, sourceRowType.length()));
//        readerContext.put("TargetRowType", sourceRowType.subSequence(sourceRowType.lastIndexOf(".")+1, sourceRowType.length()));
//        readerContext.put("Where", convertSelect.translateExpression(convertSelect.prepareWhere(select.getFilterClause()), select.getProjectionClause().getPerspective()));
//        
//        // the delimiter MUST come from the source->connection->... chain. if the direct source has no connection it should come from the upper ones
//        String header = String.join(",", attributes.values().stream().map(p-> p.name + ":" + p.internalDataType).collect(Collectors.toList()));
//        String linePattern = String.join(",", attributes.values().stream().map(p-> "String.valueOf(entity." + p.name + ")").collect(Collectors.toList()));
//        readerContext.put("linePattern", linePattern);        
//        
//        readerContext.put("rowHeader", header);        
//        readerContext.put("Ordering", orderItems);
//        readerContext.put("skip", select.getLimitClause().getSkip());
//        readerContext.put("take", select.getLimitClause().getTake());
//        readerContext.put("writeResultsToFile", convertSelect.shouldResultBeWrittenIntoFile(select.getTargetClause()));
//        //readerContext.put("Attributes", attributes.values().stream().collect(Collectors.toList()));
//        ClassGenerator generator = new ClassGenerator();
//        String reader = generator.generate(this, "MemReader", "Resource", readerContext);
//        
//        LinkedHashMap<String, InMemorySourceFile> sources = new LinkedHashMap<>();
//        InMemorySourceFile rf = new InMemorySourceFile(baseClassName + "Reader", reader);
//        rf.setEntryPoint(true);
//        rf.setFullName("x.y.z" + "." + baseClassName + "Reader");
//        sources.put(rf.getFullName(), rf); // the reader must be added first
//        
//        return sources;        
//    }
}
