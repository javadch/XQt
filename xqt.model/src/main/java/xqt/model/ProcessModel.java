/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.model;

import java.util.ArrayList;
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
        protected List<LanguageException> languageExceptions = new ArrayList<>();

    public List<LanguageException> getLanguageExceptions() {
        return languageExceptions;
    }

    public void setLanguageExceptions(List<LanguageException> languageExceptions) {
        this.languageExceptions = languageExceptions;
    }

    public ProcessModel (){
		super();
    }

    public boolean hasError(){
        // go through the languageExceptions and also ask all the elements: declarations, statements, configurations etc
        if(this.languageExceptions.stream().count() > 0)
            return true;
        if (declarations.values().stream().anyMatch(p -> p.hasError())) {
            return true;
        }
        if (configurations.values().stream().anyMatch(p -> p.hasError())) {
            return true;
        }
        return statements.values().stream().anyMatch(p -> p.hasError());
    }
    
    public List<LanguageException> getEffectiveErrors(){
        List<LanguageException> errors = new ArrayList<>();
        errors.addAll(this.languageExceptions);
        declarations.values().stream().forEach(p-> errors.addAll(p.getLanguageExceptions()));
        configurations.values().stream().forEach(p-> errors.addAll(p.getLanguageExceptions()));
        statements.values().stream().forEach(p-> errors.addAll(p.getLanguageExceptions()));
        return errors;
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
    public void addDeclaration(DeclarationDescriptor declaration) {
        //check whether all types of declarations should have unique ids or each type of them ...
        if(this.declarations.containsKey(declaration.getId()) || this.declarations.containsValue(declaration))
            this.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate("Duplicate perspective declaration encountered. Persective id: %s")
                    .setContextInfo1(declaration.getId())
                    .setLineNumber(declaration.getParserContext().getStart().getLine())
                    .setColumnNumber(declaration.getParserContext().getStop().getCharPositionInLine())
                    .build()
            );
        this.declarations.put(declaration.getId(), declaration);
    }

    public Map<String, DeclarationDescriptor> getDeclarations() {
        return declarations;
    }
    
    public void addConfiguration(ConfigurationDescriptor configuration) {
        //check whether all types of configurations should have unique ids or each type of them ...
        if(this.configurations.containsKey(configuration.getId()) 
                || this.configurations.containsValue(configuration))
            this.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate("Duplicate configuration declaration encountered. id: %s")
                    .setContextInfo1(configuration.getId())
                    .setLineNumber(configuration.getParserContext().getStart().getLine())
                    .setColumnNumber(configuration.getParserContext().getStop().getCharPositionInLine())
                    .build()
            );
        this.configurations.put(configuration.getId(), configuration);
    }

    public Map<String, ConfigurationDescriptor> getConfigurations() {
        return configurations;
    }
    
    public Integer totalElementCount(){
        return declarations.size() + configurations.size() + statements.size(); // + mConfigurations
    }    
}
