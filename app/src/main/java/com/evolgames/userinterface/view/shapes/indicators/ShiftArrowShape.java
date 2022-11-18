package com.evolgames.userinterface.view.shapes.indicators;

import com.badlogic.gdx.math.Vector2;

import com.evolgames.scenes.GameScene;
import com.evolgames.userinterface.model.PointsModel;

import org.andengine.extension.physics.box2d.util.Vector2Pool;

import java.util.ArrayList;
import java.util.List;


public class ShiftArrowShape extends LineShape {
    private final ArrayList<Vector2> points;
    private final PointsModel<?> shapePointsModel;
    private Vector2 centerOfShape;

    public ShiftArrowShape(Vector2 begin, PointsModel<?> shapePointsModel, GameScene scene) {
        super(begin, scene);
        points = new ArrayList<>();
        for (Vector2 p : shapePointsModel.getModelPoints())
            points.add(p.cpy());
        this.shapePointsModel = shapePointsModel;
        if (shapePointsModel.getCenter() != null)
            centerOfShape = shapePointsModel.getCenter().cpy();
    }

    @Override
    public void updateEnd(float x, float y) {
        super.updateEnd(x, y);

        List<Vector2> shapePoints = shapePointsModel.getModelPoints();
        Vector2 displacement = Vector2Pool.obtain(end).sub(begin);

        if (shapePointsModel.getCenter() != null) {
            shapePointsModel.getPointsShape().getCenterPointImage().setPosition(centerOfShape.x + displacement.x, centerOfShape.y + displacement.y);
            shapePointsModel.getCenter().set(centerOfShape.x + displacement.x, centerOfShape.y + displacement.y);
        }

        ArrayList<Vector2> copyPoints = new ArrayList<>();
        for (int i = 0; i < shapePoints.size(); i++) {
            Vector2 originalPoint = shapePoints.get(i);
            copyPoints.add(Vector2Pool.obtain(originalPoint));
            float ix = points.get(i).x + displacement.x;
            float iy = points.get(i).y + displacement.y;
            originalPoint.set(ix, iy);
        }
        boolean test = shapePointsModel.test(shapePoints);

        if (!test) {
            for (int i = 0; i < shapePoints.size(); i++) {
                Vector2 originalPoint = shapePoints.get(i);
                originalPoint.set(copyPoints.get(i));
            }
            return;
        }

        shapePointsModel.getPointsShape().detachPointImages();
        shapePointsModel.getPointsShape().onModelUpdated();
    }

}
