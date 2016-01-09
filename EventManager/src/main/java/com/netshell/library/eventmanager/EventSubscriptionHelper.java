package com.netshell.library.eventmanager;

import com.netshell.library.eventmanager.exceptions.EventException;
import com.netshell.library.eventmanager.listener.EventListener;
import com.netshell.library.eventmanager.listener.EventParameters;

import java.util.Objects;

/**
 * @author Abhishek on 05-04-2015.
 */
public class EventSubscriptionHelper<TObject> {
    private final EventManager<TObject> manager;

    EventSubscriptionHelper(final EventManager<TObject> manager) {
        this.manager = Objects.requireNonNull(manager, "EventManager cannot not be null");
    }

    public <P extends EventParameters, T extends EventListener<TObject, P>> void addListener(final String event, final T listener) throws EventException {
        this.manager.addListener(event, listener);
    }

    public <P extends EventParameters, T extends EventListener<TObject, P>> void removeListener(final String event, final T listener) throws EventException {
        this.manager.removeListener(event, listener);
    }

    public <P extends EventParameters, T extends EventListener<TObject, P>> void removeAllListeners(final String event) throws EventException {
        this.manager.removeAllListeners(event);
    }
}
