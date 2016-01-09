package com.netshell.library.singleton;

import com.netshell.library.singleton.exceptions.SingletonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Abhishek
 *         Created on 1/9/2016.
 */
public final class SingletonManager {

    public static final String SINGLETON_MANAGER_PROPERTIES = "singleton_manager.properties";
    private static final String TIME_OUT = "SingletonManager.TimeOut";
    private static final Logger LOGGER = LoggerFactory.getLogger(SingletonManager.class);
    private static final Map<Class<?>, SingletonInfo<?>> singletonMap;
    private static int cleanupTimeout = 3600000;

    static {

        readConfig();
        singletonMap = new ConcurrentHashMap<>(10);

        Thread cleanupThread = new Thread(() -> {
            while (true) {
                cleanUp();
                try {
                    LOGGER.trace("cleanUp: Sleeping for " + SingletonManager.cleanupTimeout / 1000 + "sec.");
                    Thread.sleep(SingletonManager.cleanupTimeout);
                } catch (InterruptedException ignored) {
                    LOGGER.warn("cleanUp: Thread Interrupted.", ignored);
                }
            }
        });
        cleanupThread.start();
    }


    private SingletonManager() {
        throw new UnsupportedOperationException("not Supported");
    }

    public static void cleanUp() {
        synchronized (SingletonManager.singletonMap) {
            LOGGER.info("cleanUp: Triggered. Cleaning Unused Singleton.");
            final Iterator<Map.Entry<Class<?>, SingletonInfo<?>>> iterator = SingletonManager.singletonMap.entrySet().iterator();
            while (iterator.hasNext()) {
                final Map.Entry<Class<?>, SingletonInfo<?>> entry = iterator.next();
                final SingletonInfo info = entry.getValue();
                if (info.getDate().getTime() + SingletonManager.cleanupTimeout > Calendar.getInstance().getTime().getTime()) {
                    LOGGER.trace("cleanUp: Removing: " + entry.getKey().getName());
                    iterator.remove();
                }
            }
        }
    }

    public static <T, TObject> void registerSingleton(final Class<T> tClass, final TObject singletonObject) {
        Objects.requireNonNull(tClass);

        final String name = tClass.getName();
        if (isRegistered(tClass)) {
            final String message = "Singleton Object already registered: " + name;
            LOGGER.error(message);
            throw new SingletonException(message);
        }
        SingletonManager.singletonMap.put(tClass, new SingletonInfo<>(singletonObject));
        LOGGER.info("Registered Singleton Object: " + name);
    }

    public static <T> boolean isRegistered(Class<T> tClass) {
        return SingletonManager.singletonMap.containsKey(tClass);
    }

    public static <T, TObject> TObject getSingleton(final Class<T> tClass, final Class<TObject> tObjectClass) {
        Objects.requireNonNull(tObjectClass);
        final Optional<SingletonInfo> singletonInfo = Optional.ofNullable(SingletonManager.singletonMap.get(Objects.requireNonNull(tClass)));
        return tObjectClass.cast(singletonInfo.orElseThrow(() -> new SingletonException(tClass.getName() + " not registered.")).getSingleton());
    }

    public static <T, TObject> TObject getSingleton(final Class<T> tClass, final Class<TObject> tObjectClass, TObject tDefault) {
        Objects.requireNonNull(tObjectClass);
        final SingletonInfo singletonInfo = SingletonManager.singletonMap.get(Objects.requireNonNull(tClass));
        return tObjectClass.cast(singletonInfo == null ? tDefault : singletonInfo.getSingleton());
    }

    public static <T> void unregisterSingleton(final Class<T> tClass) {
        SingletonManager.singletonMap.remove(tClass);
        LOGGER.info("Unregistered Singleton Object: " + tClass.getName());
    }

    private static void readConfig() {
        Properties properties = new Properties();
        try {
            final URL propertiesFile = Optional.ofNullable(SingletonManager.class.getClassLoader()
                    .getResource(SINGLETON_MANAGER_PROPERTIES)).orElseThrow(() -> new FileNotFoundException(SINGLETON_MANAGER_PROPERTIES));

            LOGGER.info("Reading Configuration file: " + propertiesFile.getPath());
            properties.load(propertiesFile.openStream());
            SingletonManager.cleanupTimeout = Integer.parseInt(properties.getProperty(SingletonManager.TIME_OUT));
            LOGGER.trace("SingletonManager.TimeOut=" + SingletonManager.cleanupTimeout);
        } catch (IOException e) {
            LOGGER.warn(e.getLocalizedMessage(), e);
        }
    }
}

