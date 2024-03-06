package com.evolgames.userinterface.view.shapes.indicators.jointindicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.activity.ResourceManager;
import com.evolgames.utilities.MathUtils;
import com.evolgames.scenes.EditorScene;
import com.evolgames.userinterface.model.jointmodels.JointModel;
import com.evolgames.userinterface.view.Colors;
import com.evolgames.userinterface.view.EditorUserInterface;
import com.evolgames.userinterface.view.shapes.indicators.AngleIndicator;
import com.evolgames.userinterface.view.shapes.points.PointImage;
import java.util.List;

public class RevoluteJointShape extends JointShape {

  private final AngleIndicator referenceAngleIndicator;
  private final AngleIndicator upperAngleIndicator;
  private final AngleIndicator lowerAngleIndicator;
  private final EditorUserInterface editorUserInterface;

  public RevoluteJointShape(EditorScene scene, Vector2 begin) {
    super(
        scene.getUserInterface(),
        begin,
        scene,
        ResourceManager.getInstance().doubleDiskTextureRegion);
    this.editorUserInterface = scene.getUserInterface();

    referenceAngleIndicator =
        new AngleIndicator(begin, scene, 48) {

          @Override
          public void onTurnAroundCommand(float dA) {
            turnAround(dA);
            RevoluteJointShape.this.onReferenceIndicatorTurned(dA);
          }
        };

    upperAngleIndicator =
        new AngleIndicator(begin, scene, 64) {

          @Override
          public void onTurnAroundCommand(float dA) {
            // if upperAngle > lowerAngle
            if (dA > 0) turnAround(dA);
            else {
              float upperAngle = RevoluteJointShape.this.getUpperAngle();
              float lowerAngle = RevoluteJointShape.this.getLowerAngle();
              if (upperAngle + dA >= lowerAngle) super.turnAround(dA);
              else {
                turnAround(lowerAngle - upperAngle);
              }
            }
            RevoluteJointShape.this.onUpperIndicatorTurned();
          }
        };

    lowerAngleIndicator =
        new AngleIndicator(begin, scene, 64) {
          @Override
          public void onTurnAroundCommand(float dA) {
            float upperAngle = RevoluteJointShape.this.getUpperAngle();
            float lowerAngle = RevoluteJointShape.this.getLowerAngle();
            if (dA < 0 || lowerAngle + dA <= upperAngle) turnAround(dA);
            else turnAround(upperAngle - lowerAngle);

            RevoluteJointShape.this.onLowerIndicatorTurned();
          }
        };

    referenceAngleIndicator.updateEnd(begin.x + 48, begin.y);
    upperAngleIndicator.setColor(Colors.palette1_red);
    lowerAngleIndicator.setColor(Colors.palette1_blue);
    upperAngleIndicator.updateEnd(begin.x + 64, begin.y);
    lowerAngleIndicator.updateEnd(begin.x + 64, begin.y);

    hideLimitsElements();
  }



  public void onBeginPointMoved(float x, float y) {
    super.onBeginPointMoved(x,y);
    referenceAngleIndicator.updateBegin(x, y);
    referenceAngleIndicator.drawSelf();
    upperAngleIndicator.updateBegin(x, y);
    upperAngleIndicator.drawSelf();
    lowerAngleIndicator.updateBegin(x, y);
    lowerAngleIndicator.drawSelf();
  }

  @Override
  public List<PointImage> getMovables(boolean moveLimits) {
    List<PointImage> result = super.getMovables(moveLimits);
    if (isIndicatorsVisible()) {
      result.add(referenceAngleIndicator.getLimit());
      result.add(lowerAngleIndicator.getLimit());
      result.add(upperAngleIndicator.getLimit());
    }
    return result;
  }

  public void onLowerIndicatorTurned() {
    float angle =
        (lowerAngleIndicator.getAngleInDegrees() - referenceAngleIndicator.getAngleInDegrees());
    editorUserInterface
        .getJointSettingsWindowController()
        .setRevoluteLowerAngle(angle * MathUtils.degreesToRadians);
  }

  public void onUpperIndicatorTurned() {
    float angle =
        (upperAngleIndicator.getAngleInDegrees() - referenceAngleIndicator.getAngleInDegrees());
    editorUserInterface
        .getJointSettingsWindowController()
        .setRevoluteUpperAngle(angle * MathUtils.degreesToRadians);
  }

  public void onReferenceIndicatorTurned(float dA) {
    if (dA < 0) {
      lowerAngleIndicator.turnAround(dA);
      upperAngleIndicator.turnAround(dA);
    } else {
      upperAngleIndicator.turnAround(dA);
      lowerAngleIndicator.turnAround(dA);
    }

    onLowerIndicatorTurned();
    onUpperIndicatorTurned();
  }

  public void turnIndicators(float dA) {
    if (dA < 0) {
      lowerAngleIndicator.turnAround(dA);
      upperAngleIndicator.turnAround(dA);
    } else {
      upperAngleIndicator.turnAround(dA);
      lowerAngleIndicator.turnAround(dA);
    }
  }

  public float getUpperAngleRelative() {
    return (upperAngleIndicator.getAngleInDegrees() - referenceAngleIndicator.getAngleInDegrees());
  }

  public float getLowerAngleRelative() {
    return (lowerAngleIndicator.getAngleInDegrees() - referenceAngleIndicator.getAngleInDegrees());
  }

  public float getReferenceAngle() {
    return referenceAngleIndicator.getAngleInDegrees();
  }

  private float getUpperAngle() {
    return upperAngleIndicator.getAngleInDegrees();
  }

  private float getLowerAngle() {
    return lowerAngleIndicator.getAngleInDegrees();
  }

  @Override
  public void detach() {
    super.detach();
    referenceAngleIndicator.detach();
    lowerAngleIndicator.detach();
    upperAngleIndicator.detach();
  }


  @Override
  public void showLimitsElements() {
    super.showLimitsElements();
    referenceAngleIndicator.setVisible(true);
    lowerAngleIndicator.setVisible(true);
    upperAngleIndicator.setVisible(true);
  }

  private void hide(AngleIndicator indicator) {
    indicator.setVisible(false);
    editorUserInterface.getCreationZoneController().releaseSelectedPointImage();
  }

  @Override
  public void hideLimitsElements() {
    super.hideLimitsElements();
    hide(referenceAngleIndicator);
    hide(upperAngleIndicator);
    hide(lowerAngleIndicator);
  }

  public void updateLowerAngleIndicator(float angle) {
    // target = current+dA
    lowerAngleIndicator.turnAround(angle - getLowerAngle());
  }

  public void updateUpperAngleIndicator(float angle) {
    upperAngleIndicator.turnAround(angle - getUpperAngle());
  }

  public void updateReferenceAngleIndicator(float angle) {
    float dA = angle - getReferenceAngle();
    referenceAngleIndicator.turnAround(dA);
    turnIndicators(dA);
  }

  public void bindModel(JointModel model) {
    this.model = model;
    Vector2 modelEnd = model.getLocalAnchorB();
    model.setJointShape(this);
    this.updateEnd(modelEnd.x, modelEnd.y);
    if (model.isEnableLimit()) {
      this.showLimitsElements();
    }
    this.updateLowerAngleIndicator(
            (float) (model.getLowerAngle() / (2 * Math.PI) * 360));
    this.updateUpperAngleIndicator(
            (float) (model.getUpperAngle() / (2 * Math.PI) * 360));
  }
}
