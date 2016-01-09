package com.netshell.library.eventmanager.exceptions;

/**
 * @author Abhishek on 05-04-2015.
 */
public class EventException extends Exception {
    public EventException() {
        super();
    }

    public EventException(String message) {
        super(message);
    }

    public EventException(String message, Throwable cause) {
        super("exception was thrown by listener", cause);
    }

    public EventException(Throwable cause) {
        super(cause);
    }
}
