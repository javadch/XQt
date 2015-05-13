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
//    @XmlType(name = "ProviderType")
//    @XmlEnum
//    public enum ProviderType {
//        Adapter,
//        Container,
//        Fallback;
//        public String value() {
//            return name();
//        }
//
//        public static ProviderType fromValue(String v) {
//            return valueOf(v);
//        }
//    }    
//    // one of this items: Adapter, Container, Fallback.
//    // If Adapter: jar/namespace/class/method is used.
//    // If Container: nativeCode is used to replace the function name (as defined in the script).
//    // If Fallback: the definition of the default implementation as in the fallback adapter will be used.
//    private ProviderType providerType = ProviderType.Adapter; //Adapter/ Container/ Fallback
//    @XmlAttribute(name="providerType", required = false)
//    public ProviderType getProvider() {
//        return providerType;
//    }
//
//    public void setProvider(ProviderType value) {
//        this.providerType = value;
//    }

    
    private String modifier = "static";
    private String jarFile;
    private String namespace;
    private String className;
    private String methodName;
    private String nativeCode = "";
    private String dialect = "default"; //in adapters that are working on various dialects, it can be used to determine which implementation should be used

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

    // there is a chance that the order of paramater passed to a native are different from dialect to dialect!
    // but they are not currently considered.
    @XmlAttribute(name="nativeCode", required = false)
    public String getNativeCode() {
        return nativeCode;
    }

    public void setNativeCode(String nativeCode) {
        this.nativeCode = nativeCode;
    }        

    @XmlAttribute(name="dialect", required = false)
    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }
    
    
}
