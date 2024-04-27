package com.evolgames.dollmutilationgame.entities.blocks;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.utilities.GeometryUtils;
import com.evolgames.dollmutilationgame.entities.factories.MeshFactory;
import com.evolgames.dollmutilationgame.entities.properties.StainProperties;

import org.andengine.opengl.texture.region.ITextureRegion;

public final class StainBlock extends AssociatedBlock<StainBlock, StainProperties> {

    private float[] data;
    private int priority;

    public ITextureRegion getTextureRegion() {
        return ResourceManager.getInstance()
                .stainTextureRegions
                .getTextureRegion(getProperties().getTextureRegionIndex());
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
        StainBlock child = new StainBlock();
        child.setPriority(priority);
        return child;
    }

    public float[] getData() {
        return data;
    }

    public void computeData() {
        data = MeshFactory.getInstance().getStainData(this);
    }

    @Override
    protected StainBlock getThis() {
        return this;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public void mirror() {
        super.mirror();
        Vector2 pos = GeometryUtils.mirrorPoint(getProperties().getLocalCenter());
        getProperties().setLocalCenter(pos);
        getProperties().setRotation(getLocalRotation());
        computeData();
    }
}
