/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.statements.query;

import java.util.UUID;
import xqt.model.ClauseDescriptor;

/**
 *
 * @author Javad Chamanara
 * @project SciQuest
 */
public class SetQualifierFeature extends ClauseDescriptor{
    private SetQualifierType qualifier = SetQualifierType.DISTINCT;

    public SetQualifierFeature(){
        id = UUID.randomUUID().toString();
        type = SelectQueryClauseType.Qualifier.toString();
    }

    public SetQualifierType getQualifier() {
        return qualifier;
    }

    public void setQualifier(SetQualifierType qualifier) {
        this.qualifier = qualifier;
    }

}
