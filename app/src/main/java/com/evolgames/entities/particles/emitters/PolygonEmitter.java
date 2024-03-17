package com.evolgames.entities.particles.emitters;

import com.evolgames.entities.blocks.CoatingBlock;

import org.andengine.entity.particle.emitter.BaseParticleEmitter;

import java.util.List;
import java.util.function.Predicate;

public class PolygonEmitter extends BaseParticleEmitter {
    protected final RandomPointPicker randomPointPicker;

    public PolygonEmitter(List<CoatingBlock> coatingBlockList, Predicate<CoatingBlock> predicate) {
        super(0, 0);
        this.randomPointPicker = new RandomPointPicker(coatingBlockList, predicate);
    }

    @Override
    public void getPositionOffset(float[] pOffset) {
        this.randomPointPicker.getRandomPoint(pOffset);
    }

    public float getCoverageRatio() {
        return randomPointPicker.getCoverageRatio();
    }

    public CoatingBlock getActiveCoatingBlock() {
        return this.randomPointPicker.getActiveCoatingBlock();
    }
}
