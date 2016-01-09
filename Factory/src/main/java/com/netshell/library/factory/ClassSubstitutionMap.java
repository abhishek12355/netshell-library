package com.netshell.library.factory;

import com.netshell.library.factory.exception.FactoryException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Abhishek
 *         Created on 8/23/2015.
 */
public final class ClassSubstitutionMap {

    private final Map<String, String> classMap = new HashMap<>();

    public String findSubstitution(String input) {
        String output = classMap.get(input);
        return output == null ? input : output;
    }

    public void add(String source, String target) throws FactoryException {
        validateClass(source, target);
        this.classMap.put(source, target);
    }

    public void addAll(Map<String, String> classMap) throws FactoryException {
        if (classMap.containsKey(null) || classMap.containsKey("")) {
            throw new FactoryException("classMap cannot contain null as Class Substitution");
        }

        this.classMap.putAll(classMap);
    }

    private void validateClass(String source, String target) throws FactoryException {
        if (source == null || "".equals(source)) {
            throw new FactoryException("Invalid source class: " + source);
        }
    }
}
