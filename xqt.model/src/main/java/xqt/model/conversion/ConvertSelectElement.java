/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.conversion;

import com.vaiona.commons.data.AttributeInfo;
import com.vaiona.commons.types.TypeSystem;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import xqt.model.adapters.AdapterInfo;
import xqt.model.adapters.DataAdapter;
import xqt.model.containers.DataContainer;
import xqt.model.containers.SingleContainer;
import xqt.model.declarations.PerspectiveAttributeDescriptor;
import xqt.model.declarations.PerspectiveDescriptor;
import xqt.model.exceptions.LanguageExceptionBuilder;
import xqt.model.statements.query.FilterClause;
import xqt.model.statements.query.OrderClause;
import xqt.model.statements.query.SelectDescriptor;
import xqt.model.statements.query.SourceClause;
import xqt.model.statements.query.TargetClause;

/**
 *
 * @author standard
 */
public class ConvertSelectElement {
    
    // take care when calling from adapters other than the default and CSV, because of the aggregate call redirection!!! in the convertor.visit method
    public Map<String, AttributeInfo> prepareAttributes(PerspectiveDescriptor perspective, DataAdapter adapter, boolean useOriginalNames) {
        ExpressionLocalizer convertor = new ExpressionLocalizer(adapter);
        Map<String, AttributeInfo> attributes = new LinkedHashMap<>();
        for(PerspectiveAttributeDescriptor attribute: perspective.getAttributes().values()){
            convertor.reset();
            convertor.visit(attribute.getForwardExpression());
            String exp = convertor.getSource(); 
            List<String> members = convertor.getMemeberNames();
            String typeNameInAdapter = attribute.getDataType();
            String runtimeType = TypeSystem.getTypes().get(TypeSystem.TypeName.String).getRuntimeType();
            if(TypeSystem.getTypes().containsKey(attribute.getDataType())){           
                typeNameInAdapter = TypeSystem.getTypes().get(attribute.getDataType()).getName();
                runtimeType = TypeSystem.getTypes().get(attribute.getDataType()).getRuntimeType();
            } else {
                perspective.getLanguageExceptions().add(
                    LanguageExceptionBuilder.builder()
                        .setMessageTemplate("Can not infer the data type of attribute '%s' in perspective '%s'! It has data type 'Unknown'")
                        .setContextInfo1(attribute.getId())
                        .setContextInfo2(perspective.getId())
                        .setLineNumber(attribute.getParserContext().getStart().getLine())
                        .setColumnNumber(-1)
                        .build()
                );   
            }
            if(!attributes.containsKey(attribute.getId())){
                AttributeInfo ad = new AttributeInfo();
                if(useOriginalNames & attribute.getReference()!= null)
                    ad.name = attribute.getReference().getId();
                else
                    ad.name = attribute.getId();
                ad.conceptualDataType = attribute.getDataType();
                ad.internalDataType = typeNameInAdapter;
                ad.unit = attribute.getSemanticKey();
                ad.forwardMap = exp;
                ad.fields = members;
                ad.index = attributes.size();
                ad.runtimeType = runtimeType;//TypeSystem.getTypes().get(attribute.getDataType()).getRuntimeType();
                ad.reference = attribute; // keeping the reference for possible further processing.
                ad.joinSide = attribute.getExtra();
                attributes.put(attribute.getId(), ad);
            }            
        }        
        return attributes;
    }

    public String prepareWhere(FilterClause filter, DataAdapter adapter) {
        ExpressionLocalizer convertor = new ExpressionLocalizer(adapter);
        if(filter == null || filter.getPredicate() == null)
            return "";
        convertor.reset();
        convertor.visit(filter.getPredicate()); // visit returns empty predicate string on null expressions
        String filterString = convertor.getSource();
        return filterString;
    }

    public Map<String, String> prepareOrdering(OrderClause order) {
        Map<String, String> ordering = new LinkedHashMap<>();
        try {
            order.getOrderItems().entrySet().stream()
                    .map((entry) -> entry.getValue())
                    .forEach((orderItem) -> {
                            ordering.put(orderItem.getSortKey().getId(), orderItem.getSortOrder().toString());
            });
        }
        catch (Exception ex){            
        }
        
        return ordering;
    }

    public boolean shouldResultBeWrittenIntoFile(TargetClause target) {
        return(
                (
                    target.getContainer().getDataContainerType() == DataContainer.DataContainerType.Single
                ||  target.getContainer().getDataContainerType() == DataContainer.DataContainerType.Joined
                )
        );
    }
        
    public String translateExpression(String expression, PerspectiveDescriptor perspective, String prefix) {
        String expressionTranslated = "";
        for (StringTokenizer stringTokenizer = new StringTokenizer(expression, " ");
                stringTokenizer.hasMoreTokens();) {
            String token = stringTokenizer.nextToken();
            // translate the wehre clause
            if(perspective.getAttributes().containsKey(token)){
                expressionTranslated = expressionTranslated + " " + prefix + "." + token;
            }
            else {
                expressionTranslated = expressionTranslated + " " + token;
            }                      
        }
        return expressionTranslated;
    }
    
    // should move here from DataReaderBuilderBase
//    public String enhanceExpression(String expression, boolean isJoinMode, String nonJoinPrefix, String joinPrefix) throws Exception {
//        String translated = "";
//        for (StringTokenizer stringTokenizer = new StringTokenizer(expression, " ");
//                stringTokenizer.hasMoreTokens();) {
//            String token = stringTokenizer.nextToken();
//            if(hasAggregate()){
//                // non aggregate attributes apear in both row and result entities, so if an attribute apears in the result but not in the row 
//                // entity, it is an aggregate attribute.
//                // translate the expression
//                if(rowEntityAttributes.containsKey(token)){
//                    if(!isJoinMode)
//                        translated = translated + " " + nonJoinPrefix + "." + token;
//                    else
//                        translated = translated + " " + joinPrefix + "." + token;
//                }
//                else {
//                    translated = translated + " " + token;
//                }                                      
//            } else {
//                // translate the wehre clause
//                if(resultEntityAttributes.containsKey(token)){
//                    if(!isJoinMode)
//                        translated = translated + " " + nonJoinPrefix + "." + token;
//                    else
//                        translated = translated + " " + joinPrefix + "." + token;
//                }
//                else {
//                    translated = translated + " " + token;
//                }                      
//            }
//        }
//        return translated;
//    }
//    
    
}
