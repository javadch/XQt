/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.data;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

/**
 *
 * @author standard
 */
public class Resultset {
    private HashSet<SchemaItem> schema = new LinkedHashSet<>();
    private Object data;
    private List<Object> tabularData;
    private ResultsetType resultsetType = ResultsetType.Tabular;

    public Resultset(ResultsetType resultsetType){
        this.resultsetType = resultsetType;
    }
    
    public HashSet<SchemaItem> getSchema() {
        return schema;
    }

    public void setSchema(HashSet<SchemaItem> schema) {
        this.schema = schema;
    }

    public Object getData() {
        return data;
    }

    public List<Object> getTabularData() {
        if(resultsetType == ResultsetType.Tabular)
            return this.tabularData;
        return null;
    }

    public void setData(Object data) {
        switch(resultsetType){
            case Tabular:
            	@SuppressWarnings("unchecked")
            	List<Object> data2 = (List<Object>)data;
            	this.tabularData = data2;
                break;
            default:
                this.data = data;
        }
    }

    public ResultsetType getResultsetType() {
        return resultsetType;
    }
  
    public void setResultsetType(ResultsetType value){
        this.resultsetType = value;
    }    
}
