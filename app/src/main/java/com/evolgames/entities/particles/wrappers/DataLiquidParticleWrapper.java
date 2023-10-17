package com.evolgames.entities.particles.wrappers;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.particles.emitters.DataEmitter;
import com.evolgames.entities.particles.systems.SpawnAction;

import org.andengine.util.adt.color.Color;

public abstract class DataLiquidParticleWrapper extends LiquidParticleWrapper {
    @Override
    protected abstract DataEmitter createEmitter(float[] emitterData, float[] weights);

    public DataLiquidParticleWrapper(GameEntity gameEntity, float[] emitterData, float[] weights, Vector2 splashVelocity, Color color, int lowerRate, int higherRate) {
        super(gameEntity, color,emitterData, weights,splashVelocity,lowerRate,higherRate);
    }

}
