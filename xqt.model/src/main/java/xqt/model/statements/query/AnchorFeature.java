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
public class AnchorFeature extends ClauseDescriptor{
        private Expression startAnchor;
        private Expression stopAnchor;

    public AnchorFeature(){
        id = UUID.randomUUID().toString();
        type = SelectQueryClauseType.Anchor.toString();
        isPresent = false;
    }

    public Expression getStartAnchor() {
        return startAnchor;
    }

    public void setStartAnchor(Expression startAnchor) {
        this.startAnchor = startAnchor;
        if(this.startAnchor != null || this.stopAnchor != null)
            isPresent = true;
        else
            isPresent = false;
    }

    public Expression getStopAnchor() {
        return stopAnchor;
    }

    public void setStopAnchor(Expression stopAnchor) {
        this.stopAnchor = stopAnchor;
        if(this.startAnchor != null || this.stopAnchor != null)
            isPresent = true;
        else
            isPresent = false;
    }

    
}
