package com.evolgames.dollmutilationgame.entities.observer;

public interface Observer<T extends Subject> {
    void update(T observable);
}
