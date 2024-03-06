package com.evolgames.userinterface.view.shapes.indicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.utilities.GeometryUtils;
import com.evolgames.scenes.EditorScene;
import com.evolgames.userinterface.model.PointsModel;
import com.evolgames.userinterface.view.shapes.indicators.strategy.TransformationStrategy;
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
        new TransformationStrategy(shapePointsModel,false) {
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
              Vector2 pp = p.cpy();
              GeometryUtils.rotate(pp, angle, rotationCenter);
              newPoints.add(pp);
            }
            return newPoints;
          }

          @Override
          protected List<Vector2> transformReferencePoints() {
            return transform(originalReferencePoints);
          }
        };
  }

  @Override
  public void updateEnd(float x, float y) {
    super.updateEnd(x, y);

    transformationStrategy.transform();
  }
}
