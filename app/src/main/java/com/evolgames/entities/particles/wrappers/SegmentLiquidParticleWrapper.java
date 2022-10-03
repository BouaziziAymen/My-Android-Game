package com.evolgames.entities.particles.wrappers;

import com.evolgames.entities.GameEntity;
import com.evolgames.entities.particles.emitters.BaseEmitter;
import com.evolgames.entities.particles.emitters.SegmentEmitter;

import org.andengine.util.adt.color.Color;

public class SegmentLiquidParticleWrapper extends DataLiquidParticleWrapper{
    public SegmentLiquidParticleWrapper(GameEntity gameEntity, float[] emitterData, Color color, int lowerRate, int higherRate) {
        super(gameEntity, emitterData,null, color, lowerRate, higherRate);
    }

    @Override
    protected BaseEmitter createEmitter(float[] emitterData) {
        return new SegmentEmitter(emitterData);
    }
}
