/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.functions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.UnmarshallerHandler;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.vaiona.commons.io.FileHelper;
import com.vaiona.commons.logging.LoggerHelper;
import xqt.model.adapters.AdapterInfo;
import xqt.model.adapters.AdapterInfoContainer;

/**
 *
 * @author standard
 */

@XmlRootElement(name="Functions")
public class FunctionInfoContainer {

    private static Map<String, FunctionInfoContainer> instances = new HashMap<>(); // its a singleton object
    private List<FunctionInfo> registeredFunctions = new ArrayList<>();
    
    private FunctionInfoContainer(){
        
    }
    public static FunctionInfoContainer getDefaultInstance(String basePaths) throws Exception{
        AdapterInfo defaultAdapter = AdapterInfoContainer.getInstance(basePaths).getDefultAdapter();
        if(defaultAdapter == null)
            return getInstance("Default", basePaths);
        return getInstance(defaultAdapter.getId(), basePaths);

    }
    
    public static FunctionInfoContainer getInstance(String adapterId, String basePaths){
        if(!instances.containsKey(adapterId)){
            try {
                //writeConfig();
                instances.put(adapterId, FunctionInfoContainer.loadRegisteredFunctions(adapterId, basePaths));
            } catch (Exception ex) {
            	LoggerHelper.logError(MessageFormat.format("Failed to load function specifications from '{0}' adapter.", adapterId));        
            }
        }
        return instances.get(adapterId);
    }

    @XmlElement(name="Function")
    public List<FunctionInfo> getRegisteredFunctions() {
        return registeredFunctions;
    }

    public void setRegisteredFunctions(List<FunctionInfo> registeredFunctions) {
        this.registeredFunctions = registeredFunctions;
    }

    private static FunctionInfoContainer loadRegisteredFunctions(String adapterId, String basePaths) throws Exception {
    	// load the functions of the adpater and that of the fallback. merge them while the adpater functions have precedence.
        List<FunctionInfo> functions = new ArrayList<>();
        List<FunctionInfo> fallbackFunctions = new ArrayList<>();
        functions = loadRegisteredFunctionsForAdapter(adapterId, basePaths);
        AdapterInfo fallback = AdapterInfoContainer.getInstance(basePaths).getDefultAdapter();
        if(fallback != null && fallback.getIsFallback() && !fallback.getId().equalsIgnoreCase(adapterId)){
        	fallbackFunctions = loadRegisteredFunctionsForAdapter(fallback.getId(), basePaths);
        }

        for(FunctionInfo function: fallbackFunctions){
        	Boolean found = functions.stream().anyMatch(p-> (function.getPackageName().equalsIgnoreCase(p.getPackageName()) 
        			&& function.getName().equalsIgnoreCase(p.getName()) ));
        	if(!found){
        		functions.add(function);
        	}
        }
        FunctionInfoContainer container = new FunctionInfoContainer();
        container.setRegisteredFunctions(functions);
        return container; //(FunctionInfoContainer)unmarshallerHandler.getResult();
    }
 
    private static List<FunctionInfo> loadRegisteredFunctionsForAdapter(String adapterId, String basePaths) throws Exception {
        JAXBContext jc = JAXBContext.newInstance(FunctionInfoContainer.class);
        UnmarshallerHandler unmarshallerHandler = jc.createUnmarshaller().getUnmarshallerHandler();

        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();
        XMLReader xr = sp.getXMLReader();
        xr.setContentHandler(unmarshallerHandler);

        // open and read all the function pack files in the config\\functionpacks folder
        //String functionPackRoot = "config\\adapters\\" + adapterId.toLowerCase() + "\\functionpacks";
        Path functionPackRoot = Paths.get(FileHelper.makeAbsolute(FileHelper.getConfigPath(basePaths)), "adapters", adapterId.toLowerCase(), "functionpacks");
        File funcDir = functionPackRoot.toFile();
        
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
                //String packageFile = functionPackRoot + "\\" + functionPackFileName;
                Path packageFile = Paths.get(functionPackRoot.toString(), functionPackFileName);
                String packageName = functionPackFileName.substring(0, functionPackFileName.lastIndexOf(".xml")).toLowerCase();
                try (InputStream inputStream = new FileInputStream(packageFile.toFile())) {
                    InputSource inputSource = new InputSource(inputStream);
                    xr.parse(inputSource);
                    FunctionInfoContainer temp = (FunctionInfoContainer)unmarshallerHandler.getResult();
                    temp.registeredFunctions.forEach(p -> p.setPackageName(packageName)); // lower case package name
                    functions.addAll(temp.registeredFunctions);
                }
            }
            functions.forEach(p -> p.setName(p.getName().toLowerCase())); // lower case function name
        }
        return functions;
    }
//    private static void writeConfigForTest() {
//        FunctionParameterInfo p1 = new FunctionParameterInfo("columnName", "Boolean|Byte|String|Integer|Long|Real|Date");
//        FunctionParameterInfo p2 = new FunctionParameterInfo("columnName2", "Boolean|Byte|String|Integer|Long|Real|Date");
//        List<FunctionParameterInfo> parameters = new ArrayList<>();
//        parameters.add(p1);
//        parameters.add(p2);
//        FunctionInfo f1 = new FunctionInfo("count", "column", "Long", "default", parameters, null);
//        
//        FunctionInfoContainer instance = new FunctionInfoContainer();
//        instance.registeredFunctions.add(f1);
//
//        try (OutputStream buffer = new BufferedOutputStream(new FileOutputStream("config\\functionpacks\\default.xml"))) {
//            JAXBContext ctx = JAXBContext.newInstance(FunctionInfoContainer.class);
//            Marshaller m = ctx.createMarshaller();
//            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//
//            File configDir = new File("config\\functionpacks");
//            if (!configDir.exists()){
//                try{
//                    configDir.mkdir();
//                } catch(Exception ex){
//                    Logger.getLogger(FunctionInfoContainer.class.getName()).log(Level.SEVERE, null, ex);
//                }    
//            }
//            m.marshal(instance, buffer);
//            buffer.flush();
//        } catch (Exception ex){
//            Logger.getLogger(FunctionInfoContainer.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//    }    
}
