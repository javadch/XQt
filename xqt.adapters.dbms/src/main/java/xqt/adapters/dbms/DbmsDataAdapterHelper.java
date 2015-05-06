/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.adapters.dbms;

import com.vaiona.commons.data.FieldInfo;
import com.vaiona.commons.types.TypeSystem;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import xqt.model.configurations.ConnectionParameterDescriptor;
import xqt.model.containers.SingleContainer;
import xqt.model.declarations.PerspectiveDescriptor;

/**
 *
 * @author Javad Chamanara <chamanara@gmail.com>
 */
public abstract class DbmsDataAdapterHelper {

    public abstract String getConceptualType(String physicalType);
    public abstract String getConnectionString(SingleContainer container);
    public abstract LinkedHashMap<String, FieldInfo> getContinerSchema(SingleContainer container);
    public abstract String getContainerUsername(SingleContainer container);
    public abstract String getContainerPassword(SingleContainer container);
    public abstract String getPhysicalType(String conceptualType);
    public abstract String assembleQuery(Map<String, Object> clauses);

    public PerspectiveDescriptor createPhysicalPerspective(Map<String, FieldInfo> fields, PerspectiveDescriptor perspective, String id) {
        for (Map.Entry<String, FieldInfo> entrySet : fields.entrySet()) {
            FieldInfo field = entrySet.getValue();
            if(field.conceptualDataType.equalsIgnoreCase(TypeSystem.TypeName.Unknown)){
                field.conceptualDataType = getConceptualType(field.internalDataType);
            }
        }
        perspective = new PerspectiveDescriptor(fields, id);
        return perspective;
    }
    
    // looks for simple members in the expressions and checks whether they refer to a physical field,
    // if yes, the data type of the the field is set for the members
    public PerspectiveDescriptor improvePerspective(Map<String, FieldInfo> fields, PerspectiveDescriptor perspective) {
        if(perspective == null){
            return null;
        }
        for (Map.Entry<String, FieldInfo> entrySet : fields.entrySet()) {
            FieldInfo field = entrySet.getValue();
            field.conceptualDataType = getConceptualType(field.internalDataType);
        }
        return perspective.improve(fields);
    }    

    public static DBMSDialect getContainerDbDialectName(SingleContainer container) {
        ConnectionParameterDescriptor p = container.getBinding().getConnection().getParameters().get("dialect");
        if(p == null || p.getValue() == null || p.getValue().equals("")){
            return DBMSDialect.PostgreSQL;
        }
        return DBMSDialect.PostgreSQL;
    }
    
    public static DbmsDataAdapterHelper getQueryHelper(SingleContainer container){
        switch (getContainerDbDialectName(container)){
             case PostgreSQL:
                 return new PgSQueryHelper();
             default:
                 return new PgSQueryHelper();
         }          
    }
}
