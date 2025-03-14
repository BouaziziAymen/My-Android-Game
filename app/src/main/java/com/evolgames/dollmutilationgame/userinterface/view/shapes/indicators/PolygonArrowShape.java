package com.evolgames.dollmutilationgame.userinterface.view.shapes.indicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.entities.factories.VerticesFactory;
import com.evolgames.dollmutilationgame.scenes.EditorScene;
import com.evolgames.dollmutilationgame.userinterface.model.PointsModel;

import org.andengine.extension.physics.box2d.util.Vector2Pool;

import java.util.ArrayList;

public class PolygonArrowShape extends ArrowShape {

    private final boolean fixedRadius;
    private final float radius;
    private final PointsModel<?> shapePointsModel;
    private final int numberOfPoints;

    public PolygonArrowShape(
            Vector2 begin, PointsModel<?> shapePointsModel, EditorScene scene, int numberOfPoints) {
        super(begin, scene);
        this.shapePointsModel = shapePointsModel;
        this.shapePointsModel.setCenter(begin.cpy());
        this.numberOfPoints = numberOfPoints;
        this.fixedRadius = false;
        radius = 0;
    }

    public PolygonArrowShape(
            Vector2 begin,
            PointsModel<?> shapePointsModel,
            EditorScene scene,
            int numberOfPoints,
            float radius) {
        super(begin, scene);
        this.shapePointsModel = shapePointsModel;
        this.shapePointsModel.setCenter(begin.cpy());
        this.numberOfPoints = numberOfPoints;
        this.fixedRadius = true;
        this.radius = radius;
    }

    @Override
    public void updateEnd(float x, float y) {
        float d = (fixedRadius) ? radius : begin.dst(x, y);
        Vector2 dir = Vector2Pool.obtain(end).sub(begin);
        float angle = (float) Math.atan2(dir.y, dir.x);
        Vector2Pool.recycle(dir);
        ArrayList<Vector2> newPoints =
                VerticesFactory.createPolygon(begin.x, begin.y, angle, d, d, numberOfPoints);
        if (shapePointsModel.testPoints(newPoints)) {
            shapePointsModel.getPointsShape().detachPointImages();
            shapePointsModel.getPointsShape().detachReferencePointImage();
            super.updateEnd(x, y);
            Vector2 p = begin.cpy();
            shapePointsModel.getPointsShape().createReferencePointImage(p);
            shapePointsModel.setPoints(newPoints);
            shapePointsModel.getPointsShape().onModelUpdated();
        }
    }
}
