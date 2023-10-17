package com.evolgames.entities.particles.emitters;

import static org.andengine.util.Constants.VERTEX_INDEX_X;
import static org.andengine.util.Constants.VERTEX_INDEX_Y;

import com.evolgames.helpers.utilities.Utils;

import java.util.Arrays;

public class ClusterEmitter extends DataEmitter {

    protected final float[] dataCopy;
    protected final float[] weights;


    public ClusterEmitter(float pCenterX, float pCenterY, float[] data, float[] weights) {
        super(pCenterX, pCenterY, data);
        assert(data.length%2==0):"Data has to be multiple of two";
        this.dataCopy = Arrays.copyOf(data,data.length);
        this.weights = weights;
    }
    @Override
    public void getPositionOffset(float[] pOffset) {
        float random = Utils.RAND.nextFloat();
        for(int index=0;index<weights.length;index++){
            if(random<weights[index]) {
                pOffset[VERTEX_INDEX_X] = this.data[2 * index];
                pOffset[VERTEX_INDEX_Y] = this.data[2 * index + 1];
                break;
            }
        }
    }

    @Override
    protected void prepareData() {
        System.arraycopy(dataCopy, 0, data, 0, dataCopy.length);
    }
}
