/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model;

/**
 *  a syntactic construction containing a subject and predicate and forming part of a sentence or
 *  constituting a whole simple sentence.
 *  A clause is an incomplete fragment of a statement that encapsulates an actor and an action.
 *  e.g. in a join clause, the action is the join and the actor is the table being joined.
 * @author Javad Chamanara
 * @project SciQuest
 */
public class ClauseDescriptor extends BaseDescriptor {
    protected boolean isPresent = false;

    // determines whether the clause exists in the statement. if not an empty/ defalt clause is added which its isPresent is false.
    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean isPresent) {
        this.isPresent = isPresent;
    }
    
}
