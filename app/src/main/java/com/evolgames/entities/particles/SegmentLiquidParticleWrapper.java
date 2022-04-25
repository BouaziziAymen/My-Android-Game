package com.evolgames.entities.particles;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

import org.andengine.util.adt.color.Color;

public class SegmentLiquidParticleWrapper extends LiquidParticleWrapper {
    @Override
    protected MyBaseEmitter createEmitter(float[] emitterData) {
        return new SegmentEmitter(emitterData);
    }

    public SegmentLiquidParticleWrapper(Color color, Vector2 first, Vector2 second,int lowerRate,int higherRate) {
        super(color,new float[]{first.x,first.y,second.x,second.y},lowerRate,higherRate);
    }


}
