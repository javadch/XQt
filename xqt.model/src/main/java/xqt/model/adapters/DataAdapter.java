/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.adapters;

import java.util.Map;
import xqt.model.data.Resultset;
import xqt.model.data.Variable;
import xqt.model.statements.query.SelectDescriptor;

/**
 *
 * @author standard
 */
public interface DataAdapter {
    boolean needsMemory();
    void setup(Map<String, Object> config);
    Resultset run(SelectDescriptor select, Object context);
    Resultset complement(SelectDescriptor select, Variable variable);

    void prepare(SelectDescriptor select, Object context);
    
    boolean isSupported(String capability);
    void registerCapability(String capabilityKey, boolean isSupported);

    boolean hasRequiredCapabilities(SelectDescriptor select);
    
    AdapterInfo getAdapterInfo();
    void setAdapterInfo(AdapterInfo value);

    String getDialect();
    void setDialect(String dialect);
}
