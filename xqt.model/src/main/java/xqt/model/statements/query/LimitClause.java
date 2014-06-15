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
public class LimitClause extends ClauseDescriptor{
    private Integer skip = -1; //Integer is nullable. null means the phrase was not presented in the statement
    private Integer take = -1;

    public LimitClause(){
        id = UUID.randomUUID().toString();
        type = SelectClauseType.Limit.toString();
    }

    public Integer getSkip() {
        return skip;
    }

    public void setSkip(Integer skip) {
        this.skip = skip;
    }

    public Integer getTake() {
        return take;
    }

    public void setTake(Integer take) {
        this.take = take;
    }

    
}
