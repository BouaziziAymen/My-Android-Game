package com.evolgames.dollmutilationgame.userinterface.view.sections.basic;

import com.evolgames.dollmutilationgame.userinterface.view.basics.Element;

public class SimpleSecondary<T extends Element> extends SimplePrimary<T> {
    private final int secondaryKey;

    public SimpleSecondary(int primaryKey, int secondaryKey, T element) {
        super(primaryKey, element);
        this.secondaryKey = secondaryKey;
    }

    public int getSecondaryKey() {
        return secondaryKey;
    }
}
