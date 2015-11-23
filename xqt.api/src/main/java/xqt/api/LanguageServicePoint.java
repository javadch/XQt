/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.api;

import com.vaiona.commons.io.FileHelper;
import com.vaiona.commons.io.MarkableFileInputStream;
import com.vaiona.commons.lang.Environment;
import com.vaiona.commons.logging.LoggerHelper;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JPanel;
import xqt.engine.QueryEngine;
import xqt.model.adapters.AdapterInfoContainer;
import xqt.model.data.Variable;
import xqt.model.functions.FunctionInfoContainer;
import xqt.model.statements.StatementDescriptor;
import xqt.runtime.RuntimeSystem;

/**
 *
 * @author Javad Chamanara
 */
public class LanguageServicePoint {
    //private final String processScript;
    private InputStream inputStream;
    private RuntimeSystem runtime = null;
    private QueryEngine engine;
    private StringBuilder processScript = new StringBuilder();
    protected List<Exception> exceptions = new ArrayList<>();
    protected ClassLoader classLoader = null;
    private String configFolders = ".";
    public LanguageServicePoint(String configFolders) throws Exception{
        // some of the functions in the default adapter, are  using jide to draw plot resultsets! those calls make license check mandatory!        
        try{
            com.jidesoft.utils.Lm.verifyLicense("Friedrich Schiller University of Jena", "SciQuest", "iBVmHbKikKMgQhcRthIhOwcUROnqer3");
        } catch (Exception ex){
            throw new Exception("Invalid lisence!");
        }
        check(configFolders);
        config();
        this.configFolders = configFolders;
        LoggerHelper.logDebug(MessageFormat.format("The system API is initiated using config folders: {0}.", configFolders));
        LoggerHelper.logDebug(MessageFormat.format("The system API is initiated at the root folder: {0}.", Paths.get(".").toFile().getAbsolutePath()));
    }

    private void config() {
    	// config the Java runtime properties such as max heap size, etc.
    	
	}

	// it is to keep the ctors clean
    private void init(InputStream processScript) throws Exception{
        //prepare the parser/ annotator and create the DST use the runtime system for all the functions, 
        // The API is just a facade over the runtime
        // every statement should have an ID
        // create dependencies of each element to the others especially statements.
        //dstNode.getDependsUponElements(ElementType.Statement ...)
        
        // Load the function specifications from the packs
        LoggerHelper.logDebug(MessageFormat.format("Loading function specifications...", 1));
        loadFunctionSpecifications();
        LoggerHelper.logDebug(MessageFormat.format("Function specifications loaded", 1));
        // Load the jars of the adapters. maybe it can be deferred to the time they actually requested!
        classLoader = this.getClass().getClassLoader();
        
        //this.inputStream = processScript;
        runtime = new RuntimeSystem();
        try{
            engine = runtime.createQueryEngine(processScript, configFolders, exceptions); // also static method should work   
            engine.setClassLoader(classLoader);
            LoggerHelper.logDebug(MessageFormat.format("Query engine is built and ready to be used.", 1));

        }
        catch (Exception ex) {
            Exception exx = new Exception("Could not prepare the query engine! Likely there are some errors in the process syntax.", ex);
            this.exceptions.add(exx);
            LoggerHelper.logDebug(exx.getMessage());
        }       
    }
    
    public List<Exception> getExceptions() {
        return exceptions;
    }

    public RuntimeSystem getRuntime() {
        return runtime;
    }

    public QueryEngine getEngine() {
        return engine;
    }

    
    // this removes whatever in the inputstream
    public String addScript(String statement){
        inputStream = null;
        if(statement != null && !statement.isEmpty())
            processScript.append(statement).append(System.getProperty("line.separator"));
        return statement;
    }

    public String getScript(){
        if(processScript == null || processScript.length() <=0){
            StringBuilder sb=new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            
            String read;
            try {
                read = br.readLine();
                while(read != null) {
                    sb.append(read); sb.append(System.getProperty("line.separator"));
                    read =br.readLine();
                }
                if(inputStream.markSupported()) // if not, subsequent get or process calls may fail
                    inputStream.reset();
                return sb.toString();
            } catch (IOException ex) {
                return ex.getMessage();
            }
        } else {
            return processScript.toString();
        }
    }
 
    public void registerScript(InputStream script){        
        processScript = new StringBuilder();
        if(script != null)
            inputStream = script;
    }

    public String registerScript(String fileName){        
        processScript = new StringBuilder();
        String filePath = fileName;
        try {
            filePath = FileHelper.makeAbsolute(fileName);
            inputStream = new MarkableFileInputStream(new FileInputStream(filePath));
        } catch (IOException ex) { // FileNotFoundException
            this.exceptions.add(ex);
            return ex.getMessage();
        }
        return MessageFormat.format("OK. Process file {0} was registered. All previous statements were cleared. {1}", filePath, System.getProperty("line.separator"));
    }

    public String process(){
        LoggerHelper.logDebug(MessageFormat.format("Initilizing the execution engine...", 1));
        try{
            if(inputStream!= null){
                init(inputStream);
            } else {
                InputStream stream = new ByteArrayInputStream(processScript.toString().getBytes("UTF-8"));
                init(stream);
            }
        } catch(Exception ex){
            this.exceptions.add(ex);
            LoggerHelper.logError(MessageFormat.format("Could not read the input stream: {0}", ex.getMessage()));
            return "Could not read the input stream: " + ex.getMessage();
        }
        
        // process all the statements and store the results, but do not return anything
        if(exceptions == null || exceptions.size() <=0){
            LoggerHelper.logDebug(MessageFormat.format("execution of the process is started.", 1));
            // Suppress garbage collection
            //System.gc();
            engine.execute();
            // Reset garbage collection
            LoggerHelper.logDebug(MessageFormat.format("execution of the process is finished.", 1));
        }
  
        String errors = getErrors();
        if(errors != null && !errors.isEmpty()){
            LoggerHelper.logError(errors);
            return errors;
        } else {
            LoggerHelper.logDebug("Execution finished successfully.");
            return "OK";
        }
    }
    
    public Object process(Integer statementId, Boolean forceExecution, Boolean executeIfNeeded){
        // Id is the pointer to the statement, find and process it, then store the result
        // if it is already executed, just return the result set
        // if there is a dependency on some previous statements, process them also (recursive: they may have dependencies too)
        // after the execution of the statement, invalidate resultset that are dependant upon this one
        // also return the result set
        // needs init to be done first, so this method should be called after the process method is called
        if(forceExecution)
        {
            Object result = engine.execute(statementId);
            return result;
        }
        StatementDescriptor sd = getStatementDescriptor(statementId);
        if(sd.hasExecutionInfo() && sd.getExecutionInfo().getVariable() != null)
            return sd.getExecutionInfo().getVariable().getResult();
        else if(executeIfNeeded)
        {
            Object result = engine.execute(statementId);
            return result;
        }
        return null;
    }
    
    public Object getVariablesInfo(){
    	try{
	        List<String> varNames = engine.getProcessModel().getStatements().values().stream()
	                                    .filter(p-> p.hasExecutionInfo() && p.getExecutionInfo().hasVariable())
	                                    .map(p->p.getExecutionInfo().getVariable().getName()).collect(Collectors.toList());
	        if(varNames == null || varNames.size() <= 0)
	        	return null;
	        String[] names = new String[varNames.size()];
	        int index =0;
	        for(String name : varNames){
	            names[index++] = name;
	        }
	        return names;
    	} catch (Exception ex){
    		return null;
    	}
    }
    
    public Object getVariable(String variableName){
        Optional<StatementDescriptor> stmt = engine.getProcessModel().getStatements().values().stream()
                .filter(p-> p.hasExecutionInfo() 
                        && p.getExecutionInfo().hasVariable() 
                        && p.getExecutionInfo().getVariable().getName().equalsIgnoreCase(variableName)
                ).findFirst();
        if(stmt.isPresent()){
            Variable variable = stmt.get().getExecutionInfo().getVariable();
            switch(variable.getResult().getResultsetType()){
                case Tabular:
                	long start = System.nanoTime();
                    Object obj = variable.getResultAsArray();                    
                    LoggerHelper.logInfo(MessageFormat.format("Converting variable {0} to a column based array took {1} milliseconds.", variableName, (System.nanoTime() - start)/1000000 ));
                    return obj;
                case Image:
                    return createImage((JPanel)variable.getResult().getData());
                default:
                    break;
            }
        }
        return null;
    }
    
    public Object getVariableSchema(String variableName){
        if(variableName.equals("Diana")){
            return new String[] { "v1", "v2", "v3" };
        }
        
        Optional<StatementDescriptor> stmt = engine.getProcessModel().getStatements().values().stream()
                .filter(p-> p.hasExecutionInfo() 
                        && p.getExecutionInfo().hasVariable() 
                        && p.getExecutionInfo().getVariable().getName().equalsIgnoreCase(variableName)
                ).findFirst();
        if(stmt.isPresent()){
            Variable variable = stmt.get().getExecutionInfo().getVariable();
            switch(variable.getResult().getResultsetType()){
                case Tabular:
                    String[] names = new String[variable.getResult().getSchema().size()];
                    int index =0;
                    for(String name :variable.getResult().getSchema().stream().map(p->p.getName()).collect(Collectors.toList())){
                        names[index++] = name;
                    }
                    return names;
                case Image:
                    break;
                default:
                    break;
            }
        }
        return null;
    }
    
    public Object getPerspectiveFor(String variableName){
        Optional<StatementDescriptor> stmt = engine.getProcessModel().getStatements().values().stream()
                .filter(p-> p.hasExecutionInfo() 
                        && p.getExecutionInfo().hasVariable() 
                        && p.getExecutionInfo().getVariable().getName().equalsIgnoreCase(variableName)
                ).findFirst();
        if(stmt.isPresent()){
            Variable variable = stmt.get().getExecutionInfo().getVariable();
            switch(variable.getResult().getResultsetType()){
                case Tabular:
                    return variable.getPerspectiveAsArray();
                case Image:
                    return null;
                default:
                    break;
            }
        }
        return null;
    }    
  
    // https://tips4java.wordpress.com/2008/10/13/screen-image/
    // http://stackoverflow.com/questions/1349220/convert-jpanel-to-image
    private BufferedImage createImage(JPanel panel) {
        int w = panel.getWidth();
        int h = panel.getHeight();
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        panel.printAll(g);
        return bi;
    }
    
    private StatementDescriptor getStatementDescriptor(int id){
        StatementDescriptor statement = engine.getProcessModel().getStatement(id);
        return statement;
    }
    
    public boolean hasError(){
        // go through the languageExceptions and also ask all the elements: declarations, statements, configurations etc
        if(this.exceptions.stream().count() > 0)
            return true;
        return false;
    }

    FunctionInfoContainer functionContainer = null;
    private void loadFunctionSpecifications() throws Exception {
        // use the function specification bean, read the function pack folder, list the packages, read them all, add them to the function list.
        functionContainer = FunctionInfoContainer.getDefaultInstance(configFolders);
    }
    
    public Object getAdapterNames(){
        try{
            AdapterInfoContainer instance = AdapterInfoContainer.getInstance(configFolders);
            return instance.getAdapterNames();
        } catch(Exception ex){
            String[] problem = new String[1];
            problem[0] = ex.getMessage();
            return problem;
        }
    }

    public String getErrors(){
        StringBuilder errors = new StringBuilder();
        int errorCount = 0;
        if(hasError()){ // lexical, syntax and ... error
            errors.append("**************************************************************************************\n");
            errors.append("************************************ Lexical Errors ***********************************\n");
            errors.append("**************************************************************************************\n");
            for(Exception p : getExceptions()){
                errors.append("Error " + ++errorCount + " : " + p.getMessage()+ "\n");
            }
        } 
        if(getEngine() != null && getEngine().getProcessModel() != null) {
            if(getEngine().getProcessModel().hasError()){ // semantic errors
                errors.append("**************************************************************************************\n");
                errors.append("******************************* Synatx and Semantic Errors *******************************\n");
                errors.append("**************************************************************************************\n");
                for(Exception p : getExceptions()){
                    errors.append("Error " + ++errorCount + " : " + p.getMessage()+ "\n");
                }
            }             
            errors.append("**************************************************************************************\n");
            errors.append("****************************** Statement Execution Results ********************************\n");
            errors.append("**************************************************************************************\n");
            getEngine().getProcessModel().getStatements().values().stream().forEachOrdered((s) -> {
                if(s.hasExecutionInfo()){
                    if(!s.getExecutionInfo().isExecuted()){
                        errors.append("Statement " + s.getId() + " was NOT executed.\n");
                        s.getLanguageExceptions().stream().forEach((exx) -> {
                            errors.append(exx.getMessage() + "\n");
                        });
                    } else if(s.hasResult()){
                        Variable v = s.getExecutionInfo().getVariable();
                        switch (v.getResult().getResultsetType()){
                            case Tabular:{
                                errors.append("Statement " + s.getId() + " was executed. Its result is in the variable: '" + v.getName() + "' and contains " + v.getResult().getTabularData().size() + " records.\n");                                     
                                break;
                            }
                            case Image: {
                                errors.append("Statement " + s.getId() + " was executed.  Its result is in the variable: '" + v.getName() + "'.\n"); 
                                break;
                            }
                        }
                    } else {
                        errors.append("Statement " + s.getId() + " was executed but returned no result.\n");
                    }                      
                } else {
                    errors.append("Statement " + s.getId() + " was NOT executed.\n");
                }
            });
        }  
        return errors.toString();
    }
    
    public void check(String configPaths) throws Exception {
        LoggerHelper.logDebug(MessageFormat.format("Checking whether all prerequisites are met", 0));
        LoggerHelper.logDebug(MessageFormat.format("Checking config folder...", 0));
        String configFolder = FileHelper.getConfigPath(configPaths);
        if(configFolder == null || configFolder.isEmpty()){
            String msg = MessageFormat.format("Config folder was not found neither in the root nor in any of these locations: {0}.", configPaths);
            LoggerHelper.logError(msg);
            throw new Exception(msg);
        } else {
            String msg = MessageFormat.format("Config folder was found either in the root or in one of these locations: {0}.", configPaths);
            LoggerHelper.logDebug(msg);
        }
        
        LoggerHelper.logDebug(MessageFormat.format("Checking whetehr the JDK 8 is available", 0));
        Environment.getJDK8Folder(); // if not found throws a proper exception
        
    }
}
