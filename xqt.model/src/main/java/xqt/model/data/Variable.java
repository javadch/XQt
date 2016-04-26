/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.data;

import com.vaiona.commons.data.DataTypeInfo;
import com.vaiona.commons.types.TypeSystem;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

    public <T> T[] createColumn(Class<?> clazz, int size) {
        @SuppressWarnings("unchecked")
		T[] array = (T[]) java.lang.reflect.Array.newInstance(clazz, size);
        return (array);
    }
    
    public Object[] getPerspectiveAsArray(){
    	Object[/*columns*/] perspective = new Object[4];
    	int attributeCount = result.getSchema().size();
    	String[] names =  new String[attributeCount]; // attribute names
    	perspective[0] = names;
    	String[] types = new String[attributeCount]; // attribute types
    	perspective[1] = types;
    	String[] constraints = new String[attributeCount]; // attribute constraints
    	perspective[2] = constraints;
    	String[] annotations = new String[attributeCount]; // attribute annotations
    	perspective[3] = annotations;
    	
    	int attCounter = 0;
    	for(SchemaItem att: result.getSchema()){
    		names[attCounter] = att.getName();
    		types[attCounter] = att.getDataType();
    		constraints[attCounter] = "NA";
    		annotations[attCounter] = "NA";
    		attCounter++;
    	}
    	
    	return perspective;
    	
    }
    public Object[] getResultAsArray(){
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
            //colSize =1;
            Object[/*columns*/] table = new Object[colSize];
            Field[] fields = new Field[colSize];
            //HashMap<Integer, Object[]> columnPointer = new HashMap<>();
            
            for(int col =0; col<colSize; col++){ // store the fields in an array for faster pickup in the loops
                try {
                    Field fld = clazz.getField(columnNames.get(col));
                    fields[col] = fld;
                    //table[col] = createColumn(int.class, col);
                    Optional<DataTypeInfo> typeInfo = TypeSystem.getTypes().values().stream()
                            .filter(p->p.getRuntimeType().equals(fld.getType().getSimpleName())).findFirst();
                    if(typeInfo.isPresent()){
                        Class<?> fieldType = typeInfo.get().getPrimitiveType();
                        table[col] = java.lang.reflect.Array.newInstance(fieldType, rowSize);
                    } else {
                        throw new NoClassDefFoundError(fld.getType().getName());
                    }
                } catch(NoSuchFieldException | SecurityException ex){
                    
                }
            }
            
            for(int row=0; row<rowSize; row++){
                Object rowData = result.getTabularData().get(row);
                for(int col =0; col<colSize; col++){
                    try {
                        Object value = fields[col].get(rowData);
                        if(value.getClass().equals(Double.class)){
                            ((double [])table[col])[row] = (double)value;                                
                        } else if(value.getClass().equals(Long.class)){
                            ((long [])table[col])[row] = (long)value;                                
                        } else if(value.getClass().equals(Integer.class)){
                            ((int [])table[col])[row] = (int)value;                                
                        } else if(value.getClass().equals(Boolean.class)){
                            ((boolean [])table[col])[row] = (boolean)value;                                
                        } else if(value.getClass().equals(String.class)){
                            ((String [])table[col])[row] = (String)value;                                
                        } else if(value.getClass().equals(LocalDateTime.class)){
                                ((LocalDateTime [])table[col])[row] = (LocalDateTime)value;                                
                        }
                    } catch (IllegalArgumentException | IllegalAccessException ex) {
                        //columnPointer.get(col)[row] = "ERROR";
                    }
                }
            }
            return table;
        }     
        return null;
    }
}
