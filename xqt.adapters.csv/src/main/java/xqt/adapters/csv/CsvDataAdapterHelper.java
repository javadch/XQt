/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.adapters.csv;

import com.vaiona.commons.data.FieldInfo;
import com.vaiona.commons.types.TypeSystem;
import com.vaiona.csv.reader.HeaderBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import xqt.model.containers.DataContainer;
import xqt.model.containers.SingleContainer;
import xqt.model.data.SchemaItem;
import xqt.model.declarations.PerspectiveAttributeDescriptor;
import xqt.model.declarations.PerspectiveDescriptor;
import xqt.model.expressions.Expression;
import xqt.model.expressions.MemberExpression;
import xqt.model.statements.query.SourceClause;
import xqt.model.statements.query.TargetClause;

/**
 *
 * @author Javad Chamanara <chamanara@gmail.com>
 */
public class CsvDataAdapterHelper {
    
    private static final HashMap<String, List<String>> typeConversion = new HashMap<>();
    
    static {
        typeConversion.put(TypeSystem.Boolean,  new ArrayList<String>() {{add("boolean");}});
        typeConversion.put(TypeSystem.Byte,     new ArrayList<String>() {{add("byte");}});
        typeConversion.put(TypeSystem.Date,     new ArrayList<String>() {{add("date");}});
        typeConversion.put(TypeSystem.Integer,  new ArrayList<String>() {{add("integer");add("int");}});
        typeConversion.put(TypeSystem.Long,     new ArrayList<String>() {{add("long");}});
        typeConversion.put(TypeSystem.Real,     new ArrayList<String>() {{add("double");add("real");add("float");}});
        typeConversion.put(TypeSystem.String,   new ArrayList<String>() {{add("string");add("char");}});        
    }
    
    public String getConceptualType(String physicalType){
        Optional<Map.Entry<String, List<String>>> entry = typeConversion.entrySet().stream()
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
    

    /**
     *
     * @param container : must be a Single container
     * @param columnDelimiter
     * @param typeDelimiter
     * @param unitDelimiter
     * @return a map on the physical fields declared in the associated container. in this case a csv file
     * @throws IOException
     */
    public LinkedHashMap<String, FieldInfo> prepareFields(SingleContainer container, String columnDelimiter, String typeDelimiter, String unitDelimiter) throws IOException {
            try {
                String fileName = getCompleteSourceName(container);
                HeaderBuilder hb = new HeaderBuilder();
                LinkedHashMap<String, FieldInfo> fields = hb.buildFromDataFile(fileName, columnDelimiter, typeDelimiter, unitDelimiter);
                fields.values().stream().forEach(field -> {
                    field.conceptualDataType = getConceptualType(field.internalDataType);
                });
                return fields;
            } catch (Exception ex){}
            return null;
    }
    
    public String getCompleteSourceName(SingleContainer container){ //may need a container index too!
        String basePath = container.getBinding().getConnection().getSourceUri();
        String container0 = container.getContainerName();
        String fileExtention = "csv";
        String fileName = "";
        try{
            fileExtention = container.getBinding().getConnection().getParameters().get("fileExtension").getValue();
        } catch (Exception ex){}
        fileName = basePath.concat(container0).concat(".").concat(fileExtention);
        return fileName;
    }

    public String getCompleteTargetName(TargetClause target){ //may need a container index too!
        if(target.getContainer().getDataContainerType() == DataContainer.DataContainerType.Single){
            SingleContainer container = (SingleContainer)target.getContainer();
            String basePath = container.getBinding().getConnection().getSourceUri();
            String container0 = container.getContainerName();
            String fileExtention = "csv";
            try{
                fileExtention = ((SingleContainer)target.getContainer())
                        .getBinding().getConnection().getParameters().get("fileExtension").getValue();
            } catch (Exception ex){}
            String fileName = basePath.concat(container0).concat(".").concat(fileExtention);
            return fileName;
        } else {
            return null;
        }
    }
    
    public boolean isFirstRowHeader(SingleContainer container){
        boolean firstRowIsHeader = Boolean.valueOf(container.getBinding().getConnection().getParameters().get("firstRowIsHeader").getValue());
        return firstRowIsHeader;
    }
    
    public PerspectiveDescriptor createPhysicalPerspective(Map<String, FieldInfo> fields, PerspectiveDescriptor perspective, String id) {
        if(perspective == null){
            perspective = new PerspectiveDescriptor();
            perspective.setPerspectiveType(PerspectiveDescriptor.PerspectiveType.Implicit);
        }
        perspective.setId("generated_Perspective_"+ id);
        for (Map.Entry<String, FieldInfo> entrySet : fields.entrySet()) {
            FieldInfo field = entrySet.getValue();
            PerspectiveAttributeDescriptor attribute = new PerspectiveAttributeDescriptor();
            attribute.setId(field.name);
            attribute.setDataType(getConceptualType(field.internalDataType));
            
            MemberExpression fwd = Expression.Member(attribute.getId(), attribute.getDataType());
            MemberExpression rvs = Expression.Member(attribute.getId(), attribute.getDataType());
            
            attribute.setForwardExpression(fwd);
            attribute.setReverseExpression(rvs);
            perspective.addAttribute(attribute);            
        }
        return perspective;
    }
    
    PerspectiveDescriptor combinePerspective(PerspectiveDescriptor perspective, PerspectiveDescriptor left, PerspectiveDescriptor right, String id) {
        if(perspective == null){
            perspective = new PerspectiveDescriptor();
            perspective.setPerspectiveType(PerspectiveDescriptor.PerspectiveType.Implicit);
        }
        perspective.setId("generated_Perspective_"+ id);
        left.getAttributes().values().stream().forEach(p-> p.setExtra("L"));
        perspective.getAttributes().putAll(left.getAttributes());
        for (Map.Entry<String, PerspectiveAttributeDescriptor> entrySet : right.getAttributes().entrySet()) {
            String key = entrySet.getKey();
            PerspectiveAttributeDescriptor value = entrySet.getValue();
            // if the name already exists in the combined perspective, the name is prefixed by "R_" because it happens to the right side perspectives.
            // all the attributes are linked to their origins, except the renamed ones. which are somehow trackable by their names
            // the right side attributes are cloned to prevent attribute interlinking when the right and left sides are using one (same) perspective
            PerspectiveAttributeDescriptor renamedAttribute = new PerspectiveAttributeDescriptor(value);
            renamedAttribute.setExtra("R"); // the attribute is marked, so that later during the repair process, it is possile to rename the referring attributes
            if(perspective.getAttributes().containsKey(key)){
                renamedAttribute.setId("R_" + renamedAttribute.getId());
            }
            perspective.addAttribute(renamedAttribute);
        }        
        return perspective;
    }

    public HashSet<SchemaItem> prepareSchema(PerspectiveDescriptor perspective) {
        // pay attention to aggrgates!
        HashSet<SchemaItem> schema = new LinkedHashSet<>();
        // do not use the functional counterpart, as it uses the streaming method, which doe not guarantee to preserve the order
        for(PerspectiveAttributeDescriptor attribute: perspective.getAttributes().values()){
            SchemaItem sItem = new SchemaItem();
            sItem.setDataType(attribute.getDataType());
            sItem.setName(attribute.getId());
            sItem.setSystemType(TypeSystem.getTypes().get(attribute.getDataType()).getName());
            sItem.setIndex(schema.size());            
            schema.add(sItem); 
        }
        return schema;
    }   

}    

    
