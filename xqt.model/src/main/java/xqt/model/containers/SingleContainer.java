/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.model.containers;

import xqt.model.configurations.BindingDescriptor;

/**
 *
 * @author Javad Chamanara <chamanara@gmail.com>
 */
public class SingleContainer extends DataContainer {
    protected BindingDescriptor binding;
    protected Integer containerIndex; //shoud be an index to one of the scopes defined in the linked binding

    public SingleContainer(){
        this.dataContainerType = DataContainerType.Single;
    }

    public BindingDescriptor getBinding() {
        return binding;
    }

    public void setBinding(BindingDescriptor binding) {
        this.binding = binding;
        // Id is set as the generic name of the container whetehr it is an external container or a variable
        if(binding != null && binding.getScopes() != null && containerIndex != null && containerIndex >=0 )
            this.id = binding.getScopes().get(containerIndex);
    }

    public Integer getContainerIndex() {
        return containerIndex;
    }

    public String getContainerName() {
        return  binding.getScopes().get(containerIndex);
    }

    public void setContainerIndex(Integer container) {
        this.containerIndex = container;
        if(binding != null && binding.getScopes() != null  && containerIndex != null && containerIndex >=0 )
            this.id = binding.getScopes().get(containerIndex);
    }    
    
}
