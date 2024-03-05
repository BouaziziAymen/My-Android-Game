package com.evolgames.userinterface.view.shapes.indicators.itemIndicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.activity.ResourceManager;
import com.evolgames.scenes.EditorScene;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.view.EditorUserInterface;
import com.evolgames.userinterface.view.shapes.indicators.MovablesContainer;
import com.evolgames.userinterface.view.shapes.indicators.VectorIndicator;
import com.evolgames.userinterface.view.shapes.points.ControllerPointImage;
import com.evolgames.userinterface.view.shapes.points.PointImage;
import java.util.ArrayList;
import java.util.List;

public class ProjectileShape extends VectorIndicator implements MovablesContainer {
  private final EditorUserInterface editorUserInterface;
  private final ControllerPointImage originPoint;
  private ProjectileModel model;

  public ProjectileShape(Vector2 begin, EditorScene scene) {
    super(begin, scene, 1);

    this.editorUserInterface = scene.getUserInterface();
    this.originPoint =
        new ControllerPointImage(
            ResourceManager.getInstance().targetShapeTextureRegion, begin.cpy()) {
          @Override
          protected void performControl(float dx, float dy) {
            float x = ProjectileShape.this.begin.x;
            float y = ProjectileShape.this.begin.y;
            model.getProperties().getProjectileOrigin().set(x, y);
            ProjectileShape.this.updateBegin(x + dx, y + dy);
            ProjectileShape.this.drawSelf();
          }
        };
    this.direction = new Vector2();
    this.editorUserInterface.addElement(originPoint);

    editorUserInterface.setUpdated(true);
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
    super.setVisible(false);
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
  public void setVisible(boolean b) {
    super.setVisible(b);
    this.originPoint.setVisible(b);
  }

  @Override
  public void onControllerMoved(float dx, float dy) {
    super.onControllerMoved(dx, dy);
    this.model.getProperties().getProjectileEnd().set(end.x, end.y);
  }

  @Override
  public void updateEnd(float x, float y) {
    super.updateEnd(x, y);
    this.model.getProperties().getProjectileEnd().set(end.x, end.y);
  }

  public ProjectileModel getModel() {
    return model;
  }

  public void bindModel(ProjectileModel model) {
    this.model = model;
    Vector2 begin = model.getProperties().getProjectileOrigin();
    updateBegin(begin.x,begin.y);
    updateEnd(
        model.getProperties().getProjectileEnd().x, model.getProperties().getProjectileEnd().y);
    model.setProjectileShape(this);
  }
}
