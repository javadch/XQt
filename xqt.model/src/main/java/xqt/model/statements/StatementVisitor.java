/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.statements;

import java.util.HashMap;

import xqt.model.adapters.AdapterSelector;
import xqt.model.data.Resultset;
import xqt.model.statements.query.SelectDescriptor;

/**
 *
 * @author standard
 */
public interface StatementVisitor {
    // the visit method should return a pointer to the result set if there is any
    public Resultset visit(StatementDescriptor statement);    
    // also visit other statement types

    // better remove this method. statements can prepare their selves, or have another visitor 
    //public void prepare(SelectDescriptor aThis);
    
    public HashMap<String, Object> getAuxiliaryData();
    
    public AdapterSelector getAdapterSelector();

}
