/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.model.statements.query;

import java.util.UUID;
import xqt.model.DataContainerDescriptor;

/**
 *
 * @author Javad Chamanara
 */
public class TargetClause extends DataContainerDescriptor{
    public final void init(){
        id = UUID.randomUUID().toString();
        type = SelectClauseType.Target.toString();        
    }
    public TargetClause(){
        init();
    }
    
    public static TargetClause convert(DataContainerDescriptor base){
        TargetClause target = new TargetClause();
        target.setBinding(base.getBinding());
        target.setContainerIndex(base.getContainerIndex());
        target.setDataContainerType(base.getDataContainerType());
        target.setLanguageExceptions(base.getLanguageExceptions());
        target.setOrderInParent(base.getOrderInParent());
        target.setParserContext(base.getParserContext());
        target.setVariableName(base.getVariableName());
        return target;
    }     
}
