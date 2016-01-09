package com.netshell.libraries.utilities.converter;

/**
 * @author Abhishek
 * @since 26-06-2015.
 */
public interface Converter<From, To> {
    To convert(From from);
}
