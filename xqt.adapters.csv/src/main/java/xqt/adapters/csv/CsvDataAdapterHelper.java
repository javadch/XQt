/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.adapters.csv;

import com.vaiona.commons.data.FieldInfo;
import com.vaiona.commons.io.FileHelper;
import com.vaiona.commons.logging.LoggerHelper;
import com.vaiona.commons.types.TypeSystem;
import com.vaiona.csv.reader.HeaderBuilder;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import xqt.model.adapters.BaseAdapterHelper;
import xqt.model.configurations.ConnectionParameterDescriptor;
import xqt.model.containers.DataContainer;
import xqt.model.containers.SingleContainer;
import xqt.model.exceptions.LanguageExceptionBuilder;
import xqt.model.statements.query.TargetClause;

/**
 *
 * @author Javad Chamanara <chamanara@gmail.com>
 */
public class CsvDataAdapterHelper extends BaseAdapterHelper{
    
    private static final HashMap<String, List<String>> typeConversion = new HashMap<>();
    
    static {
        typeConversion.put(TypeSystem.TypeName.Boolean,  new ArrayList<String>() {{add("boolean");}});
        typeConversion.put(TypeSystem.TypeName.Byte,     new ArrayList<String>() {{add("byte");}});
        typeConversion.put(TypeSystem.TypeName.Date,     new ArrayList<String>() {{add("date");}});
        typeConversion.put(TypeSystem.TypeName.Integer,  new ArrayList<String>() {{add("integer");add("int");}});
        typeConversion.put(TypeSystem.TypeName.Long,     new ArrayList<String>() {{add("long");}});
        typeConversion.put(TypeSystem.TypeName.Real,     new ArrayList<String>() {{add("double");add("real");add("float");}});
        typeConversion.put(TypeSystem.TypeName.String,   new ArrayList<String>() {{add("string");add("char");}});        
    }

    @Override
    public int getContainerDialectId(SingleContainer container) {
        DataContainerDialect ret = getContainerDialectName(container);
        return ret.getValue();
    }
    
    public static CsvDataAdapterHelper getConcreteHelper(SingleContainer container){
        switch (getContainerDialectName(container)){
             case MSExcel:
                 return new MSExcelDataAdapterHelper();
             case CSV:
             default:
                 return new CsvDataAdapterHelper();
         }          
    }

    private static DataContainerDialect getContainerDialectName(SingleContainer container) {
        ConnectionParameterDescriptor p = container.getBinding().getConnection().getParameterValue("dialect", "default");
        if(p == null || p.getValue() == null || p.getValue().equals("")  || p.getValue().equalsIgnoreCase("default")){
            return DataContainerDialect.CSV;
        }
        if(p.getValue().equalsIgnoreCase("MsExcel"))
            return DataContainerDialect.MSExcel;    
        else if (p.getValue().equalsIgnoreCase("CSV"))
            return DataContainerDialect.CSV;
        return DataContainerDialect.CSV;
    }
    
    @Override
    public String getConceptualType(String physicalType){
        Optional<Map.Entry<String, List<String>>> entry = typeConversion.entrySet().stream()
                .filter(p-> p.getValue().contains(physicalType.toLowerCase())).findFirst();
        if(entry.isPresent()){
            return entry.get().getKey();
        }
        else
            return TypeSystem.TypeName.Unknown;
    }

    @Override
    public String getPhysicalType(String conceptualType){
        if(typeConversion.containsKey(conceptualType)){
            return typeConversion.get(conceptualType).get(0); // returns first physical data type by default.
        }
            return TypeSystem.TypeName.Unknown;
    }
    

    /**
     *
     * @param container : must be a Single container
     * @param params
     * @return a map on the physical fields declared in the associated container. in this case a csv file
     */
    @Override
    public LinkedHashMap<String, FieldInfo> getContinerSchema(SingleContainer container, Object... params) {
        try {
            String columnDelimiter =    String.valueOf(params[0]);
            String typeDelimiter =      String.valueOf(params[1]);
            String unitDelimiter =      String.valueOf(params[2]);
            String fileName = getContainerSchemaHolder(container);
            HeaderBuilder hb = new HeaderBuilder();
            LinkedHashMap<String, FieldInfo> fields = hb.buildFromDataFile(fileName, columnDelimiter, typeDelimiter, unitDelimiter);
            fields.values().stream().forEach(field -> {
                field.conceptualDataType = getConceptualType(field.internalDataType);
            });
            return fields;
        } catch (Exception ex){
            return new LinkedHashMap<>();
        }
    }
    
    public String getContainerSchemaHolder(SingleContainer container){ //may need a container index too!
        String basePath = container.getBinding().getConnection().getSourceUri();
        String container0 = container.getContainerName();        
        String fileName = "";
        String fileExtention = container.getBinding().getConnection().getParameterValue("fileextension", "csv").getValue();
        if(isHeaderExternal(container)){
            fileName = basePath.concat(container0).concat(".").concat(fileExtention).concat(".hdr");
        } else {
            fileName = basePath.concat(container0).concat(".").concat(fileExtention);
        }
        try{
            fileName = FileHelper.makeAbsolute(fileName); 
            return fileName;
        } catch (IOException ex){
            container.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate("File containing header file was not found. " + ex.getMessage())
                    .setContextInfo1("")
                    .setLineNumber(container.getParserContext().getStart().getLine())
                    .setColumnNumber(container.getParserContext().getStart().getCharPositionInLine())
                    .build()
            );
            LoggerHelper.logError(MessageFormat.format("Requested file '{0}' was not found. {1}", fileName, ex.getMessage()));                        
            return null;
        }
    }

    public String getCompleteSourceName(SingleContainer container){ //may need a container index too!
        String basePath = container.getBinding().getConnection().getSourceUri();
        String container0 = container.getContainerName();
        String fileName = "";
        String fileExtention = container.getBinding().getConnection().getParameterValue("fileextension", "csv").getValue();
        fileName = basePath.concat(container0).concat(".").concat(fileExtention);
        try{
            fileName = FileHelper.makeAbsolute(fileName); 
            return fileName;
        } catch (IOException ex){
            container.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate("Source file was not found. " + ex.getMessage())
                    .setContextInfo1("")
                    .setLineNumber(container.getParserContext().getStart().getLine())
                    .setColumnNumber(container.getParserContext().getStart().getCharPositionInLine())
                    .build()
            );
            LoggerHelper.logError(MessageFormat.format("Requested file '{0}' was not found. {1}", fileName, ex.getMessage()));            
            return null;
        }
    }

    public String getCompleteTargetName(TargetClause target){ //may need a container index too!
        if(target.getContainer().getDataContainerType() == DataContainer.DataContainerType.Single){
            SingleContainer container = (SingleContainer)target.getContainer();
            String basePath = container.getBinding().getConnection().getSourceUri();
            String container0 = container.getContainerName();
            String fileExtention = ((SingleContainer)target.getContainer())
                        .getBinding().getConnection().getParameterValue("fileextension", "csv").getValue();
                                    
            String fileName = basePath.concat(container0).concat(".").concat(fileExtention);
            try{
                fileName = FileHelper.makeAbsolute(fileName); 
                return fileName;
            } catch (IOException ex){
                container.getLanguageExceptions().add(
                    LanguageExceptionBuilder.builder()
                        .setMessageTemplate("Target file was not found. " + ex.getMessage())
                        .setContextInfo1("")
                        .setLineNumber(container.getParserContext().getStart().getLine())
                        .setColumnNumber(container.getParserContext().getStart().getCharPositionInLine())
                        .build()
                );
                LoggerHelper.logError(MessageFormat.format("Requested file '{0}' was not found. {1}", fileName, ex.getMessage()));                            
                return null;
            }
        } else {
            return null;
        }
    }
    
    public boolean isFirstRowHeader(SingleContainer container){
        boolean firstRowIsHeader = Boolean.valueOf(container.getBinding().getConnection().getParameterValue("firstrowisheader", "false").getValue());
        return firstRowIsHeader;
    }

    protected Boolean isHeaderExternal(SingleContainer container){
        try{
            return Boolean.parseBoolean(container.getBinding().getConnection().getParameterValue("externalheader", "false").getValue());
        } catch (Exception ex){
            return false;
        }
    }
    
}    

    
