/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.model.configurations;

import xqt.model.PhraseDescriptor;
import xqt.model.expressions.Expression;

/**
 *
 * @author jfd
 */
public class ConnectionParameterDescriptor extends PhraseDescriptor {
    
    private ConnectionDescriptor connection;
    private String value;

    public ConnectionDescriptor getConnection() {
        return connection;
    }

    public void setConnection(ConnectionDescriptor connection) {
        this.connection = connection;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    public static ConnectionParameterDescriptor createEmpty(String defaultValue){
        ConnectionParameterDescriptor empty = new ConnectionParameterDescriptor();
        empty.setConnection(null);
        empty.setId("");
        empty.setValue(defaultValue);
        return empty;
    }
    
}
