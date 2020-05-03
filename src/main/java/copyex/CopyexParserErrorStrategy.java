package copyex;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.IntervalSet;
import org.antlr.v4.runtime.misc.ParseCancellationException;

/**
 * @author DMarinov
 * Created on: 01/Mar/2019
 */
public class CopyexParserErrorStrategy extends DefaultErrorStrategy
{
    private static final String ERROR_MESSAGE = "Got a %s but expected %s!\nLocation: line %s:%s";
    private static final String TOKEN_DISPLAY_FORMAT = "<%s>";
    
    /**
     * Override the default recovering and instead rise an error.
     *
     * @param recognizer - Parser object
     *
     * @throws RecognitionException - Throws custom recognition exception.
     */
    @Override
    public Token recoverInline(Parser recognizer) throws RecognitionException
    {
        InputMismatchException e = new InputMismatchException(recognizer);
        reportError(recognizer, e);
        throw e;
    }
    
    /**
     * Override the default report error to throw custom Input mismatch errors.
     *
     * @param recognizer - Parser object.
     * @param error      - Original recognition error.
     */
    @Override
    public void reportError(Parser recognizer, RecognitionException error)
    {
        if (error instanceof InputMismatchException)
        {
            reportInputMismatch(recognizer, (InputMismatchException) error);
        }
    }
    
    /**
     * Override the default report input mismatch error to throw custom input mismatch error.
     *
     * @param recognizer - Parser object.
     * @param e          - Input mismatch error.
     */
    @Override
    protected void reportInputMismatch(Parser recognizer, InputMismatchException e)
    {
        Integer line = e.getOffendingToken().getLine();
        Integer position = e.getOffendingToken().getCharPositionInLine();
        String token = e.getExpectedTokens().toString(recognizer.getVocabulary());
        
        String msg = "Input mismatch error!\n" + String.format(ERROR_MESSAGE, getTokenErrorDisplay(e.getOffendingToken()), token, line, position);
        
        throw new ParseCancellationException(msg, e);
    }
    
    /**
     * Override the default report unwanted token to throw custom Extraneous input error.
     *
     * @param recognizer - Parser object
     */
    @Override
    protected void reportUnwantedToken(Parser recognizer)
    {
        Token token = recognizer.getCurrentToken();
        Integer line = token.getLine();
        Integer position = token.getCharPositionInLine();
        IntervalSet expecting = getExpectedTokens(recognizer);
        
        String msg = "Extraneous input error!\n" + String.format(ERROR_MESSAGE, getTokenErrorDisplay(token), expecting.toString(recognizer.getVocabulary()), line, position);
        
        throw new ParseCancellationException(msg);
    }
    
    /**
     * Override the default strategy for getting the error token name.
     *
     * @param token - Token that produced error.
     *
     * @return - Name of the token.
     */
    @Override
    protected String getTokenErrorDisplay(Token token)
    {
        if (token == null)
        {
            return "<no token>";
        }
        return escapeWSAndQuote(String.format(TOKEN_DISPLAY_FORMAT, getErrorSymbol(token)));
    }
    
    /**
     * Get the error symbol from a given token.
     *
     * @param token - Token that needs to be converted.
     *
     * @return - Token string.
     */
    private String getErrorSymbol(Token token)
    {
        String symbol = getSymbolText(token);
        
        if (symbol == null)
        {
            if (getSymbolType(token) == Token.EOF)
            {
                symbol = "EOF";
            }
            else
            {
                symbol = String.valueOf(getSymbolType(token));
            }
        }
        return symbol;
    }
}
