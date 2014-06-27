/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.runtime;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import xqt.engine.QueryEngine;
import xqt.engine.builtin.DefaultQueryEngine;
import xqt.lang.LanguageController;
import xqt.model.ProcessModel;
import xqt.model.exceptions.LanguageException;

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
    public QueryEngine createQueryEngine(InputStream processScript, List<Exception> exceptions) {
        LanguageController controller = new LanguageController();
        ProcessModel processModel = controller.createProcessModel(processScript, exceptions);
        QueryEngine engine = new DefaultQueryEngine(processModel); 
        return engine;
    }
}
