/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.adapters;

import com.vaiona.commons.logging.LoggerHelper;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.UnmarshallerHandler;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 *
 * @author standard
 */

@XmlRootElement(name="Adapters")
public class AdapterInfoContainer {
    
    private static AdapterInfoContainer instance = null; // its a singleton object
    private List<AdapterInfo> registeredAdapterInfos = new ArrayList<>();

    private AdapterInfoContainer(){
        
    }
    
    public static AdapterInfoContainer getInstance() throws Exception{
        if(instance == null){
            //instance = new AdapterInfoContainer();
            instance = AdapterInfoContainer.loadRegisteredAdapterInfos();
        }
        return instance;
    }
    

    public void setRegisteredAdapterInfos(List<AdapterInfo> registeredAdapterInfos) {
        this.registeredAdapterInfos = registeredAdapterInfos;
    }

    @XmlElement(name="Adapter")
    public List<AdapterInfo> getRegisteredAdaptersInfo() {
//        if(registeredAdapterInfos == null){
//            loadRegisteredAdapterInfos();
//        }
        return registeredAdapterInfos;
    }

    public AdapterInfo getDefultAdapter(){
         Optional<AdapterInfo> adapterInfo = registeredAdapterInfos.stream()
                            .filter(p->p.getIsFallback()).findFirst();// .getId().equalsIgnoreCase("Default")).findFirst();
         if(adapterInfo.isPresent())
             return adapterInfo.get();
         return null;
    }
    
    public AdapterInfo getAdapterInfo(String adapterId){
        Optional<AdapterInfo> adapterInfo = registeredAdapterInfos.stream()
                            .filter(p->p.getId().equalsIgnoreCase(adapterId)).findFirst(); 
        if(adapterInfo.isPresent())
             return adapterInfo.get();
        return null;
    }
    
    private static AdapterInfoContainer loadRegisteredAdapterInfos() throws Exception {
        // read the adapters info from the config/adapters.xml
        // the default location for adpater jars is the adapters folder but they can be anywhere
        // populate registeredAdapterInfos

        JAXBContext jc = JAXBContext.newInstance(AdapterInfoContainer.class);
        UnmarshallerHandler unmarshallerHandler = jc.createUnmarshaller().getUnmarshallerHandler();

        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();
        XMLReader xr = sp.getXMLReader();
        xr.setContentHandler(unmarshallerHandler);

        
        File f = new File("config/adapters.xml");
        
        LoggerHelper.logDebug("LoadingAdapterInfo" , MessageFormat.format("Loading file {0}", f.getAbsolutePath()));
        try (InputStream inputStream = new FileInputStream(f)/*("./config/adapters.xml")*/) {
            InputSource inputSource = new InputSource(inputStream);
            xr.parse(inputSource);
            LoggerHelper.logDebug(MessageFormat.format("The adapter config file {0} was successfully loaded.", f.getAbsolutePath()));
        } catch (Exception ex){
            LoggerHelper.logError(MessageFormat.format("The adapter config file {0} was NOT loaded.", f.getAbsolutePath()));
            throw ex;
        }

        return (AdapterInfoContainer)unmarshallerHandler.getResult();

//        Marshaller marshaller = jc.createMarshaller();
//        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//        marshaller.marshal(root, System.out);
    }
    
    public void writeConfig() {
        AdapterInfo a1 = new AdapterInfo("CSV", "D:/Projects/PhD/Src/XQt/xqt.adapters.csv/target/xqt.adapters.csv-1.0-SNAPSHOT.jar", "file", "CsvDataAdapter", "xqt.adapters.csv", false);
        instance.registeredAdapterInfos.add(a1);
        AdapterInfo a2 = new AdapterInfo("DBMS", "c:\\m2", "file", "DbmsAdapter", "x1.dbms", false);
        instance.registeredAdapterInfos.add(a2);

        try (OutputStream buffer = new BufferedOutputStream(new FileOutputStream("config\\adapters.xml"))) {
            JAXBContext ctx = JAXBContext.newInstance(AdapterInfoContainer.class);
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            File configDir = new File("config");
            if (!configDir.exists()){
                try{
                    configDir.mkdir();
                } catch(Exception ex){
                    Logger.getLogger(AdapterInfoContainer.class.getName()).log(Level.SEVERE, null, ex);
                }    
            }
            m.marshal(instance, buffer);
            buffer.flush();
        } catch (Exception ex){
            Logger.getLogger(AdapterInfoContainer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
