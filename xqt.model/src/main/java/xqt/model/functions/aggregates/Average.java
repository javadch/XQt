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
public class Average implements AggregateFunction {
    long counter = 0;
    Double sum = 0.0;
    @Override
    public Double move(Object data) {
        if(data != null){
            if(data instanceof Byte){
                sum = ((counter*sum)+((Byte)data))/(counter+1);
            }
            else if(data instanceof Integer){
                sum = ((counter*sum)+((Integer)data))/(counter+1);
            }
            else if(data instanceof Long){
                sum = ((counter*sum)+((Long)data))/(counter+1);
            }
            else if(data instanceof Double){
                sum = ((counter*sum)+((Double)data))/(counter+1);
            }
            counter++;
        }
        return sum;
    }      
}
