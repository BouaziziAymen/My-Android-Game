package com.evolgames.entities.init;

import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.entities.serialization.infos.InitInfo;

public class TransformInit extends BodyInitDecorator {
  private float x;
  private float y;
  private float angle;

  public TransformInit(BodyInit bodyInit, float x, float y, float angle) {
    super(bodyInit);
    this.x = x;
    this.y = y;
    this.angle = angle;
  }

  @Override
  public void initialize(Body body) {
    super.initialize(body);
    body.setTransform(x, y, angle);
  }

  public void setX(float x) {
    this.x = x;
  }

  public void setY(float y) {
    this.y = y;
  }

  public void setAngle(float angle) {
    this.angle = angle;
  }

  @Override
  public InitInfo getInitInfo(InitInfo initInfo) {
    initInfo.setX(x);
    initInfo.setY(y);
    initInfo.setAngle(angle);
    return this.getBodyInit().getInitInfo(initInfo);
  }
}
