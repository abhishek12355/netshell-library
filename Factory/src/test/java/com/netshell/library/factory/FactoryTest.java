package com.netshell.library.factory;

import com.netshell.library.factory.custom.AbstractCustomFactory;
import com.netshell.library.factory.exception.FactoryException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Abhishek
 *         Created on 8/30/2015.
 */
public class FactoryTest {

    private static final Logger logger = LoggerFactory.getLogger(FactoryTest.class);
    private static final Factory factory;

    static {
        Map<String, String> map = new HashMap<>();
        map.put("java.lang.CharSequence", "java.lang.String");
        map.put("java.util.Collection", "java.util.List");
        map.put("java.util.List", "java.util.ArrayList");
        map.put("java.util.ArrayList", "java.lang.String");

        try {
            factory = Factory.createFactory(map);
            factory.registerFactory(String.class, new AbstractCustomFactory<String>() {
                @Override
                protected String createNew(Class<?>[] parameterTypes, Object[] params) throws FactoryException {
                    logger.debug("CustomFactory");
                    return (String) params[0];
                }
            });
        } catch (FactoryException e) {
            throw new RuntimeException("factory creation failed", e);
        }
    }

    @Test
    public void testCreateFactory() throws Exception {
        logger.info("Testing CharSequence");
        final CharSequence charSequence = factory.create(CharSequence.class, true, "new String");
        assertEquals("Instance Test Failed", String.class, charSequence.getClass());
        assertEquals("Value Test Failed", "new String", charSequence);
    }

    @Test
    public void testCreateFactory_fail() throws Exception {
        logger.info("Testing Collection fail");
        try {
            factory.create(Collection.class, true);
        } catch (FactoryException e) {
            assertEquals("Type Matched", "java.lang.String cannot be cast to java.util.Collection", e.getMessage());
            return;
        }

        assertTrue("Should have failed but passed", false);
    }

    @Test
    public void testCreateFactory_noMap() throws Exception {
        logger.info("Testing File");
        final File charSequence = factory.create(File.class, true, "log4j2.xml");
        assertEquals("Instance Test Failed", File.class, charSequence.getClass());
    }

    @Test
    public void testCreateFactory_noSubstitute() throws Exception {
        logger.info("Testing ArrayList");
        final ArrayList<?> charSequence = factory.create(ArrayList.class, false);
        assertEquals("Instance Test Failed", ArrayList.class, charSequence.getClass());
    }
}