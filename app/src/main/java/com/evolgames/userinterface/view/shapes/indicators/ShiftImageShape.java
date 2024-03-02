package com.evolgames.userinterface.view.shapes.indicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.scenes.EditorScene;
import com.evolgames.userinterface.view.shapes.ImageShape;
import org.andengine.extension.physics.box2d.util.Vector2Pool;

public class ShiftImageShape extends LineShape {

  private final ImageShape imageShape;
  private final Vector2 imagePosition;

  public ShiftImageShape(Vector2 begin, ImageShape imageShape, EditorScene scene) {
    super(begin, scene);
    this.imageShape = imageShape;
    imagePosition = new Vector2(imageShape.getSprite().getX(), imageShape.getSprite().getY());
  }

  @Override
  public void updateEnd(float x, float y) {
    super.updateEnd(x, y);
    Vector2 displacement = Vector2Pool.obtain(end).sub(begin);
    imageShape
        .updatePosition(imagePosition.x + displacement.x, imagePosition.y + displacement.y);
    imageShape.updateSelf();
  }
}
