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
import xqt.model.containers.DataContainer;
import xqt.model.containers.SingleContainer;
import xqt.model.declarations.PerspectiveAttributeDescriptor;
import xqt.model.declarations.PerspectiveDescriptor;
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
    private ExpressionLocalizer convertor = null;
    
    public ConvertSelectElement(){
        convertor = new ExpressionLocalizer();
    }

    public Map<String, AttributeInfo> prepareAttributes(PerspectiveDescriptor perspective, AdapterInfo adapterInfo, boolean useOriginalNames) {
        Map<String, AttributeInfo> attributes = new LinkedHashMap<>();
        for(PerspectiveAttributeDescriptor attribute: perspective.getAttributes().values()){
            convertor.reset();
            convertor.visit(attribute.getForwardExpression(), adapterInfo);
            String exp = convertor.getSource(); 
            List<String> members = convertor.getMemeberNames();
            String typeNameInAdapter = TypeSystem.getTypes().get(attribute.getDataType()).getName();
            if(!attributes.containsKey(attribute.getId())){
                AttributeInfo ad = new AttributeInfo();
                if(useOriginalNames & attribute.getReference()!= null)
                    ad.name = attribute.getReference().getId();
                else
                    ad.name = attribute.getId();
                ad.conceptualDataType = attribute.getDataType();
                ad.internalDataType = typeNameInAdapter;
                ad.forwardMap = exp;
                ad.fields = members;
                ad.index = attributes.size();
                ad.runtimeType = TypeSystem.getTypes().get(attribute.getDataType()).getRuntimeType();
                ad.reference = attribute; // keeping the reference for possible further processing.
                ad.joinSide = attribute.getExtra();
                attributes.put(attribute.getId(), ad);
            }            
        }        
        return attributes;
    }

    public String prepareWhere(FilterClause filter, AdapterInfo adapterInfo) {
        if(filter == null || filter.getPredicate() == null)
            return "";
        convertor.reset();
        convertor.visit(filter.getPredicate(), adapterInfo); // visit returns empty predicate string on null expressions
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
        
    public String translateExpression(String expression, PerspectiveDescriptor perspective) {
        String expressionTranslated = "";
        for (StringTokenizer stringTokenizer = new StringTokenizer(expression, " ");
                stringTokenizer.hasMoreTokens();) {
            String token = stringTokenizer.nextToken();
            // translate the wehre clause
            if(perspective.getAttributes().containsKey(token)){
                expressionTranslated = expressionTranslated + " " + "p." + token;
            }
            else {
                expressionTranslated = expressionTranslated + " " + token;
            }                      
        }
        return expressionTranslated;
    }
}
