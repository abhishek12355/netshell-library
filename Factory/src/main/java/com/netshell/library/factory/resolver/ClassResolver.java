package com.netshell.library.factory.resolver;

import com.netshell.library.factory.ClassSubstitutionMap;
import com.netshell.library.factory.exception.FactoryException;

import java.util.Map;
import java.util.Objects;

/**
 * @author Abhishek
 *         Created on 8/23/2015.
 */
public final class ClassResolver implements Resolver<String, String> {

    private final ClassSubstitutionMap classSubstitutionMap = new ClassSubstitutionMap();

    @Override
    public String resolve(String input) {

        String output = Objects.requireNonNull(input, "Input Class Name cannot be null");

        do {
            input = output;
            output = classSubstitutionMap.findSubstitution(input);
        } while (!output.equals(input));

        return output;
    }

    public void initialize(Map<String, String> classMap) throws FactoryException {
        classSubstitutionMap.addAll(classMap);
    }
}
