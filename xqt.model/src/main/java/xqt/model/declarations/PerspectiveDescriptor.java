/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.model.declarations;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
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
                        .setMessageTemplate("There is a duplicate attribute named %s defined in perspective %s.")
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
    
    public enum PerspectiveType {
        Explicit,
        Implicit,
        Inline
    }
}
