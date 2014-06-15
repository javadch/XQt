/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.statements.query;

import java.util.UUID;
import xqt.model.ClauseDescriptor;
import xqt.model.declarations.PerspectiveDescriptor;

/**
 *
 * @author Javad Chamanara
 * @project SciQuest
 */
public class ProjectionClause extends ClauseDescriptor{
    private PerspectiveDescriptor perspective = null;

    public ProjectionClause(){
        id = UUID.randomUUID().toString();
        type = SelectClauseType.Projection.toString();
    }
    
    public PerspectiveDescriptor getPerspective() {
        return perspective;
    }

    public void setPerspective(PerspectiveDescriptor perspective) {
        this.perspective = perspective;
    }
}
