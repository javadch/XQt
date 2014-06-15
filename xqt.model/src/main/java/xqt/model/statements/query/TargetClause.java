/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.model.statements.query;

import java.util.UUID;
import xqt.model.ClauseDescriptor;

/**
 *
 * @author jfd
 */
public class TargetClause extends ClauseDescriptor{
    private String variableName;
   
    public TargetClause(){
        id = UUID.randomUUID().toString();
        type = SelectClauseType.Target.toString();
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }            
}
