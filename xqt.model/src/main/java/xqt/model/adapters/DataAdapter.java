/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.adapters;

import java.util.Map;
import xqt.model.data.Resultset;
import xqt.model.statements.query.SelectDescriptor;

/**
 *
 * @author standard
 */
public interface DataAdapter {
    void setup(Map<String, Object> config);
    Resultset run(SelectDescriptor select);
}
