package com.evolgames.entities.blocks;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.properties.StainProperties;
import com.evolgames.factories.MeshFactory;
import com.evolgames.helpers.utilities.Utils;

import org.andengine.opengl.texture.region.ITextureRegion;

public final class StainBlock extends AssociatedBlock<StainBlock, StainProperties> {


    private float[] data;


    public ITextureRegion getTextureRegion() {
        return getProperties().getTextureRegion();
    }

    public float getLocalCenterX() {
        return getProperties().getLocalCenter().x;
    }

    public float getLocalCenterY() {
        return getProperties().getLocalCenter().y;
    }

    public float getLocalRotation() {
        return getProperties().getRotation();
    }

    @Override
    protected void calculateArea() {

    }

    @Override
    protected boolean shouldCalculateArea() {
        return false;
    }

    @Override
    protected StainBlock createChildBlock() {
        return new StainBlock();
    }

    public float[] getData() {
        return data;
    }

    public void setData(float[] meshData) {
        data = meshData;
    }

    public void computeData() {
        data = MeshFactory.getInstance().getStainData(this);
    }

    @Override
    public void translate(Vector2 t) {
        Utils.translatePoints(getVertices(), t);
        computeTriangles();
        getProperties().setLocalCenter(getLocalCenterX() - t.x, getLocalCenterY() - t.y);
        computeData();
    }

    @Override
    protected StainBlock getThis() {
        return this;
    }
}
