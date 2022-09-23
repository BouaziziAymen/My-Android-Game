package com.evolgames.entities.particles;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.helpers.utilities.GeometryUtils;

import org.andengine.util.adt.color.Color;

public class SegmentLiquidParticleWrapper extends LiquidParticleWrapper {
    @Override
    protected MyBaseEmitter createEmitter(float[] emitterData) {
        return new SegmentEmitter(emitterData);
    }

    public SegmentLiquidParticleWrapper(GameEntity gameEntity, Color color, Vector2 first, Vector2 second, int lowerRate, int higherRate) {
        super(gameEntity,color,new float[]{first.x,first.y,second.x,second.y},lowerRate,higherRate);
    }


}
