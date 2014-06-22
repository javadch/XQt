package xqt.model.configurations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.antlr.v4.runtime.ParserRuleContext;
import xqt.model.exceptions.LanguageException;
import xqt.model.exceptions.LanguageExceptionBuilder;


/**
 *
 * @author jfd
 */
public class BindingDescriptor extends ConfigurationDescriptor{
   
    private ConnectionDescriptor connection;
    private String versionSelector;
    //private Map<String, BindingScopeDescriptor> scopes = new LinkedHashMap<String, BindingScopeDescriptor>();
    private ArrayList<String> scopes = new ArrayList<>();

    public ConnectionDescriptor getConnection() {
        return connection;
    }

    public void setConnection(ConnectionDescriptor connection) {
        this.connection = connection;
    }

    public String getVersionSelector() {
        return versionSelector;
    }

    public void setVersionSelector(String versionSelector) {
        this.versionSelector = versionSelector;
    }

    public ArrayList<String> getScopes() {
        return scopes;
    }

    public void addScope(String scope, ParserRuleContext context) {
        if(this.scopes.contains(scope)) {  //the scope already exists
            this.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate("There is a duplicate scope named %s defined in perspective %s.")
                    .setContextInfo1(scope)
                    .setContextInfo2(id)
                    .setLineNumber(context.getStart().getLine())
                    .setColumnNumber(context.getStop().getCharPositionInLine())
                    .build()
            );
        } else {
            this.scopes.add(scope);
        }
    }
}
