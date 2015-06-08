/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.adapters;

import com.vaiona.commons.compilation.ObjectCreator;
import com.vaiona.commons.logging.LoggerHelper;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import xqt.model.functions.FunctionInfoContainer;

/**
 *
 * @author standard
 */
@XmlRootElement(name="Adapter")
public class AdapterInfo {

    private String id;
    private String location;
    private String locationType;
    private String mainClassName;
    private String mainNamespace;
    private Boolean isFallback;
    
    public AdapterInfo(){
        
    }

    public AdapterInfo(String id, String location, String locationType, String mainClassName, String mainNamespace, boolean isFallback){
        this.id = id;
        this.location = location;
        this.locationType = locationType;
        this.mainClassName = mainClassName;
        this.mainNamespace = mainNamespace;
        this.isFallback = isFallback;
    }
    
    @XmlElement(name="IsFallback", required = false)
    public Boolean getIsFallback() {
        return isFallback;
    }

    public void setIsFallback(Boolean isFallback) {
        this.isFallback = isFallback;
    }

    @XmlElement(name="Id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlElement(name="Location")
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @XmlElement(name="LocationType")
    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    @XmlElement(name="ClassName")
    public String getMainClassName() {
        return mainClassName;
    }

    public void setMainClassName(String mainClassName) {
        this.mainClassName = mainClassName;
    }

    @XmlElement(name="Namespace")
    //@XmlAttribute
    public String getMainNamespace() {
        return mainNamespace;
    }

    public void setMainNamespace(String mainNamespace) {
        this.mainNamespace = mainNamespace;
    }
      
    public FunctionInfoContainer getFunctionInfoContainer(){
        return FunctionInfoContainer.getInstance(id);
    }

    public DataAdapter load(String dialect, ClassLoader parentLoader) throws Exception  {
        String absulotePath = location;
        try{            
            if(location.startsWith("/")){ // the path is relative
                Path relative = Paths.get("config", "adapters", this.id.toLowerCase(), location.substring(1));
                Path absulote =relative.toAbsolutePath();
                absulotePath = absulote.toString();
            }
            //LoggerHelper.logDebug("Looking up adapater: " + id + " to load.");
            LoggerHelper.logDebug(MessageFormat.format("The exeution engine is loading adapter: {0} from {1}:{2}.", id, locationType, absulotePath));
            ClassLoader classLoader = ObjectCreator.getURLClassLoader(locationType + ":" + absulotePath, parentLoader);
            LoggerHelper.logDebug(MessageFormat.format("Class loader is instantiated for adapter: {0} from {1}:{2}.", id, locationType, absulotePath));
            Class claz = ObjectCreator.getClass(mainNamespace + "." + mainClassName, classLoader);
            LoggerHelper.logDebug(MessageFormat.format("Adapter class definition is loaded for adapter: {0} from {1}:{2}.", id, locationType, absulotePath));
            DataAdapter adapter = null;
            adapter = (DataAdapter)ObjectCreator.createInstance(claz);
            LoggerHelper.logDebug(MessageFormat.format("Adapter class is instantiated for adapter: {0} from {1}:{2}.", id, locationType, absulotePath));
            if(adapter != null)
                adapter.setDialect(dialect);
            LoggerHelper.logDebug(MessageFormat.format("The {0} adapter located at {1}:{2} was successfully loaded.", id, locationType, absulotePath));
            return adapter;
        } catch(Exception ex){
            LoggerHelper.logError(MessageFormat.format("The {0} adapter located at {1}:{2} was NOT loaded. Cauase: {3}", id, locationType, absulotePath, ex.getMessage()));
            throw ex;
            //return null;
        }
    }
}
