package xqt.api;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import xqt.model.data.Variable;
import xqt.model.statements.StatementDescriptor;

/*
Provides a multi-threaded environment for the process execution.
In the UI, it is possible for the user to have more than one processes (code editors) open.
Running one or more of the processes may take a long time. During the execution of one of running processes
the user may want to go to other processes to edit, author or look at their result sets. For the UI to be responsive in this situations
the LanguageServiceTask class is designed. the base class also contains some methods to show a sort of progress in term of events. so that it is possible to show a progress bar if needed.
*/
public class LanguageServiceTask extends SwingWorker<LanguageServicePoint, String>{
	List<ProcessExecutionListener> listeners = new ArrayList<ProcessExecutionListener>();
    double elapsedTime;
    private StringBuilder report = new StringBuilder();
    private LanguageServicePoint lsp = null;
    private String processFile = "";
    
    public StringBuilder getReport() {
		return report;
	}

	//public void setReport(StringBuilder value) {
	//	this.report = value;
	//}

    public String getProcessFilePath(){
    	return processFile;
    }
    
	public LanguageServiceTask(String processScript, ProcessExecutionListener listener){
        try {
        	lsp = new LanguageServicePoint(".", listener.getProcessPath());
        	lsp.addScript(processScript);
        	processFile = "";
    	} catch (Exception ex) { // execution errors
            report.append("Program execution was interrupted. " + ex.getMessage() + "\n");
        }
        listeners.add(listener);
    }
    
	public LanguageServiceTask(File file, ProcessExecutionListener listener){
        try {
        	lsp = new LanguageServicePoint(".");
        	lsp.registerScript(file.getAbsolutePath());
        	processFile = file.getAbsolutePath();
    	} catch (Exception ex) { // execution errors
            report.append("Program execution was interrupted. " + ex.getMessage() + "\n");
        }
        listeners.add(listener);
    }
	
	private void executing(){
		listeners.forEach(p->p.executing());
	}

	private void executed(StringBuilder report){
		listeners.forEach(p->p.executed(report));
	}
	
	private void present(Variable v){
		listeners.forEach(p->p.present(v));
	}
	
	private void draw(Variable v){
		listeners.forEach(p->p.draw(v));
	}
	
	// the execute function is divided into to phases to be used in both threaded and non threaded cases.
	// the threaded case is designed for UI fore/background worker threading.
	public LanguageServicePoint executePhase1(){
		report.append(MessageFormat.format("execution started at {0}.\n", new Date().toString()));
        executing();
        try {
            if(!lsp.hasError()){
                long start = System.nanoTime();
                String ret = lsp.process();
                long end = System.nanoTime();
                elapsedTime = (double)(end - start) / 1000000000f;
                errorCount = 0;
            }
        } catch (Exception ex) { // execution errors
            report.append("Program execution was interrupted. " + ex.getMessage() + "\n");
        } 
        return lsp;
	}
	
	public void executePhase2(LanguageServicePoint lsp){
        // see whether the process model contains any exception, if so throw an InputMismatchException
        // to signal the callers to go through the process model and handle the actual exceptions.        
        if(lsp != null && lsp.hasError()){ // lexical, syntax and ... error
        	report.append("**************************************************************************************\n");
        	report.append("************************************ Lexical Errors ***********************************\n");
        	report.append("**************************************************************************************\n");
            lsp.getExceptions().forEach(p-> {                    
            	report.append("Error " + ++errorCount + " : " + p.getMessage()+ "\n");  
            }  
            );
        } 
        if(lsp!=null && lsp.getEngine() != null && lsp.getEngine().getProcessModel() != null) {
            if(lsp.getEngine().getProcessModel().hasError()){ // semantic errors
            	report.append("**************************************************************************************\n");
            	report.append("******************************* Synatx and Semantic Errors *******************************\n");
            	report.append("**************************************************************************************\n");
                lsp.getEngine().getProcessModel().getEffectiveErrors().forEach(p->
                    {report.append("Error " + ++errorCount + " : " + p.getMessage()+ "\n");  }  
                );
            }             
            report.append("**************************************************************************************\n");
            report.append("****************************** Statement Execution Results ********************************\n");
            report.append("**************************************************************************************\n");
            //lsp.getEngine().getProcessModel().getStatements().values().stream().forEachOrdered((stmt) -> {
            for(StatementDescriptor stmt: lsp.getEngine().getProcessModel().getStatements().values()){
                try{
                    if(stmt.hasExecutionInfo()){
                        if(!stmt.getExecutionInfo().isExecuted()){
                        	report.append("Statement " + stmt.getId() + " was NOT executed.\n");
                        } else if(stmt.hasResult()){
                            Variable v = stmt.getExecutionInfo().getVariable();
                            double xTime = (double) stmt.getExecutionInfo().getExecutionTime() / 1000000000f;
                            switch (v.getResult().getResultsetType()){
                                case Tabular:{
                                	report.append("Statement " + stmt.getId() + " was executed. Its result is in variable: '" 
                                			+ v.getName() + "' and contains " + v.getResult().getTabularData().size() 
                                			+ " records. The statement took " + xTime + " seconds to be executed."+ "\n");
                                    present(v);//addTabularTab(v); 
                                    break;
                                }
                                case Image: {
                                	report.append("Statement " + stmt.getId() + " was executed.  Its result is visualized in: '" + v.getName() + "'." 
                                			+ "The statement took " + xTime + " seconds to be executed."+ "\n");
                                    draw(v);//addChartTab(v); 
                                    break;
                                }
                            }
                        } else {
                        	report.append("Statement " + stmt.getId() + " was executed but returned no result.\n");
                        }                      
                    } else {
                    	report.append("Statement " + stmt.getId() + " was NOT executed.\n");
                    }
                } catch (Exception ex){
                	report.append("Not able to present the resultset of statement " + stmt.getId() + ". The internal error is: " + ex.getMessage() + "\n");
                }
            }//);
        }    
	    report.append("**************************************************************************************\n");
	    report.append("******************************** Process Execution Time **********************************\n");
	    report.append("**************************************************************************************\n");
	    report.append("The execution finished in " + elapsedTime + " seconds\n");  
	    executed(report);
	}
	
	public void executeSync(){
		executePhase2(executePhase1());
	}
	
	/*
    Runs the process execution task in the background using a separate thread.
    */
    int errorCount = 0;
    @Override
    protected LanguageServicePoint doInBackground()  {
        return executePhase1();
    }
    
    /*
    Gets executed when the execution of the process is finished. the result is accessible by calling the get() method.
    When called, it created proper UI elements like Tables, graphs an so on and updated the UI.
    */
    @Override
    protected void done(){
        //LanguageServicePoint lsp = null; 
        try {
            lsp = get(); // check what happens if doInBackground throws an exception. answer: the lsp does not throw any exception, instead it collects and returns an exception list.
        } catch (InterruptedException | ExecutionException ex) { // execution errors
            report.append("Program execution was interrupted. " + ex.getMessage() + "\n");
        } 
        executePhase2(lsp);
	}
}
