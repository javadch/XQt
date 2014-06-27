/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.model.exceptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.antlr.v4.runtime.RecognitionException;


/**
 *
 * @author jfd
 */
public class LanguageExceptionBuilder {
    private String messageTemplate;
    private Integer lineNumber = 0;
    private Integer columnNumber = 0;
    private String contextInfo1 = "";
    private String contextInfo2 = "";
    private List<String> ruleStackTrace = new ArrayList<>();
    private RecognitionException cause;
    private List<String> contexInfos = new ArrayList<>();
    private boolean useAsTemplate = true;

    public LanguageExceptionBuilder setMessageTemplate(String value) {
        this.messageTemplate = value;
        return this;
    }

    public LanguageExceptionBuilder setLineNumber(Integer value) {
        this.lineNumber = value;
        return this;
    }

    public LanguageExceptionBuilder setColumnNumber(Integer value) {
        this.columnNumber = value;
        return this;
    }

    public LanguageExceptionBuilder setContextInfo(String contextInfo1, String... theRest) {
        contexInfos = new ArrayList<>();
        // try to replace setContextInfo1 and setContextInfo2 with this more generic one!
        contexInfos.add(contextInfo1);
        contexInfos.addAll(Arrays.asList(theRest));
        
        //this.contextInfo1 = contextInfo1;
        return this;
    }

    public LanguageExceptionBuilder setContextInfo1(String value) {
        this.contextInfo1 = value;
        return this;
    }

    public LanguageExceptionBuilder setContextInfo2(String value) {
        this.contextInfo2 = value;
        return this;
    }

    public LanguageExceptionBuilder setRuleStackTrace(List<String> value) {
        this.ruleStackTrace = value;
        return this;
    }

    public LanguageExceptionBuilder setCause(RecognitionException value) {
        this.cause = value;
        return this;
    }

    public LanguageExceptionBuilder useAsTemplate(boolean value) {
        this.useAsTemplate = value;
        return this;
    }

    private LanguageExceptionBuilder(){
        
    }
 
    public static LanguageExceptionBuilder builder() {
        return new LanguageExceptionBuilder();
    }
    
    public LanguageException build(){
        // the number of arguements used in the template is between 0 - 2. as I do not know how to pass proper arguent 
        // to its corresponsing placeholder in the template, I am just counting them and decide!
        int count = 0;
        if(useAsTemplate) //otherwise the message body does not have any placeholder for context 1 and 2
            count = messageTemplate == null? 0: (messageTemplate.length() - messageTemplate.replaceAll("\\%","").length());
        if(messageTemplate == null || messageTemplate.isEmpty() || messageTemplate.toUpperCase().equals("NULL")){
            messageTemplate = "Unknown";
            count = 0;
        }
        if(count <=0){
            LanguageException ex = new LanguageException(
                messageTemplate, lineNumber, columnNumber, ruleStackTrace, cause);
            return ex;            
        } else if (count == 1){
            LanguageException ex = new LanguageException(
                messageTemplate, lineNumber, columnNumber, contextInfo1, ruleStackTrace, cause);
            return ex;            
        } else if (count == 2){
            LanguageException ex = new LanguageException(
                messageTemplate, lineNumber, columnNumber, contextInfo1, contextInfo2, ruleStackTrace, cause);
            return ex;
        }
        return null; // should be another exception
    }

}
