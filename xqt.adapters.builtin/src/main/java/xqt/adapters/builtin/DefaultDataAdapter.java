/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.adapters.builtin;

import com.jidesoft.chart.Chart;
import com.jidesoft.chart.annotation.AutoPositionedLabel;
import com.jidesoft.chart.axis.Axis;
import com.jidesoft.chart.axis.CategoryAxis;
import com.jidesoft.chart.axis.NumericAxis;
import com.jidesoft.chart.model.DefaultChartModel;
import com.jidesoft.chart.model.TableToChartAdapter;
import com.jidesoft.chart.style.ChartStyle;
import com.jidesoft.grid.BeanTableModel;
import com.jidesoft.grid.SortableTable;
import com.jidesoft.range.CategoryRange;
import com.jidesoft.range.NumericRange;
import com.jidesoft.range.Range;
import com.vaiona.commons.compilation.InMemorySourceFile;
import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
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
        registerCapability("select.projection.perspective", true);
        registerCapability("select.projection.perspective.implicit", true);
        registerCapability("select.projection.perspective.explicit", true);
        registerCapability("select.projection.perspective.inline", true);
        registerCapability("select.source.simple", true);
        registerCapability("select.source.join", false);
        registerCapability("select.target.persist", false);
        registerCapability("select.target.plot", true);
        registerCapability("select.source.variable", true);
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
                    // create a reader, much like the Variable case.
                    // get data from the reader
                    // draw data into a plot. drawing here makes the template easier!
                    // return the plot
                    PlotClause plotModel = (PlotClause)select.getTargetClause();
                    if(source == null || source.stream().count() <= 0){                    
                        resultSet.setData(null);
                        resultSet.setSchema(sourceVariable.getResult().getSchema());
                    } else {
                        Class entryPoint = select.getExecutionInfo().getSources().values().stream()
                            .filter(p-> p.isEntryPoint() == true).findFirst().get().getCompiledClass();
                        DataReader reader = builder.build(entryPoint);
                        List<Object> result = reader.read(source);
                        resultSet.setSchema(sourceVariable.getResult().getSchema()); 
                        // use the plot clause (model) in order to build the chart's data model
                        DefaultChartModel modelA = new DefaultChartModel("ModelA"); 
                        //DefaultTableModel b = new DefaultTableModel();   
                        // investigate using a table model and a TableToChartAdapter object ...
                        Object [][] data = null;
                        List<Field> axes = new ArrayList<>();
                        if (result != null && result.size() > 0) {
                            Class<?> clazz = result.get(0).getClass();
                            //Field x = null;
                            //Field y = null;
                            
                            try {
                                 //x = clazz.getField(plotModel.getHax());
                                 //y = clazz.getField(plotModel.getVaxes().get(0));
                                 axes.add(clazz.getField(plotModel.getHax()));
                                 for(String yAx: plotModel.getVaxes()) {
                                     axes.add(clazz.getField(yAx));
                                 };                                 
                            } catch (NoSuchFieldException | SecurityException ex) {
                                Logger.getLogger(DefaultDataAdapter.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            data = new Object[(int)result.stream().count()][axes.size()];
                            int rowCounter = 0;
                            for(Object row: result) {  
                                int columnCounter = 0;
                                for(Field axField: axes){
                                    //Object xValue = x.get(row); // convert the values to proper types and also choose proper point type
                                    //Object yValue = y.get(row);  
                                    Object cellValue = axField.get(row);
                                    data[rowCounter][columnCounter++] = cellValue;
                                    //modelA.addPoint((double)xValue, (double)yValue); 
                                }
                                rowCounter++;
                            }
                        }
                        //modelA.addPoint(102, 135);
                        //modelA.addPoint(170, 200);
                        TableModel tableModel = new DefaultTableModel(data, axes.stream().map(p->p.getName()).collect(Collectors.toList()).toArray());
                        SortableTable table = new SortableTable(tableModel);
                        
                        Axis xAxis = new NumericAxis(new AutoPositionedLabel(plotModel.gethLabel()));
                        xAxis.setRange(0, 400);
                        Axis yAxis = new NumericAxis(new AutoPositionedLabel(plotModel.getvLabel()));
                        yAxis.setRange(0, 200);
                        
                        Chart chart = new Chart(); 
                        chart.setXAxis(xAxis);
                        chart.setYAxis(yAxis);

                        
                        List<TableToChartAdapter> adapters = new ArrayList<>();
                        int adapterCounter = 0;
                        List<Color> colorPallet = getDrawingColorPallet((int)axes.stream().count()-1);
                        for(Field ax: axes.stream().skip(1).collect(Collectors.toList())){ // the first filed is the X variable
                            TableToChartAdapter adapter = new TableToChartAdapter(ax.getName() + "Series", table.getModel());
                            ChartStyle style = new ChartStyle(colorPallet.get(adapterCounter++), false, true); 
                            adapter.setXColumn(0);
                            adapter.setYColumn(adapterCounter);  // the first y column starts from 1, which is incremented at the color setting line                          
                            adapters.add(adapter);
                            chart.addModel(adapter, style); // and the style
                        }
                        updateXRange(adapters, plotModel.gethLabel(), chart);
                        updateYRange(adapters, plotModel.getvLabel(), chart);
    
                        //ChartStyle styleA = new ChartStyle(Color.blue, false, true); 
                        //chart.addModel(modelA, styleA); 
                        resultSet.setData(chart);
                    }
                    
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

    @SuppressWarnings("unchecked")
    private void updateXRange(List<TableToChartAdapter> adapters, String hLabel, Chart chart) {
        // check the axis variable type and based on the type decise on the Axis type: Category, Numeric, etc.
        Range<?> xRange = (Range) adapters.get(0).getXRange();
        Axis xAxis = new NumericAxis(xRange, hLabel);
        chart.setXAxis(xAxis);
    }

    @SuppressWarnings("unchecked")
    private void updateYRange(List<TableToChartAdapter> adapters, String vLabel, Chart chart) {
        NumericRange nRange = null;
        for(TableToChartAdapter adapter: adapters){
            Range<?> yRange = adapter.getYRange();
            nRange = NumericRange.union((NumericRange) nRange, (NumericRange) yRange);
        }        
        Axis yAxis = null;// chart.getYAxis();
        if (nRange.getMin() == nRange.getMax()) {
            // Deal with the special case of only one point
            yAxis = new NumericAxis(nRange.getMin(), nRange.getMax() + 1, vLabel);
        }
        else {
            yAxis = new NumericAxis(nRange, vLabel);
        }
        chart.setYAxis(yAxis);
    }

    private List<Color> getDrawingColorPallet(int palletSize) {
        List<Color> pallet = new ArrayList<>(palletSize);
        for (int i = 0; i < palletSize; i++)
            pallet.add(Color.getHSBColor((float) i / palletSize, 1, 1));    
        return pallet;
    }
}
