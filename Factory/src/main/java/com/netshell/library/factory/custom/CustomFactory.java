package com.netshell.library.factory.custom;

import com.netshell.library.factory.exception.FactoryException;

/**
 * @author Abhishek
 *         Created on 8/31/2015.
 */
public interface CustomFactory<T> {
    T create(Object... params) throws FactoryException;
}
