package com.evolgames.userinterface.view.shapes.points;

import com.badlogic.gdx.math.Vector2;

import org.andengine.opengl.texture.region.ITextureRegion;

public abstract class ControllerPointImage extends PointImage {
    protected ControllerPointImage(ITextureRegion pTextureRegion, Vector2 point) {
        super(pTextureRegion, point);
    }

    protected abstract void performControl(float dx, float dy);

    @Override
    public void onControllerMoved(float dx, float dy) {
        super.onControllerMoved(dx, dy);
        performControl(dx, dy);
    }
}
