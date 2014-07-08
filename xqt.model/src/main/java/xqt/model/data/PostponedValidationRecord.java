/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.data;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 *
 * @author standard
 */
public class PostponedValidationRecord {
    // in some cases it is not possible to validate a part of the input. for example during visiting
    // a Description IS NUMBER is not possible to know whether the Description attribute is of type string.
    // in these cases, enough information is generated and passed to upper level visitors so that they can do the validation
    // context fields can be any thing needed by the upper level validator.
    private String context1; 
    private String context2;
    private String context3;
    private ParserRuleContext parserContext;

    public String getContext1() {
        return context1;
    }

    public void setContext1(String context1) {
        this.context1 = context1;
    }

    public String getContext2() {
        return context2;
    }

    public void setContext2(String context2) {
        this.context2 = context2;
    }

    public String getContext3() {
        return context3;
    }

    public void setContext3(String context3) {
        this.context3 = context3;
    }

    public ParserRuleContext getParserContext() {
        return parserContext;
    }

    public void setParserContext(ParserRuleContext parserContext) {
        this.parserContext = parserContext;
    }
}
