/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.adapters.builtin;

import com.vaiona.commons.data.FieldInfo;
import java.util.LinkedHashMap;
import xqt.model.adapters.BaseAdapterHelper;
import xqt.model.adapters.BaseDataAdapter;
import xqt.model.containers.SingleContainer;

/**
 *
 * @author Javad Chamanara <chamanara@gmail.com>
 */
public class DefaultDataAdapterHelper extends BaseAdapterHelper {

    @Override
    public String getConceptualType(String physicalType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getPhysicalType(String conceptualType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LinkedHashMap<String, FieldInfo> getContinerSchema(SingleContainer container, Object... params) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getContainerDialectId(SingleContainer container) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getEntityResourceName() {
        return "MemEntity";
    }

    @Override
    public String getJoinedEntityResourceName() {
        return "MemJoinedEntity";
    }

    @Override
    public String getRecordResourceName() {
        return "Entity";
    }
    
    @Override
    public String getAggregateReaderResourceName() {
        return "MemAggregateReader";
    }

    @Override
    public String getReaderResourceName() {
        return "MemReader";
    }

    @Override
    public String getJoinReaderResourceName() {
        return "MemJoinedEntity";
    }    
}
