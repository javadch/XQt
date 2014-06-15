/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.lang.annotation;

import org.antlr.v4.runtime.ParserRuleContext;
import xqt.model.exceptions.LanguageException;
import xqt.lang.grammar.XQtParser;
import xqt.model.ProcessModel;
import xqt.model.configurations.BindingDescriptor;
//import xqt.model.configurations.BindingScopeDescriptor;
import xqt.model.configurations.ConnectionDescriptor;
import xqt.model.exceptions.LanguageExceptionBuilder;

/**
 *
 * @author jfd
 */
public class BindingAnnotator {

    public static BindingDescriptor describeBinding(XQtParser.BindingContext ctx, ProcessModel processModel) throws LanguageException {
        BindingDescriptor binding = new BindingDescriptor();
        binding.setId(ctx.name.getText());
        //binding.setName(binding.getId());

        if(ctx.connectionName == null){
             throw LanguageExceptionBuilder.builder()
                    .setMessageTemplate("Binding %s has not defined a connection!")
                    .setContextInfo1(binding.getId())
                    .setLineNumber(ctx.getStart().getLine())
                    .setColumnNumber(ctx.getStop().getCharPositionInLine())
                    .build()
                    ;
        }
        ConnectionDescriptor connection = (ConnectionDescriptor)processModel.getConfigurations().get(ctx.connectionName.getText());
        if(connection != null){
            binding.setConnection(connection);
        } else {
                throw LanguageExceptionBuilder.builder()
                    .setMessageTemplate("Binding %s is using the connection %s, which is not declared!")
                    .setContextInfo1(binding.getId())
                    .setContextInfo2(ctx.connectionName.getText())
                    .setLineNumber(ctx.getStart().getLine())
                    .setColumnNumber(ctx.getStart().getCharPositionInLine())
                    .build()
                    ;
        }        
        binding.setParserContext((ParserRuleContext)ctx);
        return binding;       
        
    }

    public static String describeBindingSource(XQtParser.Binding_scope_defContext ctx, String bindingId) throws LanguageException {
        if(ctx.ID()!= null){
            return ctx.ID().getText();
        } else { //Error: the attribute has no ID
            throw LanguageExceptionBuilder.builder()
                    .setMessageTemplate("There is a scope in the binding %s without an identifier.")
                    .setContextInfo1(bindingId)
                    .setLineNumber(ctx.getStart().getLine())
                    .setColumnNumber(ctx.getStop().getCharPositionInLine())
                    .build()
                    ;
        }
    }
}
