/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.model.exceptions;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.RecognitionException;

/**
 *
 * @author Javad Chamanara
 */
public class LanguageException extends Exception{
	private static final long serialVersionUID = 1L;
	private String messageTemplate;
    private Integer lineNumber = 0;
    private Integer columnNumber = 0;
    private String contextInfo1 = "";
    private String contextInfo2 = "";
    private List<String> ruleStackTrace = new ArrayList<>();
    private RecognitionException cause;

    public String getMessageTemplate() {
        return messageTemplate;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public Integer getColumnNumber() {
        return columnNumber;
    }

    public String getContextInfo1() {
        return contextInfo1;
    }

    public String getContextInfo2() {
        return contextInfo2;
    }

    public List<String> getRuleStackTrace() {
        return ruleStackTrace;
    }

    public RecognitionException getCause() {
        return cause;
    }  
    
//    public LanguageException(String message){
//        super(message);
//    }

    @SuppressWarnings("unused") // used to prevent instantiating an empty onject
	private LanguageException(){
        //super();
    }
    
    public LanguageException(String messageTemplate, Integer lineNumber, Integer columnNumber, List<String> ruleStack, RecognitionException cause){        
        //super(String.format(messageTemplate + " Line %s ", contextInfo1, contextInfo2, lineNumber, columnNumber));
        super(MessageFormat.format("{0} Line {1} Column {2}", messageTemplate, lineNumber, columnNumber));
        //super(String.format(messageTemplate + " Line %s " + "Column %s.", lineNumber, columnNumber));
        this.messageTemplate = messageTemplate;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
        this.contextInfo1 = "";
        this.contextInfo2 = "";
        this.ruleStackTrace = ruleStack;
        this.cause = cause;
    }

    public LanguageException(String messageTemplate, Integer lineNumber, Integer columnNumber, String contextInfo1, List<String> ruleStack, RecognitionException cause){        
        //super(String.format(messageTemplate + " Line %s ", contextInfo1, contextInfo2, lineNumber, columnNumber));
        super(String.format(messageTemplate + " Line %s " + "Column %s.", contextInfo1, lineNumber, columnNumber));
        this.messageTemplate = messageTemplate;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
        this.contextInfo1 = contextInfo1;
        this.contextInfo2 = "";
        this.ruleStackTrace = ruleStack;
        this.cause = cause;
    }

        
    public LanguageException(String messageTemplate, Integer lineNumber, Integer columnNumber,
            String contextInfo1, String contextInfo2, List<String> ruleStack, RecognitionException cause){        
        //super(String.format(messageTemplate + " Line %s ", contextInfo1, contextInfo2, lineNumber, columnNumber));
        super(String.format(messageTemplate + " Line %s " + "Column %s.", contextInfo1, contextInfo2, lineNumber, columnNumber));
        this.messageTemplate = messageTemplate;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
        this.contextInfo1 = contextInfo1;
        this.contextInfo2 = contextInfo2;
        this.ruleStackTrace = ruleStack;
        this.cause = cause;
    }

}
