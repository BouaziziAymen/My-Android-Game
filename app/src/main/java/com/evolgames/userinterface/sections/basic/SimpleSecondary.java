package com.evolgames.userinterface.sections.basic;

import com.evolgames.userinterface.view.basics.Element;

public class SimpleSecondary<T extends Element> extends SimplePrimary<T> {
    private int secondaryKey;
    public SimpleSecondary(int primaryKey, int secondaryKey, T element) {
        super(primaryKey, element);
        this.secondaryKey = secondaryKey;
    }

    public int getSecondaryKey() {
        return secondaryKey;
    }
}
