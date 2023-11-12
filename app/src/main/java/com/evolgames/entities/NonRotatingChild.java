package com.evolgames.entities;

import com.badlogic.gdx.math.Vector2;
import org.andengine.entity.Entity;

public class NonRotatingChild extends Entity {
  public NRType type;
  protected float x0;
  protected float y0;
  private final float theta0;
  private GameEntity parent;

  protected NonRotatingChild(float x, float y, float theta, GameEntity parent, NRType type) {
    setParentGameEntity(parent);
    this.type = type;
    this.x0 = x;
    this.y0 = y;

    this.theta0 = theta;
  }

  public void setParentGameEntity(GameEntity parent) {
    this.parent = parent;
  }

  public Vector2 getPixelCenterSpeed() {
    if (this.parent != null) {
      Vector2 local = new Vector2(this.x0, this.y0).mul(1 / 32f);
      return this.parent.getBody().getLinearVelocityFromLocalPoint(local).cpy().mul(32f);
    }
    return null;
  }
}
