package com.evolgames.userinterface.sections.basic;

import com.evolgames.userinterface.view.basics.Element;
import com.evolgames.userinterface.view.layouts.LinearLayout;

public class PrimaryLinearLayout extends LinearLayout implements PrimaryInterface {
    private int primaryKey;

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
