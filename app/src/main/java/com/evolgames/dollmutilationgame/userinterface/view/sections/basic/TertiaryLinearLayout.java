package com.evolgames.dollmutilationgame.userinterface.view.sections.basic;

public class TertiaryLinearLayout extends SecondaryLinearLayout implements TertiaryInterface {
    private final int tertiaryKey;

    public TertiaryLinearLayout(int primaryKey, int secondaryKey, int tertiaryKey, float margin) {
        super(primaryKey, secondaryKey, margin);
        this.tertiaryKey = tertiaryKey;
    }

    @Override
    public int getTertiaryKey() {
        return tertiaryKey;
    }
}
