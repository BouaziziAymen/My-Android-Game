package com.evolgames.entities.cut;

import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.physics.PhysicsConstants;

public abstract class FreshCut {
    private boolean alive = true;
    protected float limit;

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    protected float computeLimit(LayerBlock block) {
        return block.getProperties().getJuicinessLowerPressure() * getLength() * PhysicsConstants.BLEEDING_CONSTANT;
    }

    public abstract float getLength();

    public void decrementLimit() {
        limit-=getLength();
    }

    public float getLimit() {
        return limit;
    }
}
