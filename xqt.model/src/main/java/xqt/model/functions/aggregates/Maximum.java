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
public class Maximum implements AggregateFunction {
    Double max = Double.NEGATIVE_INFINITY;

    @Override
    public Double move(Object data) {
        if(data != null){
            if(data instanceof Byte){
                if(max < ((Byte)data)){
                    max = ((double) ((Byte)data));
                }
            }
            else if(data instanceof Integer){
                if(max < ((Integer)data)){
                    max = ((double) (Integer)data);
                }
            }
            else if(data instanceof Long){
                if(max < ((Long)data)){
                    max = ((double) (Long)data);
                }
            }
            else if(data instanceof Double){
                if(max < ((Double)data)){
                    max = (Double)data;
                }            
            }
        }
        return max;
    }      
}
