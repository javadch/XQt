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
import java.util.logging.Level;
import java.util.logging.Logger;
import xqt.engine.QueryEngine;
import xqt.model.adapters.DataAdapter;
import xqt.model.data.Resultset;
import xqt.model.statements.StatementVisitor;
import xqt.model.statements.query.SelectDescriptor;

/**
 *
 * @author standard
 */
public class StatementExecuter implements StatementVisitor{
    private final QueryEngine engine;
    public StatementExecuter(QueryEngine engine){
        this.engine = engine;
    }
    // keeps connections, binding and adapters alive for later use!
    @Override
    public Resultset visit(SelectDescriptor selectStatement) {
        // create of get the adapter object
        // call its run statement with the statement object, currently there is no capability matching
        // get the result set back and assign it to the variable named in the atrget caluse
        // put the result set in the engine's memory
        // return a pointer to the variable!
        //this.engine.setVariableValue("test", new Object());
        
        // check for the adapter type, name and etc. if the statement is accessing a variable, use the defualt MemoryAdapter
        String adapterId = selectStatement.getSourceClause().getBinding().getConnection().getAdapterName();
        DataAdapter adapter = getAdapter(adapterId); // create the adapter based on its registration info and the statement's bindinf info
        Resultset result = adapter.run(selectStatement);
        return result;
    }

    private DataAdapter getAdapter(String adapterId) {
        try {
            DataAdapter adapter = null;
            //adapter = new CsvDataAdapter();
            if(adapter == null){
                // read the adapter info from the adapters config file in the applications installation folder
                String adapterJar = "file:D:/javad/Projects/XQtProjects/XQt/xqt.adapters.csv/target/CsvAdapter-1.0-SNAPSHOT.jar";
                String adapterClass = "xqt.adapters.csv.CsvDataAdapter";
                ClassLoader classLoader = new URLClassLoader(new URL[]{new URL(adapterJar)});
                Class cl = classLoader.loadClass(adapterClass);
                Constructor<?> ctor = cl.getConstructor();
                ctor.setAccessible(true);
                adapter = (DataAdapter)ctor.newInstance();
            }
            return adapter;
        }
        catch (MalformedURLException | ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(StatementExecuter.class.getName()).log(Level.SEVERE, null, ex);
        }  
        return null;
    }    
}
