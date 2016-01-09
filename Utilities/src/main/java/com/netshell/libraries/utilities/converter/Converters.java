package com.netshell.libraries.utilities.converter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Abhishek
 * @since 26-06-2015.
 */
public final class Converters {

    private static final Map<Class<? extends Converter>, Converter<?, ?>> converterMap;

    static {
        converterMap = new HashMap<>();
        registerConverters();
    }

    private static void registerConverters() {
        registerConverter(new StringToIntegerConverter());
    }

    @SuppressWarnings("unchecked")
    public static <From, To> Converter<From, To> getConverter(Class<? extends Converter> converterClass) {
        return (Converter<From, To>) converterMap.get(converterClass);
    }

    public static <From, To> void registerConverter(Converter<From, To> converter) {
        converterMap.put(converter.getClass(), converter);
    }
}
