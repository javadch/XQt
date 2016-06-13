/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.adapters.dbms;

import com.vaiona.commons.data.AttributeInfo;
import com.vaiona.commons.data.FieldInfo;
import java.util.List;
import java.util.Map;
import xqt.model.adapters.BaseAdapterHelper;
import xqt.model.configurations.ConnectionParameterDescriptor;
import xqt.model.containers.SingleContainer;
import java.sql.ResultSet;

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
    
    // override in concrete DBMS dialects if needed
    @Override
    public String getEntityResourceName() {
        return "DbmsEntity";
    }

    @Override
    public String getJoinedEntityResourceName() {
        return "DbmsEntity";
    }

    @Override
    public String getRecordResourceName() {
        return "DbmsEntity";
    }

    @Override
    public String getAggregateReaderResourceName() {
        return "DbmsReader";
    }

    @Override
    public String getReaderResourceName() {
        return "DbmsReader";
    }

    @Override
    public String getJoinReaderResourceName() {
        return "DbmsJoinReader";
    }    
    
    
    public static DbmsDataAdapterHelper getConcreteHelper(SingleContainer container){
        switch (getContainerDialectName(container)){
             case PostgreSQL:
                 return new PgSQueryHelper();
             default:
                 return new PgSQueryHelper();
         }          
    }

    // add a collection of column indexes or names to make the function process a projection of needed columns only
    public static String[] createRowArray(ResultSet row, List<AttributeInfo> attributes){
        String[] cellValues = new String[attributes.size()];
        for(int cellIndex =0;  cellIndex < attributes.size(); cellIndex++){
            AttributeInfo at = attributes.get(cellIndex);
            try {
                cellValues[cellIndex] = row.getString(at.name);
            } catch (Exception ex){
                cellValues[cellIndex] = "";
            }
            
//            switch (field.conceptualDataType.toUpperCase())
//            {
//                // what about the DATE type
//                case "BOOLEAN":
//                    cellValues[cellIndex] = row.getBoolean(field.name);
//                    break;
//                case Cell.CELL_TYPE_STRING:
//                    //System.out.print(cellValue.getStringValue()  + "\t");
//                    cellValues[cellIndex] = cellValue.getStringValue();
//                    break;
//                case Cell.CELL_TYPE_BOOLEAN:
//                    //System.out.println(cellValue.getBooleanValue()  + "\t");
//                    cellValues[cellIndex] = String.valueOf(cellValue.getBooleanValue());
//                    break;
//                case Cell.CELL_TYPE_FORMULA: // should not happen. It is evaluated by the evaluator
//                case Cell.CELL_TYPE_BLANK:
//                case Cell.CELL_TYPE_ERROR:
//                    cellValues[cellIndex] = "";
//                    break;
//            } 
        }                    
     return cellValues;   
    }        
    
    private static DBMSDialect getContainerDialectName(SingleContainer container) {
        ConnectionParameterDescriptor p = container.getBinding().getConnection().getParameterValue("dialect", "");
        if(p == null || p.getValue() == null || p.getValue().equals("")){
            return DBMSDialect.PostgreSQL;
        }
        return DBMSDialect.PostgreSQL;
    }
}
