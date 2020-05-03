package copyex;

/**
 * @author DMarinov
 * Created on: 01/Mar/2019
 */
class CopyexException extends RuntimeException
{
    /**
     * Custom Copyex exception used for reporting parse errors.
     *
     * @param message - Error message of the exception.
     */
    CopyexException(String message)
    {
        super(message);
    }
}
