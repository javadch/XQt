/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.model.configurations;

import java.util.HashMap;
import java.util.Map;
import xqt.model.exceptions.LanguageExceptionBuilder;


/**
 *
 * @author Javad Chamanara
 */
public class ConnectionDescriptor extends ConfigurationDescriptor{
   
    private String adapterName;
    private String sourceUri;
    private Map<String, ConnectionParameterDescriptor> parameters = new HashMap<String, ConnectionParameterDescriptor>();
    
    public String getAdapterName() {
        return adapterName;
    }

    public void setAdapterName(String adapterName) {
        this.adapterName = adapterName;
    }

    public String getSourceUri() {
        return sourceUri;
    }

    public void setSourceUri(String sourceUri) {
        this.sourceUri = sourceUri;
        if(this.sourceUri.startsWith("\"")){
            this.sourceUri = this.sourceUri.substring(1);
        }
        if(this.sourceUri.endsWith("\"")){
            this.sourceUri = this.sourceUri.substring(0, this.sourceUri.length() -1);
        }
    }

    public Map<String, ConnectionParameterDescriptor> getParameters() {
        return parameters;
    }

    public void addParameter(ConnectionParameterDescriptor parameter) {
         if(this.parameters.containsKey(parameter.getId()) || this.parameters.containsValue(parameter)) {  //the parameter already exists
            this.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                        .setMessageTemplate("There is a duplicate parameter named %s defined in connection %s.")
                        .setContextInfo1(parameter.getId())
                        .setContextInfo2(id)
                        .setLineNumber(parameter.getParserContext().getStart().getLine())
                        .setColumnNumber(parameter.getParserContext().getStop().getCharPositionInLine())
                        .build()
            );
        } else {
            parameter.setConnection(this);
            this.parameters.put(parameter.getId(), parameter);
        }
    }
    
    public ConnectionParameterDescriptor getParameterValue(String paramName, String defaultValue){
        if(parameters.containsKey(paramName))
            return parameters.get(paramName);
        else
            return ConnectionParameterDescriptor.createEmpty(defaultValue);
    }
}
