package com.evolgames.entities.blocks;

import com.evolgames.entities.properties.DecorationProperties;

public final class DecorationBlockConcrete extends DecorationBlock<DecorationBlockConcrete, DecorationProperties>{



    @Override
    protected void calculateArea() {

    }

    @Override
    protected boolean calcArea() {
        return false;
    }

    @Override
    protected DecorationBlockConcrete createChildBlock() {
        return new DecorationBlockConcrete();
    }

    @Override
    protected DecorationBlockConcrete getThis() {
        return this;
    }
}
