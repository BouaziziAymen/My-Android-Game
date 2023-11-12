package com.evolgames.userinterface.view.shapes.indicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.scenes.EditorScene;
import org.andengine.extension.physics.box2d.util.Vector2Pool;

public class ArrowShape extends LineShape {

  public static float[][] arrowHeadMeasures = new float[][] {{16, 6}, {8, 3}};
  private final float getArrowHeadLength;
  private final float arrowHeadWidth;

  public ArrowShape(Vector2 begin, EditorScene scene, int size) {
    super(begin, scene);
    getArrowHeadLength = arrowHeadMeasures[size][0];
    arrowHeadWidth = arrowHeadMeasures[size][1];
  }

  public ArrowShape(Vector2 begin, EditorScene scene) {
    this(begin, scene, 0);
  }

  @Override
  protected void onExtremityUpdated() {
    super.onExtremityUpdated();
    drawArrow();
  }

  public void drawSelf() {
    drawLine();
    drawArrow();
  }

  public void drawArrow() {
    float nx = -direction.y;
    float ny = direction.x;

    Vector2 n = Vector2Pool.obtain(nx, ny);
    Vector2 p =
        Vector2Pool.obtain(end)
            .sub(direction.x * getArrowHeadLength, direction.y * getArrowHeadLength);
    Vector2 p1 = Vector2Pool.obtain(p).add(n.x * arrowHeadWidth, n.y * arrowHeadWidth);
    Vector2 p2 = Vector2Pool.obtain(p).sub(n.x * 6, n.y * arrowHeadWidth);

    lineStrip.add(p1.x, p1.y);
    lineStrip.add(p2.x, p2.y);
    lineStrip.add(end.x, end.y);

    Vector2Pool.recycle(p);
    Vector2Pool.recycle(p1);
    Vector2Pool.recycle(p2);
    Vector2Pool.recycle(n);
  }
}
