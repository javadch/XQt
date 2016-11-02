/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.adapters.builtin;

import com.jidesoft.chart.Chart;
import com.jidesoft.chart.Legend;
import com.jidesoft.chart.Orientation;
import com.jidesoft.chart.annotation.AutoPositionedLabel;
import com.jidesoft.chart.axis.Axis;
import com.jidesoft.chart.axis.CategoryAxis;
import com.jidesoft.chart.axis.NumericAxis;
import com.jidesoft.chart.model.TableToChartAdapter;
import com.jidesoft.chart.style.BarStyle;
import com.jidesoft.chart.style.ChartStyle;
import com.jidesoft.grid.SortableTable;
import com.jidesoft.range.NumericRange;
import com.jidesoft.range.Range;
import com.vaiona.commons.data.AttributeInfo;
import com.vaiona.commons.types.TypeSystem;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.PageAttributes.OrientationRequestedType;
import java.awt.Paint;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import xqt.model.adapters.BaseDataAdapter;
import xqt.model.containers.DataContainer;
import xqt.model.containers.JoinedContainer;
import xqt.model.containers.PlotContainer;
import xqt.model.containers.VariableContainer;
import xqt.model.data.Resultset;
import xqt.model.data.ResultsetType;
import xqt.model.data.Variable;
import xqt.model.declarations.PerspectiveAttributeDescriptor;
import xqt.model.declarations.PerspectiveDescriptor;
import xqt.model.exceptions.LanguageExceptionBuilder;
import xqt.model.expressions.ExpressionType;
import xqt.model.expressions.MemberExpression;
import xqt.model.statements.query.JoinedSelectDescriptor;
import xqt.model.statements.query.SelectDescriptor;

/**
 *
 * @author standard
 */
public class DefaultDataAdapter extends BaseDataAdapter {
	private DataReaderBuilder builder = null;
	private DefaultDataAdapterHelper helper = null;

	public DefaultDataAdapter() {
		needsMemory = true;
		runtimeJoinOperators.put(JoinedContainer.JoinOperator.EQ, "==");
		runtimeJoinOperators.put(JoinedContainer.JoinOperator.NotEQ, "!=");
		runtimeJoinOperators.put(JoinedContainer.JoinOperator.GT, ">");
		runtimeJoinOperators.put(JoinedContainer.JoinOperator.GTEQ, ">=");
		runtimeJoinOperators.put(JoinedContainer.JoinOperator.LT, "<");
		runtimeJoinOperators.put(JoinedContainer.JoinOperator.LTEQ, "<=");
		runtimeJoinOperators.put(JoinedContainer.JoinOperator.EqString, ".equals");
		runtimeJoinOperators.put(JoinedContainer.JoinOperator.NotEqString, "!equals"); // this
																						// is
																						// a
																						// special
																						// case
																						// that
																						// is
																						// replaced
																						// properly
																						// in
																						// the
																						// reader
																						// class
																						// template
		helper = new DefaultDataAdapterHelper();
	}

	@Override
	public Resultset run(SelectDescriptor select, Object context) {
		Map<String, Variable> memory = (Map<String, Variable>) context;
		switch (select.getSourceClause().getContainer().getDataContainerType()) {
		case Variable:
			Variable sourceVariable = (Variable) memory
					.get(((VariableContainer) select.getSourceClause().getContainer()).getVariableName());
			return runForSingleContainer(select, sourceVariable);
		case Joined:
			Variable leftVariable = null;
			Variable rightVariable = null;
			if (select instanceof JoinedSelectDescriptor) {
				JoinedSelectDescriptor join = (JoinedSelectDescriptor) select;
				leftVariable = join.getLeftSide().getExecutionInfo().getVariable();
				rightVariable = join.getRightSide().getExecutionInfo().getVariable();
			} else {
				JoinedContainer join = ((JoinedContainer) select.getSourceClause().getContainer());
				leftVariable = (Variable) memory.get(((VariableContainer) join.getLeftContainer()).getVariableName());
				rightVariable = (Variable) memory.get(((VariableContainer) join.getRightContainer()).getVariableName());
			}
			// run the hetero join on the result of its side queries.
			return runForJoinedContainer(select, leftVariable, rightVariable);
		default:
			return null;
		}
	}

	@Override
	public Resultset complement(SelectDescriptor select, Variable variable) {
		switch (select.getSourceClause().getContainer().getDataContainerType()) {
		case Variable:
			Resultset resultSet = runForSingleContainer(select, variable); // internalRun(select,
																			// variable);
			return resultSet;
		default:
			return null;
		}
	}

	@Override
	public void prepare(SelectDescriptor select, Object context) {
		builder = new DataReaderBuilder();
		builder.statementId(select.getId());
		switch (select.getSourceClause().getContainer().getDataContainerType()) {
		case Plot: {
			break;
		}
		case Joined:
			prepareJoined(select, context);
			break;
		case Single:
			break;
		case Variable: {
			prepareVariable(select);
		}
		}
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
		registerCapability("select.source.joined", true);
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

	@SuppressWarnings("unchecked")
	private void updateXRange(List<TableToChartAdapter> adapters, String hLabel, Chart chart) {
		// check the axis variable type and based on the type decise on the Axis
		// type: Category, Numeric, etc.
		Axis xAxis = chart.getXAxis();
		try {
			Range<?> xRange = (Range) adapters.get(0).getXRange();
			xAxis = new NumericAxis(xRange.minimum() * 0.95, xRange.maximum() * 1.05, hLabel);
			chart.setXAxis(xAxis);
		} catch (Exception ex) {
			// the range may contain invlid data, usually null values
			xAxis.setLabel(xAxis.getLabel().getLabel() + " -> Erroneous data");
		}
	}

	@SuppressWarnings("unchecked")
	private void updateYRange(List<TableToChartAdapter> adapters, String vLabel, Chart chart) {
		Axis yAxis = chart.getYAxis();
		try {
			NumericRange nRange = null;
			for (TableToChartAdapter adapter : adapters) {
				Range<?> yRange = adapter.getYRange();
				nRange = NumericRange.union((NumericRange) nRange, (NumericRange) yRange);
			}
			if (nRange != null && nRange.getMin() == nRange.getMax()) {
				// Deal with the special case of only one point
				yAxis = new NumericAxis(nRange.getMin(), nRange.getMax() + 1, vLabel);
			} else {
				yAxis = new NumericAxis(nRange.minimum() * 0.95, nRange.maximum() * 1.05, vLabel);
			}
			chart.setYAxis(yAxis);
		} catch (Exception ex) {
			// the range may contain invlid data, usually null values
			yAxis.setLabel(yAxis.getLabel().getLabel() + " -> Erroneous data");
		}
	}

	private JPanel createChart(List<Object> result, PlotContainer plotModel) {

		switch (plotModel.getPlotType().trim()) {
		case "l":
			return createLineChart(result, plotModel);
		case "b":
			//return createBarOrigianlChart();
		return createBarChart(result, plotModel);
		default:
			return null;
		}
	}

	private JPanel createLineChart(List<Object> result, PlotContainer plotModel) {
		Object[][] data = null;
		List<Field> axes = new ArrayList<>();
		if (result != null && result.size() > 0) {
			Class<?> clazz = result.get(0).getClass();
			// Field x = null;
			// Field y = null;

			try {
				// x = clazz.getField(plotModel.getHax());
				// y = clazz.getField(plotModel.getVaxes().get(0));
				axes.add(clazz.getField(plotModel.getHax().getId()));
				for (MemberExpression yAx : plotModel.getVaxes()) {
					axes.add(clazz.getField(yAx.getId()));
				}
			} catch (NoSuchFieldException | SecurityException ex) {
				Logger.getLogger(DefaultDataAdapter.class.getName()).log(Level.SEVERE, null, ex);
			}
			data = new Object[(int) result.stream().count()][axes.size()];
			int rowCounter = 0;
			for (Object row : result) {
				int columnCounter = 0;
				for (Field axField : axes) {
					try {
						// Object xValue = x.get(row); // convert the values to
						// proper types and also choose proper point type
						// Object yValue = y.get(row);
						Object cellValue = axField.get(row);
						data[rowCounter][columnCounter++] = cellValue;
						// modelA.addPoint((double)xValue, (double)yValue);
					} catch (IllegalArgumentException | IllegalAccessException ex) {
						// report an error
					}
				}
				rowCounter++;
			}
		}
		// modelA.addPoint(102, 135);
		// modelA.addPoint(170, 200);
		TableModel tableModel = new DefaultTableModel(data,
				axes.stream().map(p -> p.getName()).collect(Collectors.toList()).toArray());
		SortableTable table = new SortableTable(tableModel);
		String vLabel = plotModel.getVaxes().stream().map(p -> p.getId()).collect(Collectors.joining(", "));
		Axis xAxis = new NumericAxis(new AutoPositionedLabel(plotModel.gethLabel()));
		xAxis.setRange(0, 400);
		Axis yAxis = new NumericAxis(
				new AutoPositionedLabel(plotModel.getvLabel().isEmpty() ? vLabel : plotModel.getvLabel()));
		yAxis.setRange(0, 200);

		Chart chart = new Chart();
		chart.setTitle(plotModel.getPlotLabel());
		chart.setXAxis(xAxis);
		chart.setYAxis(yAxis);

		List<TableToChartAdapter> adapters = new ArrayList<>();
		int adapterCounter = 0;
		List<Color> colorPallet = getDrawingColorPallet((int) axes.stream().count() - 1);
		for (Field ax : axes.stream().skip(1).collect(Collectors.toList())) { // the
																				// first
																				// filed
																				// is
																				// the
																				// X
																				// variable
			TableToChartAdapter adapter = new TableToChartAdapter(
					ax.getName() /* + " Series" */, table.getModel());
			ChartStyle style = new ChartStyle(colorPallet.get(adapterCounter++), false, true);
			adapter.setXColumn(0);
			adapter.setYColumn(adapterCounter); // the first y column starts
												// from 1, which is incremented
												// at the color setting line
			adapters.add(adapter);
			chart.addModel(adapter, style); // and the style
		}
		updateXRange(adapters, plotModel.gethLabel(), chart);
		updateYRange(adapters, plotModel.getvLabel().isEmpty() ? vLabel : plotModel.getvLabel(), chart);

		JPanel chartPanel = new JPanel();
		chartPanel.setLayout(new BorderLayout());
		chartPanel.add(chart, BorderLayout.CENTER);

		JPanel legendPanel = new JPanel();
		chartPanel.add(legendPanel, BorderLayout.EAST);

		Legend legend = new Legend(chart);
		legendPanel.add(legend);

		return chartPanel;
	}

	private JPanel createBarOrigianlChartStop() {
		List<Object> result = new ArrayList<>();
		result = createDataset();

		Object[][] data = null;
		List<Field> axes = new ArrayList<>();
		if (result != null && result.size() > 0) {
			Field[] fields = Customer.class.getDeclaredFields();
			try {
				// x = clazz.getField(plotModel.getHax());
				// y = clazz.getField(plotModel.getVaxes().get(0));
				for (Field f : fields) {	
					if (f.getName() != "id")
						axes.add(f);		
				}

			} catch (SecurityException ex) {
				Logger.getLogger(DefaultDataAdapter.class.getName()).log(Level.SEVERE, null, ex);
			}
			data = new Object[(int) result.stream().count()][axes.size()];
			int rowCounter = 0;
			for (Object row : result) { // All table column
				int columnCounter = 0;
				for (Field axField : axes) {
					try {
						// Object xValue = x.get(row); // convert the values to
						// proper types and also choose proper point type
						// Object yValue = y.get(row);
						Object cellValue = axField.get(row);
						data[rowCounter][columnCounter++] = cellValue;
						// modelA.addPoint((double)xValue, (double)yValue);
					} catch (IllegalArgumentException | IllegalAccessException ex) {
						// report an error
					}
				}
				rowCounter++;
			}
		}
		// modelA.addPoint(102, 135);
		// modelA.addPoint(170, 200);
		TableModel tableModel = new DefaultTableModel(data,
				axes.stream().map(p -> p.getName()).collect(Collectors.toList()).toArray());
		SortableTable table = new SortableTable(tableModel);
		table.setShowHorizontalLines(false);
		table.setShowVerticalLines(false);
		String vLabel = "Number_of_Students";// plotModel.getVaxes().stream().map(p
												// ->
												// p.getId()).collect(Collectors.joining(",
												// "));
		Axis xAxis = new NumericAxis(new AutoPositionedLabel("Activites"));

		xAxis.setRange(0, 400);
		Axis yAxis = new NumericAxis(new AutoPositionedLabel(vLabel));// plotModel.getvLabel().isEmpty()
																		// ?
																		// vLabel
																		// :
																		// plotModel.getvLabel()));
		yAxis.setRange(0, 200);

		Chart chart = new Chart();
		chart.setTitle("Student Activites afer School");// plotModel.getPlotLabel());
		chart.setXAxis(xAxis);
		chart.setYAxis(yAxis);

		List<TableToChartAdapter> adapters = new ArrayList<>();
		int adapterCounter = 0;
		int colorCounter = 0;
		int barWidth = 10;
		int RowCounter=-1;
		List<Color> colorPallet = getDrawingColorPallet((int) result.stream().count()+10);
		for (Field ax : axes.stream().skip(1).collect(Collectors.toList())) { // the
																				// first
																				// filed
																				// is
			/*if(RowCounter >=0 )	{															// the
			//for(RowCounter=0; RowCounter <(int) result.stream().count();RowCounter++ ){																	// X
			int studentNum=(int)table.getModel().getValueAt(0, 0);																	// variable
			table.getModel().setValueAt(studentNum+5, 0, 0);
			//}
			}*/
			TableToChartAdapter adapter = new TableToChartAdapter(
					ax.getName() /* + " Series " */ , table.getModel());

			ChartStyle style = new ChartStyle();// colorPallet.get(adapterCounter++),
												// false, true, true);
			style = style.withBars();
			style.setBarsVisible(true);
			style.setBarColor((Color) colorPallet.get(colorCounter++));
			style.setBarOrientation(Orientation.vertical);
			style.setBarWidth(barWidth);
			adapter.setXColumn(0);
			adapter.setYColumn(colorCounter); // the first y column starts
												// from 1, which is incremented
												// at the color setting line
			adapters.add(adapter);
			chart.addModel(adapter, style);// and the style
			RowCounter++;
		}
		updateXRange(adapters, "Activites After School", chart);
		// updateYRange(adapters, plotModel.getvLabel().isEmpty() ? vLabel :
		// plotModel.getvLabel(), chart);
		updateYRange(adapters, vLabel, chart);

		JPanel chartPanel = new JPanel();
		chartPanel.setLayout(new BorderLayout());
		chartPanel.add(chart, BorderLayout.CENTER);

		JPanel legendPanel = new JPanel();
		chartPanel.add(legendPanel, BorderLayout.EAST);

		Legend legend = new Legend(chart);
		legendPanel.add(legend);

		return chartPanel;
	}
	
	private JPanel createBarOrigianlChart( ) {
		List<Object> result= new ArrayList<>();
		result=createDataset();
		
		Object[][] data = null;
		List<Field> axes = new ArrayList<>();
		if (result != null && result.size() > 0) {
		Field[] fields = Customer.class.getDeclaredFields();
			try {
				// x = clazz.getField(plotModel.getHax());
				// y = clazz.getField(plotModel.getVaxes().get(0));
				for(Field f : fields){
					if(f.getName()!="id")
					axes.add(f);
		        }
								
			} catch ( SecurityException ex) {
				Logger.getLogger(DefaultDataAdapter.class.getName()).log(Level.SEVERE, null, ex);
			}
			data = new Object[(int) result.stream().count()][axes.size()];
			int rowCounter = 0;
			for (Object row : result) { //All table column 
				int columnCounter = 0;
				for (Field axField : axes) {
					try {
						// Object xValue = x.get(row); // convert the values to
						// proper types and also choose proper point type
						// Object yValue = y.get(row);
						Object cellValue = axField.get(row);
						data[rowCounter][columnCounter++] = cellValue;
						// modelA.addPoint((double)xValue, (double)yValue);
					} catch (IllegalArgumentException | IllegalAccessException ex) {
						// report an error
					}
				}
				rowCounter++;
			}
		}
		// modelA.addPoint(102, 135);
		// modelA.addPoint(170, 200);
		TableModel tableModel = new DefaultTableModel(data,
				axes.stream().map(p -> p.getName()).collect(Collectors.toList()).toArray());
		SortableTable table = new SortableTable(tableModel);
		table.setShowHorizontalLines(false);
		table.setShowVerticalLines(false);
		String vLabel ="Number_of_Students" ;//plotModel.getVaxes().stream().map(p -> p.getId()).collect(Collectors.joining(", "));
		Axis xAxis = new NumericAxis(new AutoPositionedLabel("Activites"));
		 
		xAxis.setRange(0, 400);
		Axis yAxis = new NumericAxis(
				new AutoPositionedLabel(vLabel));//plotModel.getvLabel().isEmpty() ? vLabel : plotModel.getvLabel()));
		yAxis.setRange(0, 200);

		Chart chart = new Chart();
		chart.setTitle("Student Activites afer School");//plotModel.getPlotLabel());
		chart.setXAxis(xAxis);
		chart.setYAxis(yAxis);
		
		List<TableToChartAdapter> adapters = new ArrayList<>();
		int adapterCounter = 0;
		int colorCounter=0;
		int barWidth= 10;
		int RowCounter=-1;
		List<Color> colorPallet = getDrawingColorPallet((int) result.stream().count() +5);
		for (Field ax : axes.stream().skip(1).collect(Collectors.toList())) { // the
																				// first
																				// filed
																				// is
																				// the
																				// X
																				// variable
			
			if(RowCounter >=0 )	{															// the
			for(RowCounter=0; RowCounter <(int) result.stream().count();RowCounter++ ){																	// X
			int studentNum=(int)table.getModel().getValueAt(0, 0);																	// variable
			table.getModel().setValueAt(studentNum+15, 0, 0);
			}
			}
			TableToChartAdapter adapter = new TableToChartAdapter(
					ax.getName() /* + " Series "*/ , table.getModel());
						
			ChartStyle style = new ChartStyle();//colorPallet.get(adapterCounter++), false, true, true);
			style=style.withBars();
			style.setBarsVisible(true);
			style.setBarColor((Color)colorPallet.get(colorCounter++));
			style.setBarOrientation( Orientation.vertical);
			style.setBarWidth(barWidth);
			
			adapter.setXColumn(0);
			//adapter.getXColumn();
			adapter.setYColumn(colorCounter); // the first y column starts
												// from 1, which is incremented
												// at the color setting line
			adapters.add(adapter);
			chart.addModel(adapter, style);// and the style
			adapter.clearAnnotations();
			
			RowCounter++;
			
		}
		updateXRange(adapters, "Activites", chart);
		//updateYRange(adapters, plotModel.getvLabel().isEmpty() ? vLabel : plotModel.getvLabel(), chart);
		updateYRange(adapters, vLabel, chart);

		JPanel chartPanel = new JPanel();
		chartPanel.setLayout(new BorderLayout());
		chartPanel.add(chart, BorderLayout.CENTER);

		JPanel legendPanel = new JPanel();
		chartPanel.add(legendPanel, BorderLayout.EAST);

		Legend legend = new Legend(chart);
		legendPanel.add(legend);

		return chartPanel;
	}

	private JPanel createBarChart(List<Object> result, PlotContainer plotModel) {
		Object[][] data = null;
		List<Field> axes = new ArrayList<>();
		if (result != null && result.size() > 0) {
			Class<?> clazz = result.get(0).getClass();
			// Field x = null;
			// Field y = null;

			try {
				// x = clazz.getField(plotModel.getHax());
				// y = clazz.getField(plotModel.getVaxes().get(0));
				axes.add(clazz.getField(plotModel.getHax().getId()));
				for (MemberExpression yAx : plotModel.getVaxes()) {
					axes.add(clazz.getField(yAx.getId()));
				}
			} catch (NoSuchFieldException | SecurityException ex) {
				Logger.getLogger(DefaultDataAdapter.class.getName()).log(Level.SEVERE, null, ex);
			}
			data = new Object[(int) result.stream().count()][axes.size()];
			int rowCounter = 0;
			for (Object row : result) { // All table column
				int columnCounter = 0;
				for (Field axField : axes) {
					try {
						// Object xValue = x.get(row); // convert the values to
						// proper types and also choose proper point type
						// Object yValue = y.get(row);
						Object cellValue = axField.get(row);
						data[rowCounter][columnCounter++] = cellValue;
						// modelA.addPoint((double)xValue, (double)yValue);
					} catch (IllegalArgumentException | IllegalAccessException ex) {
						// report an error
					}
				}
				rowCounter++;
			}
		}
		// modelA.addPoint(102, 135);
		// modelA.addPoint(170, 200);
		TableModel tableModel = new DefaultTableModel(data,
				axes.stream().map(p -> p.getName()).collect(Collectors.toList()).toArray());
		SortableTable table = new SortableTable(tableModel);
		table.setShowHorizontalLines(false);
		table.setShowVerticalLines(false);
		String vLabel = plotModel.getVaxes().stream().map(p -> p.getId()).collect(Collectors.joining(", "));
		Axis xAxis = new NumericAxis(new AutoPositionedLabel(plotModel.gethLabel()));
		xAxis.setRange(0, 400);
		Axis yAxis = new NumericAxis(
				new AutoPositionedLabel(plotModel.getvLabel().isEmpty() ? vLabel : plotModel.getvLabel()));
		yAxis.setRange(0, 200);

		Chart chart = new Chart();
		chart.setTitle(plotModel.getPlotLabel());
		chart.setXAxis(xAxis);
		chart.setYAxis(yAxis);

		List<TableToChartAdapter> adapters = new ArrayList<>();
		int adapterCounter = 0;
		int colorCounter = 0;
		int barWidth = 10;
		List<Color> colorPallet = getDrawingColorPallet((int) result.stream().count() - 1);
		for (Field ax : axes.stream().skip(1).collect(Collectors.toList())) { // the
																				// first
																				// filed
																				// is
																				// the
																				// X
																				// variable
			TableToChartAdapter adapter = new TableToChartAdapter(
					ax.getName() /* + " Series" */, table.getModel());

			ChartStyle style = new ChartStyle();// colorPallet.get(adapterCounter++),
												// false, true, true);
			style = style.withBars();
			style.setBarsVisible(true);
			style.setBarColor((Color) colorPallet.get(colorCounter++));
			style.setBarOrientation(Orientation.vertical);
			style.setBarWidth(barWidth);
			adapter.setXColumn(0);
			adapter.setYColumn(colorCounter); // the first y column starts
												// from 1, which is incremented
												// at the color setting line
			adapters.add(adapter);
			chart.addModel(adapter, style); // and the style
		}
		updateXRange(adapters, plotModel.gethLabel(), chart);
		updateYRange(adapters, plotModel.getvLabel().isEmpty() ? vLabel : plotModel.getvLabel(), chart);

		JPanel chartPanel = new JPanel();
		chartPanel.setLayout(new BorderLayout());
		chartPanel.add(chart, BorderLayout.CENTER);

		JPanel legendPanel = new JPanel();
		chartPanel.add(legendPanel, BorderLayout.EAST);

		Legend legend = new Legend(chart);
		legendPanel.add(legend);

		return chartPanel;
	}

	private List<Object> createDTset() {
		final List<Object> data = new ArrayList<Object>();
		final String fiat = "FIAT";
		final String audi = "AUDI";
		final String ford = "FORD";
		final String speed = "Speed";
		final String millage = "Millage";
		final String userrating = "User Rating";
		final String safety = "safety";
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		dataset.addValue(1.0, fiat, speed);
		dataset.addValue(3.0, fiat, userrating);
		dataset.addValue(5.0, fiat, millage);
		data.add(dataset);

		final DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
		dataset2.addValue(5.0, audi, speed);
		dataset2.addValue(6.0, audi, userrating);
		dataset2.addValue(10.0, audi, millage);
		dataset2.addValue(4.0, audi, safety);
		data.add(dataset2);

		final DefaultCategoryDataset dataset3 = new DefaultCategoryDataset();
		dataset3.addValue(4.0, ford, speed);
		dataset3.addValue(2.0, ford, userrating);
		dataset3.addValue(3.0, ford, millage);
		dataset3.addValue(6.0, ford, safety);

		data.add(dataset3);
		return data;
	}

	private List<Object> createDataset() {
		final List<Object> data = new ArrayList<Object>();
		// final String B="Bangladesh";
		// final String G="Germany";
		// final String U="USA";
		// final String I="IRAN";

		// new DefaultCategoryDataset( );
		Customer dataset = new Customer();
		dataset.setId(1);
		dataset.setActivites(10);
		dataset.Food=20;
		dataset.Fd=10;
		
		//dataset.setSports(30);
		//dataset.EarnMoney = 10;
		//dataset.PlayGame = 60;
		//dataset.WatchTV = 55;
		dataset.setNumber_of_Students(30);

		data.add(dataset);

		Customer dat2 = new Customer();
		dat2.setActivites(10);;
		dat2.setNumber_of_Students(20);
		dat2.Food=10;
		dat2.Fd=20;
		//dat2.EarnMoney = 20;
		//dat2.PlayGame = 50;
		//dat2.WatchTV = 85;
		data.add(dat2);

		Customer dat3 = new Customer();
		dat3.setActivites(70);
		dat3.Food=70;
		dat3.Fd=25;
		//dat3.EarnMoney = 300;
		//dat3.PlayGame = 40;
		//dat3.WatchTV = 65;
		dat3.setNumber_of_Students(80);
		data.add(dat3);

		/*Customer dat4 = new Customer();
		dat4.setSports(30);
		dat4.EarnMoney = 50;
		dat4.PlayGame = 10;
		dat4.WatchTV = 90;
		dat4.setNumber_of_Students(50);

		data.add(dat4);*/
		return data;
	}

	private List<Color> getDrawingColorPallet(int palletSize) {
		List<Color> pallet = new ArrayList<>(palletSize);
		for (int i = 0; i < palletSize; i++)
			pallet.add(Color.getHSBColor((float) i / palletSize, 1, 1));
		return pallet;
	}

	private Resultset runForSingleContainer(SelectDescriptor select, Variable sourceVariable) {
		try {
			List<Object> source = (List<Object>) sourceVariable.getResult().getTabularData();
			Resultset resultSet = new Resultset(ResultsetType.Tabular);
			if (source == null || source.stream().count() <= 0) {
				return null;
			}
			Class entryPoint = select.getExecutionInfo().getExecutionSource().getCompiledClass();
			DataReader reader = builder.build(entryPoint);
			List<Object> result = reader.read(source, null);
			if (result == null)
				return null;
			resultSet.setData(result);
			resultSet.setSchema(select.getProjectionClause().getPerspective().createSchema());
			return refineTargetAfterRun(select, resultSet);
		} catch (Exception ex) {
			return null;
		}
	}

	private Resultset runForJoinedContainer(SelectDescriptor select, Variable leftVariable, Variable rightVariable) {
		try {
			List<Object> leftSource = (List<Object>) leftVariable.getResult().getTabularData();
			List<Object> rightSource = (List<Object>) rightVariable.getResult().getTabularData();

			Resultset resultSet = new Resultset(ResultsetType.Tabular);
			Class entryPoint = select.getExecutionInfo().getExecutionSource().getCompiledClass();
			DataReader reader = builder.build(entryPoint);
			List<Object> result;
			result = reader.read(leftSource, rightSource);
			resultSet.setData(result);
			resultSet.setSchema(select.getProjectionClause().getPerspective().createSchema());
			return refineTargetAfterRun(select, resultSet);
		} catch (Exception ex) {
			return null;
		}
	}

	private Resultset refineTargetAfterRun(SelectDescriptor select, Resultset resultset) {
		try {
			switch (select.getTargetClause().getContainer().getDataContainerType()) {
			case Plot: {
				List<Object> source = (List<Object>) resultset.getTabularData();
				resultset.setResultsetType(ResultsetType.Image);
				PlotContainer plotModel = (PlotContainer) select.getTargetClause().getContainer();
				if (source == null || source.stream().count() <= 0) {
					resultset.setData(null);
					// resultSet.setSchema(sourceVariable.getResult().getSchema());
				} else {
					// use the plot clause (model) in order to build the chart's
					// data model
					// DefaultChartModel modelA = new
					// DefaultChartModel("ModelA");
					// DefaultTableModel b = new DefaultTableModel();
					// investigate using a table model and a TableToChartAdapter
					// object ...
					JPanel chart = createChart(source, plotModel);
					// ChartStyle styleA = new ChartStyle(Color.blue, false,
					// true);
					// chart.addModel(modelA, styleA);
					resultset.setData(chart);
				}
				return resultset;
			}
			case Joined:
				return resultset;
			case Single:
				return resultset;
			case Variable: {
				return resultset;
			}
			}
		} catch (Exception ex) {
			// do something here!!
		}
		return null;
	}

	private void prepareJoined(SelectDescriptor select, Object context) {
		JoinedContainer join = ((JoinedContainer) select.getSourceClause().getContainer());

		if (join.getLeftContainer().getDataContainerType() != DataContainer.DataContainerType.Variable) {
			select.getLanguageExceptions()
					.add(LanguageExceptionBuilder.builder()
							.setMessageTemplate("A variable is expected on the left side of the JOIN.")
							.setContextInfo1(select.getId())
							.setLineNumber(select.getParserContext().getStart().getLine()).setColumnNumber(-1).build());
			return;
		}
		VariableContainer leftContainer = (VariableContainer) join.getLeftContainer();

		if (join.getRightContainer().getDataContainerType() != DataContainer.DataContainerType.Variable) {
			select.getLanguageExceptions()
					.add(LanguageExceptionBuilder.builder()
							.setMessageTemplate("A variable is expected on the right side of the JOIN.")
							.setContextInfo1(select.getId())
							.setLineNumber(select.getParserContext().getStart().getLine()).setColumnNumber(-1).build());
			return;
		}
		VariableContainer rightContainer = (VariableContainer) join.getRightContainer();

		if (leftContainer.getPerspective() == null || leftContainer.getPerspective().getAttributes().size() <= 0) {
			if (select.getDependsUpon() != null && select.getDependsUpon() instanceof SelectDescriptor) {
				leftContainer.setPerspective(((SelectDescriptor) select.getDependsUpon()).getProjectionClause()
						.getPerspective().createCanonicPerspective());
				leftContainer.getPerspective().setPerspectiveType(PerspectiveDescriptor.PerspectiveType.Implicit);
			} else {
				// error
			}
		}
		if (rightContainer.getPerspective() == null || rightContainer.getPerspective().getAttributes().size() <= 0) {
			if (select.getDependsUpon2() != null && select.getDependsUpon2() instanceof SelectDescriptor) {
				rightContainer.setPerspective(((SelectDescriptor) select.getDependsUpon2()).getProjectionClause()
						.getPerspective().createCanonicPerspective());
				rightContainer.getPerspective().setPerspectiveType(PerspectiveDescriptor.PerspectiveType.Implicit);
			} else {
				// error
			}
		}

		if (join.getLeftKey().getReturnType().equalsIgnoreCase(TypeSystem.TypeName.Unknown)) {
			// get the latest component of the name, as the identifier may be a
			// compound having R or L prefix
			String memberName = join.getLeftKey().getComponents().get(join.getLeftKey().getComponents().size() - 1)
					.toLowerCase();
			// String memberName =
			// join.getLeftKey().getId().replace(join.getLeftKey().getId(),
			// "l.");
			String dataType = leftContainer.getPerspective().getAttributes().get(memberName).getDataType();
			join.getLeftKey().setReturnType(dataType);
		}

		if (join.getRightKey().getReturnType().equalsIgnoreCase(TypeSystem.TypeName.Unknown)) {
			String memberName = join.getRightKey().getComponents().get(join.getRightKey().getComponents().size() - 1)
					.toLowerCase();
			// String memberName =
			// join.getRightKey().getId().replace(join.getRightKey().getId(),
			// "l.");
			String dataType = rightContainer.getPerspective().getAttributes().get(memberName).getDataType();
			join.getRightKey().setReturnType(dataType);
		}

		// create an implicit perspective for the whole select statement
		select.getProjectionClause()
				.setPerspective(PerspectiveDescriptor.combinePerspective(select.getProjectionClause().getPerspective(),
						leftContainer.getPerspective(), rightContainer.getPerspective(), "joined_" + select.getId()));
		select.getProjectionClause().setPresent(true);
		// filter, ordering, and grouping may face attribute rename issues
		// because of the combined attributes of the left and right.
		// they should be renamed accordingly
		// select.repair();
		select.validate();
		if (select.hasError())
			return;

		if (select instanceof JoinedSelectDescriptor) {
			builder.leftClassName(((JoinedSelectDescriptor) select).getLeftSide().getEntityType().getFullName());
			builder.rightClassName(((JoinedSelectDescriptor) select).getRightSide().getEntityType().getFullName());
		} else {
			builder.leftClassName(select.getDependsUpon().getEntityType().getFullName());
			builder.rightClassName(select.getDependsUpon2().getEntityType().getFullName());
		}
		builder.readerResourceName(helper.getReaderResourceName());
		builder.entityResourceName(helper.getJoinedEntityResourceName());
		Map<String, AttributeInfo> attributes = convertSelect
				.prepareAttributes(select.getProjectionClause().getPerspective(), this, false);
		builder.addResultAttributes(attributes);
		// builder.getAttributes().values().stream().forEach(at -> {
		// at.internalDataType = helper.getPhysicalType(at.conceptualDataType);
		// });
		try {
			if (isSupported("select.filter"))
				builder.where(convertSelect.prepareWhere(select.getFilterClause(), this), true);
			else
				builder.where("", false);
		} catch (Exception ex) {
			select.getLanguageExceptions()
					.add(LanguageExceptionBuilder.builder().setMessageTemplate(ex.getMessage())
							.setContextInfo1(select.getId())
							.setLineNumber(select.getParserContext().getStart().getLine()).setColumnNumber(-1).build());
		}

		// Map<AttributeInfo, String> orderItems = new LinkedHashMap<>();
		// for (Map.Entry<String, String> entry :
		// convertSelect.prepareOrdering(select.getOrderClause()).entrySet()) {
		// if(attributes.containsKey(entry.getKey())){
		// orderItems.put(attributes.get(entry.getKey()), entry.getValue());
		// }
		// }
		// builder.orderBy(orderItems);
		prepareOrderBy(builder, select);
		prepareLimit(builder, select);
		builder.writeResultsToFile(convertSelect.shouldResultBeWrittenIntoFile(select.getTargetClause()));

		builder.joinType(join.getJoinType().toString()).leftJoinKey(join.getLeftKey().getId())
				.rightJoinKey(join.getRightKey().getId())
				.joinOperator(runtimeJoinOperators.get(join.getJoinOperator()));
		if (join.getLeftKey().getReturnType().equalsIgnoreCase(TypeSystem.TypeName.String)
				|| join.getRightKey().getReturnType().equalsIgnoreCase(TypeSystem.TypeName.String)) {
			switch (join.getJoinOperator()) {
			case EQ:
			case EqString:
				builder.joinOperator(runtimeJoinOperators.get(JoinedContainer.JoinOperator.EqString));
				break;
			case NotEQ:
			case NotEqString:
				builder.joinOperator(runtimeJoinOperators.get(JoinedContainer.JoinOperator.NotEqString));
				break;
			// more options for LIKE etc, later. needs an stable solution!
			}
		}

		try {
			select.getExecutionInfo().setSources(builder.createSources());
		} catch (IOException ex) {
			select.getLanguageExceptions()
					.add(LanguageExceptionBuilder.builder().setMessageTemplate(ex.getMessage())
							.setContextInfo1(select.getId())
							.setLineNumber(select.getParserContext().getStart().getLine()).setColumnNumber(-1).build());
		}
	}

	private void prepareVariable(SelectDescriptor select) {
		try {
			// the statement should depend on another, because the source is a
			// variable!
			String sourceRowType = select.getEntityType().getFullName();
			if (sourceRowType.isEmpty())
				throw new Exception("No dependecy trace is found"); // is caught
																	// by the
																	// next
																	// catch
																	// block
			if (select.getDependsUpon() != null && select.getDependsUpon() instanceof SelectDescriptor) {
				builder.recordPerspective(
						((SelectDescriptor) select.getDependsUpon()).getProjectionClause().getPerspective());
				// this IF may cause some issues, take care or it!!!
				if (select.getProjectionClause().getPerspective()
						.getPerspectiveType() == PerspectiveDescriptor.PerspectiveType.Implicit
						|| select.getProjectionClause().getPerspective().getAttributes().size() <= 0) {
					// The current variable based statement is reading data from
					// another statement's output sx, so that sx was having a
					// physical schema
					// This is why the select has no attribute! in perspective
					// less statements, the schema is lazily extracted from the
					// data container, which causes the
					// depending statement remain attribute less!
					// the master statement's perspective is now available in
					// builder
					select.getProjectionClause()
							.setPerspective(builder.getRecordPerspective().createCanonicPerspective());
				}
			}

			Boolean hasAggregates = prepareAggregates(builder, select);
			if (hasAggregates) {
				builder.readerResourceName(helper.getAggregateReaderResourceName())
						.entityResourceName(helper.getEntityResourceName());
				builder.sourceRowType(sourceRowType);
				builder.addAggregates(aggregattionCallInfo);
				Map<String, AttributeInfo> rowEntityAttributeInfos = convertSelect
						.prepareAttributes(aggregatePerspective, this, false); // should
																				// not
																				// be
																				// needed
				// Map<String, AttributeInfo> rowEntityAttributeInfos =
				// convertSelect.prepareAttributes(builder.getRecordPerspective(),
				// this, false);
				// set the resultset perspective.
				attributeInfos = convertSelect.prepareAttributes(select.getProjectionClause().getPerspective(), this,
						false);
				for (AttributeInfo attInfo : attributeInfos.values()) {
					String aggAttributeName = attInfo.forwardMap
							.replaceAll(".* DONOTCHANGE.([^\\s]*).NOCALL\\s*\\(\\s*([^\\s]*)\\s*\\) .*", "$2").trim();
					AttributeInfo aggAttribute = rowEntityAttributeInfos.get(aggAttributeName);
					if (aggAttribute != null) {
						String newMap = convertSelect.translateExpression(aggAttribute.forwardMap,
								builder.getRecordPerspective(), "rowEntity");
						String pattern = "functions.get(\"$1\").move(" + newMap + ")";
						attInfo.forwardMap = attInfo.forwardMap
								.replaceAll("DONOTCHANGE.([^\\s]*).NOCALL\\s*\\(\\s*([^\\s]*)\\s*\\)", pattern);
					} else {
						String newMap = convertSelect.translateExpression(attInfo.forwardMap,
								builder.getRecordPerspective(), "rowEntity");
						attInfo.forwardMap = newMap;
					}
				}
				prepareGroupBy(builder, select);

				List<PerspectiveAttributeDescriptor> auxiliaryAttributes = select.getProjectionClause().getPerspective()
						.getAttributes().values().stream().filter(p -> p.isAuxiliary()).collect(Collectors.toList());
				// in the aggregate mode, all auxiliary attributes should be
				// taken out, so that they do not apear
				for (PerspectiveAttributeDescriptor p : auxiliaryAttributes) {
					if (attributeInfos.containsKey(p.getId())) {
						AttributeInfo tobeAddedToTheRowEntity = convertSelect
								.convert(select.getProjectionClause().getPerspective(), p, p.getId(), this);
						// new AttributeInfo(attributeInfos.get(p.getId()));
						rowEntityAttributeInfos.put(tobeAddedToTheRowEntity.name, tobeAddedToTheRowEntity);
						// tobeAddedToTheRowEntity.forwardMap = "rowEntity." +
						// tobeAddedToTheRowEntity.name; // pointing to a
						// veraible of same name in the row
						// entity//attInfo.forwardMap.replaceAll("DONOTCHANGE.([^\\s]*).NOCALL\\s*\\(\\s*([^\\s]*)\\s*\\)",
						// "functions.get(\"$1\").move(rowEntity.$2)");
						attributeInfos.remove(p.getId());
					}
				}

				builder.addRowAttributes(rowEntityAttributeInfos);
				builder.addResultAttributes(attributeInfos);

				// // check if there are groupby attributes, add them to the row
				// entity and replace the access method of the result entity
				// if(groupByAttributes != null && groupByAttributes.size() >
				// 0){ // the groupby attributes hsould be added to the row
				// entity to be used in the group constrcution keys
				// //replace the forward map of the resultentity to point to the
				// same attribute in the row entity
				// for(AttributeInfo attInfo: attributeInfos.values()){
				// if(groupByAttributes.stream().anyMatch(p->
				// p.name.equals(attInfo.name))){
				// //AttributeInfo tobeAddedToTheRowEntity = new
				// AttributeInfo(attInfo);
				// //rowEntityAttributeInfos.put(tobeAddedToTheRowEntity.name,
				// tobeAddedToTheRowEntity);
				// // the attinfo.fowardmap should be translated to something
				// like rowEntity.x+rowEntity.y/2 if the forwardmap is x+y/2
				// // this value should be computed on the rowEntity during
				// group key generation in getKey method of the
				// MemAggregateReader
				// String translated =
				// builder.enhanceExpression(attInfo.forwardMap, true, "p",
				// "rowEntity");
				// attInfo.forwardMap = translated; // pointing to a veraible of
				// same name in the row
				// entity//attInfo.forwardMap.replaceAll("DONOTCHANGE.([^\\s]*).NOCALL\\s*\\(\\s*([^\\s]*)\\s*\\)",
				// "functions.get(\"$1\").move(rowEntity.$2)");
				// }
				// }
				// }

			} else {
				Map<String, AttributeInfo> attributes = convertSelect
						.prepareAttributes(select.getProjectionClause().getPerspective(), this, false);
				builder.addResultAttributes(attributes);
				// transform the ordering clauses to their bound equivalent, in
				// each attribute names are linked to the attibutes objects
				builder.sourceRowType(sourceRowType).readerResourceName(helper.getReaderResourceName())
						.entityResourceName("");
				if (select.getProjectionClause().getPerspective()
						.getPerspectiveType() == PerspectiveDescriptor.PerspectiveType.Explicit
						|| select.getProjectionClause().getPerspective()
								.getPerspectiveType() == PerspectiveDescriptor.PerspectiveType.Inline) {
					builder.entityResourceName(helper.getEntityResourceName());
				}

			}
			if (select.getProjectionClause().getPerspective()
					.getPerspectiveType() == PerspectiveDescriptor.PerspectiveType.Explicit
					|| select.getProjectionClause().getPerspective()
							.getPerspectiveType() == PerspectiveDescriptor.PerspectiveType.Inline) {
				builder.sourceOfData("variable");
			}
			builder.where(convertSelect.translateExpression(convertSelect.prepareWhere(select.getFilterClause(), this),
					select.getProjectionClause().getPerspective(), "p"), false);
			builder.writeResultsToFile(convertSelect.shouldResultBeWrittenIntoFile(select.getTargetClause()));
			prepareOrderBy(builder, select);
			prepareLimit(builder, select);
			select.getExecutionInfo().setSources(builder.createSources());
		} catch (Exception ex) {
			// return a language exception
			select.getLanguageExceptions()
					.add(LanguageExceptionBuilder.builder()
							.setMessageTemplate(
									"The depenedent statement '%s' is found but no dependency information found! detail: "
											+ ex.getMessage())
							.setContextInfo1(select.getId())
							.setLineNumber(select.getParserContext().getStart().getLine()).setColumnNumber(-1).build());
		}
	}

	@Override
	public Map<ExpressionType, String> getExpressionPatterns() {
		// Does not need to do anything!
		return null;
	}

}
