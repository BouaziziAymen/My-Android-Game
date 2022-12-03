package com.evolgames.userinterface.view.shapes.indicators.strategy;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.userinterface.model.PointsModel;

import java.util.List;

public abstract class TransformationStrategy {
    private final PointsModel<?> shapePointsModel;
    protected final List<Vector2> originalPoints;

    public TransformationStrategy(PointsModel<?> shapePointsModel, List<Vector2> originalPoints) {
        this.shapePointsModel = shapePointsModel;
        this.originalPoints = originalPoints;
    }

    public void transform() {
        List<Vector2> newPoints = transformPoints(originalPoints);
        if (!testPoints(newPoints)) {
            return;
        }
        shapePointsModel.getPointsShape().detachPointImages();
        shapePointsModel.setPoints(newPoints);
        shapePointsModel.getPointsShape().onModelUpdated();
    }

    protected abstract boolean testPoints(List<Vector2> transformedPoints);

    protected abstract List<Vector2> transformPoints(List<Vector2> originalPoints);

}
