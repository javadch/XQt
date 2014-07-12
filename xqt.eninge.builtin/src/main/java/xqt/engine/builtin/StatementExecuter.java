/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.engine.builtin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import xqt.adapters.builtin.DefaultDataAdapter;
import xqt.engine.QueryEngine;
import xqt.model.DataContainerDescriptor;
import xqt.model.adapters.DataAdapter;
import xqt.model.data.Resultset;
import xqt.model.data.Variable;
import xqt.model.execution.ExecutionInfo;
import xqt.model.statements.StatementVisitor;
import xqt.model.statements.query.SelectDescriptor;

/**
 *
 * @author standard
 */
public class StatementExecuter implements StatementVisitor{
    private QueryEngine engine;
    private Map<String, Variable> memory;
    public StatementExecuter(QueryEngine engine){
        this.engine = engine;
    }

    StatementExecuter(DefaultQueryEngine engine, Map<String, Variable> memory) {
        this.engine = engine;
        this.memory = memory;
    }
    // keeps connections, binding and adapters alive for later use!
    @Override
    public Resultset visit(SelectDescriptor select) {
        // get the adapter object
        // call its run statement with the statement object, currently there is no capability matching
        // get the result set back and assign it to the variable named in the atrget caluse
        // put the result set in the engine's memory
        // return a pointer to the variable!
        //this.engine.setVariableValue("test", new Object());
        
        // check the source clause to see where data is supposed to come from, if its a container, then pass the exeution to the adapater
        // otherwise it should be a variable. if so pass the statement to the memory adapter.
        // check for the adapter type, name and etc. if the statement is accessing a variable, use the defualt MemoryAdapter
        //String adapterId = selectStatement.getSourceClause().getBinding().getConnection().getAdapterName();
        
        DataAdapter adapter = select.getExecutionInfo().getAdapter();
        Resultset result;
        if(adapter.needsMemory()){
            result = adapter.run(select, memory);
        } else {
            result = adapter.run(select, null);
        }
        return result;
    }

    @Override
    public void prepare(SelectDescriptor select) {
        ExecutionInfo eix = new ExecutionInfo();
        select.setExecutionInfo(eix);
        eix.setExecuted(false);
        DataAdapter adapter = chooseAdapter(select); // create the adapter based on its registration info and the statement's bindinf info
        eix.setAdapter(adapter);
        adapter.prepare(select); // creates the source files but does not compile them           
    }
    
    private static HashMap<String, DataAdapter> loadedAdapters = new HashMap<>();
    
    private DataAdapter chooseAdapter(SelectDescriptor selectStatement) {
        if(selectStatement.getSourceClause().getDataContainerType() == DataContainerDescriptor.DataContainerType.Variable){
            if(!loadedAdapters.containsKey("Default")) {
                DataAdapter adapter = new DefaultDataAdapter();
                loadedAdapters.put("Default", adapter);
            }
            return loadedAdapters.get("Default");
        }
        try {
            if(!loadedAdapters.containsKey("CSV")) {// read the key from the select statement's associated connection
                // read the adapter info from the adapters config file in the applications installation folder
                String adapterJar = "file:D:/Projects/PhD/Src/XQt/xqt.adapters.csv/target/CsvAdapter-1.0-SNAPSHOT.jar";
                String adapterClass = "xqt.adapters.csv.CsvDataAdapter";
                ClassLoader classLoader = new URLClassLoader(new URL[]{new URL(adapterJar)});
                Class cl = classLoader.loadClass(adapterClass);
                Constructor<?> ctor = cl.getConstructor();
                ctor.setAccessible(true);
                DataAdapter adapter = (DataAdapter)ctor.newInstance();
                loadedAdapters.put("CSV", adapter);
                return adapter;
            }
            return loadedAdapters.get("CSV");
        }
        catch (MalformedURLException | ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            //add a language exception to the model/ select
            Logger.getLogger(StatementExecuter.class.getName()).log(Level.SEVERE, null, ex);
        }  
        return null;
    }    


}
