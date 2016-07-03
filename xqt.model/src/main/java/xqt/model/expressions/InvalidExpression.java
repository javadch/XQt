/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.expressions;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author standard
 */
public class InvalidExpression extends Expression{

    public InvalidExpression(){
        this.expressionType = ExpressionType.Invalid;
    }

    @Override
    public void accept(ExpressionVisitor visitor){
        // do nothing
    }    
    
    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public List<MemberExpression> getMemberExpressions(){
    	return new ArrayList<MemberExpression>();	
    }
}
