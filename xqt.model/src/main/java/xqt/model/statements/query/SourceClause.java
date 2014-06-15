/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.statements.query;

import java.util.UUID;
import xqt.model.ClauseDescriptor;
import xqt.model.configurations.BindingDescriptor;

/**
 *
 * @author Javad Chamanara
 * @project SciQuest
 */
public class SourceClause extends ClauseDescriptor{
    private BindingDescriptor binding; // can be an external source (binding) or a variable
    private Integer containerIndex; //shoud be an index to one of the scopes defined in the linked binding
    // all the above fields are temporary
    
    public BindingDescriptor getBinding() {
        return binding;
    }

    public void setBinding(BindingDescriptor binding) {
        this.binding = binding;
    }

    public Integer getContainerIndex() {
        return containerIndex;
    }

    public String getContainer() {
        return  binding.getScopes().get(containerIndex);
    }

    public void setContainerIndex(Integer container) {
        this.containerIndex = container;
    }

    public SourceClause(){
        id = UUID.randomUUID().toString();
        type = SelectClauseType.Source.toString();
    }
}
