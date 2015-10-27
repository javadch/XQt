/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.model.containers;

import xqt.model.configurations.BindingDescriptor;
import xqt.model.declarations.PerspectiveDescriptor;

/**
 *
 * @author Javad Chamanara <chamanara@gmail.com>
 */
public class SingleContainer extends DataContainer {
    private BindingDescriptor binding;
    private Integer containerIndex; //Should be an index to one of the scopes defined in the linked binding
    // when the container is used in a joined container, it needs to now its perspective.
    // in these cases the perspective is set on the container itself not on the statement.
    // it is also set for the target clause of single source containers during the visitation, but currently not used.
    private PerspectiveDescriptor perspective; 
    private String detachedContainerName = "";

    public SingleContainer(){
        this.dataContainerType = DataContainerType.Single;
    }

    public BindingDescriptor getBinding() {
        return binding;
    }

    public void setBinding(BindingDescriptor binding) {
        this.binding = binding;
        // Id is set as the generic name of the container whether it is an external container or a variable
        if(binding != null && binding.getScopes() != null && containerIndex != null && containerIndex >=0 && containerIndex < binding.getScopes().size())
            this.id = binding.getScopes().get(containerIndex);
    }

    public String getContainerName() {
    	if(containerIndex == null) // container index was a non numerical string. detected by the grammar visitor.
    		return detachedContainerName;
    	if(binding.getScopes() != null && containerIndex >=0  && containerIndex >= 0  && containerIndex < binding.getScopes().size()){ // there are some scope items defined, so the container name should be among them.
    		return  binding.getScopes().get(containerIndex);
    	}
    	return containerIndex.toString(); // no scope is defined, but an integer index is provided. Example: an excel sheet index without names.
    }

    public void setContinaerName(String value){
    	detachedContainerName = value;
    }
    public Integer getContainerIndex() {
        return containerIndex;
    }

    public void setContainerIndex(Integer container) {
        this.containerIndex = container;
        if(binding != null && binding.getScopes() != null  && containerIndex != null && containerIndex >=0  && containerIndex >= 0  && containerIndex < binding.getScopes().size())
            this.id = binding.getScopes().get(containerIndex);
    }    

    public PerspectiveDescriptor getPerspective() {
        return perspective;
    }

    public void setPerspective(PerspectiveDescriptor perspective) {
        this.perspective = perspective;
    }
    
}
