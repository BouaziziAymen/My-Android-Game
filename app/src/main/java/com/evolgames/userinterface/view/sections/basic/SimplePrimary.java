package com.evolgames.userinterface.view.sections.basic;

import com.evolgames.userinterface.view.basics.Container;
import com.evolgames.userinterface.view.basics.Element;

public class SimplePrimary<T extends Element> extends Container implements PrimaryInterface {
    private final int primaryKey;
    protected T main;

    public SimplePrimary(int primaryKey, T element) {
        this.main = element;
        this.primaryKey = primaryKey;
        addElement(element);
    }

    @Override
    public float getHeight() {
        return main.getHeight();
    }

    @Override
    public float getWidth() {
        return main.getWidth();
    }

    @Override
    public int getPrimaryKey() {
        return primaryKey;
    }

    public T getMain() {
        return main;
    }
}
