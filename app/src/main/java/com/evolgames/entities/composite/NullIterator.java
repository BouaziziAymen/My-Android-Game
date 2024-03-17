package com.evolgames.entities.composite;

import java.util.Iterator;

public class NullIterator<T extends Component<T>> implements Iterator<T> {
    public T next() {
        return null;
    }

    public boolean hasNext() {
        return false;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
