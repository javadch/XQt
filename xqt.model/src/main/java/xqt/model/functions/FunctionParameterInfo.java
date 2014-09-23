/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.functions;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author standard
 */
@XmlRootElement(name="Parameter")
public class FunctionParameterInfo {
    private String name;
    private String permittedDataTypes;

    public FunctionParameterInfo(){
        
    }
    
    public FunctionParameterInfo(String name, String permittedDataTypes){
        this.name = name;
        this.permittedDataTypes = permittedDataTypes;
    }
    
    @XmlAttribute(name="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute(name="permittedDataTypes")
    public String getPermittedDataTypes() {
        return permittedDataTypes;
    }

    public void setPermittedDataTypes(String permittedDataTypes) {
        this.permittedDataTypes = permittedDataTypes;
    }
        
}
