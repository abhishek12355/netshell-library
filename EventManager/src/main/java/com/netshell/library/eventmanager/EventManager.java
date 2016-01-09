package com.netshell.library.eventmanager;

import com.netshell.library.eventmanager.exceptions.EventException;
import com.netshell.library.eventmanager.listener.EventListener;
import com.netshell.library.eventmanager.listener.EventParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Abhishek on 05-04-2015.
 */
public class EventManager<TObject> {

    private static final Logger logger = LoggerFactory.getLogger(EventManager.class);
    private final EventSubscriptionHelper<TObject> helper;
    private final ConcurrentMap<String, EventListenerInfo<? extends EventParameters, ? extends EventListener<?, ? extends EventParameters>>> listenersMap;
    private final transient TObject source;

    public EventManager(final TObject source) {
        logger.info("Initialising Event Manger");
        this.source = source;
        this.helper = new EventSubscriptionHelper<>(this);
        this.listenersMap = new ConcurrentHashMap<>();
    }

    public <E extends EventParameters, T extends EventListener<TObject, E>> void registerEvent(final String event, final Class<T> listenerClass, final Class<E> parametersClass) throws EventException {
        if (event == null || "".equals(event.trim())) {
            final String message = "event name cannot be blank";
            logger.error(message);
            throw new EventException(message);
        }

        if (this.isEventRegistered(event)) {
            if (!this.getEventListenerInfo(event).isListenerClass(listenerClass)) {
                final String message = "Duplicate event with different listener class is being registered: " + listenerClass.getName();
                logger.error(message);
                throw new EventException(message);
            }
            return;
        }

        logger.debug("Registering Event: " + event);
        final EventListenerInfo<E, T> info = new EventListenerInfo<>(listenerClass, parametersClass);
        this.listenersMap.put(event, info);
    }

    public void unregisterEvent(final String event) {
        logger.debug("Unregistering Event: " + event);
        this.listenersMap.remove(event);
    }

    @SuppressWarnings("unchecked")
    public <E extends EventParameters> void raiseEvent(final String event, final E parameters) throws EventException {

        validateRegisteredEvent(event);
        validateParametersClass(event, parameters.getClass());
        try {
            logger.debug("Raising Event: " + event);
            for (EventListener<TObject, E> listener : (Collection<EventListener<TObject, E>>) this.getListeners(event)) {
                logger.trace(String.format("Raising event %s for listener %s", event, listener));
                listener.listen(event, this.source, parameters);
            }
        } catch (Exception ex) {
            final String message = "exception was thrown by listener";
            logger.error(message, ex);
            throw new EventException(message, ex);
        }
    }

    public <E extends EventParameters> void raiseEventNoThrow(final String event, final E parameters) {
        try {
            this.raiseEvent(event, parameters);
        } catch (Exception ignored) {
            logger.warn("exception was thrown by listener", ignored);
        }
    }

    public boolean isEventRegistered(final String event) {
        return this.listenersMap.containsKey(event);
    }

    public EventSubscriptionHelper<TObject> getEventSubscriptionHelper() {
        return helper;
    }

    public TObject getSource() {
        return source;
    }

    @SuppressWarnings("unchecked")
    <E extends EventParameters, T extends EventListener<TObject, E>> void addListener(final String event, T listener) throws EventException {
        validateRegisteredEvent(event);
        validateListenerClass(event, listener.getClass());

        logger.debug("Adding Listener: " + listener);
        final Collection listeners = this.getEventListeners(event);
        listeners.add(listener);
    }

    <E extends EventParameters, T extends EventListener<TObject, E>> void removeListener(final String event, T listener) throws EventException {
        validateRegisteredEvent(event);
        logger.debug("Removing Listener");
        this.getEventListeners(event).remove(listener);
    }

    void removeAllListeners(final String event) throws EventException {
        logger.debug("Removing All Listeners");
        this.getEventListeners(event).clear();
    }

    public Collection<? extends EventListener<?, ? extends EventParameters>> getListeners(final String event) {
        // TODO: why package scope?
        return Collections.unmodifiableCollection(this.getEventListeners(event));
    }

    private Collection<? extends EventListener<?, ? extends EventParameters>> getEventListeners(final String event) {
        return getEventListenerInfo(event).getListeners();
    }

    private EventListenerInfo<? extends EventParameters, ? extends EventListener<?, ? extends EventParameters>> getEventListenerInfo(final String event) {
        return this.listenersMap.get(Objects.requireNonNull(event));
    }

    private void validateParametersClass(final String event, final Class<? extends EventParameters> parametersClass) throws EventException {
        if (!this.getEventListenerInfo(event).isParametersClass(parametersClass)) {
            final String message = "provided parameters are of different type than registered parameters: " + parametersClass.getName();
            logger.error(message);
            throw new EventException(message);
        }
    }

    private void validateListenerClass(final String event, final Class<? extends EventListener> listenerClass) throws EventException {
        if (!this.getEventListenerInfo(event).isListenerClass(listenerClass)) {
            final String message = "provided listener is of different type than is registered";
            logger.error(message);
            throw new EventException(message);
        }
    }

    private void validateRegisteredEvent(final String event) throws EventException {
        if (!this.isEventRegistered(event)) {
            final String message = "event not registered: " + event;
            logger.error(message);
            throw new EventException(message);
        }
    }

}
