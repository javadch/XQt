/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.adapters.dbms;

import com.vaiona.commons.data.FieldInfo;
import com.vaiona.commons.types.TypeSystem;
import java.util.LinkedHashMap;
import java.util.Map;
import xqt.model.adapters.BaseAdapterHelper;
import xqt.model.configurations.ConnectionParameterDescriptor;
import xqt.model.containers.SingleContainer;
import xqt.model.declarations.PerspectiveDescriptor;

/**
 *
 * @author Javad Chamanara <chamanara@gmail.com>
 */
public abstract class DbmsDataAdapterHelper extends BaseAdapterHelper{

    public abstract String getConnectionString(SingleContainer container);
    public abstract String getContainerUsername(SingleContainer container);
    public abstract String getContainerPassword(SingleContainer container);
    public abstract String assembleQuery(Map<String, Object> clauses);

    @Override
    public int getContainerDialectId(SingleContainer container) {
        DBMSDialect ret = getContainerDialectName(container);
        return ret.getValue();
    }
    
    public static DbmsDataAdapterHelper getConcreteHelper(SingleContainer container){
        switch (getContainerDialectName(container)){
             case PostgreSQL:
                 return new PgSQueryHelper();
             default:
                 return new PgSQueryHelper();
         }          
    }

    private static DBMSDialect getContainerDialectName(SingleContainer container) {
        ConnectionParameterDescriptor p = container.getBinding().getConnection().getParameters().get("dialect");
        if(p == null || p.getValue() == null || p.getValue().equals("")){
            return DBMSDialect.PostgreSQL;
        }
        return DBMSDialect.PostgreSQL;
    }
}
