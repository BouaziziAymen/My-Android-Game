package com.evolgames.entities.particles.wrappers;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.particles.emitters.DataEmitter;
import com.evolgames.entities.particles.emitters.SegmentEmitter;

import org.andengine.util.adt.color.Color;

public class SegmentLiquidParticleWrapper extends DataLiquidParticleWrapper{
    public SegmentLiquidParticleWrapper(GameEntity gameEntity, float[] emitterData, Vector2 splashVelocity,Color color, int lowerRate, int higherRate) {
        super(gameEntity, emitterData,splashVelocity, color, lowerRate, higherRate);
    }

    @Override
    protected DataEmitter createEmitter(float[] emitterData) {
        return new SegmentEmitter(emitterData);
    }
}
