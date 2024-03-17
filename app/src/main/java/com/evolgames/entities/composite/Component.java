package com.evolgames.entities.composite;

import com.evolgames.entities.blockvisitors.Visitor;

import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unused")
public abstract class Component<T extends Component<T>> {

    public void add(T blockComponent) {
        throw new UnsupportedOperationException();
    }

    public void remove(T blockComponent) {
        throw new UnsupportedOperationException();
    }

    abstract void acceptVisitor(Visitor<T> visitor);

    public Component<T> getChild(int i) {
        throw new UnsupportedOperationException();
    }

    public Iterator<T> createIterator() {
        throw new UnsupportedOperationException();
    }

    public Iterator<T> getIterator() {
        throw new UnsupportedOperationException();
    }

    public List<T> getChildren() {
        throw new UnsupportedOperationException();
    }
}
