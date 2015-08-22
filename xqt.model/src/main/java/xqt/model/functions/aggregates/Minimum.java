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
public class Minimum implements AggregateFunction {
    Double min = Double.POSITIVE_INFINITY;
    
    @Override
    public Double move(Object data) {
        if(data != null){
            if(data instanceof Byte){
                if(min > ((Byte)data)){
                    min = ((double) ((Byte)data));
                }
            }
            else if(data instanceof Integer){
                if(min > ((Integer)data)){
                    min = ((double) (Integer)data);
                }
            }
            else if(data instanceof Long){
                if(min > ((Long)data)){
                    min = ((double) (Long)data);
                }
            }
            else if(data instanceof Double){
                if(min > ((Double)data)){
                    min = (Double)data;
                }            
            }
        }
        return min;
    }      
}
