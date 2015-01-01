/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.model.functions;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Javad Chamanara
 */
public class FunctionImplementation {
    @XmlType(name = "ProviderType")
    @XmlEnum
    public enum ProviderType {
        Adapter,
        Container,
        Fallback;
        public String value() {
            return name();
        }

        public static ProviderType fromValue(String v) {
            return valueOf(v);
        }
    }    
    
    private String modifier = "static";
    private String jarFile;
    private String namespace;
    private String className;
    private String methodName;
    // one of this items: Adapter, Container, Fallback.
    // If Adapter: jar/namespace/class/method is used.
    // If Container: providerString is used to replace the function name (as defined in the script).
    // If Fallback: the definition of the default implementation as in the fallback adapter will be used.
    private ProviderType providerType = ProviderType.Adapter; //Adapter/ Container/ Fallback
    private String providerString = "";

    @XmlAttribute(name="modifier")        
    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    @XmlAttribute(name="jar")
    public String getJarFile() {
        return jarFile;
    }

    public void setJarFile(String jarFile) {
        this.jarFile = jarFile;
    }

    @XmlAttribute(name="namespace")
    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @XmlAttribute(name="class")
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @XmlAttribute(name="method")
    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }        

    @XmlAttribute(name="providerType", required = false)
    public ProviderType getProvider() {
        return providerType;
    }

    public void setProvider(ProviderType value) {
        this.providerType = value;
    }

    @XmlAttribute(name="providerString", required = false)
    public String getProviderString() {
        return providerString;
    }

    public void setProviderString(String providerString) {
        this.providerString = providerString;
    }        
}
