/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.model.declarations;

import com.vaiona.commons.data.FieldInfo;
import com.vaiona.commons.data.FieldInfo.DataTypeQuality;
import com.vaiona.commons.logging.LoggerHelper;
import com.vaiona.commons.types.TypeSystem;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.stream.Collectors;

import xqt.model.data.SchemaItem;
import xqt.model.exceptions.LanguageExceptionBuilder;
import xqt.model.expressions.Expression;
import xqt.model.expressions.ExpressionType;
import xqt.model.expressions.MemberExpression;

/**
 *
 * @author Javad Chamanara
 */
public class PerspectiveDescriptor extends DeclarationDescriptor{
    private PerspectiveDescriptor superPerspective;
    private Map<String, PerspectiveAttributeDescriptor> attributes = new LinkedHashMap<>(); // should keep the order
    private PerspectiveType perspectiveType = PerspectiveType.Explicit;

    public PerspectiveDescriptor(){
        this.perspectiveType = PerspectiveType.Explicit;
    }
    
    public PerspectiveDescriptor(PerspectiveType value){
        this.perspectiveType = value;
    }
    
    public PerspectiveDescriptor(Map<String, FieldInfo> fields, String id) {
        this.setPerspectiveType(PerspectiveDescriptor.PerspectiveType.Implicit);
        this.setId("generated_Perspective_"+ id);
        for (Map.Entry<String, FieldInfo> entrySet : fields.entrySet()) {
            FieldInfo field = entrySet.getValue();
            PerspectiveAttributeDescriptor attribute = new PerspectiveAttributeDescriptor();
    		attribute.setId(field.name);
            attribute.setDataType(field.conceptualDataType);
            if(field.unit != null && !field.name.isEmpty()){
                attribute.setSemanticKey(field.unit);
            }
            MemberExpression fwd = Expression.Member(field.name, attribute.getDataType()); // attribute Id may change e.g., because of conflicting with the keywords
            MemberExpression rvs = Expression.Member(field.name, attribute.getDataType());
            
            attribute.setForwardExpression(fwd);
            attribute.setReverseExpression(rvs);
            this.addAttribute(attribute);            
        }
    }
    
    // Canonic perspective is a perspective which its conceptual and physical side are equal and taken from the 
    // input perspective. It is used in complementing scenarios
    public PerspectiveDescriptor createCanonicPerspective() {
        PerspectiveDescriptor canonic = new PerspectiveDescriptor(PerspectiveDescriptor.PerspectiveType.Implicit);
        canonic.setId("canonic_Perspective_"+ id);
        for (PerspectiveAttributeDescriptor sourceAtt : attributes.values()
                .stream().filter(p->p.isAuxiliary() == false).collect(Collectors.toList())) {
            //PerspectiveAttributeDescriptor sourceAtt = entry;
            PerspectiveAttributeDescriptor attribute = new PerspectiveAttributeDescriptor();
            attribute.setId(sourceAtt.getId());
            attribute.setDataType(sourceAtt.getDataType());
            MemberExpression fwd = Expression.Member(sourceAtt.getId(), sourceAtt.getDataType());
            MemberExpression rvs = Expression.Member(sourceAtt.getId(), sourceAtt.getDataType());
            attribute.setAuxiliary(sourceAtt.isAuxiliary());
            attribute.setForwardExpression(fwd);
            attribute.setReverseExpression(rvs);
            canonic.addAttribute(attribute);            
        }
        return canonic;
    }

    // creates a perspective without any attributes
    public PerspectiveDescriptor createUnitPerspective() {
        PerspectiveDescriptor unit = new PerspectiveDescriptor(PerspectiveDescriptor.PerspectiveType.Implicit);
        unit.setId("unit_Perspective_"+ id);
        return unit;
    }
    
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
            if(entrySet.getValue().isAuxiliary())
                continue;
            String key = entrySet.getKey();
            PerspectiveAttributeDescriptor value = entrySet.getValue();
            PerspectiveAttributeDescriptor renamedAttribute = new PerspectiveAttributeDescriptor(value);
            renamedAttribute.setExtra("L"); // the attribute is marked, so that later during the repair process, it is possile to rename the referring attributes
            // because the left side is put to the list first, there shouldn't be any attribute prefixed with "L_"
            if(perspective.getAttributes().containsKey(key)){
                renamedAttribute.setId(("L_" + renamedAttribute.getId()).toLowerCase());
            }
            perspective.addAttribute(renamedAttribute);
        }        
        for (Map.Entry<String, PerspectiveAttributeDescriptor> entrySet : right.getAttributes().entrySet()) {
            if(entrySet.getValue().isAuxiliary())
                continue;
            String key = entrySet.getKey();
            PerspectiveAttributeDescriptor value = entrySet.getValue();
            // if the name already exists in the combined perspective, the name is prefixed by "R_" because it happens to the right side perspectives.
            // all the attributes are linked to their origins, except the renamed ones. which are somehow trackable by their names
            // the right side attributes are cloned to prevent attribute interlinking when the right and left sides are using one (same) perspective
            PerspectiveAttributeDescriptor renamedAttribute = new PerspectiveAttributeDescriptor(value);
            renamedAttribute.setExtra("R"); // the attribute is marked, so that later during the repair process, it is possile to rename the referring attributes
            if(perspective.getAttributes().containsKey(key)){
                renamedAttribute.setId(("R_" + renamedAttribute.getId()).toLowerCase());
            }
            perspective.addAttribute(renamedAttribute);
        }        
        return perspective;
    }

    public HashSet<SchemaItem> createSchema() {
        // pay attention to aggregates!
        HashSet<SchemaItem> schema = new LinkedHashSet<>();
        // do not use the functional counterpart, as it uses the streaming method, which doe not guarantee to preserve the order
        for(PerspectiveAttributeDescriptor attribute: this.getAttributes().values()){
            if(!attribute.isAuxiliary()){
                SchemaItem sItem = new SchemaItem();
                sItem.setDataType(attribute.getDataType());
                sItem.setName(attribute.getId());
                sItem.setSystemType(TypeSystem.getTypes().get(attribute.getDataType()).getName());
                sItem.setIndex(schema.size());            
                schema.add(sItem); 
            }
        }
        return schema;
    }   
    
    // looks for simple members in the expressions and checks whether they refer to a physical field,
    // if yes, the data type of the the field is set for the members
    public PerspectiveDescriptor improve_old(Map<String, FieldInfo> fields) {
        for(PerspectiveAttributeDescriptor unknownTypedAttribute: 
            this.getAttributes().values().stream()
                .filter(p->p.getDataType().equalsIgnoreCase(TypeSystem.TypeName.Unknown))
                .filter(p->p.getForwardExpression().getExpressionType() == ExpressionType.Member)
                .filter(p-> ((MemberExpression)p.getForwardExpression()).getMemberType() == MemberExpression.MemberType.Simple)
                .collect(Collectors.toList())
                )
            {
                String fieldName = ((MemberExpression)unknownTypedAttribute.getForwardExpression()).getId().toLowerCase();
                if(fields.containsKey(fieldName)){
                    FieldInfo field = fields.get(fieldName);
                    if(field!= null){
                        unknownTypedAttribute.setDataType(field.conceptualDataType);
                    }
                }
            }
        return this;
    }
    
    /*
     * Goes through each and every member expression in the forward mappings of the attributes
     * If the member has a counterpart field name, tries to update the member's data type from the filed, only if the field has an explicit type
     *  If the field's data type is not explicit, but the member has a type other than Unknown, the member type gets applied to the field too.
     *  This iterative improvement, should eventually case all the members in various attributes to converge to identical data types for members reffering to each field.
    */
    public PerspectiveDescriptor improve(Map<String, FieldInfo> fields) {
    	for(PerspectiveAttributeDescriptor att: this.getAttributes().values()){
    		for(MemberExpression member: att.getForwardExpression().getMemberExpressions()){
    			String memberId = member.getId().toLowerCase();
    			if(fields.containsKey(memberId)){
    				FieldInfo field = fields.get(memberId);
    				if(field.dataTypeQuality == FieldInfo.DataTypeQuality.Extracted || field.dataTypeQuality == FieldInfo.DataTypeQuality.Enforced){
    					member.setReturnType(field.conceptualDataType);
    					updateAttribiteType(att);
    				} else if(!member.getReturnType().equals(TypeSystem.TypeName.Unknown)) {
    					field.conceptualDataType = member.getReturnType();
    					field.internalDataType = TypeSystem.TypeName.Unknown; //??
    					field.dataTypeQuality = DataTypeQuality.Enforced;
    				} else {
    					member.setReturnType(field.conceptualDataType); // experimental
    					updateAttribiteType(att);
    				}
    			}
    		}
    	}
        return this;
    }

    private void updateAttribiteType(PerspectiveAttributeDescriptor attribute) {
		// Take a look at how the final return type of expression is set, write a refresh type of expressions, call it from here and then set the
    	// attributes data type from it.
		attribute.setDataType(attribute.getForwardExpression().getReturnType());
	}

	public enum PerspectiveType {
        Explicit,
        Implicit,
        Inline
    }
}
