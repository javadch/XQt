/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.functions;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
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

@XmlRootElement(name="Functions")
public class FunctionInfoContainer {

    private static FunctionInfoContainer instance = null; // its a singleton object
    private List<FunctionInfo> registeredFunctions = new ArrayList<>();
    
    private FunctionInfoContainer(){
        
    }
    
    public static FunctionInfoContainer getInstance(){
        if(instance == null){
            try {
                //writeConfig();
                instance = FunctionInfoContainer.loadRegisteredFunctions();
            } catch (Exception ex) {
                Logger.getLogger(FunctionInfoContainer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return instance;
    }

    @XmlElement(name="Function")
    public List<FunctionInfo> getRegisteredFunctions() {
        return registeredFunctions;
    }

    public void setRegisteredFunctions(List<FunctionInfo> registeredFunctions) {
        this.registeredFunctions = registeredFunctions;
    }

    private static FunctionInfoContainer loadRegisteredFunctions() throws Exception {
        JAXBContext jc = JAXBContext.newInstance(FunctionInfoContainer.class);
        UnmarshallerHandler unmarshallerHandler = jc.createUnmarshaller().getUnmarshallerHandler();

        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();
        XMLReader xr = sp.getXMLReader();
        xr.setContentHandler(unmarshallerHandler);

        // open and read all the function pack files in the config\\functionpacks folder
        String functionPackRoot = "config\\functionpacks";
        File funcDir = new File(functionPackRoot);
        
        List<FunctionInfo> functions = new ArrayList<>();
        if (funcDir.exists() && funcDir.isDirectory()){
            String[] functionPackFileNames = funcDir.list(new FilenameFilter() {
                                                @Override
                                                public boolean accept(File dir, String name) {
                                                    return name.toLowerCase().endsWith(".xml");
                                                }
                                            }
            );
            for(String functionPackFileName: functionPackFileNames){
                String packageFile = functionPackRoot + "\\" + functionPackFileName;
                String packageName = functionPackFileName.substring(0, functionPackFileName.lastIndexOf(".xml")).toLowerCase();
                try (InputStream inputStream = new FileInputStream(packageFile)) {
                    InputSource inputSource = new InputSource(inputStream);
                    xr.parse(inputSource);
                    FunctionInfoContainer temp = (FunctionInfoContainer)unmarshallerHandler.getResult();
                    temp.registeredFunctions.forEach(p -> p.setPackageName(packageName)); // lowe case package name
                    functions.addAll(temp.registeredFunctions);
                }
            }
            functions.forEach(p -> p.setName(p.getName().toLowerCase())); // lower case function name
        }
        FunctionInfoContainer container = new FunctionInfoContainer();
        container.setRegisteredFunctions(functions);
        return container; //(FunctionInfoContainer)unmarshallerHandler.getResult();
    }
 
    private static void writeConfig() {
        FunctionParameterInfo p1 = new FunctionParameterInfo("columnName", "Boolean|Byte|String|Integer|Long|Real|Date");
        FunctionParameterInfo p2 = new FunctionParameterInfo("columnName2", "Boolean|Byte|String|Integer|Long|Real|Date");
        List<FunctionParameterInfo> parameters = new ArrayList<>();
        parameters.add(p1);
        parameters.add(p2);
        FunctionInfo f1 = new FunctionInfo("count", "column", "Long", "default", parameters, null);
        
        instance = new FunctionInfoContainer();
        instance.registeredFunctions.add(f1);

        try (OutputStream buffer = new BufferedOutputStream(new FileOutputStream("config\\functionpacks\\default.xml"))) {
            JAXBContext ctx = JAXBContext.newInstance(FunctionInfoContainer.class);
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            File configDir = new File("config\\functionpacks");
            if (!configDir.exists()){
                try{
                    configDir.mkdir();
                } catch(Exception ex){
                    Logger.getLogger(FunctionInfoContainer.class.getName()).log(Level.SEVERE, null, ex);
                }    
            }
            m.marshal(instance, buffer);
            buffer.flush();
        } catch (Exception ex){
            Logger.getLogger(FunctionInfoContainer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    
}
