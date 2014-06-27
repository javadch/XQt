/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.lang;

import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import xqt.model.exceptions.LanguageException;
import xqt.model.exceptions.LanguageExceptionBuilder;

/**
 *
 * @author standard
 */
public class DescriptiveErrorListener extends BaseErrorListener {
    public static DescriptiveErrorListener INSTANCE = new DescriptiveErrorListener();

    private List<Exception> exceptions = new ArrayList<>();

    public List<Exception> getExceptions() {
        return exceptions;
    }

    public void setExceptions(List<Exception> exceptions) {
        this.exceptions = exceptions;
    }
    
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
                            String msg, RecognitionException e)
    {
//        String sourceName = "test";
//        System.err.println(sourceName+"line "+line+":"+charPositionInLine+" "+msg);

        LanguageException lx = 
            LanguageExceptionBuilder.builder()
                .setMessageTemplate(msg)
                //.setContextInfo1()
                .setLineNumber(line)
                .setColumnNumber(charPositionInLine)
                //.setCause(ex)
                .useAsTemplate(false)
                .build();
        exceptions.add(lx);
    }
}
