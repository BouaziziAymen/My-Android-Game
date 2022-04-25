package com.evolgames.entities.blocks;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.properties.BlockCProperties;
import com.evolgames.factories.MeshFactory;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.helpers.utilities.Utils;
import com.evolgames.userinterface.view.layouts.Layout;

import org.andengine.opengl.texture.region.ITextureRegion;

import java.util.ArrayList;

public final class StainBlock extends DecorationBlock<StainBlock, BlockCProperties> {


    private float[] data;



    public ITextureRegion getTextureRegion() {
        return getProperties().getTextureRegion();
    }

    public float getLocalCenterX() {
        return getProperties().getProperties()[0];
    }

    public float getLocalCenterY() {
        return getProperties().getProperties()[1];
    }

    public float getLocalRotation() {
        return getProperties().getProperties()[2];
    }

    @Override
    protected void calculateArea() {

    }

    @Override
    protected boolean calcArea() {
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
       getProperties().getProperties()[0] -=  t.x;
       getProperties().getProperties()[1] -= t.y;
       computeData();
    }

    @Override
    protected StainBlock getThis() {
        return this;
    }
}
