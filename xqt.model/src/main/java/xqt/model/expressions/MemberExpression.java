/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.expressions;

/**
 *
 * @author standard
 */
public class MemberExpression extends Expression{
    
    public MemberExpression(String name){
        this.id = name;
        this.expressionType = ExpressionType.Member;
    }
    
    // types: Attribute, Field
    
    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }   
}
