package com.evolgames.userinterface.view.sections.basic;

import com.evolgames.userinterface.view.layouts.LinearLayout;

public class PrimaryLinearLayout extends LinearLayout implements PrimaryInterface {
    private final int primaryKey;

    public PrimaryLinearLayout(int primaryKey, float margin) {
        super(Direction.Horizontal, margin);
        this.primaryKey = primaryKey;
    }

    @Override
    public int getPrimaryKey() {
        return primaryKey;
    }

    @Override
    public void drawSelf() {
    }
}
