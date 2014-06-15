/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.lang.parsing;

/**
 *
 * @author standard
 */

import org.antlr.v4.runtime.ParserRuleContext;
import xqt.lang.grammar.*;


public class GrammarListener extends XQtBaseListener {
    XQtParser parser;

    public GrammarListener(XQtParser parser) {
        this.parser = parser;
    }
    /** Listen to matches of classDeclaration */
    @Override
    public void enterEveryRule(ParserRuleContext ctx){
        //System.out.println("entered rule: " + ctx.getText() + "-->" + ctx.getRuleIndex());
    }
}