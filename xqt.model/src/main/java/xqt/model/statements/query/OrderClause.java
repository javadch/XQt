/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.statements.query;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import xqt.model.ClauseDescriptor;

/**
 *
 * @author Javad Chamanara
 * @project SciQuest
 */
public class OrderClause extends ClauseDescriptor{

    private Map<String, OrderEntry> orderItems = new LinkedHashMap<>();

    public OrderClause(){
        id = UUID.randomUUID().toString();
        type = SelectClauseType.Order.toString();
    }

    public Map<String, OrderEntry> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Map<String, OrderEntry> orderItems) {
        this.orderItems = orderItems;
        isPresent = this.orderItems != null && this.orderItems.size() > 0;
    }

}
