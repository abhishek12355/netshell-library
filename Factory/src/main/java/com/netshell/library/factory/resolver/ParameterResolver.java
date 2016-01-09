package com.netshell.library.factory.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author Abhishek
 *         Created on 8/23/2015.
 */
public final class ParameterResolver implements Resolver<Object[], Class<?>[]> {

    private static final Logger logger = LoggerFactory.getLogger(ParameterResolver.class);

    @Override
    public Class<?>[] resolve(Object[] input) {

        if (input == null) {
            return null;
        }

        Class<?>[] paramTypes = new Class[input.length];

        for (int i = 0; i < input.length; i++) {
            paramTypes[i] = input[i].getClass();
        }

        logger.debug(String.format("Parameter Types: %s", Arrays.toString(paramTypes)));

        return paramTypes;
    }
}
