package com.netshell.library.eventmanager;

import com.netshell.library.eventmanager.listener.EventListener;
import com.netshell.library.eventmanager.listener.EventParameters;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Abhishek
 *         Created on 8/30/2015.
 */
public class EventManagerTest {

    private static final Logger logger = LoggerFactory.getLogger(EventManagerTest.class);
    private final EventManager<EventManagerTest> eventManager = new EventManager<>(this);

    @Before
    public void setUp() throws Exception {
        eventManager.registerEvent("Event1", EventListener.class, EventParameters.class);
        final EventSubscriptionHelper<EventManagerTest> helper = eventManager.getEventSubscriptionHelper();
        helper.addListener("Event1", (event, source, parameters) -> logger.info(String.format("Listener1: %s event with source %s and params %s", event, source, parameters)));
        helper.addListener("Event1", (event, source, parameters) -> logger.info(String.format("Listener2: %s event with source %s and params %s", event, source, parameters)));
    }

    @After
    public void tearDown() throws Exception {
        final EventSubscriptionHelper<EventManagerTest> helper = eventManager.getEventSubscriptionHelper();
        helper.removeAllListeners("Event1");
        eventManager.unregisterEvent("Event1");
    }

    @Test
    public void testRaiseEvent() throws Exception {
        eventManager.raiseEvent("Event1", new EventParameters() {
            @Override
            public String toString() {
                return "Event Params";
            }
        });
    }

    @Test
    public void testRaiseEventNoThrow() throws Exception {

    }
}