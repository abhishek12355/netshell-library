package com.netshell.library.factory;

import com.netshell.library.factory.custom.CustomFactory;
import com.netshell.library.factory.exception.FactoryException;
import com.netshell.library.factory.resolver.ClassResolver;
import com.netshell.library.factory.resolver.ParameterResolver;
import com.netshell.library.factory.resolver.Resolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Abhishek
 *         Created on 8/23/2015.
 */
public final class Factory {

    private static final Logger logger = LoggerFactory.getLogger(Factory.class);

    private final Resolver<String, String> classResolver;
    private final Resolver<Object[], Class<?>[]> parameterTypeResolver;

    private final Map<String, CustomFactory<?>> customFactoryMap = new HashMap<>();

    private Factory(Map<String, String> initialMap) throws FactoryException {
        parameterTypeResolver = new ParameterResolver();
        classResolver = new ClassResolver();
        ((ClassResolver) classResolver).initialize(initialMap);
    }

    public static Factory createFactory(Map<String, String> initialMap) throws FactoryException {
        return new Factory(initialMap);
    }

    @SuppressWarnings("unchecked")
    public <T> T create(Class<T> tClass, boolean substitute, Object... params) throws FactoryException {
        logger.info("Initialising Class: " + tClass.getName());
        try {
            Class<? extends T> tSubstitutedClass = tClass;
            if (substitute) {
                final Class<?> aClass = Class.forName(classResolver.resolve(tSubstitutedClass.getName()));
                logger.debug(String.format("Substituted Class: %s -> %s", tClass.getName(), aClass.getName()));
                if (!tClass.isAssignableFrom(aClass)) {
                    final String message = String.format("%s cannot be cast to %s", aClass.getName(), tClass.getName());
                    logger.error(message);
                    throw new FactoryException(message);
                }

                tSubstitutedClass = (Class<? extends T>) aClass;
            }

            if (customFactoryMap.containsKey(tSubstitutedClass.getName())) {
                return ((CustomFactory<T>) customFactoryMap.get(tSubstitutedClass.getName())).create(params);
            }

            return tSubstitutedClass.getConstructor(parameterTypeResolver.resolve(params)).newInstance(params);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            final FactoryException factoryException = new FactoryException("Unable To instantiate: " + tClass.getName(), e);
            logger.error(factoryException.getMessage(), e);
            throw factoryException;
        }
    }

    public <T> void registerFactory(Class<T> tClass, CustomFactory<T> tCustomFactory) throws FactoryException {
        if (this.customFactoryMap.containsKey(tClass.getName())) {
            final String message = "Already Registered Factory: " + tClass.getName();
            logger.error(message);
            throw new FactoryException(message);
        }
        this.customFactoryMap.put(tClass.getName(), tCustomFactory);
    }

    public <T> void unregisterFactory(Class<T> tClass) {
        this.customFactoryMap.remove(tClass.getName());
    }
}
