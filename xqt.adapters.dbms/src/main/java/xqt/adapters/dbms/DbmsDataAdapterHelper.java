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
public class DbmsDataAdapterHelper {
    
    private static final HashMap<String, List<String>> typeConversion = new HashMap<>();
    
    static {
        typeConversion.put(TypeSystem.TypeName.Boolean,  new ArrayList<String>() {{add("boolean");}});
        typeConversion.put(TypeSystem.TypeName.Byte,     new ArrayList<String>() {{add("byte");}});
        typeConversion.put(TypeSystem.TypeName.Date,     new ArrayList<String>() {{add("date");}});
        typeConversion.put(TypeSystem.TypeName.Integer,  new ArrayList<String>() {{add("integer");add("int");}});
        typeConversion.put(TypeSystem.TypeName.Long,     new ArrayList<String>() {{add("long");}});
        typeConversion.put(TypeSystem.TypeName.Real,     new ArrayList<String>() {{add("double");add("real");add("float");add("numeric");}});
        typeConversion.put(TypeSystem.TypeName.String,   new ArrayList<String>() {{add("string");add("char");}});        
    }
    
    public String getConceptualType(String physicalType){
        // get the DBMS dialect as parameter and try to call DBMS specific type convertors e.g., convertPostgreSqlTypeToConceptualType
        Optional<Map.Entry<String, List<String>>> entry = typeConversion.entrySet().stream()
                .filter(p-> p.getValue().contains(physicalType.toLowerCase())).findFirst();
        if(entry.isPresent()){
            return entry.get().getKey();
        }
        else
            return TypeSystem.TypeName.Unknown;
    }

    public String getPhysicalType(String conceptualType){
        if(typeConversion.containsKey(conceptualType)){
            return typeConversion.get(conceptualType).get(0); // returns first physical data type by default.
        }
            return TypeSystem.TypeName.Unknown;
    }

    /**
     *
     * @param container : must be a Single container
     * @return a map on the physical fields declared in the associated container/ table.
     */
    public LinkedHashMap<String, FieldInfo> prepareFields(SingleContainer container){
            try {
                LinkedHashMap<String, FieldInfo> fields = new LinkedHashMap<>();
                // take the fields from the DB schema, by executing the follwoing query for postgresql, ..
                // use regular expressions for field type detection, and use the adapater specific type conversion...
                
                // determine the Db dialect
                ConnectionParameterDescriptor p = container.getBinding().getConnection().getParameters().get("Provider");
                switch (p.getValue().toLowerCase()){
                    case "postgresql":
                        fields = getContinerSchemaFromPgS(container);
                        break;
                    default:
                        break;
                }
                //    
//                fields.values().stream().forEach(field -> {
//                    field.conceptualDataType = getConceptualType(field.internalDataType);
//                });
                return fields;
            } catch (Exception ex){}
            return null;
    }
    
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

    public String getConnectionString(SingleContainer container){
        ConnectionParameterDescriptor p = container.getBinding().getConnection().getParameters().get("Provider");
        switch (p.getValue().toLowerCase()){
            case "postgresql":
                return getConnectionStringForPgS(container);
            default:
                return "";
        }        
    }

    public String getContainerUsername(SingleContainer container){
        ConnectionParameterDescriptor p = container.getBinding().getConnection().getParameters().get("Provider");
        switch (p.getValue().toLowerCase()){
            case "postgresql":
                return container.getBinding().getConnection().getParameters().get("Username").getValue();
            default:
                return "";
        }        
    }

    String getContainerDbProviderName(SingleContainer container) {
        ConnectionParameterDescriptor p = container.getBinding().getConnection().getParameters().get("Provider");
        if(p == null || p.getValue() == null || p.getValue().equals("")){
            return "postgresql";
        }
        return p.getValue().toLowerCase();
    }

    public String getContainerPassword(SingleContainer container){
        ConnectionParameterDescriptor p = container.getBinding().getConnection().getParameters().get("Provider");
        switch (p.getValue().toLowerCase()){
            case "postgresql":
                return container.getBinding().getConnection().getParameters().get("Password").getValue();
            default:
                return "";
        }        
    }

    private LinkedHashMap<String, FieldInfo> getContinerSchemaFromPgS(SingleContainer container) {
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
            String connectionString = getConnectionStringForPgS(container);
            connection = DriverManager.getConnection(connectionString, getContainerUsername(container), getContainerPassword(container));
        } catch (SQLException e) {
                System.out.println("Connection Failed! Check output console");
                return fields;
        }
 
        if (connection != null) {
            try{
                try (Statement stmt = connection.createStatement()) {
                    //String sql = "SELECT attnum as fieldOrder, attname AS fieldName, format_type(atttypid, atttypmod) AS fieldType " +
                    String sql = "SELECT attnum as fieldOrder, attname AS fieldName, format_type(atttypid, NULL) AS fieldType " +
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
                        fieldInfo.conceptualDataType = convertPostgreSqlTypeToConceptualType(fieldType);;
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
                System.out.println("Failed to make connection!");
                return fields;
        }
    }

    private String getConnectionStringForPgS(SingleContainer container){
        String serverName = container.getBinding().getConnection().getParameters().get("Server").getValue();
        String port = container.getBinding().getConnection().getParameters().get("Port").getValue();
        String dbName = container.getBinding().getConnection().getParameters().get("DbName").getValue();
        String connectionString = MessageFormat.format("jdbc:postgresql://{0}:{1}/{2}", serverName, port, dbName);
        return connectionString;
    }
    
    private String convertPostgreSqlTypeToConceptualType(String dbmsType) {
        dbmsType = dbmsType.toLowerCase();
        if (dbmsType.startsWith("character") || dbmsType.startsWith("text") || dbmsType.startsWith("uuid")){
            return TypeSystem.TypeName.String;
        } else if(dbmsType.startsWith("bit")){ // by having the size as a parameter, it would be possible to have a better type selection
            return TypeSystem.TypeName.Integer;
        } else if(dbmsType.startsWith("bool")){
            return TypeSystem.TypeName.Boolean;
        } else if(dbmsType.startsWith("integer") || dbmsType.startsWith("serial") || dbmsType.startsWith("small") || dbmsType.startsWith("smallserial")){
            return TypeSystem.TypeName.Integer;
        } else if(dbmsType.startsWith("bigint") || dbmsType.startsWith("bigserial")){
            return TypeSystem.TypeName.Long;
        } else if(dbmsType.startsWith("double") || dbmsType.startsWith("real")){
            return TypeSystem.TypeName.Real;
        } else if(dbmsType.startsWith("date") || dbmsType.startsWith("time") || dbmsType.startsWith("timestamp")){
            return TypeSystem.TypeName.Date;
        } else {
            return TypeSystem.TypeName.Invalid;
        }
    }

}
