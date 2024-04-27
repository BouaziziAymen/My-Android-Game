package com.evolgames.dollmutilationgame.entities.observer;

import java.util.ArrayList;
import java.util.List;

public class Subject {

    private final List<Observer> observers = new ArrayList<>();
    private int state;

    protected List<Observer> getObservers() {
        return observers;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        notifyAllObservers();
    }

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void detach(Observer observer) {
        observers.remove(observer);
    }

    protected void notifyAllObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }
}
