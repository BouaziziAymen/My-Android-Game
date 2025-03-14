package com.evolgames.dollmutilationgame.entities.particles.wrappers;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.entities.particles.emitters.ClusterEmitter;
import com.evolgames.dollmutilationgame.entities.particles.emitters.DataEmitter;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.utilities.GeometryUtils;

import org.andengine.util.adt.color.Color;

public class ClusterLiquidParticleWrapper extends LiquidParticleWrapper {
    public ClusterLiquidParticleWrapper(
            GameEntity gameEntity,
            Color color,
            float flammability,
            float[] data,
            float[] weights,
            Vector2 splashVelocity,
            int lowerRate,
            int higherRate) {
        super(gameEntity, data, weights, color, flammability, splashVelocity, lowerRate, higherRate);
    }

    @Override
    protected DataEmitter createEmitter(float[] emitterData, float[] weights, GameEntity gameEntity) {
        Vector2 center = GeometryUtils.calculateCentroid(emitterData);
        return new ClusterEmitter(gameEntity, center.x, center.y, emitterData, weights);
    }

}
