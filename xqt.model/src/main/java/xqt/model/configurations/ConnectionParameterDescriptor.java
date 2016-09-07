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

import java.text.MessageFormat;

import xqt.model.PhraseDescriptor;

/**
 *
 * @author Javad Chamanara
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
    
    @Override
    public String toString(){
    	return MessageFormat.format("Parameter {0}: {1}.", this.id, this.value);
    }
    public static ConnectionParameterDescriptor createEmpty(String defaultValue){
        ConnectionParameterDescriptor empty = new ConnectionParameterDescriptor();
        empty.setConnection(null);
        empty.setId("");
        empty.setValue(defaultValue);
        return empty;
    }
    
}
