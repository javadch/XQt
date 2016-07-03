/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.runtime;

import java.io.InputStream;
import java.util.List;
import xqt.engine.QueryEngine;
import xqt.engines.builtin.DefaultQueryEngine;
import xqt.lang.LanguageController;
import xqt.model.ProcessModel;

/**
 *
 * @author jfd
 */
public class RuntimeSystem {

    public RuntimeSystem(){
        // do nothing for now
    }
    
    // hides the engine selection and creation mechanism from the client
    // also hides the transformation of the process script into the process model
    public QueryEngine createQueryEngine(InputStream processScript, String configPaths, String processFoler, String applicationFolder, List<Exception> exceptions) {
        LanguageController controller = new LanguageController(configPaths);
        ProcessModel processModel = controller.createProcessModel(processScript, exceptions);
        QueryEngine engine = new DefaultQueryEngine(processModel, configPaths, processFoler, applicationFolder); 
        return engine;
    }
}
