/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.engine;

import java.util.List;
import java.util.Set;
import xqt.model.ProcessModel;
import xqt.model.data.Variable;
import xqt.model.execution.ExecutionInfo;


/**
 *
 * @author Javad Chamanara
 */
public interface QueryEngine {

    ProcessModel getProcessModel();

    //checks whether the target processor is capable of performing the specified capability,
    // if not the default processor should take it over
    Boolean isCapableOf(String capabilityName);

    //checks whether the capability is valid in the target processor. E.g., anchor is not valid in SQL
    Boolean isValid(String capabilityName);

    //returns the value of specified variable. The variable should be introduced before this call, by
    // other statements like SELECT ... INTO ....
    Object getVariableValue(String variableName);

    // puts or updates a variable in the engine's memory
//    void setVariableValue(String variableName, Object data);
    
    Set<String> getVariableNames();
    
    List<Variable> getVariables();

    // deletes the specified variable and its data from the engine's memory
    void deleteVariable(String variableName);
    
    void addVariable(Variable variable);

//executes all the statements in the process.
    void execute();

    ExecutionInfo execute(Integer statementDescriptorId);

    void setClassLoader(ClassLoader classLoader);

    ClassLoader getClassLoader();
    
    String getConfigPaths();
    void setConfigPaths(String value);

}
