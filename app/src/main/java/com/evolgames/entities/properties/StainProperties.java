package com.evolgames.entities.properties;

import com.badlogic.gdx.math.Vector2;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.adt.color.Color;

public class StainProperties extends ColoredProperties {
    private final ITextureRegion textureRegion;
    private final Color color;
    private final Vector2 localCenter;
    private float rotation;
    public StainProperties(ITextureRegion textureRegion, Vector2 localCenter, float rotation, Color color) {
        this.textureRegion = textureRegion;
        this.localCenter = new Vector2(localCenter);
        this.rotation = rotation;
        this.color = color;
    }

    @Override
    public ColoredProperties copy() {
        return new StainProperties(textureRegion, new Vector2(localCenter), rotation, color);
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
