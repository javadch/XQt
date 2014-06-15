/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.lang.annotation;

import org.antlr.v4.runtime.ParserRuleContext;
import xqt.model.exceptions.LanguageException;
import xqt.lang.grammar.XQtParser;
import xqt.model.ProcessModel;
import xqt.model.configurations.ConnectionDescriptor;
import xqt.model.configurations.ConnectionParameterDescriptor;
import xqt.model.exceptions.LanguageExceptionBuilder;



/**
 *
 * @author jfd
 */
public class ConnectionAnnotator {
 
    public static ConnectionDescriptor describeConnection(XQtParser.ConnectionContext ctx, ProcessModel processModel) throws LanguageException{
        
        ConnectionDescriptor connection = new ConnectionDescriptor();
        connection.setId(ctx.name.getText());
        //connection.setName(connection.getId());

        if(ctx.adapterName == null){
            throw LanguageExceptionBuilder.builder()
                .setMessageTemplate("Connection %s has not defined an adpater.")
                .setContextInfo1(connection.getId())
                .setLineNumber(ctx.getStart().getLine())
                .setColumnNumber(ctx.getStop().getCharPositionInLine())
                .build()
                ;
        }
        connection.setAdapterName(ctx.adapterName.getText());
        //check if the adapter is registered in the system
        if(!isAdapterRegistered()){
            throw LanguageExceptionBuilder.builder()
                .setMessageTemplate("Adapter %s is not registered.")
                .setContextInfo1(connection.getAdapterName())
                .setLineNumber(ctx.getStart().getLine())
                .setColumnNumber(ctx.getStart().getCharPositionInLine())
                .build()
                ;
        }
        if(ctx.srcURI == null){
            throw LanguageExceptionBuilder.builder()
                .setMessageTemplate("Connection %s has not defined a data source.")
                .setContextInfo1(connection.getId())
                .setLineNumber(ctx.getStart().getLine())
                .setColumnNumber(ctx.getStart().getCharPositionInLine())
                .build()
                ;
        }
        connection.setSourceUri(ctx.srcURI.getText());
        
        connection.setParserContext((ParserRuleContext)ctx);
        return connection;       
    }

    public static ConnectionParameterDescriptor describeConnectionParameter(XQtParser.Parameter_defContext ctx, String connectionId) throws LanguageException {        

        ConnectionParameterDescriptor paramDesc = new ConnectionParameterDescriptor();        
        paramDesc.setParserContext(ctx);
        if(ctx.ID()!= null){
            paramDesc.setId(ctx.ID().getText());
            //paramDesc.setName(paramDesc.getId());

            if(ctx.expression() != null){ //these items will be moved to expression visitor/ descriptor methds
                paramDesc.setValue(ctx.expression().getText());
            } else {
                throw LanguageExceptionBuilder.builder()
                    .setMessageTemplate("Parameter %s has no value.")
                    .setContextInfo1(paramDesc.getId())
                    .setLineNumber(ctx.getStart().getLine())
                    .setColumnNumber(ctx.getStop().getCharPositionInLine())
                    .build()
                    ;
            }
            return paramDesc;
        } else { //Error: the attribute has no ID
                throw LanguageExceptionBuilder.builder()
                    .setMessageTemplate("There is an attribute in perspective %s without an identifier.")
                    .setContextInfo1(connectionId)
                    .setLineNumber(ctx.getStart().getLine())
                    .setColumnNumber(ctx.getStart().getCharPositionInLine())
                    .build()
                    ;
        }
    }

    private static boolean isAdapterRegistered() {
        return true;
    }

}
