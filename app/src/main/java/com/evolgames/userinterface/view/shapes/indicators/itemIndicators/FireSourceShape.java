package com.evolgames.userinterface.view.shapes.indicators.itemIndicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.scenes.EditorScene;
import com.evolgames.userinterface.model.toolmodels.FireSourceModel;
import com.evolgames.userinterface.view.EditorUserInterface;
import com.evolgames.userinterface.view.shapes.indicators.AngleIndicator;
import com.evolgames.userinterface.view.shapes.indicators.MovablesContainer;
import com.evolgames.userinterface.view.shapes.points.ControllerPointImage;
import com.evolgames.userinterface.view.shapes.points.PointImage;
import java.util.ArrayList;
import java.util.List;

public class FireSourceShape extends AngleIndicator implements MovablesContainer {
  private final EditorUserInterface editorUserInterface;
  private final ControllerPointImage originPoint;
  private final ControllerPointImage extentPoint;
  private FireSourceModel model;
  private float extent;

  public FireSourceShape(Vector2 begin, EditorScene scene) {
    super(begin, scene, 32, 1);

    this.editorUserInterface = scene.getUserInterface();
    this.originPoint =
        new ControllerPointImage(
            ResourceManager.getInstance().fireShapeTextureRegion, begin.cpy()) {
          @Override
          protected void performControl(float dx, float dy) {
            float x = FireSourceShape.this.begin.x;
            float y = FireSourceShape.this.begin.y;
            FireSourceShape.this.updateBegin(x + dx, y + dy);
            FireSourceShape.this.drawSelf();
          }
        };

    this.direction = new Vector2(1, 0);
    this.editorUserInterface.addElement(originPoint);
    this.extent = 12f;
    this.extentPoint =
        new ControllerPointImage(
            ResourceManager.getInstance().diamondTextureRegion, new Vector2(begin)) {
          @Override
          protected void performControl(float dx, float dy) {
            extent+=dy;
            FireSourceShape.this.updateExtent();
            FireSourceShape.this.drawSelf();
          }
        };
    this.editorUserInterface.addElement(extentPoint);
    editorUserInterface.setUpdated(true);
  }

  @Override
  public void updateBegin(float x, float y) {
    super.updateBegin(x, y);
    this.model.getProperties().setFireSourceOrigin(new Vector2(x,y));
    updateExtent();
  }

  public Vector2 getBegin() {
    return this.begin;
  }

  public Vector2 getDirection() {
    return this.direction;
  }

  @Override
  public void select() {
    super.select();
    originPoint.select();
    originPoint.setDepth(2);
    setVisible(true);
  }

  @Override
  public void release() {
    super.release();
    originPoint.release();
    originPoint.setDepth(1);
    this.setVisible(false);
  }

  @Override
  public void setVisible(boolean b) {
    super.setVisible(b);
    this.extentPoint.setVisible(b);
  }

  @Override
  public List<PointImage> getMovables(boolean moveLimits) {
    ArrayList<PointImage> movables = new ArrayList<>();
    movables.add(getLimit());
    movables.add(originPoint);
    movables.add(extentPoint);
    return movables;
  }

  @Override
  public void detach() {
    super.detach();
    editorUserInterface.removeElement(originPoint);
    editorUserInterface.removeElement(extentPoint);
  }

  @Override
  public void onTurnAroundCommand(float dA) {
    turnAround(dA);
    onTurnAround();
  }

  @Override
  public void updateDirection(Vector2 direction) {
    super.updateDirection(direction);
    model.getFireSourceProperties().setFireSourceDirection(direction);
    updateExtent();
  }

  private void updateExtent() {
    extentPoint.setPosition(begin.x - extent*direction.y, begin.y + extent*direction.x);
    extentPoint.setUpdated(true);
  }

  @Override
  public void updateEnd(float x, float y) {
    super.updateEnd(x, y);
    updateExtent();
    this.model.getProperties().setFireSourceDirection(direction);
  }

  @Override
  public void drawLine() {
    while (lineStrip.getIndex() != 0) {
      lineStrip.shift();
      lineStrip.setIndex(lineStrip.getIndex() - 1);
    }
    lineStrip.add(extentPoint.getPoint().x,extentPoint.getPoint().y);
    lineStrip.add(begin.x, begin.y);
    lineStrip.add(end.x, end.y);
    lineStrip.setColor(mRed, mGreen, mBlue);
    lineStrip.setVisible(visible);
  }

  private void onTurnAround() {
    model.getProperties().getFireSourceDirection().set(direction);
  }

  @Override
  public void onControllerMoved(float dx, float dy) {
    super.onControllerMoved(dx, dy);
  }

  public FireSourceModel getModel() {
    return model;
  }

  public void bindModel(FireSourceModel model) {
    this.model = model;
    updateDirection(model.getProperties().getFireSourceDirection());
    Vector2 begin = model.getProperties().getFireSourceOrigin();
    this.originPoint.setPosition(begin.x,begin.y);
    updateBegin(begin.x,begin.y);
    this.drawSelf();

    model.setFireSourceShape(this);
    onTurnAround();
  }
}
