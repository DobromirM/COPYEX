package copyex;

import AbstractTree.AbstractSyntaxTree;
import compiler.PythonCompiler;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * @author DMarinov
 * Created on: 01/Mar/2019
 */
public class Copyex
{
    private AbstractSyntaxTree abstractTree;
    private ParseTree tree;
    private CopyexLexer lexer;
    private CopyexParser parser;
    
    /**
     * Parse a string of code using the custom Copyex lexer and parser.
     *
     * @param code - Code to be parsed.
     *
     * @return - Abstract Syntax Tree representing the parsed code.
     */
    public AbstractSyntaxTree parse(String code)
    {
        CharStream input = CharStreams.fromString(code);
        try
        {
            lexer = new CopyexLexer(input);
            lexer.removeErrorListeners();
            lexer.addErrorListener(new CopyexLexerErrorListener());
            parser = new CopyexParser(new CommonTokenStream(lexer));
            parser.setErrorHandler(new CopyexParserErrorStrategy());
            tree = parser.file();
            CopyexVisitor visitor = new CopyexVisitor();
            abstractTree = new AbstractSyntaxTree(visitor.visit(tree));
            return abstractTree;
        }
        catch (Exception e)
        {
            tree = null;
            System.out.println(e.getMessage());
            return null;
        }
    }
    
    /**
     * Compile the abstract syntax tree into python code.
     *
     * @return - Compiled python code.
     */
    public String compile()
    {
        try
        {
            PythonCompiler compiler = new PythonCompiler();
            return compiler.compilePython(abstractTree);
        }
        catch (Exception e)
        {
            tree = null;
            System.out.println(e.getMessage());
            return null;
        }
    }
}
