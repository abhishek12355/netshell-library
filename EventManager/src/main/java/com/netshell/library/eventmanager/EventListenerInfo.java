package com.netshell.library.eventmanager;

import com.netshell.library.eventmanager.listener.EventListener;
import com.netshell.library.eventmanager.listener.EventParameters;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * @author Abhishek on 05-04-2015.
 */
class EventListenerInfo<TParameters extends EventParameters, TListener extends EventListener<?, TParameters>> {
    private final Collection<TListener> listeners;
    private final Class<TListener> listenerClass;
    private final Class<TParameters> parametersClass;

    EventListenerInfo(final Class<TListener> listenerClass, final Class<TParameters> parametersClass) {
        this.listenerClass = Objects.requireNonNull(listenerClass, "listenerClass cannot be null");
        this.parametersClass = Objects.requireNonNull(parametersClass, "parameterClass cannot be null");
        this.listeners = new HashSet<>();
    }

    public Collection<TListener> getListeners() {
        return listeners;
    }

    public Class<TListener> getListenerClass() {
        return listenerClass;
    }

    public Class<TParameters> getParametersClass() {
        return parametersClass;
    }

    public <E> boolean isListenerClass(final Class<E> listenerClass) {
        return this.listenerClass.isAssignableFrom(listenerClass);
    }

    public <E> boolean isParametersClass(final Class<E> parametersClass) {
        return this.parametersClass.isAssignableFrom(parametersClass);
    }
}
