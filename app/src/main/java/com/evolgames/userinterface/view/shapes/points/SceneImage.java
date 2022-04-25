package com.evolgames.userinterface.view.shapes.points;

import com.evolgames.userinterface.view.UserInterface;
import com.evolgames.userinterface.view.basics.Image;

import org.andengine.opengl.texture.region.ITextureRegion;

public class SceneImage extends Image {

    public SceneImage(ITextureRegion pTextureRegion) {
        super(pTextureRegion);
    }


    @Override
    public void drawSelf() {

        UserInterface.sceneBatcher.draw(textureRegion, getAbsoluteX(), getAbsoluteY(), getWidth(), getHeight(), scaleX, scaleY, getRed(), getGreen(), getBlue(), 1f);
    }

}
