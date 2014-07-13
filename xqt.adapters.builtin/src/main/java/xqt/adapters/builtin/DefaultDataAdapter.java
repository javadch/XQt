/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.adapters.builtin;

import com.vaiona.commons.compilation.InMemorySourceFile;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
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
public class DefaultDataAdapter implements DataAdapter{
  
    private DataReaderBuilder builder = new DataReaderBuilder();
    @Override
    public void setup(Map<String, Object> config) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Resultset run(SelectDescriptor select, Object context) {
        Map<String, Variable> memory = (Map<String, Variable>)context;
        try{
            if(select.getSourceClause().getDataContainerType() == DataContainerDescriptor.DataContainerType.Variable){ // the data source must be a variable
                ////         READER AREA

                // check whether the data is tabular!
                Variable sourceData = (Variable)memory.get(select.getSourceClause().getVariableName());
                // do something with the source data using the select definition

                List<Object> source = (List<Object>)sourceData.getResult().getTabularData(); // for testing purpose, it just returns the source
                //Stream<Object> stream = source.stream();

                //// END OF READER AREA

                //// MAPPER AREA: maps the result of the reading part, which is a collection, to the specified output type. it can be a plot or a collection with another row object.
                //// maybe they get mixed over time :-(
                switch (select.getTargetClause().getDataContainerType()){
                    case Plot:{
                        Resultset resultSet = new Resultset(ResultsetType.Image); 
//                        resultSet.setData(result);
//                        resultSet.setSchema(sourceData.getResult().getSchema());
//                        //resultSet.setSchema(prepareSchema(select));
                        return resultSet;
                    }
                    case JoinedContainer:
                        break;
                    case Simplecontainer:
                        break;
                    case Variable:{
                        Resultset resultSet = new Resultset(ResultsetType.Tabular); 
                        if(source == null || source.stream().count() <=0){                    
                            resultSet.setData(null);
                            resultSet.setSchema(sourceData.getResult().getSchema());
                        } else {
                            Class entryPoint = select.getExecutionInfo().getSources().values().stream()
                                .filter(p-> p.isEntryPoint() == true).findFirst().get().getCompiledClass();
                            DataReader reader = builder.build(entryPoint);
                            List<Object> result = reader.read(source);
                            resultSet.setData(result);
                            resultSet.setSchema(sourceData.getResult().getSchema());                               
                        }
                        //resultSet.setSchema(prepareSchema(select));
                        return resultSet;
                    }
                }
                //// END OF MAPPER AREA
        }
        } catch (IOException | IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException ex) {
            
        }       
        return null;
    }

    @Override
    public boolean needsMemory() {
        return true;
    }

    @Override
    public void prepare(SelectDescriptor select) {
        switch (select.getTargetClause().getDataContainerType()){
            case Plot:{
                break;
            }
            case JoinedContainer:
                break;
            case Simplecontainer:
                break;
            case Variable:{
            try {
                // the statement should depend on another, because the source is a variable!
                SelectDescriptor master = (SelectDescriptor)select.getDependsUpon();
                String sourceRowType = master.getExecutionInfo().getSources().values().stream()
                        .filter(p->p.getFullName().endsWith("Entity")).findFirst().get().getFullName();
                      
                LinkedHashMap<String, InMemorySourceFile> rs = builder.createSources(select, sourceRowType);
                select.getExecutionInfo().setSources(rs);
                
                //DataReader reader = createReader(select, sourceRowType);
                //List<Object> result = reader.read(source);
                //resultSet.setData(result);
                //resultSet.setSchema(sourceData.getResult().getSchema());
            } catch (IOException ex) {
                // return a language exception
                Logger.getLogger(DefaultDataAdapter.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
        }       
    }    
}
