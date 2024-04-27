package com.evolgames.dollmutilationgame.userinterface.view.sections.basic;

import com.evolgames.dollmutilationgame.userinterface.view.basics.Element;

public class SimpleTertiary<T extends Element> extends SimpleSecondary<T> {
    private final int tertiaryKey;

    public SimpleTertiary(int primaryKey, int secondaryKey, int tertiaryKey, T element) {
        super(primaryKey, secondaryKey, element);
        this.tertiaryKey = tertiaryKey;
    }

    public int getTertiaryKey() {
        return tertiaryKey;
    }
}
