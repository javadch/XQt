/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.statements.query;

import java.util.UUID;
import xqt.model.DataContainerDescriptor;

/**
 *
 * @author Javad Chamanara
 * @project XQt
 */
public class SourceClause extends DataContainerDescriptor{
    
    public final void init(){
        id = UUID.randomUUID().toString();
        type = SelectClauseType.Source.toString();        
    }
   
    public SourceClause(){
        init();
    }
    
    public static SourceClause convert(DataContainerDescriptor base){
        SourceClause source = new SourceClause();
        source.setBinding(base.getBinding());
        source.setContainerIndex(base.getContainerIndex());
        source.setDataContainerType(base.getDataContainerType());
        source.setLanguageExceptions(base.getLanguageExceptions());
        source.setOrderInParent(base.getOrderInParent());
        source.setParserContext(base.getParserContext());
        source.setVariableName(base.getVariableName());
        return source;
    } 

    public static SourceClause createVariableSource(String variableName){
        SourceClause source = new SourceClause();
        source.setLifeTime(ContinaerLifeTime.Short);
        source.setDataContainerType(DataContainerType.Variable);
        source.setVariableName(variableName);
        return source;
    } 

}
