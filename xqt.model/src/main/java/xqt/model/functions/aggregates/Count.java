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
public class Count implements AggregateFunction{
    long counter = 0;

    @Override
    public Long move(Object data) { // Long is a subclass of Object, hence, compatible...
        if(data != null)
            counter++;
        return counter;
    }
}
