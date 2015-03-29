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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import xqt.model.containers.DataContainer;
import xqt.model.containers.SingleContainer;
import xqt.model.declarations.PerspectiveAttributeDescriptor;
import xqt.model.declarations.PerspectiveDescriptor;
import xqt.model.expressions.Expression;
import xqt.model.expressions.ExpressionType;
import xqt.model.expressions.MemberExpression;
import xqt.model.statements.query.TargetClause;

/**
 *
 * @author Javad Chamanara <chamanara@gmail.com>
 */
public class CsvDataAdapterHelper {
    
    private static final HashMap<String, List<String>> typeConversion = new HashMap<>();
    
    static {
        typeConversion.put(TypeSystem.TypeName.Boolean,  new ArrayList<String>() {{add("boolean");}});
        typeConversion.put(TypeSystem.TypeName.Byte,     new ArrayList<String>() {{add("byte");}});
        typeConversion.put(TypeSystem.TypeName.Date,     new ArrayList<String>() {{add("date");}});
        typeConversion.put(TypeSystem.TypeName.Integer,  new ArrayList<String>() {{add("integer");add("int");}});
        typeConversion.put(TypeSystem.TypeName.Long,     new ArrayList<String>() {{add("long");}});
        typeConversion.put(TypeSystem.TypeName.Real,     new ArrayList<String>() {{add("double");add("real");add("float");}});
        typeConversion.put(TypeSystem.TypeName.String,   new ArrayList<String>() {{add("string");add("char");}});        
    }
    
    public String getConceptualType(String physicalType){
        Optional<Map.Entry<String, List<String>>> entry = typeConversion.entrySet().stream()
                .filter(p-> p.getValue().contains(physicalType.toLowerCase())).findFirst();
        if(entry.isPresent()){
            return entry.get().getKey();
        }
        else
            return TypeSystem.TypeName.Unknown;
    }

    public String getPhysicalType(String conceptualType){
        if(typeConversion.containsKey(conceptualType)){
            return typeConversion.get(conceptualType).get(0); // returns first physical data type by default.
        }
            return TypeSystem.TypeName.Unknown;
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
        for (Map.Entry<String, FieldInfo> entrySet : fields.entrySet()) {
            FieldInfo field = entrySet.getValue();
            field.conceptualDataType = getConceptualType(field.internalDataType);
        }
        perspective = new PerspectiveDescriptor(fields, id);
        return perspective;
    }
    
    // looks for simple members in the expressions and checks whether they refer to a physical field,
    // if yes, the data type of the the field is set for the members
    public PerspectiveDescriptor improvePerspective(Map<String, FieldInfo> fields, PerspectiveDescriptor perspective) {
        if(perspective == null){
            return null;
        }
        for (Map.Entry<String, FieldInfo> entrySet : fields.entrySet()) {
            FieldInfo field = entrySet.getValue();
            field.conceptualDataType = getConceptualType(field.internalDataType);
        }
        return perspective.improve(fields);
    }
}    

    
