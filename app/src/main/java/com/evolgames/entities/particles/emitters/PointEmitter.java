package com.evolgames.entities.particles.emitters;

import static org.andengine.util.Constants.VERTEX_INDEX_X;
import static org.andengine.util.Constants.VERTEX_INDEX_Y;

public class PointEmitter  extends BaseEmitter {

    private final float beginX;
    private final float beginY;

    public PointEmitter(float[] data) {
        super(data[0], data[1],data);
        this.beginX = data[0];
        this.beginY = data[1];
    }

    @Override
    public void getPositionOffset(float[] pOffset) {
        pOffset[VERTEX_INDEX_X] = this.data[0];
        pOffset[VERTEX_INDEX_Y] = this.data[1];
    }

    @Override
    protected void prepareData() {
        data[0] = beginX;
        data[1] = beginY;
    }

}



