package com.evolgames.dollmutilationgame.entities.blockvisitors;

public interface Visitor<T> {
    void visitTheElement(T element);
}
