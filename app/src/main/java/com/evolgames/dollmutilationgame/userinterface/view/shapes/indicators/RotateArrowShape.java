package com.evolgames.dollmutilationgame.userinterface.view.shapes.indicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.userinterface.view.shapes.indicators.strategy.TransformationStrategy;
import com.evolgames.dollmutilationgame.scenes.EditorScene;
import com.evolgames.dollmutilationgame.userinterface.model.PointsModel;
import com.evolgames.dollmutilationgame.utilities.GeometryUtils;

import java.util.ArrayList;
import java.util.List;

public class RotateArrowShape extends FixedLengthArrowShape {

    private final TransformationStrategy transformationStrategy;
    private final Vector2 rotationCenter;

    public RotateArrowShape(
            Vector2 begin, PointsModel<?> shapePointsModel, EditorScene scene, float length) {
        super(begin, scene, length);

        rotationCenter = begin.cpy();

        this.transformationStrategy =
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
                        ArrayList<Vector2> newPoints = new ArrayList<>();
                        float angle = (float) Math.atan2(direction.y, direction.x);
                        for (Vector2 p : points) {
                            Vector2 pCopy = p.cpy();
                            GeometryUtils.rotate(pCopy, angle, rotationCenter);
                            newPoints.add(pCopy);
                        }
                        return newPoints;
                    }

                    @Override
                    protected Vector2 transformCenter() {
                        float angle = (float) Math.atan2(direction.y, direction.x);
                        Vector2 pCopy = center.cpy();
                          GeometryUtils.rotate(pCopy,angle, rotationCenter);
                          return pCopy;
                    }
                };
    }

    @Override
    public void updateEnd(float x, float y) {
        super.updateEnd(x, y);

        transformationStrategy.transform();
    }
}
