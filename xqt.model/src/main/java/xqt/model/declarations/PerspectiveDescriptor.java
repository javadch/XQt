/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.model.declarations;

import com.vaiona.commons.types.TypeSystem;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import xqt.model.data.SchemaItem;
import xqt.model.exceptions.LanguageException;
import xqt.model.exceptions.LanguageExceptionBuilder;

/**
 *
 * @author jfd
 */
public class PerspectiveDescriptor extends DeclarationDescriptor{
    private PerspectiveDescriptor superPerspective;
    private Map<String, PerspectiveAttributeDescriptor> attributes = new LinkedHashMap<>(); // should keep the order
    private PerspectiveType perspectiveType = PerspectiveType.Explicit;

    public PerspectiveType getPerspectiveType() {
        return perspectiveType;
    }

    public void setPerspectiveType(PerspectiveType perspectiveType) {
        this.perspectiveType = perspectiveType;
    }

    
    public void setExplicit() {
        this.perspectiveType = PerspectiveDescriptor.PerspectiveType.Explicit;
    }

    public Boolean isExplicit() {
        return this.perspectiveType == PerspectiveDescriptor.PerspectiveType.Explicit;
    }

    public Map<String, PerspectiveAttributeDescriptor> getAttributes() {
        return attributes;
    }

    public void addAttribute(PerspectiveAttributeDescriptor attribute) {
        attribute.setPerspective(this);
        if(attributes.containsKey(attribute.getId())){  //the attribute already exists
            PerspectiveAttributeDescriptor existing = attributes.get(attribute.getId());
            if(existing.getPerspective() != null && !existing.getPerspective().equals(this)){//the attribute come from one of the parents and should be overriden
                //override the attribute "existing" be removing it from the list and adding the new one in its place
                attributes.remove(attribute.getId());
                attributes.put(attribute.getId(), attribute);
            } else { // the attribute is an illegal duplicate
                this.getLanguageExceptions().add(
                    LanguageExceptionBuilder.builder()
                        .setMessageTemplate("There is a duplicate attribute named \'%s\' defined in perspective \'%s\'.")
                        .setContextInfo1(attribute.getId())
                        .setContextInfo2(id)
                        .setLineNumber(attribute.getParserContext().getStart().getLine())
                        .setColumnNumber(attribute.getParserContext().getStop().getCharPositionInLine())
                        .build()
                );
            }
        } else { //the attribute is not defined in the super perspective, nor defined before so add it to the perspective
            attributes.put(attribute.getId(), attribute);
        }
    }

    public PerspectiveDescriptor getSuperPerspective() {
        return superPerspective;
    }

    public void setSuperPerspective(PerspectiveDescriptor superPerspective) {
        this.superPerspective = superPerspective;
    }
    
    public static PerspectiveDescriptor combinePerspective(PerspectiveDescriptor perspective, PerspectiveDescriptor left, PerspectiveDescriptor right, String id) {
        if(perspective == null){
            perspective = new PerspectiveDescriptor();
            perspective.setPerspectiveType(PerspectiveDescriptor.PerspectiveType.Implicit);
        }
        perspective.setId("generated_Perspective_"+ id);
        for (Map.Entry<String, PerspectiveAttributeDescriptor> entrySet : left.getAttributes().entrySet()) {
            String key = entrySet.getKey();
            PerspectiveAttributeDescriptor value = entrySet.getValue();
            PerspectiveAttributeDescriptor renamedAttribute = new PerspectiveAttributeDescriptor(value);
            renamedAttribute.setExtra("L"); // the attribute is marked, so that later during the repair process, it is possile to rename the referring attributes
            // because the left side is put to the list first, there shouldn't be any attribute prefixed with "L_"
            if(perspective.getAttributes().containsKey(key)){
                renamedAttribute.setId("L_" + renamedAttribute.getId());
            }
            perspective.addAttribute(renamedAttribute);
        }        
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

    public HashSet<SchemaItem> createSchema() {
        // pay attention to aggrgates!
        HashSet<SchemaItem> schema = new LinkedHashSet<>();
        // do not use the functional counterpart, as it uses the streaming method, which doe not guarantee to preserve the order
        for(PerspectiveAttributeDescriptor attribute: this.getAttributes().values()){
            SchemaItem sItem = new SchemaItem();
            sItem.setDataType(attribute.getDataType());
            sItem.setName(attribute.getId());
            sItem.setSystemType(TypeSystem.getTypes().get(attribute.getDataType()).getName());
            sItem.setIndex(schema.size());            
            schema.add(sItem); 
        }
        return schema;
    }   
    
    public enum PerspectiveType {
        Explicit,
        Implicit,
        Inline
    }
}
