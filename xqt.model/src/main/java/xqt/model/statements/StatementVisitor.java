/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.statements;

import xqt.model.data.Resultset;
import xqt.model.statements.query.SelectDescriptor;

/**
 *
 * @author standard
 */
public interface StatementVisitor {
    // the visit method should return a pointer to the result set if there is any
    public Resultset visit(SelectDescriptor selectStatement);    
    // also visit other statement types
}
