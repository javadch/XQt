/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.model.functions.aggregates;

import xqt.model.functions.AggregateFunction;

/**
 *
 * @author Javad Chamanara <chamanara@gmail.com>
 */
public class Sum implements AggregateFunction{
    Double sum = 0.0;
   
    @Override
    public Double move(Object data) {
        if(data != null){
            if(data instanceof Byte)
                sum = sum+((Byte)data);
            else if(data instanceof Integer)
                sum = sum+((Integer)data);
            else if(data instanceof Long)
                sum = sum+((Long)data);
            else if(data instanceof Double)
                sum = sum+((Double)data);
        }
        return sum;
    }    
}
