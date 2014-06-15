package xqt.model;

import java.util.HashMap;
import java.util.Map;
import org.antlr.v4.runtime.ParserRuleContext;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jfd
 */
public class ElementDescriptor extends BaseDescriptor {
    protected Map<String, Object> properties = new HashMap<>();
    protected String elementType;
    
    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(HashMap<String, Object> attributes) {
        this.properties = attributes;
    }

}
