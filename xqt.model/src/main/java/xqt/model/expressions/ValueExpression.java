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
public class ValueExpression extends Expression {

    private String value;

    public String getValue() {
        return value;
    }
    
    public ValueExpression(String value, String type){
        this.value = value;
        this.type = type;
        this.expressionType = ExpressionType.Constant;
    }
    
   @Override
    public void accept(ExpressionVisitor visitor){
        visitor.visit(this);
    }
    
    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
