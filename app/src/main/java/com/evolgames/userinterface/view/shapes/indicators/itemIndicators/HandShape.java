package com.evolgames.userinterface.view.shapes.indicators.itemIndicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.scenes.EditorScene;
import com.evolgames.userinterface.model.toolmodels.HandModel;
import com.evolgames.userinterface.view.EditorUserInterface;
import com.evolgames.userinterface.view.shapes.indicators.AngleIndicator;
import com.evolgames.userinterface.view.shapes.indicators.MovablesContainer;
import com.evolgames.userinterface.view.shapes.points.ControllerPointImage;
import com.evolgames.userinterface.view.shapes.points.PointImage;
import java.util.ArrayList;
import java.util.List;

public class HandShape extends AngleIndicator implements MovablesContainer {
  private final EditorUserInterface editorUserInterface;
  private final ControllerPointImage originPoint;
  private HandModel model;

  public HandShape(Vector2 begin, EditorScene scene) {
    super(begin, scene, 32, 1);

    this.editorUserInterface = scene.getUserInterface();
    originPoint =
        new ControllerPointImage(ResourceManager.getInstance().squareTextureRegion, begin.cpy()) {

          @Override
          protected void performControl(float dx, float dy) {
            float x = HandShape.this.begin.x;
            float y = HandShape.this.begin.y;
            HandShape.this.updateBegin(x + dx, y + dy);
            HandShape.this.drawSelf();
          }
        };
    originPoint.setScale(0.5f, 0.5f);
    editorUserInterface.addElement(originPoint);

    editorUserInterface.setUpdated(true);
  }

  @Override
  public void select() {
    super.select();
    originPoint.select();
  }

  @Override
  public void release() {
    super.release();
    originPoint.release();
  }

  @Override
  public List<PointImage> getMovables(boolean moveLimits) {
    ArrayList<PointImage> movables = new ArrayList<>();
    movables.add(getLimit());
    movables.add(originPoint);
    return movables;
  }

  @Override
  public void detach() {
    super.detach();
    editorUserInterface.removeElement(originPoint);
  }

  @Override
  public void onTurnAroundCommand(float dA) {
    turnAround(dA);
  }

  public HandModel getModel() {
    return model;
  }

  public void setModel(HandModel model) {
    this.model = model;
  }

  public Vector2 getCenter() {
    return originPoint.getPoint();
  }

  public Vector2 getDir() {
    return direction;
  }
}
