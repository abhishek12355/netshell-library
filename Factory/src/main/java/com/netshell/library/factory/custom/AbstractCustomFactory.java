package com.netshell.library.factory.custom;

import com.netshell.library.factory.exception.FactoryException;
import com.netshell.library.factory.resolver.ParameterResolver;

/**
 * @author Abhishek
 *         Created on 8/31/2015.
 */
public abstract class AbstractCustomFactory<T> implements CustomFactory<T> {

    @Override
    public T create(Object... params) throws FactoryException {
        return createNew(new ParameterResolver().resolve(params), params);
    }

    protected abstract T createNew(Class<?>[] parameterTypes, Object[] params) throws FactoryException;
}
