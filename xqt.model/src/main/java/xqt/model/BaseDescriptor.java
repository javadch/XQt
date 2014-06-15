/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 *
 * @author Javad Chamanara
 * @project SciQuest
 */
public class BaseDescriptor {
    protected Integer orderInParent = -1;
    //protected String name;
    protected String id = "";
    protected String type = "";
    protected ParserRuleContext parserContext;

    public ParserRuleContext getParserContext() {
        return parserContext;
    }

    public void setParserContext(ParserRuleContext parserContext) {
        this.parserContext = parserContext;
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getOrderInParent() {
        return orderInParent;
    }

    public void setOrderInParent(Integer orderInParent) {
        this.orderInParent = orderInParent;
    }

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
