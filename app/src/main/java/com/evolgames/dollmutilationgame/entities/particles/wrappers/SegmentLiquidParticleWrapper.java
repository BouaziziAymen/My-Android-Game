package com.evolgames.dollmutilationgame.entities.particles.wrappers;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.entities.particles.emitters.DataEmitter;
import com.evolgames.dollmutilationgame.entities.particles.emitters.SegmentEmitter;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;

import org.andengine.util.adt.color.Color;

public class SegmentLiquidParticleWrapper extends LiquidParticleWrapper {
    public SegmentLiquidParticleWrapper(
            GameEntity gameEntity,
            float[] emitterData,
            Vector2 splashVelocity,
            Color color, float flammability,
            int lowerRate,
            int higherRate) {
        super(gameEntity, emitterData, null, color, flammability, splashVelocity, lowerRate, higherRate);
    }

    @Override
    protected DataEmitter createEmitter(float[] emitterData, float[] weights, GameEntity gameEntity) {
        return new SegmentEmitter(gameEntity, emitterData);
    }
}
