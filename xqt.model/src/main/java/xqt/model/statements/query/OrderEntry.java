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
public class OrderEntry {
    private MemberExpression sortKey;
    private SortOrder sortOrder = SortOrder.ASC;
    private NullOrdering nullOrdering = NullOrdering.NULL_LAST;
    private ParserRuleContext parserContext;


    public MemberExpression getSortKey() {
        return sortKey;
    }

    public void setSortKey(MemberExpression sortKey) {
        this.sortKey = sortKey;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public NullOrdering getNullOrdering() {
        return nullOrdering;
    }

    public void setNullOrdering(NullOrdering nullOrdering) {
        this.nullOrdering = nullOrdering;
    }

    public ParserRuleContext getParserContext() {
        return parserContext;
    }

    public void setParserContext(ParserRuleContext parserContext) {
        this.parserContext = parserContext;
    }

    
}
