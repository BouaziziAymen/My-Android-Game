package com.evolgames.entities.properties;

import com.badlogic.gdx.math.Vector2;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.adt.color.Color;

public class StainProperties extends ColoredProperties {
    private transient  ITextureRegion textureRegion;
    private  Color color;
    private  Vector2 localCenter;
    private  int textureRegionIndex;
    private float rotation;

    @SuppressWarnings("unused")
    public StainProperties(){}

    public StainProperties(ITextureRegion textureRegion, int textureRegionIndex, Vector2 localCenter, float rotation, Color color) {
        this.textureRegion = textureRegion;
        this.textureRegionIndex = textureRegionIndex;
        this.localCenter = new Vector2(localCenter);
        this.rotation = rotation;
        this.color = color;
    }

    @Override
    public ColoredProperties copy() {
        return new StainProperties(textureRegion, textureRegionIndex, new Vector2(localCenter), rotation, color);
    }

    public void setLocalCenter(float x, float y) {
        this.localCenter.set(x,y);
    }

    public ITextureRegion getTextureRegion() {
        return textureRegion;
    }

    public Color getColor() {
        return color;
    }

    public Vector2 getLocalCenter() {
        return localCenter;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
}
