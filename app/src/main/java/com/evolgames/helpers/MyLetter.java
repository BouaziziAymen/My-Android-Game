package com.evolgames.helpers;

import org.andengine.opengl.texture.region.TextureRegion;

public class MyLetter {
    private float mAdvance;
    private TextureRegion mRegion;
    private int mOffsetX;
    private int mOffsetY;
    private boolean isWhiteSpace = false;

    public MyLetter(float pAdvance) {
        this.mAdvance = pAdvance;
        isWhiteSpace = true;
    }

    public MyLetter(float pAdvance, TextureRegion pRegion, int pOffsetX, int pOffsetY) {
        this.mAdvance = pAdvance;
        this.mRegion = pRegion;
        this.mOffsetX = pOffsetX;
        this.mOffsetY = pOffsetY;
    }


    public TextureRegion getRegion() {
        return mRegion;
    }

    public float getAdvance() {
        return mAdvance;
    }

    public int getOffsetY() {
        return mOffsetY;
    }

    public int getOffsetX() {
        return mOffsetX;
    }

    public float getKerning(char previous) {
        return 0;
    }

    public boolean isWhiteSpace() {
        return isWhiteSpace;
    }
}
