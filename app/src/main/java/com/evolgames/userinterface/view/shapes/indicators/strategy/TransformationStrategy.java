package com.evolgames.userinterface.view.shapes.indicators.strategy;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.userinterface.model.PointsModel;
import java.util.List;
import java.util.stream.Collectors;

public abstract class TransformationStrategy {
  protected final PointsModel<?> shapePointsModel;
  protected final List<Vector2> originalPoints;
  protected final List<Vector2> originalReferencePoints;

  public TransformationStrategy(PointsModel<?> shapePointsModel) {
    this.shapePointsModel = shapePointsModel;
    this.originalPoints =
        shapePointsModel.getPoints().stream().map(Vector2::cpy).collect(Collectors.toList());
    this.originalReferencePoints =
        shapePointsModel.getReferencePoints().stream()
            .map(Vector2::cpy)
            .collect(Collectors.toList());
  }

  public TransformationStrategy(PointsModel<?> shapePointsModel, List<Vector2> originalPoints) {
    this.shapePointsModel = shapePointsModel;
    this.originalPoints = originalPoints;
    this.originalReferencePoints =
        shapePointsModel.getReferencePoints().stream()
            .map(Vector2::cpy)
            .collect(Collectors.toList());
  }

  public void transform() {
    List<Vector2> newPoints = transformPoints();

    if (!testPoints(newPoints)) {
      return;
    }
    List<Vector2> newReferencePoints = transformReferencePoints();
    shapePointsModel.getPointsShape().detachPointImages();
    shapePointsModel.getPointsShape().detachReferencePointImages();
    shapePointsModel.setPoints(newPoints);
    shapePointsModel.setReferencePoints(newReferencePoints);
    shapePointsModel.getPointsShape().onModelUpdated();
  }

  protected abstract boolean testPoints(List<Vector2> transformedPoints);

  protected abstract List<Vector2> transformPoints();

  protected abstract List<Vector2> transformReferencePoints();
}
