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
    long count = 0;
    Double sum = 0.0;

    @Override
    public Double move(Object data) {
        if(data != null){
            if(data instanceof Byte){
                sum = sum+(Byte)data;
            }
            else if(data instanceof Integer){
                sum = sum+(Integer)data;
            }
            else if(data instanceof Long){
                sum = sum+(Long)data;
            }
            else if(data instanceof Double){
                sum = sum+(Double)data;
            }
            count++;
        }
        return sum/count;
    }      

//    Double avg = 0.0;
//    @Override
//    public Double move2(Object data) {
//        if(data != null){
//            if(data instanceof Byte){
//                avg = ((count*avg)+((Byte)data))/(count+1);
//            }
//            else if(data instanceof Integer){
//                avg = ((count*avg)+((Integer)data))/(count+1);
//            }
//            else if(data instanceof Long){
//                avg = ((count*avg)+((Long)data))/(count+1);
//            }
//            else if(data instanceof Double){
//                avg = ((count*avg)+((Double)data))/(count+1);
//            }
//            count++;
//        }
//        return avg;
//    }      

}
