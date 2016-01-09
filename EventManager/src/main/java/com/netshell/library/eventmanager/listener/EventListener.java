package com.netshell.library.eventmanager.listener;

import com.netshell.library.eventmanager.exceptions.EventException;

/**
 * @author Abhishek on 05-04-2015.
 */
public interface EventListener<TObject, TParameters extends EventParameters> {
    void listen(String event, TObject source, TParameters parameters) throws EventException;
}
