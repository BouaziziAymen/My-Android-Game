package com.evolgames.userinterface.view.shapes.points;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.userinterface.view.shapes.PointsShape;

import org.andengine.opengl.texture.region.ITextureRegion;

public class ModelPointImage extends PointImage {

    private final PointsShape pointsShape;

    public ModelPointImage(PointsShape pointsShape, ITextureRegion pTextureRegion, Vector2 p) {
        super(pTextureRegion, p);
        this.pointsShape = pointsShape;
    }

    @Override
    public void onControllerMoved(float dx, float dy) {
        if (pointsShape.getOutlineModel().testMove(point, dx, dy)) {
            super.onControllerMoved(dx, dy);
            pointsShape.getOutlineModel().updateOutlinePoints();
            pointsShape.onModelUpdated();
        } else {
            Vector2 d = new Vector2(dx, dy);
            float len = d.len();
            d.nor().mul(len / 2);
            if (d.len() > 0.02f) {
                this.onControllerMoved(d.x, d.y);
            }
        }
    }
}
