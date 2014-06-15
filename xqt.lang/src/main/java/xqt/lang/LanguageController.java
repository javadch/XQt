/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.lang;

import java.io.IOException;
import java.io.InputStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import xqt.lang.grammar.XQtLexer;
import xqt.lang.grammar.XQtParser;
import xqt.lang.parsing.GrammarListener;
import xqt.lang.parsing.GrammarVisitor;
import xqt.model.ProcessModel;

/**
 *
 * @author jfd
 */
public class LanguageController {
    
    public ProcessModel createProcessModel(InputStream inputStream) throws IOException {
        XQtLexer lexer = new XQtLexer(new ANTLRInputStream(inputStream));
        CommonTokenStream tokens = new CommonTokenStream(lexer); 
        XQtParser parser = new XQtParser(tokens);
        ParseTree tree = parser.createProcessModel(); // the result is the parse tree
        //System.out.println(tree.toStringTree(parser)); // print tree as text
        
        ParseTreeWalker walker = new ParseTreeWalker(); // create standard walker
        GrammarListener extractor = new GrammarListener(parser);
        // initiate walk of tree with listener, refomat, add, delete, change the tree. 
        // also create ast, e-ast, ...
        walker.walk(extractor, tree); 

        // ast and e-ast tree construction, after changes made by the listener
        // keep antlr away from the query engine as much as possible. just pass the constructed e-ast
        // put a getMethod on the visitor to return ast/ e-ast
        // also visit returns a T type that seems to be possible to use it to return ast/ e-ast
        GrammarVisitor gVisitor = new GrammarVisitor();
        gVisitor.visit(tree);
        return (gVisitor.getProcessModel());
    }

}
