/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.data;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import xqt.model.execution.ExecutionInfo;

/**
 *
 * @author Javad Chamanara
 * @project SciQuest
 */
public class Variable {
    private String name;
    private Resultset result;
    private ExecutionInfo executionInfo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Resultset getResult() {
        return result;
    }

    public void setResult(Resultset result) {
        this.result = result;
    }

    public ExecutionInfo getExecutionInfo() {
        return executionInfo;
    }

    public void setExecutionInfo(ExecutionInfo executionInfo) {
        this.executionInfo = executionInfo;
    }

    public Object [][] getResultAsArray(){
        List<String> columnNames = result.getSchema().stream().map(p->p.getName()).collect(Collectors.toList());
        if (result.getTabularData()!= null && result.getTabularData().size() > 0) {
                        
            Class<?> clazz = null;
            if(executionInfo.getEntitySource()!= null){
                clazz = executionInfo.getEntitySource().getCompiledClass();
            }
            if (clazz == null){
                clazz = result.getTabularData().get(0).getClass();
            }
            if(clazz == null)
                return null;
            
            int rowSize = result.getTabularData().size();
            int colSize = columnNames.size();
            Object[/*columns*/][/*rows*/] table = new Object[rowSize][colSize];
            Field[] fields = new Field[colSize];
            for(int col =0; col<colSize; col++){ // store the fields in an array for faster pickup in the loops
                try {
                    fields[col] = clazz.getField(columnNames.get(col));
                } catch(NoSuchFieldException | SecurityException ex){
                    
                }
            }
            for(int row=0; row<rowSize; row++){
                Object rowData = result.getTabularData().get(row);
                for(int col =0; col<colSize; col++){
                    try {
                        table[row][col] = fields[col].get(rowData);
                    } catch (IllegalArgumentException | IllegalAccessException ex) {
                        table[col][row] = "ERROR";
                    }
                }
            }
            return table;
        }     
        return null;
    }
}
