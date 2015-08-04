/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.adapters.dbms;

import com.vaiona.commons.data.DataReaderBase;
import com.vaiona.commons.data.FieldInfo;
import java.util.List;

/**
 *
 * @author Javad Chamanara <chamanara@gmail.com>
 */
public interface DbmsDataReader<T, S1, S2> extends DataReaderBase<T, S1, S2> {
    DbmsDataReader<T, S1, S2> connectionString(String value);
    DbmsDataReader<T, S1, S2> userName(String value);
    DbmsDataReader<T, S1, S2> password(String value);
    DbmsDataReader<T, S1, S2> fields(List<FieldInfo> value);
}
