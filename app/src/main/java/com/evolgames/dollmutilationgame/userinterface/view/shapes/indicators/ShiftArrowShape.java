package com.evolgames.dollmutilationgame.userinterface.view.shapes.indicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.userinterface.view.shapes.indicators.strategy.TransformationStrategy;
import com.evolgames.dollmutilationgame.scenes.EditorScene;
import com.evolgames.dollmutilationgame.userinterface.model.PointsModel;

import org.andengine.extension.physics.box2d.util.Vector2Pool;

import java.util.ArrayList;
import java.util.List;

public class ShiftArrowShape extends LineShape {
    private final TransformationStrategy transformationStrategy;

    public ShiftArrowShape(Vector2 begin, PointsModel<?> shapePointsModel, EditorScene scene) {
        super(begin, scene);
        transformationStrategy =
                new TransformationStrategy(shapePointsModel, false) {
                    @Override
                    protected boolean testPoints(List<Vector2> transformedPoints) {
                        return shapePointsModel.testPoints(transformedPoints);
                    }

                    @Override
                    protected List<Vector2> transformPoints() {
                        return transform(originalPoints);
                    }

                    private List<Vector2> transform(List<Vector2> points) {
                        Vector2 displacement = Vector2Pool.obtain(end).sub(begin);
                        ArrayList<Vector2> newPoints = new ArrayList<>();
                        for (int i = 0; i < points.size(); i++) {
                            float ix = points.get(i).x + displacement.x;
                            float iy = points.get(i).y + displacement.y;
                            newPoints.add(Vector2Pool.obtain(ix, iy));
                        }
                        return newPoints;
                    }

                    @Override
                    protected Vector2 transformCenter() {
                        Vector2 displacement = Vector2Pool.obtain(end).sub(begin);
                        float ix = center.x + displacement.x;
                        float iy = center.y + displacement.y;
                       return new Vector2(ix,iy);
                    }
                };
    }

    @Override
    public void updateEnd(float x, float y) {
        super.updateEnd(x, y);
        transformationStrategy.transform();
    }
}
