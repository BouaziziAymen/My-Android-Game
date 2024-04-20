package com.evolgames.userinterface.view.shapes.points;

import com.evolgames.activity.ResourceManager;
import com.evolgames.userinterface.view.basics.Image;

import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.opengl.texture.region.ITextureRegion;

public class SceneImage extends Image {

    public SceneImage(ITextureRegion pTextureRegion) {
        super(pTextureRegion);
    }

    @Override
    public void drawSelf(SpriteBatch hudBatcher, SpriteBatch sceneBatcher) {
        sceneBatcher.draw(
                textureRegion,
                getAbsoluteX(),
                getAbsoluteY(),
                getWidth(),
                getHeight(),
                scaleX,
                scaleY,
                getRed(),
                getGreen(),
                getBlue(),
                1f);
    }
}
