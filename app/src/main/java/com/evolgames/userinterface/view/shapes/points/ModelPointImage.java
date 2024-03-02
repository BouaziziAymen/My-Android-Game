package com.evolgames.userinterface.view.shapes.points;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.userinterface.model.DecorationModel;
import com.evolgames.userinterface.model.LayerModel;
import com.evolgames.userinterface.view.shapes.PointsShape;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;

public class ModelPointImage extends PointImage {

  private final PointsShape pointsShape;

  public ModelPointImage(PointsShape pointsShape, ITextureRegion pTextureRegion, Vector2 p) {
    super(pTextureRegion, p);
    this.pointsShape = pointsShape;
  }

  @Override
  public void onControllerMoved(float dx, float dy) {
      Vector2 d = new Vector2(dx, dy);
      if (pointsShape.getOutlineModel().testMove(point, d.x, d.y)) {
        super.onControllerMoved(d.x, d.y);
        pointsShape.getOutlineModel().updateOutlinePoints();
        pointsShape.onModelUpdated();
      } else {
        if(d.len()>0.02f) {
          this.onControllerMoved(d.x / 2, d.y / 2);
        }
      }
  }
}
