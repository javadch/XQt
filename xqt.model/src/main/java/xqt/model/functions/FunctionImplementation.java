/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.model.functions;

import javax.xml.bind.annotation.XmlAttribute;

/**
 *
 * @author Javad Chamanara
 */
public class FunctionImplementation {
    private String modifier = "static";
    private String jarFile;
    private String namespace;
    private String className;
    private String methodName;

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
}
