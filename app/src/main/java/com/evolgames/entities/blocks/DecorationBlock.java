package com.evolgames.entities.blocks;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.properties.DecorationProperties;
import com.evolgames.utilities.Utils;

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
    public void translate(Vector2 translationVector, Vector2 worldTranslation) {
        Utils.translatePoints(this.getVertices(), translationVector);
        computeTriangles();
    }

    @Override
    protected DecorationBlock getThis() {
        return this;
    }

}
