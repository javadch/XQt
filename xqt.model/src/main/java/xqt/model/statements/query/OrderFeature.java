/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.statements.query;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import xqt.model.ClauseDescriptor;

/**
 *
 * @author Javad Chamanara
 * @project SciQuest
 */
public class OrderFeature extends ClauseDescriptor{
    private Map<String, OrderEntry> orderItems = new LinkedHashMap<>();

    public OrderFeature(){
        id = UUID.randomUUID().toString();
        type = SelectQueryClauseType.Order.toString();
    }

    public Map<String, OrderEntry> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Map<String, OrderEntry> orderItems) {
        this.orderItems = orderItems;
        isPresent = this.orderItems != null && this.orderItems.size() > 0;
    }

}
