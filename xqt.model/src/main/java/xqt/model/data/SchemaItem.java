/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.data;

/**
 *
 * @author standard
 */
public class SchemaItem {
    public static final String UNKOWN_TYPE = "String";
    public static final String UNKOWN_UNIT = "Unknown";
    public String systemType = UNKOWN_UNIT; // unit of measurement
    
    public String name = "";
    public String dataType = UNKOWN_TYPE;

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String unit) {
        this.systemType = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    public int index = 0;
}
