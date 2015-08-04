/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.adapters.csv;

import xqt.model.containers.SingleContainer;

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
        Boolean externalHeader = false;
        try{
            externalHeader = Boolean.parseBoolean(container.getBinding().getConnection().getParameters().get("externalheader").getValue());
        } catch (Exception ex){}
        if(externalHeader){
            return basePath.concat(".hdr");
        } else {
            return basePath;
        }
    }
    
    @Override
    public String getCompleteSourceName(SingleContainer container){ //may need a container index too!
        String basePath = container.getBinding().getConnection().getSourceUri();
        String fileExtention = "xlsx";
        String fileName = "";
        try{
            fileExtention = container.getBinding().getConnection().getParameters().get("fileextension").getValue();
        } catch (Exception ex){}
        fileName = basePath.concat(".").concat(fileExtention);
        return fileName;
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
