/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.model.configurations;

import xqt.model.ClauseDescriptor;

/**
 *
 * @author Javad Chamanara
 * Not in use, should be deleted eventually
 */
public class BindingScopeDescriptor extends ClauseDescriptor {

    private BindingDescriptor binding;

    public BindingDescriptor getBinding() {
        return binding;
    }

    public void setBinding(BindingDescriptor binding) {
        this.binding = binding;
    }

}
