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
public interface DataReader<T, S1, S2> {
    // in join cases, S1 is the left and S2 is the right side data sources.
    // in single sources, only S1 is used and the S2 should be null
    List<T> read(List<S1> source1, List<S2> source2) throws FileNotFoundException, IOException;
}
