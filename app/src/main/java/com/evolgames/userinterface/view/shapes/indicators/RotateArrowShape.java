package com.evolgames.userinterface.view.shapes.indicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.scenes.GameScene;
import com.evolgames.userinterface.model.PointsModel;
import com.evolgames.userinterface.view.shapes.indicators.strategy.TransformationStrategy;

import java.util.ArrayList;
import java.util.List;

public class RotateArrowShape extends FixedLengthArrowShape {

    private final TransformationStrategy transformationStrategy;
    private final Vector2 rotationCenter;

    public RotateArrowShape(Vector2 begin, PointsModel<?> shapePointsModel, GameScene scene, float length) {
        super(begin, scene, length);
        ArrayList<Vector2> points = new ArrayList<>();
        for (Vector2 p : shapePointsModel.getModelPoints()) {
            points.add(p.cpy());
        }

        rotationCenter = begin.cpy();

        this.transformationStrategy = new TransformationStrategy(shapePointsModel, points) {
            @Override
            protected boolean testPoints(List<Vector2> transformedPoints) {
                return shapePointsModel.test(transformedPoints);
            }

            @Override
            protected List<Vector2> transformPoints(List<Vector2> originalPoints) {
                ArrayList<Vector2> newPoints = new ArrayList<>();
                float angle = (float) Math.atan2(direction.y, direction.x);

                for (Vector2 p : originalPoints) {
                    Vector2 pp = p.cpy();
                    GeometryUtils.rotate(pp, angle, rotationCenter);
                    newPoints.add(pp);
                }
                return newPoints;
            }
        };
    }


    @Override
    public void updateEnd(float x, float y) {
        super.updateEnd(x, y);

        transformationStrategy.transform();
    }

}

