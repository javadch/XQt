/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.statements.query;

import java.util.UUID;
import xqt.model.ClauseDescriptor;
import xqt.model.expressions.Expression;

/**
 *
 * @author Javad Chamanara
 * @project SciQuest
 */
public class SelectionFeature extends ClauseDescriptor{
    private Expression predicate;

    public SelectionFeature(){
        id = UUID.randomUUID().toString();
        type = SelectQueryClauseType.Filter.toString();
    }

    public Expression getPredicate() {
        return predicate;
    }

    public void setPredicate(Expression predicate) {
        this.predicate = predicate;
        if(this.predicate != null)
            isPresent = true;
        else
            isPresent = false;
        
    }
}
