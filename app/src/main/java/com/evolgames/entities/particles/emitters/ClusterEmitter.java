package com.evolgames.entities.particles.emitters;

import static org.andengine.util.Constants.VERTEX_INDEX_X;
import static org.andengine.util.Constants.VERTEX_INDEX_Y;

import com.evolgames.helpers.utilities.Utils;

import java.util.Arrays;
import java.util.Random;

public class ClusterEmitter extends BaseEmitter {
    final float[] dataCopy;

    public ClusterEmitter(float pCenterX, float pCenterY, float[] data) {
        super(pCenterX, pCenterY, data);
        assert(data.length%2==0):"Data has to be multiple of two";
        dataCopy = Arrays.copyOf(data,data.length);
    }
    @Override
    public void getPositionOffset(float[] pOffset) {
        int index = Utils.RAND.nextInt(data.length/2);
        pOffset[VERTEX_INDEX_X] = this.data[2*index]+(1f-2*(float)Math.random())*2;
        pOffset[VERTEX_INDEX_Y] = this.data[2*index+1]+(1f-2*(float)Math.random())*2;
    }

    @Override
    protected void prepareData() {
        System.arraycopy(dataCopy, 0, data, 0, dataCopy.length);
    }
}
