package database;

/**
 * An exception which indicates a database query contained an invalid parameter.
 */
public class BadQueryParameterException extends Exception {
    public BadQueryParameterException(String message) {
        super(message);
    }
}
