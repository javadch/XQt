/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model;

import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import xqt.model.exceptions.LanguageException;

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
    protected List<LanguageException> languageExceptions = new ArrayList<>();

    public boolean hasError(){
        return languageExceptions.size() > 0;
    }
    
    public List<LanguageException> getLanguageExceptions() {
        return languageExceptions;
    }

    public void setLanguageExceptions(List<LanguageException> languageExceptions) {
        this.languageExceptions = languageExceptions;
    }

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
