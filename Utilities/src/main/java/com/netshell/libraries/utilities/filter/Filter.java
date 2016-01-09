package com.netshell.libraries.utilities.filter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Abhishek
 *         Created on 12/4/2015.
 */
public final class Filter {
    public static <T> Collection<T> filter(Collection<T> tCollection, T filterCriteria) {
        return tCollection.stream().filter(item -> Filter.match(item, filterCriteria)).collect(Collectors.toList());
    }

    public static <T> Collection<T> filter(Collection<T> tCollection, Predicate<T> filterCriteria) {
        return tCollection.stream().filter(filterCriteria::test).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private static <T> boolean match(T item, T filterCriteria) {
        final Class<?> iClass = item.getClass();
        final Field[] declaredFields = iClass.getDeclaredFields();

        try {
            for (Field f : declaredFields) {
                f.setAccessible(true);
                final Object obj = f.get(filterCriteria);
                final Object other = f.get(item);
                if (!isDefault(obj, f.getType()) && (
                        obj instanceof Comparable && ((Comparable) obj).compareTo(other) != 0 ||
                                !obj.equals(other)
                )) {
                    return false;
                }
            }
        } catch (IllegalAccessException e) {
            throw new FilterException(e);
        }

        return true;
    }

    private static boolean isDefault(Object obj, Class<?> type) {
        return obj == null ||
                Byte.TYPE.equals(type) && ((byte) obj) == 0 ||
                Long.TYPE.equals(type) && ((long) obj) == 0l ||
                Short.TYPE.equals(type) && ((short) obj) == 0 ||
                Double.TYPE.equals(type) && ((double) obj) == 0d ||
                Integer.TYPE.equals(type) && ((int) obj) == 0 ||
                Boolean.TYPE.equals(type) && ((boolean) obj);
    }

    public static final class FilterException extends RuntimeException {
        public FilterException(Exception e) {
            super(e);
        }
    }
}
