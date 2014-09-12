/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.adapters;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
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

@XmlRootElement(name="Container")
public class AdapterInfoContainer {
    
    private static AdapterInfoContainer instance = null; // its a singleton object
    private AdapterInfoContainer(){
        
    }
    
    public static AdapterInfoContainer getInstance(){
        if(instance == null){
            instance = new AdapterInfoContainer();
            try {
                instance.loadRegisteredAdapterInfos();
            } catch (Exception ex) {
                Logger.getLogger(AdapterInfoContainer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return instance;
    }
    
    private List<AdapterInfo> registeredAdapterInfos = new ArrayList<>();

    public void setRegisteredAdapterInfos(List<AdapterInfo> registeredAdapterInfos) {
        this.registeredAdapterInfos = registeredAdapterInfos;
    }

    @XmlElement(name="RegisteredAdapter")
    public List<AdapterInfo> getRegisteredAdapterInfos() {
//        if(registeredAdapterInfos == null){
//            loadRegisteredAdapterInfos();
//        }
        return registeredAdapterInfos;
    }

    private void loadRegisteredAdapterInfos() throws Exception {
        // read the adapters info from the config/adapters.xml
        // the default location for adpater jars is the adapters folder but they can be anywhere
        // populate registeredAdapterInfos

        JAXBContext jc = JAXBContext.newInstance(AdapterInfoContainer.class);
        UnmarshallerHandler unmarshallerHandler = jc.createUnmarshaller().getUnmarshallerHandler();

        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();
        XMLReader xr = sp.getXMLReader();
        xr.setContentHandler(unmarshallerHandler);

        try (InputStream inputStream = new FileInputStream("config\\adapters.xml")) {
            InputSource inputSource = new InputSource(inputStream);
            xr.parse(inputSource);
        }

        instance = (AdapterInfoContainer)unmarshallerHandler.getResult();

//        Marshaller marshaller = jc.createMarshaller();
//        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//        marshaller.marshal(root, System.out);
    }
    
    public void writeConfig() {
        AdapterInfo a1 = new AdapterInfo("CSV", "D:/Projects/PhD/Src/XQt/xqt.adapters.csv/target/csv.adapter-1.0-SNAPSHOT.jar", "file", "CsvDataAdapter", "xqt.adapters.csv");
        instance.registeredAdapterInfos.add(a1);
        AdapterInfo a2 = new AdapterInfo("DBMS", "c:\\m2", "file", "DbmsAdapter", "x1.dbms");
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
