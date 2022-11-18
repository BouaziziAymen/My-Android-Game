package com.evolgames.userinterface.view.shapes.points;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.userinterface.view.shapes.PointsShape;

import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;

public class ModelPointImage extends PointImage {

    private final PointsShape pointsShape;


    public ModelPointImage(PointsShape pointsShape, ITextureRegion pTextureRegion, Vector2 p) {
        super(pTextureRegion, p);
        this.pointsShape = pointsShape;

    }


    @Override
    public void onControllerMoved(float dx, float dy) {
        if (!pointsShape.getOutlineModel().test(point, dx, dy)){
            return;
        }
        super.onControllerMoved(dx,dy);
        pointsShape.onModelUpdated();
    }


    @Override
    public boolean onTouchHud(TouchEvent pTouchEvent, boolean isTouched) {
        return false;
    }
}
