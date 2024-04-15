package com.evolgames.userinterface.view.shapes.indicators.strategy;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.userinterface.model.PointsModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class TransformationStrategy {
    protected final PointsModel<?> shapePointsModel;
    protected final List<Vector2> originalPoints;
    protected final Vector2 center;
    private final boolean append;

    public TransformationStrategy(PointsModel<?> shapePointsModel, boolean append) {
        this.shapePointsModel = shapePointsModel;
        this.originalPoints = shapePointsModel.getPoints().stream().map(Vector2::cpy).collect(Collectors.toList());
        this.center = shapePointsModel.getCenter();
        this.append = append;
    }

    private List<Vector2> collect(List<Vector2> transformed, List<Vector2> original) {
        List<Vector2> result = new ArrayList<>();
        if (append) {
            result.addAll(original);
            result.addAll(transformed);
        } else {
            result.addAll(transformed);
        }
        return result;
    }

    public void transform() {
        List<Vector2> newPoints = collect(transformPoints(), originalPoints);
        if (testPoints(newPoints)) {

            shapePointsModel.getPointsShape().detachPointImages();
            shapePointsModel.getPointsShape().detachReferencePointImage();
            shapePointsModel.setPoints(newPoints);
            if(center!=null) {
                Vector2 newCenter = transformCenter();
                shapePointsModel.setCenter(newCenter);
                shapePointsModel.getPointsShape().createReferencePointImage(newCenter);
            }
            shapePointsModel.getPointsShape().onModelUpdated();
        }
    }

    protected abstract boolean testPoints(List<Vector2> transformedPoints);

    protected abstract List<Vector2> transformPoints();

    protected abstract Vector2 transformCenter();
}
