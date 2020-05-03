package copyex;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

/**
 * @author DMarinov
 * Created on: 01/Mar/2019
 */
public class CopyexLexerErrorListener extends BaseErrorListener
{
    /**
     * Custom implementation of the lexer method for reporting syntax errors.
     * This method adheres to the copyex error message standard.
     *
     * @param recognizer         - Recognizer object.
     * @param offendingSymbol    - Symbol which is not recognized.
     * @param line               - Line of the error.
     * @param charPositionInLine - Position in line of the error.
     * @param msg                - Error message.
     * @param e                  - Recognition exception object.
     */
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e)
    {
        throw new RuntimeException("Unrecognized character error!\n" + msg + "\nLocation " + line + ":" + charPositionInLine);
    }
}