package com.evolgames.entities.particles.wrappers;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.cut.FreshCut;
import com.evolgames.entities.particles.emitters.ClusterEmitter;
import com.evolgames.entities.particles.emitters.AbsoluteEmitter;
import com.evolgames.helpers.utilities.GeometryUtils;

import org.andengine.util.adt.color.Color;

public class ClusterLiquidParticleWrapper extends  DataLiquidParticleWrapper{
    public ClusterLiquidParticleWrapper(GameEntity gameEntity, LayerBlock layerBlock, Color color, float[] data, Vector2 splashVelocity, int lowerRate, int higherRate) {
        super(gameEntity, layerBlock,data,splashVelocity,color, lowerRate, higherRate);
    }

    @Override
    protected AbsoluteEmitter createEmitter(float[] emitterData) {
        Vector2 center = GeometryUtils.calculateCentroid(emitterData);
        return new ClusterEmitter(center.x,center.y,emitterData);
    }
}
