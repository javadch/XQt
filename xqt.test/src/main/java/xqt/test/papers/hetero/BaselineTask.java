package xqt.test.papers.hetero;

import java.sql.ResultSet;

import xqt.test.helpers.PostgreSqlHelper;

public class BaselineTask {
	private String connectionString = "";
	private String username = "";
	private String password = "";
	private String query = "";	
	
	public void run(){
		runCase(1);
		runCase(2);
		runCase(3);
		runCase(4);
		runCase(5);	
	}

	
	private static void runCase(int theCase) {
		BaselineTask baseline = new BaselineTask();
		// case 1: 
		baseline.measureQ1(theCase);
		baseline.measureQ2(theCase);
		baseline.measureQ3(theCase);
		baseline.measureQAll(theCase);
	}
	
	public void measureQ1(int measurmentCase){
		switch (measurmentCase) {
		case 1: 		// selectivity 0
			query = ""; // change the where clause
			break;
		case 2:			// selectivity 10
			query = ""; // change the where clause
			break;
		case 3:			// selectivity 50
			query = ""; // change the where clause
			break;
		case 4:			// selectivity 90
			query = ""; // change the where clause
			break;
		case 5:			// selectivity 100
			query = ""; // change the where clause
			break;
		default: 			// maybe interpreted as selectivity 100?
			break;
		}
		instrumentedCode();
		
	}

	public void measureQ2(int measurmentCase){
		
	}

	public void measureQ3(int measurmentCase){
		
	}

	public void measureQAll(int measurmentCase){
		
	}
	
	private void instrumentedCode(){
		//measure the metrics, get a logger, log the measurements in the logger in a CSV compatible format
		// MeasuringDate, MeasuringTime, TaskName (Baseline), Scenario (Q1), Case (selectivity, etc), ElapsedTime (nano seconds), SizeIndicator(affectedRows)
		
		PostgreSqlHelper helper = new PostgreSqlHelper();
		long startTime = System.nanoTime();
		
		/////////////////////////////The observed code segment//////////////////////////
		ResultSet result = helper.execute(connectionString, username, password, query);
		long consumed = helper.consumeResultSet(result);
		////////////////////////////////////////////////////////////////////////////////
		
		long stopTime = System.nanoTime();
		long elapsed = stopTime - startTime;
	}
}
