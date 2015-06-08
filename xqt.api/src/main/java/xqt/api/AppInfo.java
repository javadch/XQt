/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.api;

import xqt.model.adapters.AdapterInfoContainer;

/**
 *
 * @author jfd
 */
public class AppInfo {
    public static String getVersion(){
        return "0.2.0-SNAPSHOT"; 
    }
    
    public static String getMaturityLevel(){
        return "Development";
    }
    
    public static String getName(){
        return "XQt Language";
    }
    
    public static String getFullName(){
        return (getName() + " Version " + getVersion() + " (" + getMaturityLevel() + ")");
    }    
}
