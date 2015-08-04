/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.model.adapters;

import com.vaiona.commons.data.FieldInfo;
import com.vaiona.commons.types.TypeSystem;
import java.util.LinkedHashMap;
import java.util.Map;
import xqt.model.containers.SingleContainer;
import xqt.model.declarations.PerspectiveDescriptor;

/**
 *
 * @author Javad Chamanara <chamanara@gmail.com>
 */
public abstract class BaseAdapterHelper {
    
    public abstract String getConceptualType(String physicalType);
    public abstract String getPhysicalType(String conceptualType);
    public abstract LinkedHashMap<String, FieldInfo> getContinerSchema(SingleContainer container, Object... params);
    public abstract int getContainerDialectId(SingleContainer container);
    
    public PerspectiveDescriptor createPhysicalPerspective(Map<String, FieldInfo> fields, PerspectiveDescriptor perspective, String id) {
        for (Map.Entry<String, FieldInfo> entrySet : fields.entrySet()) {
            FieldInfo field = entrySet.getValue();
            if(field.conceptualDataType.equalsIgnoreCase(TypeSystem.TypeName.Unknown)){
                field.conceptualDataType = getConceptualType(field.internalDataType);
            }
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
    
    public String getEntityResourceName() {
        return "Entity";
    }

    public String getJoinedEntityResourceName() {
        return "Entity";
    }

    public String getRecordResourceName() {
        return "Entity";
    }

    public String getAggregateReaderResourceName() {
        return "AggregateReader";
    }

    public String getReaderResourceName() {
        return "Reader";
    }

    public String getJoinReaderResourceName() {
        return "JoinReader";
    }    
    
}
