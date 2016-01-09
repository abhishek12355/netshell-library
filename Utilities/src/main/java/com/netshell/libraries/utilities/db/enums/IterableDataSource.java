package com.netshell.libraries.utilities.db.enums;

import java.util.Iterator;

/**
 * @author Abhishek
 *         Created on 12/26/2015.
 */
public class IterableDataSource<T> implements DataSource<T> {

    private final Iterator<T> tIterator;

    public IterableDataSource(Iterable<T> tIterable) {
        this.tIterator = tIterable.iterator();
    }

    @Override
    public void activate() {
    }

    @Override
    public void close() {
    }

    @Override
    public boolean hasNext() {
        return this.tIterator.hasNext();
    }

    @Override
    public T next() {
        return this.tIterator.next();
    }
}
