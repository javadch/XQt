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
import xqt.model.containers.DataContainer;
import xqt.model.containers.SingleContainer;
import xqt.model.declarations.PerspectiveAttributeDescriptor;
import xqt.model.statements.query.SelectDescriptor;

/**
 *
 * @author standard
 */
public class ConvertSelectElement {
    private ExpressionToJavaSource convertor = null;
    
    public ConvertSelectElement(){
        convertor = new ExpressionToJavaSource();
    }

    public Map<String, AttributeInfo> prepareAttributes(SelectDescriptor select) {
        Map<String, AttributeInfo> attributes = new LinkedHashMap<>();
        for(PerspectiveAttributeDescriptor attribute: select.getProjectionClause().getPerspective().getAttributes().values()){
            convertor.reset();
            convertor.visit(attribute.getForwardExpression());
            String exp = convertor.getSource(); 
            List<String> members = convertor.getMemeberNames();
            String typeNameInAdapter = TypeSystem.getTypes().get(attribute.getDataType()).getName();
            if(!attributes.containsKey(attribute.getId())){
                AttributeInfo ad = new AttributeInfo();
                ad.name = attribute.getId();
                ad.formalDataType = attribute.getDataType();
                ad.internalDataType = typeNameInAdapter;
                ad.forwardMap = exp;
                ad.fields = members;
                ad.index = attributes.size();
                attributes.put(attribute.getId(), ad);
            }            
        }        
        return attributes;
    }

    public String prepareWhere(SelectDescriptor select) {
        if(select.getFilterClause() == null || select.getFilterClause().getPredicate() == null)
            return "";
        convertor.reset();
        convertor.visit(select.getFilterClause().getPredicate()); // visit returns empty predicate string on null expressions
        String filterString = convertor.getSource();
        return filterString;
    }

    public Map<String, String> prepareOrdering(SelectDescriptor select) {
        Map<String, String> ordering = new LinkedHashMap<>();
        try {
            select.getOrderClause().getOrderItems().entrySet().stream()
                    .map((entry) -> entry.getValue())
                    .forEach((orderItem) -> {
                            ordering.put(orderItem.getSortKey(), orderItem.getSortOrder().toString());
            });
        }
        catch (Exception ex){            
        }
        
        return ordering;
    }

    public boolean shouldResultBeWrittenIntoFile(SelectDescriptor select) {
        return(
                (
                    select.getTargetClause().getContainer().getDataContainerType() == DataContainer.DataContainerType.Single
                ||  select.getTargetClause().getContainer().getDataContainerType() == DataContainer.DataContainerType.Joined
                )
        );
    }
    
    public String getCompleteSourceName(SelectDescriptor select){ //may need a container index too!
        // see whether the source is a simple one or a joined!
        if(select.getSourceClause().getContainer().getDataContainerType() == DataContainer.DataContainerType.Single){
            SingleContainer container = (SingleContainer)select.getSourceClause().getContainer();
            String basePath = container.getBinding().getConnection().getSourceUri();
            String container0 = container.getContainerName();
            String fileExtention = "csv";
            String fileName = "";
            try{
                fileExtention = container.getBinding().getConnection().getParameters().get("fileExtension").getValue();
            } catch (Exception ex){}
            fileName = basePath.concat(container0).concat(".").concat(fileExtention);
            return fileName;
            }
        else if(select.getSourceClause().getContainer().getDataContainerType() == DataContainer.DataContainerType.Joined){
            return null;
        }
        return null;
    }
  
    public String getCompleteTargetName(SelectDescriptor select){ //may need a container index too!
        if(select.getTargetClause().getContainer().getDataContainerType() == DataContainer.DataContainerType.Single){
            SingleContainer container = (SingleContainer)select.getTargetClause().getContainer();
            String basePath = container.getBinding().getConnection().getSourceUri();
            String container0 = container.getContainerName();
            String fileExtention = "csv";
            String fileName = "";
            try{
                fileExtention = ((SingleContainer)select.getSourceClause().getContainer())
                        .getBinding().getConnection().getParameters().get("fileExtension").getValue();
            } catch (Exception ex){}
            fileName = basePath.concat(container0).concat(".").concat(fileExtention);
            return fileName;
        } else {
            return null;
        }
    }
    
    public String prepareExpression(SelectDescriptor select, String expression) {
        String expressionTranslated = "";
        for (StringTokenizer stringTokenizer = new StringTokenizer(expression, " ");
                stringTokenizer.hasMoreTokens();) {
            String token = stringTokenizer.nextToken();
            // translate the wehre clause
            if(select.getProjectionClause().getPerspective().getAttributes().containsKey(token)){
                expressionTranslated = expressionTranslated + " " + "p." + token;
            }
            else {
                expressionTranslated = expressionTranslated + " " + token;
            }                      
        }
        return expressionTranslated;
    }
}
