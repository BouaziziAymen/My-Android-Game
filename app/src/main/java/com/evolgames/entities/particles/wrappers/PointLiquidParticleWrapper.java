package com.evolgames.entities.particles.wrappers;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.particles.emitters.BaseEmitter;
import com.evolgames.entities.particles.emitters.PointEmitter;

import org.andengine.util.adt.color.Color;

public class PointLiquidParticleWrapper  extends DataLiquidParticleWrapper {
    @Override
    protected BaseEmitter createEmitter(float[] emitterData) {
        return new PointEmitter(emitterData);
    }

    public PointLiquidParticleWrapper(GameEntity gameEntity, Color color, Vector2 point, int lowerRate, int higherRate) {
        super(gameEntity,new float[]{point.x,point.y},null,color,lowerRate,higherRate);
    }

}