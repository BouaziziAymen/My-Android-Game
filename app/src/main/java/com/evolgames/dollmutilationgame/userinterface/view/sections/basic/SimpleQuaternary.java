package com.evolgames.dollmutilationgame.userinterface.view.sections.basic;

import com.evolgames.dollmutilationgame.userinterface.view.basics.Element;

public class SimpleQuaternary<T extends Element> extends SimpleTertiary<T>
        implements QuaternaryInterface {
    private final int quaternaryKey;

    public SimpleQuaternary(
            int primaryKey, int secondaryKey, int tertiaryKey, int quaternaryKey, T element) {
        super(primaryKey, secondaryKey, tertiaryKey, element);
        this.quaternaryKey = quaternaryKey;
    }

    @Override
    public int getQuaternaryKey() {
        return quaternaryKey;
    }
}
