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
public class FilterClause extends ClauseDescriptor{
    private Expression predicate;

    public FilterClause(){
        id = UUID.randomUUID().toString();
        type = SelectClauseType.Filter.toString();
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
