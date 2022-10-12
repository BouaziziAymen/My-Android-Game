package com.evolgames.entities.blocks;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.properties.DecorationProperties;
import com.evolgames.helpers.utilities.Utils;
import com.evolgames.physics.WorldFacade;

import java.util.ArrayList;

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
    public void translate(Vector2 translationVector) {
        Utils.translatePoints(this.getVertices(), translationVector);
        computeTriangles();
    }

    @Override
    protected DecorationBlock getThis() {
        return this;
    }


    public void applyClip(ArrayList<Vector2> clipPath) {
        WorldFacade.getClipVisitor().setClipPath(clipPath);
        WorldFacade.getClipVisitor().visitTheElement(this);
        setVertices(WorldFacade.getClipVisitor().getResult());
    }
}
