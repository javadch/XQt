/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import xqt.model.configurations.ConfigurationDescriptor;
import xqt.model.declarations.DeclarationDescriptor;
import xqt.model.exceptions.LanguageException;
import xqt.model.exceptions.LanguageExceptionBuilder;
import xqt.model.statements.StatementDescriptor;

/**
 *
 * @author jfd
 */
public class ProcessModel {
    //private List<StatementDescriptor> statements = new ArrayList<>();
    private Map<Integer, StatementDescriptor> statements = new LinkedHashMap<>();
    private Map<String, DeclarationDescriptor> declarations = new HashMap<>();
    private Map<String, ConfigurationDescriptor> configurations = new HashMap<>();

    public ProcessModel (){
		super();
    }

    public void addStatementDescriptor(StatementDescriptor statement){
            statements.put(statement.getOrderInParent(), statement);
    }

    public Map<Integer, StatementDescriptor> getStatements(){
            return(statements);
    }

    public StatementDescriptor getStatement(Integer statementId){
        // satatement id is the id aasigned to the statement during the annotation/ parsin, not its index in the list
        return statements.get(statementId);
    }
    public void addDeclaration(DeclarationDescriptor declaration) throws LanguageException {
        //check whether all types of declarations should have unique ids or each type of them ...
        if(this.declarations.containsKey(declaration.getId()) || this.declarations.containsValue(declaration))
            throw LanguageExceptionBuilder.builder()
                    .setMessageTemplate("Duplicate perspective declaration encountered. Persective id: %s")
                    .setContextInfo1(declaration.getId())
                    .setLineNumber(declaration.getParserContext().getStart().getLine())
                    .setColumnNumber(declaration.getParserContext().getStop().getCharPositionInLine())
                    .build()
                    ;
        this.declarations.put(declaration.getId(), declaration);
    }

    public Map<String, DeclarationDescriptor> getDeclarations() {
        return declarations;
    }
    
    public void addConfiguration(ConfigurationDescriptor configuration) throws Exception {
        //check whether all types of configurations should have unique ids or each type of them ...
        if(this.configurations.containsKey(configuration.getId()) 
                || this.configurations.containsValue(configuration))
            throw new Exception(String.format("Duplicate configuration declaration encountered. id: %s", configuration.getId()));
        this.configurations.put(configuration.getId(), configuration);
    }

    public Map<String, ConfigurationDescriptor> getConfigurations() {
        return configurations;
    }
    
    public Integer totalElementCount(){
        return declarations.size() + configurations.size() + statements.size(); // + mConfigurations
    }
    
}
