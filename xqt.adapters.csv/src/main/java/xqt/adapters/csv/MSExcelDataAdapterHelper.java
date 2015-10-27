/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.adapters.csv;

import com.vaiona.commons.data.FieldInfo;
import com.vaiona.commons.io.FileHelper;
import com.vaiona.commons.logging.LoggerHelper;
import com.vaiona.csv.reader.HeaderBuilder;
import com.vaiona.csv.reader.RowBuilder;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import javafx.scene.control.Cell;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import xqt.model.containers.SingleContainer;
import xqt.model.exceptions.LanguageExceptionBuilder;

/**
 *
 * @author Javad Chamanara <chamanara@gmail.com>
 */
class MSExcelDataAdapterHelper extends CsvDataAdapterHelper {

    public MSExcelDataAdapterHelper() {
    }
    
    @Override
    public String getContainerSchemaHolder(SingleContainer container){
        String basePath = getCompleteSourceName(container);
        String container0 = container.getContainerName();
        basePath = basePath.concat(".").concat(container0);
        if(isHeaderExternal(container)){
            return basePath.concat(".hdr");
        } else {
            return basePath;
        }
    }
    
    @Override
    public String getCompleteSourceName(SingleContainer container){ //may need a container index too!
        String basePath = container.getBinding().getConnection().getSourceUri();
        if(basePath.endsWith("\\")){
            basePath = basePath.substring(0, basePath.length() - "\\".length());
        }
        if(basePath.endsWith("/")){
            basePath = basePath.substring(0, basePath.length() - "/".length());
        }
        String fileExtention = "xlsx";
        String fileName = "";
        try{
            fileExtention = container.getBinding().getConnection().getParameters().get("fileextension").getValue();
        } catch (Exception ex){}
        
        fileName = basePath.concat(".").concat(fileExtention);
        try{
            fileName = FileHelper.makeAbsolute(fileName); 
            return fileName;
        } catch (IOException ex){
            container.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate("Requested file was not found. " + ex.getMessage())
                    .setContextInfo1("")
                    .setLineNumber(container.getParserContext().getStart().getLine())
                    .setColumnNumber(container.getParserContext().getStart().getCharPositionInLine())
                    .build()
            );
            LoggerHelper.logError(MessageFormat.format("Requested file '{0}' was not found. {1}", fileName, ex.getMessage()));            
            return null;
        }
    }    
    
    @Override
    public LinkedHashMap<String, FieldInfo> getContinerSchema(SingleContainer container, Object... params) {
        if(isHeaderExternal(container)){
            return super.getContinerSchema(container, params);
        } else {
            try {
                LinkedHashMap<String, FieldInfo> headers = new LinkedHashMap<>();
                //String columnDelimiter =    String.valueOf(params[0]);
                String typeDelimiter =      String.valueOf(params[1]);
                String unitDelimiter =      String.valueOf(params[2]);
                String fileName = getCompleteSourceName(container);
                HeaderBuilder hb = new HeaderBuilder();
                //XSSFWorkbook workbook2 = new XSSFWorkbook(fileName);
                InputStream inp = new FileInputStream(fileName);
                Workbook workbook = WorkbookFactory.create(inp);                
                Sheet sheet = null;
                String sheetName = container.getContainerName();
                if(sheetName != null && !sheetName.isEmpty()){
                	sheet = workbook.getSheet(sheetName);
                }
                if(sheet == null){ // sheetName is not valid or is an index
                	sheet = workbook.getSheetAt(container.getContainerIndex());
                }
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                if (sheet.rowIterator().hasNext()) {
                    Row row=sheet.getRow(0);
                    String[] cellvalues = RowBuilder.createRowArray(row, evaluator);
                    int indexCount = 0;
                    for(String cell: cellvalues) {
                        if(cell != null && !cell.isEmpty()){
                            FieldInfo field = hb.convert(cell, typeDelimiter, unitDelimiter);
                            field.index = indexCount;
                            if(field.name!= null && !field.name.isEmpty() && !headers.containsKey(field.name)){
                                headers.put(field.name, field);
                                indexCount++;
                            }
                        }
                    }
                }
                for(FieldInfo field: headers.values()){
                    field.conceptualDataType = getConceptualType(field.internalDataType);
                }
                return headers;
            } catch (IOException | InvalidFormatException ex){
                LoggerHelper.logError(MessageFormat.format("Schema generation error for adapter: \'CSV\'. {0}", ex.getMessage()));            
                return new LinkedHashMap<>();
            }
        }
    }

    @Override
    public String getAggregateReaderResourceName() {
        return "MSExcelAggregateReader";
    }
    
    @Override
    public String getReaderResourceName() {
        return "MSExcelReader";
    }    
    
    @Override
    public String getJoinReaderResourceName() {
        return "MSExcelJoinReader";
    }    
    
}
