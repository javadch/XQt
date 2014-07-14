/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.adapters.builtin;

import com.vaiona.commons.compilation.*;
import com.vaiona.commons.data.AttributeInfo;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.tools.JavaFileManager;
import xqt.model.conversion.ConvertSelectElement;
import xqt.model.statements.query.SelectDescriptor;

/**
 *
 * @author standard
 */
public class DataReaderBuilder {
    public static String NAME_SPACE = "xqt.adapters.builtin";
    ConvertSelectElement convertSelect = null;
    
    public DataReaderBuilder(){        
        convertSelect = new ConvertSelectElement();
    }

    DataReader build(Class classObject) throws NoSuchMethodException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException  {
        DataReader instance = (DataReader)ObjectCreator.load(classObject);
        return instance;
    }

    LinkedHashMap<String, InMemorySourceFile> createSources(SelectDescriptor select, String sourceRowType) throws IOException {
        // set the entrypoint
        // set the full name
        String baseClassName = "C" + (new Date()).getTime(); // just to make a unique name
        
        Map<String, AttributeInfo>  attributes = convertSelect.prepareAttributes(select);
        
        // transform the ordering clauses to their bound equivalent, in each attribute names are linked to the attibutes objects
        Map<AttributeInfo, String> orderItems = new LinkedHashMap<>();        
        for (Map.Entry<String, String> entry : convertSelect.prepareOrdering(select).entrySet()) {
                if(attributes.containsKey(entry.getKey())){
                    orderItems.put(attributes.get(entry.getKey()), entry.getValue());
                }            
        }
        
        Map<String, Object> readerContext = new HashMap<>();
        readerContext.put("namespace", NAME_SPACE);
        readerContext.put("BaseClassName", baseClassName);
        readerContext.put("SourceRowNamespace", sourceRowType.subSequence(0, sourceRowType.lastIndexOf(".")));
        readerContext.put("SourceRowType", sourceRowType.subSequence(sourceRowType.lastIndexOf(".")+1, sourceRowType.length()));
        readerContext.put("TargetRowType", sourceRowType.subSequence(sourceRowType.lastIndexOf(".")+1, sourceRowType.length()));
        readerContext.put("Where", convertSelect.prepareExpression(select, convertSelect.prepareWhere(select)));
        
        // the delimiter MUST come from the source->connection->... chain. if the direct source has no connection it should come from the upper ones
        String header = String.join(",", attributes.values().stream().map(p-> p.name + ":" + p.internalDataType).collect(Collectors.toList()));
        String linePattern = String.join(",", attributes.values().stream().map(p-> "String.valueOf(entity." + p.name + ")").collect(Collectors.toList()));
        readerContext.put("linePattern", linePattern);        
        
        readerContext.put("rowHeader", header);        
        readerContext.put("Ordering", orderItems);
        readerContext.put("skip", select.getLimitClause().getSkip());
        readerContext.put("take", select.getLimitClause().getTake());
        readerContext.put("writeResultsToFile", convertSelect.shouldResultBeWrittenIntoFile(select));
        //readerContext.put("Attributes", attributes.values().stream().collect(Collectors.toList()));
        ClassGenerator generator = new ClassGenerator();
        String reader = generator.generate(this, "MemReader", "Resource", readerContext);
        
        LinkedHashMap<String, InMemorySourceFile> sources = new LinkedHashMap<>();
        InMemorySourceFile rf = new InMemorySourceFile(baseClassName + "Reader", reader);
        rf.setEntryPoint(true);
        rf.setFullName(NAME_SPACE + "." + baseClassName + "Reader");
        sources.put(rf.getFullName(), rf); // the reader must be added first
        
        return sources;        
    }
}
