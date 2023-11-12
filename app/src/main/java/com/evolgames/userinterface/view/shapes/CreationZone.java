package com.evolgames.userinterface.view.shapes;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.userinterface.control.CreationZoneController;
import com.evolgames.userinterface.view.shapes.points.ReferencePointImage;
import java.util.ArrayList;
import org.andengine.input.touch.TouchEvent;

public class CreationZone {

  private final CreationZoneController creationZoneController;
  private boolean touchLocked;
  private final ArrayList<ReferencePointImage> referencePointImageArrayList = new ArrayList<>();

  public CreationZone(CreationZoneController controller) {

    this.creationZoneController = controller;
  }

  public boolean isTouchLocked() {
    return touchLocked;
  }

  public void setTouchLocked(boolean b) {
    this.touchLocked = b;
  }

  public Vector2 applyMagnet(float x, float y) {

    for (ReferencePointImage referencePointImage : referencePointImageArrayList) {
      Vector2 v = referencePointImage.getPoint();
      if (v.dst(x, y) < 32) return v;
    }
    return new Vector2(x, y);
  }

  public ArrayList<ReferencePointImage> getReferencePointImageArrayList() {
    return referencePointImageArrayList;
  }

  public void onTouchScene(TouchEvent pTouchEvent, boolean scroll) {

    Vector2 touch =
        (creationZoneController.isMagnet())
            ? applyMagnet(pTouchEvent.getX(), pTouchEvent.getY())
            : new Vector2(pTouchEvent.getX(), pTouchEvent.getY());

    if (pTouchEvent.isActionUp()) {
      creationZoneController.onZoneActionUp(touch.x, touch.y);
    }

    if (pTouchEvent.isActionDown()) {
      creationZoneController.onZoneActionDown(touch.x, touch.y);
    }

    if (pTouchEvent.isActionMove()) {
      creationZoneController.onZoneActionMove(touch.x, touch.y);
    }
  }
}
