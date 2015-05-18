/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.lang.annotation;

import org.antlr.v4.runtime.ParserRuleContext;
import xqt.lang.grammar.XQtParser;
import xqt.model.ProcessModel;
import xqt.model.declarations.PerspectiveAttributeDescriptor;
import xqt.model.declarations.PerspectiveDescriptor;
import xqt.model.exceptions.LanguageExceptionBuilder;

/**
 *
 * @author jfd
 */
public class PerspectiveAnnotator {
    // its better to move these methods to PerspectiveDescriptor and PerspectiveAttributeDescriptor classes, but currently it makes the model project dependent upon the lang model!!!
    public static PerspectiveDescriptor describePerspective(XQtParser.PerspectiveContext ctx, ProcessModel processModel) {
        PerspectiveDescriptor perspective = new PerspectiveDescriptor();
        perspective.setId(ctx.name.getText());
        perspective.setExplicit();
        //perspective.setName(perspective.getId());
        if(ctx.superPerspective != null){
            PerspectiveDescriptor sup = (PerspectiveDescriptor)processModel.getDeclarations().get(ctx.superPerspective.getText());
            // check whether the super is available and add its attributes to the current perspective
            if(sup != null){
                perspective.setSuperPerspective(sup);
                perspective.getAttributes().putAll(sup.getAttributes());// .entrySet().addAll(sup.getAttributes().entrySet());
                // add the sup's attributes to pers
            } else {
                perspective.getLanguageExceptions().add(
                    LanguageExceptionBuilder.builder()
                        .setMessageTemplate("Perspective %s has extended perspective %s but it does not exist!")
                        .setContextInfo1( perspective.getId())
                        .setContextInfo2(ctx.superPerspective.getText())
                        .setLineNumber(ctx.getStart().getLine())
                        .setColumnNumber(ctx.getStop().getCharPositionInLine())
                        .build()
                );
            }
        }
        perspective.setParserContext((ParserRuleContext)ctx);
        return perspective;
    }

    public static PerspectiveAttributeDescriptor describePerspectiveAttribute(XQtParser.AttributeContext ctx, String perspectiveId) {
    
        PerspectiveAttributeDescriptor attDesc = new PerspectiveAttributeDescriptor();  
        attDesc.setParserContext(ctx);
        if(ctx.smartId() != null){
            attDesc.setId(ctx.smartId().ID().getText());
            //attDesc.setName(attDesc.getId());

            if(ctx.smartId().dataType() != null){
                String typeName = XQtParser.tokenNames[ctx.smartId().dataType().getStart().getType()];
                attDesc.setDataType(typeName);
            }
            if(ctx.smartId().semanticKey()!= null){
                attDesc.setSemanticKey(ctx.smartId().semanticKey().getText());
                attDesc.setSemanticKey(attDesc.getSemanticKey().replace("\"", ""));
            }
        } else { //Error: the attribute has no ID
            attDesc.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate("There is an attribute in perspective %s without an identifier.")
                    .setContextInfo1(perspectiveId)
                    .setLineNumber(ctx.getStart().getLine())
                    .setColumnNumber(ctx.getStop().getCharPositionInLine())
                    .build()
            );
        }
        return attDesc;
    }    
}
