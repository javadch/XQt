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
    
    public static PerspectiveDescriptor describePerspective(XQtParser.PerspectiveContext ctx, ProcessModel processModel) throws Exception {
        PerspectiveDescriptor perspective = new PerspectiveDescriptor();
        perspective.setId(ctx.name.getText());
        perspective.setExplicit(Boolean.TRUE);
        //perspective.setName(perspective.getId());
        if(ctx.superPerspective != null){
            PerspectiveDescriptor sup = (PerspectiveDescriptor)processModel.getDeclarations().get(ctx.superPerspective.getText());
            // check whether the super is available and add its attributes to the current perspective
            if(sup != null){
                perspective.setSuperPerspective(sup);
                perspective.getAttributes().putAll(sup.getAttributes());// .entrySet().addAll(sup.getAttributes().entrySet());
                // add the sup's attributes to pers
            } else {
                throw LanguageExceptionBuilder.builder()
                    .setMessageTemplate("Perspective %s has extended perspective %s but it does not exist!")
                    .setContextInfo1( perspective.getId())
                    .setContextInfo2(ctx.superPerspective.getText())
                    .setLineNumber(ctx.getStart().getLine())
                    .setColumnNumber(ctx.getStop().getCharPositionInLine())
                    .build()
                    ;
            }
        }
        perspective.setParserContext((ParserRuleContext)ctx);
        return perspective;
    }

    public static PerspectiveAttributeDescriptor describePerspectiveAttribute(XQtParser.Attribute_defContext ctx, String perspectiveId) throws Exception {
    
        PerspectiveAttributeDescriptor attDesc = new PerspectiveAttributeDescriptor();  
        attDesc.setParserContext(ctx);
        if(ctx.smartId() != null){
            attDesc.setId(ctx.smartId().ID().getText());
            //attDesc.setName(attDesc.getId());

            if(ctx.smartId().dataType() != null){
                String typeName = XQtParser.tokenNames[ctx.smartId().dataType().getStart().getType()];
                attDesc.setDataType(typeName);
            }
            if(ctx.smartId().semanticKey()!= null)
                attDesc.setSemanticKey(ctx.smartId().semanticKey().getText());

//            if(ctx.fwd != null){ //these items will be moved to expression visitor/ descriptor methds
//                attDesc.getForwardExpression().setBody(ctx.fwd.getText());
//            }
//            if(ctx.rvs != null){
//                attDesc.getReverseExpression().setBody(ctx.rvs.getText());
//            }
            return attDesc;
        } else { //Error: the attribute has no ID
                throw LanguageExceptionBuilder.builder()
                    .setMessageTemplate("There is an attribute in perspective %s without an identifier.")
                    .setContextInfo1(perspectiveId)
                    .setLineNumber(ctx.getStart().getLine())
                    .setColumnNumber(ctx.getStop().getCharPositionInLine())
                    .build()
                    ;
        }
    }
    
}
