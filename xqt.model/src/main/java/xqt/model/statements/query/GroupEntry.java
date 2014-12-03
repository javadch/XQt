/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.statements.query;

import org.antlr.v4.runtime.ParserRuleContext;
import xqt.model.expressions.MemberExpression;

/**
 *
 * @author Javad Chamanara
 * @project SciQuest
 */
public class GroupEntry {
    private MemberExpression key;
    private ParserRuleContext parserContext;

    public MemberExpression getKey() {
        return key;
    }

    public void setKey(MemberExpression key) {
        this.key = key;
    }

    public ParserRuleContext getParserContext() {
        return parserContext;
    }

    public void setParserContext(ParserRuleContext parserContext) {
        this.parserContext = parserContext;
    }
    
}
