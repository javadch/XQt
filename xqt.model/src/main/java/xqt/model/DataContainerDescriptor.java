/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model;

import xqt.model.configurations.BindingDescriptor;

/**
 *
 * @author Javad Chamanara
 */
public class DataContainerDescriptor extends ClauseDescriptor{
    public enum DataContainerType {
        Simplecontainer, JoinedContainer, Variable
    }
    
    protected DataContainerType dataContainerType = DataContainerType.Simplecontainer;
    protected BindingDescriptor binding;
    protected Integer containerIndex; //shoud be an index to one of the scopes defined in the linked binding
    protected String variableName;

    public DataContainerType getDataContainerType() {
        return dataContainerType;
    }

    public void setDataContainerType(DataContainerType dataContainerType) {
        this.dataContainerType = dataContainerType;
    }
    
    public BindingDescriptor getBinding() {
        return binding;
    }

    public void setBinding(BindingDescriptor binding) {
        this.binding = binding;
        // Id is et as the generic name of the container whetehr it is an external container or a variable
        if(binding != null && binding.getScopes() != null && containerIndex != null && containerIndex >=0 )
            this.id = binding.getScopes().get(containerIndex);
    }

    public Integer getContainerIndex() {
        return containerIndex;
    }

    public String getContainer() {
        return  binding.getScopes().get(containerIndex);
    }

    public void setContainerIndex(Integer container) {
        this.containerIndex = container;
        if(binding != null && binding.getScopes() != null  && containerIndex != null && containerIndex >=0 )
            this.id = binding.getScopes().get(containerIndex);
    }    
    
    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
        this.id = variableName;
    }            
    
}
