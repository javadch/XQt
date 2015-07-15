/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.adapters.csv;

/**
 *
 * @author Javad Chamanara <chamanara@gmail.com>
 */
public enum DataContainerDialect {
    CSV(1),
    MSExcel(2),
    OpenOffice(4);
    private final int value;
    private DataContainerDialect(int value){
        this.value = value;
    }
    
    public int getValue(){
        return value;
    }
    
    public static DataContainerDialect getEnum(int index){
        return DataContainerDialect.values()[index];
    }    
}
