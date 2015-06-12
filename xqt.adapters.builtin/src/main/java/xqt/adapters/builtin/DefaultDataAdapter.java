/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.adapters.builtin;

import com.jidesoft.chart.Chart;
import com.jidesoft.chart.annotation.AutoPositionedLabel;
import com.jidesoft.chart.axis.Axis;
import com.jidesoft.chart.axis.NumericAxis;
import com.jidesoft.chart.model.TableToChartAdapter;
import com.jidesoft.chart.style.ChartStyle;
import com.jidesoft.grid.SortableTable;
import com.jidesoft.range.NumericRange;
import com.jidesoft.range.Range;
import com.vaiona.commons.data.AttributeInfo;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import xqt.model.adapters.AdapterInfo;
import xqt.model.adapters.DataAdapter;
import xqt.model.containers.DataContainer;
import xqt.model.containers.JoinedContainer;
import xqt.model.containers.PlotContainer;
import xqt.model.containers.VariableContainer;
import xqt.model.conversion.ConvertSelectElement;
import xqt.model.data.Resultset;
import xqt.model.data.ResultsetType;
import xqt.model.data.Variable;
import xqt.model.declarations.PerspectiveDescriptor;
import xqt.model.exceptions.LanguageExceptionBuilder;
import xqt.model.expressions.MemberExpression;
import xqt.model.statements.query.SelectDescriptor;

/**
 *
 * @author standard
 */
public class DefaultDataAdapter implements DataAdapter{
    private String dialect = "";
    private DataReaderBuilder builder = null;
    private ConvertSelectElement convertSelect = null;
    private Map<JoinedContainer.JoinOperator, String> runtimeJoinOperators = new HashMap<>();
    
    public DefaultDataAdapter(){
        convertSelect = new ConvertSelectElement();
        runtimeJoinOperators.put(JoinedContainer.JoinOperator.EQ, "==");
        runtimeJoinOperators.put(JoinedContainer.JoinOperator.NotEQ, "!=");
        runtimeJoinOperators.put(JoinedContainer.JoinOperator.GT, ">");
        runtimeJoinOperators.put(JoinedContainer.JoinOperator.GTEQ, ">=");
        runtimeJoinOperators.put(JoinedContainer.JoinOperator.LT, "<");
        runtimeJoinOperators.put(JoinedContainer.JoinOperator.LTEQ, "<=");        
    }
    
    @Override
    public Resultset run(SelectDescriptor select, Object context) {
        Map<String, Variable> memory = (Map<String, Variable>)context;
        switch (select.getSourceClause().getContainer().getDataContainerType()) {
            case Variable:
                return runForSingleContainer(select, memory);
            case Joined:
                return runForJoinedContainer(select, memory);
            default:
                return null;
        }
    }

    @Override
    public Resultset complement(SelectDescriptor select, Variable variable){
        switch (select.getSourceClause().getContainer().getDataContainerType()) {
            case Variable:
                Resultset resultSet = internalRun(select, variable);
                return resultSet;
            default:
                return null;
        }
    }
    
    @Override
    public boolean needsMemory() {
        return true;
    }

    @Override
    public void prepare(SelectDescriptor select, Object context) {
        builder = new DataReaderBuilder();
        switch (select.getSourceClause().getContainer().getDataContainerType()){
            case Plot:{
                break;
            }
            case Joined:
                prepareJoined(select, context);
                break;
            case Single:
                break;
            case Variable:{
                prepareVariable(select);
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

    private AdapterInfo adapterInfo;
    
    @Override
    public AdapterInfo getAdapterInfo(){
        return adapterInfo;
    }
    
    @Override
    public void setAdapterInfo(AdapterInfo value){
        adapterInfo = value;
    }
    
    @Override
    public void setup(Map<String, Object> config) {
        registerCapability("select.qualifier", false);
        registerCapability("function", true);
        registerCapability("function.default.max", true);
        registerCapability("expression", true);
        registerCapability("select.projection.perspective", true);
        registerCapability("select.projection.perspective.implicit", true);
        registerCapability("select.projection.perspective.explicit", true);
        registerCapability("select.projection.perspective.inline", true);
        registerCapability("select.source.single", true);
        registerCapability("select.source.joined", false);
        registerCapability("select.source.variable", true);
        registerCapability("select.target.variable", true);
        registerCapability("select.target.persist", false);
        registerCapability("select.target.plot", true);
        registerCapability("select.anchor", false);
        registerCapability("select.filter", true);
        registerCapability("select.orderby", true);
        registerCapability("select.groupby", true);
        registerCapability("select.limit", true);
        registerCapability("select.limit.take", true);
        registerCapability("select.limit.skip", true);
    }
    
    @Override
    public boolean hasRequiredCapabilities(SelectDescriptor select) {
        boolean allmatched = select.getRequiredCapabilities().stream().allMatch(p-> this.isSupported(p));
        return allmatched;
    }

    private Resultset internalRun(SelectDescriptor select, Variable sourceVariable) {
        try{
            List<Object> source = (List<Object>)sourceVariable.getResult().getTabularData(); // for testing purpose, it just returns the source
            switch (select.getTargetClause().getContainer().getDataContainerType()){
                case Plot:{
                    Resultset resultSet = new Resultset(ResultsetType.Image); 
                    // create a reader, much like the Variable case.
                    // get data from the reader
                    // draw data into a plot. drawing here makes the template easier!
                    // return the plot
                    PlotContainer plotModel = (PlotContainer)select.getTargetClause().getContainer();
                    if(source == null || source.stream().count() <= 0){                    
                        resultSet.setData(null);
                        resultSet.setSchema(sourceVariable.getResult().getSchema());
                    } else {
                        Class entryPoint = select.getExecutionInfo().getExecutionSource().getCompiledClass();
                        DataReader reader = builder.build(entryPoint);
                        List<Object> result = reader.read(source, null);
                        resultSet.setSchema(sourceVariable.getResult().getSchema()); 
                        // use the plot clause (model) in order to build the chart's data model
                        //DefaultChartModel modelA = new DefaultChartModel("ModelA"); 
                        //DefaultTableModel b = new DefaultTableModel();   
                        // investigate using a table model and a TableToChartAdapter object ...            
                        Chart chart = createChart(result, plotModel);
                        //ChartStyle styleA = new ChartStyle(Color.blue, false, true); 
                        //chart.addModel(modelA, styleA); 
                        resultSet.setData(chart);
                    }
                    
                    return resultSet;
                }
                case Joined:
                    break;
                case Single:
                    break;
                case Variable:{
                    Resultset resultSet = new Resultset(ResultsetType.Tabular); 
                    if(source == null || source.stream().count() <= 0){  
                        return null;
//                        resultSet.setData(null);
//                        resultSet.setSchema(sourceVariable.getResult().getSchema());
                    } else {
                        Class entryPoint = select.getExecutionInfo().getExecutionSource().getCompiledClass();
                        DataReader reader = builder.build(entryPoint);
                        List<Object> result = reader.read(source, null);
                        if(result == null)
                            return null;
                        resultSet.setData(result);
                        resultSet.setSchema(select.getProjectionClause().getPerspective().createSchema());                               
                    }
                    //resultSet.setSchema(prepareSchema(select));
                    return resultSet;
                }
                //// END OF MAPPER AREA                //// END OF MAPPER AREA
            }
        } catch (Exception ex) {
            // do something here!!
        }       
        return null;    
    }

    @SuppressWarnings("unchecked")
    private void updateXRange(List<TableToChartAdapter> adapters, String hLabel, Chart chart) {
        // check the axis variable type and based on the type decise on the Axis type: Category, Numeric, etc.
        Axis xAxis = chart.getXAxis();
        try{
            Range<?> xRange = (Range) adapters.get(0).getXRange();
            xAxis = new NumericAxis(xRange.minimum()*0.95, xRange.maximum()*1.05, hLabel);
            chart.setXAxis(xAxis);
        } catch (Exception ex){
            // the range may contain invlid data, usually null values
            xAxis.setLabel(xAxis.getLabel().getLabel() + " -> Erroneous data");
        }
    }

    @SuppressWarnings("unchecked")
    private void updateYRange(List<TableToChartAdapter> adapters, String vLabel, Chart chart) {
        Axis yAxis = chart.getYAxis();
        try{
            NumericRange nRange = null;
            for(TableToChartAdapter adapter: adapters){
                Range<?> yRange = adapter.getYRange();
                nRange = NumericRange.union((NumericRange) nRange, (NumericRange) yRange);
            }        
            if (nRange!= null && nRange.getMin() == nRange.getMax()) {
                // Deal with the special case of only one point
                yAxis = new NumericAxis(nRange.getMin(), nRange.getMax() + 1, vLabel);
            }
            else {
                yAxis = new NumericAxis(nRange.minimum()*0.95, nRange.maximum()*1.05, vLabel);
            }
            chart.setYAxis(yAxis);
        } catch (Exception ex){
            // the range may contain invlid data, usually null values
            yAxis.setLabel(yAxis.getLabel().getLabel() + " -> Erroneous data");
        }
    }

    private Chart createChart( List<Object> result, PlotContainer plotModel){
        Object [][] data = null;
        List<Field> axes = new ArrayList<>();
        if (result != null && result.size() > 0) {
            Class<?> clazz = result.get(0).getClass();
            //Field x = null;
            //Field y = null;

            try {
                 //x = clazz.getField(plotModel.getHax());
                 //y = clazz.getField(plotModel.getVaxes().get(0));
                 axes.add(clazz.getField(plotModel.getHax().getId()));
                 for(MemberExpression yAx: plotModel.getVaxes()) {
                     axes.add(clazz.getField(yAx.getId()));
                 }                              
            } catch (NoSuchFieldException | SecurityException ex) {
                Logger.getLogger(DefaultDataAdapter.class.getName()).log(Level.SEVERE, null, ex);
            }
            data = new Object[(int)result.stream().count()][axes.size()];
            int rowCounter = 0;
            for(Object row: result) {  
                int columnCounter = 0;
                for(Field axField: axes){
                    try {
                        //Object xValue = x.get(row); // convert the values to proper types and also choose proper point type
                        //Object yValue = y.get(row);
                        Object cellValue = axField.get(row);
                        data[rowCounter][columnCounter++] = cellValue;
                        //modelA.addPoint((double)xValue, (double)yValue); 
                    } catch (IllegalArgumentException | IllegalAccessException ex) {
                        // report an error
                    }
                }
                rowCounter++;
            }
        }
        //modelA.addPoint(102, 135);
        //modelA.addPoint(170, 200);
        TableModel tableModel = new DefaultTableModel(data, axes.stream().map(p->p.getName()).collect(Collectors.toList()).toArray());
        SortableTable table = new SortableTable(tableModel);
        String vLabel = plotModel.getVaxes().stream().map(p->p.getId()).collect(Collectors.joining(", "));
        Axis xAxis = new NumericAxis(new AutoPositionedLabel(plotModel.gethLabel()));
        xAxis.setRange(0, 400);
        Axis yAxis = new NumericAxis(new AutoPositionedLabel(plotModel.getvLabel().isEmpty()? vLabel: plotModel.getvLabel()));
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
        updateYRange(adapters, plotModel.getvLabel().isEmpty()? vLabel: plotModel.getvLabel(), chart);        
        return chart;
    }
    
    private List<Color> getDrawingColorPallet(int palletSize) {
        List<Color> pallet = new ArrayList<>(palletSize);
        for (int i = 0; i < palletSize; i++)
            pallet.add(Color.getHSBColor((float) i / palletSize, 1, 1));    
        return pallet;
    }

    private Resultset runForSingleContainer(SelectDescriptor select, Map<String, Variable> memory) {
        // check whether the data is tabular!
        Variable sourceVariable = (Variable)memory.get(((VariableContainer)select.getSourceClause().getContainer()).getVariableName());
        // do something with the source data using the select definition
        Resultset resultSet = internalRun(select, sourceVariable);
        return resultSet;
    }

    private Resultset runForJoinedContainer(SelectDescriptor select, Map<String, Variable> memory){
        try {
            JoinedContainer join = ((JoinedContainer)select.getSourceClause().getContainer());
            
            Variable leftVariable = (Variable)memory.get(((VariableContainer)join.getLeftContainer()).getVariableName());
            List<Object> leftSource = (List<Object>)leftVariable.getResult().getTabularData();
            
            Variable rightVariable = (Variable)memory.get(((VariableContainer)join.getRightContainer()).getVariableName());
            List<Object> rightSource = (List<Object>)rightVariable.getResult().getTabularData();
            
            Resultset resultSet = new Resultset(ResultsetType.Tabular);
            Class entryPoint = select.getExecutionInfo().getExecutionSource().getCompiledClass();
            DataReader reader = builder.build(entryPoint);
            List<Object> result;
            result = reader.read(leftSource, rightSource);          
            resultSet.setData(result);
            resultSet.setSchema(select.getProjectionClause().getPerspective().createSchema());
            //resultSet.setSchema(prepareSchema(select));
            return resultSet;
       } catch (Exception ex) {
            
       }
       return null;
    }

    private void prepareJoined(SelectDescriptor select, Object context) {
        JoinedContainer join = ((JoinedContainer)select.getSourceClause().getContainer());
        
        if(join.getLeftContainer().getDataContainerType() != DataContainer.DataContainerType.Variable){
            select.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate("A variable is expected on the left side of the JOIN.")
                    .setContextInfo1(select.getId())
                    .setLineNumber(select.getParserContext().getStart().getLine())
                    .setColumnNumber(-1)
                    .build()
                );    
            return;
        }
        VariableContainer leftContainer = (VariableContainer)join.getLeftContainer();
        
        if(join.getRightContainer().getDataContainerType() != DataContainer.DataContainerType.Variable){
            select.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate("A variable is expected on the right side of the JOIN.")
                    .setContextInfo1(select.getId())
                    .setLineNumber(select.getParserContext().getStart().getLine())
                    .setColumnNumber(-1)
                    .build()
                );    
            return;
        }
        VariableContainer rightContainer = (VariableContainer)join.getRightContainer();

        if(leftContainer.getPerspective() == null) {
            // error
        }
        if(rightContainer.getPerspective() == null) {
            // error
        }

        // create an implicit perspective for the whole select statement
        select.getProjectionClause().setPerspective(
                PerspectiveDescriptor.combinePerspective(
                        select.getProjectionClause().getPerspective(), leftContainer.getPerspective(), rightContainer.getPerspective(), "joined_" + select.getId()
                ));
        select.getProjectionClause().setPresent(true);
        // filter, ordering, and grouping may face attribute rename issues because of the combined attributes of the left and right.
        // they should be renamed accordingly
        // select.repair();
        select.validate();
        if(select.hasError())
            return;
        
        builder.leftClassName(select.getDependsUpon().getEntityType().getFullName());
        builder.rightClassName(select.getDependsUpon2().getEntityType().getFullName());
        
        builder.readerResourceName("MemReader");
        builder.entityResourceName("MemJoinedEntity");        
        Map<String, AttributeInfo>  attributes = convertSelect.prepareAttributes(select.getProjectionClause().getPerspective(), this, false);            
        builder.addResultAttributes(attributes);
//        builder.getAttributes().values().stream().forEach(at -> {
//            at.internalDataType = helper.getPhysicalType(at.conceptualDataType);
//        });
        try{
            if(isSupported("select.filter")) 
                builder.where(convertSelect.prepareWhere(select.getFilterClause(), this), true);
            else 
                builder.where("", false);
        } catch(Exception ex){
            select.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate(ex.getMessage())
                    .setContextInfo1(select.getId())
                    .setLineNumber(select.getParserContext().getStart().getLine())
                    .setColumnNumber(-1)
                    .build()
                );                
        }

        Map<AttributeInfo, String> orderItems = new LinkedHashMap<>();        
        for (Map.Entry<String, String> entry : convertSelect.prepareOrdering(select.getOrderClause()).entrySet()) {
                if(attributes.containsKey(entry.getKey())){
                    orderItems.put(attributes.get(entry.getKey()), entry.getValue());
                }            
        }
        builder.orderBy(orderItems);
        prepareLimit(builder, select);
        builder.writeResultsToFile(convertSelect.shouldResultBeWrittenIntoFile(select.getTargetClause()));

        builder.joinType(join.getJoinType().toString())
                .joinOperator(runtimeJoinOperators.get(join.getJoinOperator()))
                .leftJoinKey(join.getLeftKey().getId())
                .rightJoinKey(join.getRightKey().getId());

        try {
            select.getExecutionInfo().setSources(builder.createSources());
        } catch (IOException ex){
            select.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate(ex.getMessage())
                    .setContextInfo1(select.getId())
                    .setLineNumber(select.getParserContext().getStart().getLine())
                    .setColumnNumber(-1)
                    .build()
                );
        }            
    }

    private void prepareVariable(SelectDescriptor select) {
        try {
            // the statement should depend on another, because the source is a variable!
            String sourceRowType = select.getEntityType().getFullName();                    
            if(sourceRowType.isEmpty())
                throw new Exception("No dependecy trace is found"); // is caught by the next catch block
            if(select.getDependsUpon()!= null && select.getDependsUpon() instanceof SelectDescriptor ){
                builder.recordPerspective(((SelectDescriptor)select.getDependsUpon()).getProjectionClause().getPerspective());
            }
            Map<String, AttributeInfo>  attributes = convertSelect.prepareAttributes(select.getProjectionClause().getPerspective(), this, false);
            builder.addResultAttributes(attributes);
            // transform the ordering clauses to their bound equivalent, in each attribute names are linked to the attibutes objects
            Map<AttributeInfo, String> orderItems = new LinkedHashMap<>();        
            for (Map.Entry<String, String> entry : convertSelect.prepareOrdering(select.getOrderClause()).entrySet()) {
                    if(attributes.containsKey(entry.getKey())){
                        orderItems.put(attributes.get(entry.getKey()), entry.getValue());
                    }            
            }
            builder.sourceRowType(sourceRowType)
                .readerResourceName("MemReader")
                .entityResourceName("")
                .where(convertSelect.translateExpression(convertSelect.prepareWhere(select.getFilterClause(), this), select.getProjectionClause().getPerspective()), false)
                .orderBy(orderItems)
                .writeResultsToFile(convertSelect.shouldResultBeWrittenIntoFile(select.getTargetClause()));
                ;
            if(select.getProjectionClause().getPerspective().getPerspectiveType() == PerspectiveDescriptor.PerspectiveType.Explicit
                || select.getProjectionClause().getPerspective().getPerspectiveType() == PerspectiveDescriptor.PerspectiveType.Inline){
                builder.entityResourceName("MemEntity");
                builder.sourceOfData("variable");
            }    
            prepareLimit(builder, select);
            select.getExecutionInfo().setSources(builder.createSources());
        } catch (Exception ex) {
            // return a language exception
            select.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate("The depenedent statement '%s' is found but no dependency information found!")
                    .setContextInfo1(select.getId())
                    .setLineNumber(select.getParserContext().getStart().getLine())
                    .setColumnNumber(-1)
                    .build()
            );                        
        }
    }
    
    private void prepareLimit(DataReaderBuilder builder, SelectDescriptor select) {
        if(isSupported("select.limit")){
            builder.skip(select.getLimitClause().getSkip())
                   .take(select.getLimitClause().getTake());
        }
        else{
            builder.skip(-1)
                   .take(-1);
        }
    }

    @Override
    public String getDialect() {
        return dialect;
    }

    @Override
    public void setDialect(String dialect) {
        dialect = dialect;
    }
    
}
