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
        // grab this information from a config file that is used by other parts of the software too. DRY please.
        return "0.3.0"; 
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
