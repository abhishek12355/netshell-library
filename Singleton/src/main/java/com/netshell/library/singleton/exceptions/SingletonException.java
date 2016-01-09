package com.netshell.library.singleton.exceptions;

/**
 * @author Abhishek on 05-04-2015.
 */
public class SingletonException extends RuntimeException {
    public SingletonException() {
        super();
    }

    public SingletonException(String message) {
        super(message);
    }

    public SingletonException(String message, Throwable cause) {
        super(message, cause);
    }

    public SingletonException(Throwable cause) {
        super(cause);
    }
}
