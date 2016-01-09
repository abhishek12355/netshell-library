package com.netshell.libraries.utilities.db.enums;

/**
 * @author Abhishek
 *         Created on 12/13/2015.
 */
public final class DBEnumException extends RuntimeException {
    public <T> DBEnumException(String id) {
        super("Unable to find " + id /*+ " of type " + tClass.getName()*/);
    }

    public DBEnumException(String s, Exception e) {
        super(s, e);
    }
}
