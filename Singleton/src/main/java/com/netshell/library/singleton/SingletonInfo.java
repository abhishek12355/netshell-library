package com.netshell.library.singleton;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * @author Abhishek
 *         Created on 1/9/2016.
 */
public final class SingletonInfo<TObject> {
    private final TObject singleton;
    private Date date;

    public SingletonInfo(final TObject singleton) {
        this.singleton = Objects.requireNonNull(singleton);
        this.date = Calendar.getInstance().getTime();
    }

    public Date getDate() {
        return date;
    }

    public TObject getSingleton() {
        date = Calendar.getInstance().getTime();
        return singleton;
    }

    public TObject peekSingleton() {
        return singleton;
    }
}
