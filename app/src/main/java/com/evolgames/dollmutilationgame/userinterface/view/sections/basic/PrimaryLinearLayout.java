package com.evolgames.dollmutilationgame.userinterface.view.sections.basic;

import com.evolgames.dollmutilationgame.userinterface.view.layouts.LinearLayout;

import org.andengine.entity.sprite.batch.SpriteBatch;

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
    public void drawSelf(SpriteBatch hudBatcher, SpriteBatch sceneBatcher) {
    }
}
