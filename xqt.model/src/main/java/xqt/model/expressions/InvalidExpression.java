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
public class InvalidExpression extends Expression{

    public InvalidExpression(){
        this.expressionType = ExpressionType.Invalid;
    }
    
    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
