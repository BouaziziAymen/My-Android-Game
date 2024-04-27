package com.evolgames.dollmutilationgame.entities.composite;

import com.evolgames.dollmutilationgame.entities.blockvisitors.Visitor;

import java.util.Iterator;

public class Leaf<T extends Component<T>> extends Component<T> {
    @Override
    public void acceptVisitor(Visitor<T> visitor) {
    }

    @Override
    public Iterator<T> createIterator() {
        return new NullIterator<T>();
    }
}
