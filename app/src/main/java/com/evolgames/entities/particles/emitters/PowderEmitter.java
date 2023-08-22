package com.evolgames.entities.particles.emitters;

import com.evolgames.entities.blocks.CoatingBlock;

import java.util.List;

public class PowderEmitter extends PolygonEmitter{
    public PowderEmitter(List<CoatingBlock> coatingBlockList) {
        super(coatingBlockList,CoatingBlock::isPulverized);
    }


}
