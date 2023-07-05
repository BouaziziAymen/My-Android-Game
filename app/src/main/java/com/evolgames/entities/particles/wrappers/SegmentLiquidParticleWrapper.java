package com.evolgames.entities.particles.wrappers;

import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.cut.FreshCut;
import com.evolgames.entities.particles.emitters.AbsoluteEmitter;
import com.evolgames.entities.particles.emitters.SegmentEmitter;

import org.andengine.util.adt.color.Color;

public class SegmentLiquidParticleWrapper extends DataLiquidParticleWrapper{
    public SegmentLiquidParticleWrapper(GameEntity gameEntity, LayerBlock layerBlock, float[] emitterData, Color color, int lowerRate, int higherRate) {
        super(gameEntity, layerBlock,emitterData,null, color, lowerRate, higherRate);
    }

    @Override
    protected AbsoluteEmitter createEmitter(float[] emitterData) {
        return new SegmentEmitter(emitterData);
    }
}
