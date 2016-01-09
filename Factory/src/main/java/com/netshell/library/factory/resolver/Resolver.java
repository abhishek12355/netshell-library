package com.netshell.library.factory.resolver;

/**
 * @author Abhishek
 *         Created on 8/23/2015.
 */
public interface Resolver<TIn, TOut> {

    TOut resolve(TIn input);
}
