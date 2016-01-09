package com.netshell.libraries.utilities.db.enums;

import java.util.Iterator;

/**
 * @author Abhishek
 *         Created on 12/13/2015.
 */
public interface DataSource<T> extends Iterator<T> {
    void activate();

    void close();
}
