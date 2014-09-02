/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.adapters.builtin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author standard
 * @param <Object>
 * @param <T>
 * @param <S>
 */
public interface DataReader<T, S> {
    List<T> read(List<S> source) throws FileNotFoundException, IOException;
}
