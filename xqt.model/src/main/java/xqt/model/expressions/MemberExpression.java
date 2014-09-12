/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.expressions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author standard
 */
public class MemberExpression extends Expression{
    private MemberType memberType = MemberType.Simple;
    List<String> components = new ArrayList<>();

    public MemberType getMemberType() {
        return memberType;
    }

    public List<String> getComponents() {
        return components;
    }
    
    public MemberExpression(String name){
        this.id = name;
        components.clear();
        components.add(name);
        this.expressionType = ExpressionType.Member;
        this.memberType = MemberType.Simple;
    }
    
    public MemberExpression(List<String> names){
        this.id = names.stream().collect(Collectors.joining("."));
        components.clear();
        components.addAll(names);
        this.expressionType = ExpressionType.Member;
        this.memberType = MemberType.Compound;
    }

    // types: Attribute, Field
    
    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }   
    
    public enum MemberType {
        Simple,
        Compound
    }
}
