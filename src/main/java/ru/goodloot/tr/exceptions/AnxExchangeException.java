package ru.goodloot.tr.exceptions;

/**
 * @author Artur M.
 * @created Oct 9, 2014
 * 
 * @Description ...
 */
public class AnxExchangeException extends RuntimeException {

    public AnxExchangeException(Throwable cause) {
        super(cause);
    }

    public AnxExchangeException(String message) {
        super(message);
    }

    public AnxExchangeException(String message, Throwable cause) {
        super(message, cause);
    }
}
