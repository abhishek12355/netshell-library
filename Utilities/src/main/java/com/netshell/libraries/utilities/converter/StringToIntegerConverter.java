package com.netshell.libraries.utilities.converter;

/**
 * @author Abhishek
 * @since 26-06-2015.
 */
public class StringToIntegerConverter implements Converter<String, Integer> {
    StringToIntegerConverter() {
    }

    @Override
    public Integer convert(String s) {
        return Integer.parseInt(s);
    }
}
