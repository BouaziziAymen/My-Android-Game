package com.evolgames.dollmutilationgame.entities.blocks;

import com.evolgames.dollmutilationgame.entities.properties.DecorationProperties;

public final class DecorationBlock extends AssociatedBlock<DecorationBlock, DecorationProperties> {

    @Override
    protected void calculateArea() {
    }

    @Override
    protected boolean shouldCalculateArea() {
        return false;
    }

    @Override
    protected DecorationBlock createChildBlock() {
        return new DecorationBlock();
    }

    @Override
    protected DecorationBlock getThis() {
        return this;
    }

}
