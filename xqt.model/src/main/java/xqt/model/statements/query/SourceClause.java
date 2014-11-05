
package xqt.model.statements.query;

import java.util.UUID;
import xqt.model.ClauseDescriptor;
import xqt.model.containers.DataContainer;

/**
 *
 * @author Javad Chamanara
 * @project XQt
 */
public class SourceClause extends ClauseDescriptor { //DataContainerDescriptor{
    private DataContainer container;
   
    public SourceClause(){
        id = UUID.randomUUID().toString();
        type = SelectClauseType.Source.toString();     
    }

    public DataContainer getContainer() {
        return container;
    }

    public void setContainer(DataContainer container) {
        this.container = container;
    }
    
}
