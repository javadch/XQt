/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.adapters.dbms;

import com.vaiona.commons.data.FieldInfo;
import com.vaiona.commons.types.TypeSystem;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import xqt.adapters.dbms.postgresql.PgSGroupByFeatureTransformer;
import xqt.adapters.dbms.postgresql.PgSProjectionFeatureTransformer;
import xqt.model.containers.SingleContainer;
import xqt.model.transformation.QueryFeatureTransformer;

/**
 *
 * @author Javad Chamanara <chamanara@gmail.com>
 */
public class PgSQueryHelper extends DbmsDataAdapterHelper{
    
    @Override
    public LinkedHashMap<String, FieldInfo> getContinerSchema(SingleContainer container, Object... params) {
        LinkedHashMap<String, FieldInfo> fields = new LinkedHashMap<>();
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            //DriverManager.registerDriver(new org.postgresql.Driver());
        } catch (Exception e) {
            System.err.println(e.getClass().getName()+ ": " + e.getMessage()); // chenge to better exceptions
            return fields;
        }        
        try {
            String connectionString = getConnectionString(container);
            connection = DriverManager.getConnection(connectionString, getContainerUsername(container), getContainerPassword(container));
        } catch (SQLException e) {
                System.out.println("Connection Failed! Check output console");
                return fields;
        }
 
        // retrieves the schema from the database catalog
        if (connection != null) {
            try{
                try (Statement stmt = connection.createStatement()) {
                    //String sql = "SELECT attnum as fieldOrder, attname AS fieldName, format_type(atttypid, atttypmod) AS fieldType " +
                    String sql = "SELECT attnum - 1 as fieldOrder, attname AS fieldName, format_type(atttypid, NULL) AS fieldType " +
                            "FROM   pg_attribute " +
                            "WHERE  attrelid = '" + container.getContainerName() + "'::regclass " +
                            "AND    NOT attisdropped " +
                            "AND    attnum > 0 " +
                            "ORDER  BY attnum ";
                    
                    ResultSet rs = stmt.executeQuery(sql);
                    while ( rs.next() ) {
                        String fieldName = rs.getString("fieldName");
                        String fieldType =  rs.getString("fieldType");
                        //String fieldTypeConceptual =  convertPostgreSqlTypeToConceptualType(fieldType);
                        FieldInfo fieldInfo = new FieldInfo();
                        fieldInfo.name = fieldName;
                        fieldInfo.index = rs.getInt("fieldOrder");
                        fieldInfo.internalDataType = fieldType;
                        fieldInfo.conceptualDataType = getConceptualType(fieldType);;
                        fields.put(fieldName, fieldInfo);
                    }
                }
                connection.close();      
                return fields;
            } catch (SQLException ex){
                System.out.println("Failed to obtain table schema! " + ex.getMessage());
                return fields;
            }
        } else {
                System.out.println("Failed to make a connection!");
                return fields;
        }
    }
 
    @Override
    public String getConnectionString(SingleContainer container){
        String serverName = container.getBinding().getConnection().getParameterValue("server", "localhost").getValue();
        String port = container.getBinding().getConnection().getParameterValue("port", "5432").getValue();
        String dbName = container.getBinding().getConnection().getParameterValue("dbname", "").getValue();
        String connectionString = MessageFormat.format("jdbc:postgresql://{0}:{1}/{2}", serverName, port, dbName);
        return connectionString;
    }

    @Override
    public String getConceptualType(String physicalType) {
        physicalType = physicalType.toLowerCase();
        if (physicalType.startsWith("character") || physicalType.startsWith("text") || physicalType.startsWith("uuid")){
            return TypeSystem.TypeName.String;
        } else if(physicalType.startsWith("bit")){ // by having the size as a parameter, it would be possible to have a better type selection
            return TypeSystem.TypeName.Integer;
        } else if(physicalType.startsWith("bool")){
            return TypeSystem.TypeName.Boolean;
        } else if(physicalType.startsWith("integer") || physicalType.startsWith("serial") || physicalType.startsWith("small") || physicalType.startsWith("smallserial")){
            return TypeSystem.TypeName.Integer;
        } else if(physicalType.startsWith("bigint") || physicalType.startsWith("bigserial")){
            return TypeSystem.TypeName.Long;
        } else if(physicalType.startsWith("double") || physicalType.startsWith("real")){
            return TypeSystem.TypeName.Real;
        } else if(physicalType.startsWith("date") || physicalType.startsWith("time") || physicalType.startsWith("timestamp")){
            return TypeSystem.TypeName.Date;
        } else {
            return TypeSystem.TypeName.Invalid;
        }
    }
    
    @Override
    public String getContainerUsername(SingleContainer container){
        try{
            return container.getBinding().getConnection().getParameterValue("username", "").getValue();
        } catch (Exception ex){
            return "";
        }
    }
    
    @Override
    public String getContainerPassword(SingleContainer container){
        try{
            return container.getBinding().getConnection().getParameterValue("password", "").getValue();
        } catch (Exception ex){
            return "";
        }
    }
    
    @Override
    public String getPhysicalType(String conceptualType){
        // should do the reverse of the getConceptualType
        return TypeSystem.TypeName.Unknown;
    }
    
    @Override
    public String assembleQuery(Map<String, Object> queryFeatures){// its called from the builder!
        // generate the projection clause -> ((temp_lo+temp_hi)/2) as Temperature, xyz as xyz, beware of functions SUBSTRING(x, 0, 10) as m,
        QueryFeatureTransformer projection = new PgSProjectionFeatureTransformer();
        String projectionStr = projection.transform(queryFeatures.get("Attributes"), queryFeatures);
        
        // generate the source clause
        String sourceStr = queryFeatures.get("ContainerName").toString(); // should be changed
        // generate the filter clause
        String selectionStr = "TRUE";
        // generate the group by clause
        QueryFeatureTransformer groupBy = new PgSGroupByFeatureTransformer();
        String groupByStr = groupBy.transform(queryFeatures.get("Attributes"), queryFeatures);
        // generate the ordering clause
        // generate the offesting clause
        
        //The query pattern may go upper to be reused by other dialects.
        String query = MessageFormat.format("SELECT {0} FROM {1} WHERE ({2})", projectionStr, sourceStr, selectionStr);//
        return query;
    }
    
    
}
