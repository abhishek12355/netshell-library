package com.netshell.library.singleton;

import com.netshell.library.singleton.exceptions.SingletonException;
import org.junit.Test;

/**
 * @author Abhishek on 22-04-2015.
 */
public class SingletonManagerTest {

    @Test
    public void testCleanUp() throws Exception {
        final SingletonManagerTest singletonObject = new SingletonManagerTest();
        SingletonManager.registerSingleton(Object.class, new SingletonManagerTest());
        Thread.sleep(1000);
        SingletonManager.registerSingleton(Object.class, singletonObject);
        assert SingletonManager.getSingleton(Object.class, SingletonManagerTest.class) == singletonObject;

        SingletonManager.unregisterSingleton(Object.class);
    }

    @Test
    public void testRegisterSingleton() throws Exception {

    }

    @Test
    public void testGetSingleton() throws Exception {
        final SingletonManagerTest singletonObject = new SingletonManagerTest();
        SingletonManager.registerSingleton(Object.class, singletonObject);
        assert SingletonManager.getSingleton(Object.class, SingletonManagerTest.class) == singletonObject;
        SingletonManager.unregisterSingleton(Object.class);
    }

    @Test
    public void testGetSingleton_Duplicate() throws Exception {
        final SingletonManagerTest singletonObject = new SingletonManagerTest();
        SingletonManager.registerSingleton(Object.class, singletonObject);
        try {
            SingletonManager.registerSingleton(Object.class, new SingletonManagerTest());
            assert SingletonManager.getSingleton(Object.class, SingletonManagerTest.class) == singletonObject;
        } catch (SingletonException ex) {
            assert true;
        }

        SingletonManager.unregisterSingleton(Object.class);
    }

    @Test
    public void testUnregisterSingleton() throws Exception {
        final SingletonManagerTest singletonObject = new SingletonManagerTest();
        SingletonManager.registerSingleton(Object.class, singletonObject);
        SingletonManager.unregisterSingleton(Object.class);
        try {
            SingletonManager.getSingleton(Object.class, SingletonManagerTest.class);
        } catch (SingletonException ex) {
            assert true;
            return;
        }

        assert false;
    }
}