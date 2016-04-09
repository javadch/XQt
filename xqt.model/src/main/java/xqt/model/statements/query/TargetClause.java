/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.model.statements.query;

import java.util.UUID;
import xqt.model.ClauseDescriptor;
import xqt.model.containers.DataContainer;

/**
 *
 * @author Javad Chamanara
 */
public class TargetClause extends ClauseDescriptor{
    private DataContainer container;
   
    public TargetClause(){
        id = UUID.randomUUID().toString();
        type = SelectQueryClauseType.Target.toString();     
    }

    public DataContainer getContainer() {
        return container;
    }

    public void setContainer(DataContainer container) {
        this.container = container;
    }
    
}
