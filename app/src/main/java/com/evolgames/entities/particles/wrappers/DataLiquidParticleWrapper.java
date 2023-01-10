package com.evolgames.entities.particles.wrappers;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.particles.emitters.AbsoluteEmitter;

import org.andengine.util.adt.color.Color;

public abstract class DataLiquidParticleWrapper extends LiquidParticleWrapper {
    @Override
    protected abstract AbsoluteEmitter createEmitter(float[] emitterData);


    public DataLiquidParticleWrapper(GameEntity gameEntity, float[] emitterData, Vector2 splashVelocity, Color color, int lowerRate, int higherRate) {
        super(gameEntity,color,emitterData,splashVelocity,lowerRate,higherRate);
    }


}
