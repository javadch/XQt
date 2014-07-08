/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.adapters.builtin;

import java.util.Map;
import xqt.model.DataContainerDescriptor;
import xqt.model.adapters.DataAdapter;
import xqt.model.data.Resultset;
import xqt.model.data.ResultsetType;
import xqt.model.data.Variable;
import xqt.model.statements.query.PlotClause;
import xqt.model.statements.query.SelectDescriptor;

/**
 *
 * @author standard
 */
public class MemoryDataAdapter implements DataAdapter{

    @Override
    public void setup(Map<String, Object> config) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Resultset run(SelectDescriptor select, Object context) {
        Map<String, Variable> memory = (Map<String, Variable>)context;
        if(select.getSourceClause().getDataContainerType() == DataContainerDescriptor.DataContainerType.Variable){ // the data source must be a variable
            ////         READER AREA
            Variable sourceData = (Variable)memory.get(select.getSourceClause().getVariableName());
            // do something with the source data using the select definition
            Object result = sourceData.getResult().getTabularData(); // for testing purpose, it just returns the source
            //// END OF READER AREA
            
            //// MAPPER AREA: maps the result of the reading part, which is a collection, to the specified output type. it can be a plot or a collection with another row object.
            //// maybe they get mixed over time :-(
            if(result != null){
                switch (select.getTargetClause().getDataContainerType()){
                    case Plot:{
                        Resultset resultSet = new Resultset(ResultsetType.Image); 
                        resultSet.setData(result);
                        resultSet.setSchema(sourceData.getResult().getSchema());
                        //resultSet.setSchema(prepareSchema(select));
                        return resultSet;
                    }
                    case JoinedContainer:
                        break;
                    case Simplecontainer:
                        break;
                    case Variable:{
                        Resultset resultSet = new Resultset(ResultsetType.Tabular); 
                        resultSet.setData(result);
                        resultSet.setSchema(sourceData.getResult().getSchema());
                        //resultSet.setSchema(prepareSchema(select));
                        return resultSet;
                    }
                }
            }else {
                return null;
            }
            //// END OF MAPPER AREA
        }
        
        return null;
    }
    
}
