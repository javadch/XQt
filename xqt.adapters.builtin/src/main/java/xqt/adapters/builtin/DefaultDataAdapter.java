/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.adapters.builtin;

import com.vaiona.commons.compilation.InMemorySourceFile;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
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
  
    private DataReaderBuilder builder = null;
    
    @Override
    public Resultset run(SelectDescriptor select, Object context) {
        Map<String, Variable> memory = (Map<String, Variable>)context;
        if(select.getSourceClause().getDataContainerType() == DataContainerDescriptor.DataContainerType.Variable){ // the data source must be a variable
            ////         READER AREA

            // check whether the data is tabular!
            Variable sourceVariable = (Variable)memory.get(select.getSourceClause().getVariableName());
            // do something with the source data using the select definition
            Resultset resultSet = internalRun(select, sourceVariable);
            return resultSet;
        }
        return null;
    }

    @Override
    public Resultset compensate(SelectDescriptor select, Variable variable){
        if(select.getSourceClause().getDataContainerType() == DataContainerDescriptor.DataContainerType.Variable){ // the data source must be a variable
            Resultset resultSet = internalRun(select, variable);
            return resultSet;
        }
        return null;
    }
    
    @Override
    public boolean needsMemory() {
        return true;
    }

    @Override
    public void prepare(SelectDescriptor select) {
        builder = new DataReaderBuilder();
        switch (select.getSourceClause().getDataContainerType()){
            case Plot:{
                break;
            }
            case JoinedContainer:
                break;
            case SimpleContainer:
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
            } catch (Exception ex) {
                // return a language exception
                Logger.getLogger(DefaultDataAdapter.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
        }       
    }    
    
    private HashMap<String, Boolean> capabilities = new HashMap<>();
    
    @Override
    public boolean isSupported(String capability) {
        if(capabilities.containsKey(capability) && capabilities.get(capability) == true)
            return true;
        return false;
    }

    @Override
    public void registerCapability(String capabilityKey, boolean isSupported) {
        capabilities.put(capabilityKey, isSupported);
    }    

    @Override
    public void setup(Map<String, Object> config) {
        registerCapability("select.qualifier", false);
        registerCapability("function", true);
        registerCapability("function.default.Max", true);
        registerCapability("expression", true);
        registerCapability("select.source.simple", true);
        registerCapability("select.source.join", false);
        registerCapability("select.target.persist", false);
        registerCapability("select.target.plot", true);
        //registerCapability("select.target.memory", true);
        registerCapability("select.anchor", false);
        registerCapability("select.filter", true);
        registerCapability("select.orderby", true);
        registerCapability("select.groupby", true);
        registerCapability("select.limit", true);
    }
    
    @Override
    public boolean hasRequiredCapabilities(SelectDescriptor select) {
        boolean allmatched = select.getRequiredCapabilities().stream().allMatch(p-> this.isSupported(p));
        return allmatched;
    }

    private Resultset internalRun(SelectDescriptor select, Variable sourceVariable) {
        try{
            // check whether the data is tabular!
            // do something with the source data using the select definition
            List<Object> source = (List<Object>)sourceVariable.getResult().getTabularData(); // for testing purpose, it just returns the source
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
                case SimpleContainer:
                    break;
                case Variable:{
                    Resultset resultSet = new Resultset(ResultsetType.Tabular); 
                    if(source == null || source.stream().count() <= 0){                    
                        resultSet.setData(null);
                        resultSet.setSchema(sourceVariable.getResult().getSchema());
                    } else {
                        Class entryPoint = select.getExecutionInfo().getSources().values().stream()
                            .filter(p-> p.isEntryPoint() == true).findFirst().get().getCompiledClass();
                        DataReader reader = builder.build(entryPoint);
                        List<Object> result = reader.read(source);
                        resultSet.setData(result);
                        resultSet.setSchema(sourceVariable.getResult().getSchema());                               
                    }
                    //resultSet.setSchema(prepareSchema(select));
                    return resultSet;
                }
                //// END OF MAPPER AREA
            }
        } catch (IOException | IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException ex) {
            
        }       
        return null;    
    }
}
