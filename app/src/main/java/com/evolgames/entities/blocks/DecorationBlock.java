package com.evolgames.entities.blocks;

import com.evolgames.entities.properties.DecorationProperties;

public final class DecorationBlock extends AssociatedBlock<DecorationBlock, DecorationProperties> {



    @Override
    protected void calculateArea() {

    }

    @Override
    protected boolean calcArea() {
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
