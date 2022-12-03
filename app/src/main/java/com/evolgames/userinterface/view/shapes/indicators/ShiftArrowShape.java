package com.evolgames.userinterface.view.shapes.indicators;

import com.badlogic.gdx.math.Vector2;

import com.evolgames.scenes.GameScene;
import com.evolgames.userinterface.model.PointsModel;
import com.evolgames.userinterface.view.shapes.indicators.strategy.TransformationStrategy;

import org.andengine.extension.physics.box2d.util.Vector2Pool;

import java.util.ArrayList;
import java.util.List;


public class ShiftArrowShape extends LineShape {
    private final TransformationStrategy transformationStrategy;

    public ShiftArrowShape(Vector2 begin, PointsModel<?> shapePointsModel, GameScene scene) {
        super(begin, scene);
        ArrayList<Vector2> points = new ArrayList<>();
        for (Vector2 p : shapePointsModel.getModelPoints()) {
            points.add(p.cpy());
        }
        transformationStrategy = new TransformationStrategy(shapePointsModel, points) {
            @Override
            protected boolean testPoints(List<Vector2> transformedPoints) {
                return shapePointsModel.test(transformedPoints);
            }

            @Override
            protected List<Vector2> transformPoints(List<Vector2> originalPoints) {
                Vector2 displacement = Vector2Pool.obtain(end).sub(begin);
                ArrayList<Vector2> newPoints = new ArrayList<>();
                for (int i = 0; i < originalPoints.size(); i++) {
                    float ix = originalPoints.get(i).x + displacement.x;
                    float iy = originalPoints.get(i).y + displacement.y;
                    newPoints.add(Vector2Pool.obtain(ix, iy));
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
