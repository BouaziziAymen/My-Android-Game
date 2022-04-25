package com.evolgames.entities.properties;

import com.badlogic.gdx.math.Vector2;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.adt.color.Color;

import is.kul.learningandengine.basics.Block;

public class BlockCProperties extends BlockProperties {
    public ITextureRegion getTextureRegion() {
        return textureRegion;
    }

    private ITextureRegion textureRegion;
    private Color color;

    public Color getColor() {
        return color;
    }

    public BlockCProperties(ITextureRegion textureRegion, Vector2 localCenter, float rotation, Color color) {
        this.textureRegion = textureRegion;
        this.properties = new float[]{localCenter.x,localCenter.y,rotation};
        this.color = color;
    }


    @Override
    public BlockProperties getCopy() {
        return new BlockCProperties(textureRegion,new Vector2(properties[0],properties[1]),properties[2],color);
    }
}
