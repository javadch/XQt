/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.statements.query;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 *
 * @author Javad Chamanara
 * @project SciQuest
 */
public class GroupEntry {
    private String id;
    private ParserRuleContext parserContext;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ParserRuleContext getParserContext() {
        return parserContext;
    }

    public void setParserContext(ParserRuleContext parserContext) {
        this.parserContext = parserContext;
    }
    
}
