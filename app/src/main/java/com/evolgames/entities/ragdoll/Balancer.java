package com.evolgames.entities.ragdoll;

import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.entities.basics.EntityWithBody;

public class Balancer {

  private final EntityWithBody entity;
  private final float limit;
  private final float angle;

  public Balancer(EntityWithBody entity, float limit, float angle) {
    this.entity = entity;
    this.limit = limit;
    this.angle = angle;
  }

  public EntityWithBody getEntity() {
    return entity;
  }

  public void equilibrate() {
    Body body = entity.getBody();
    float rot = body.getAngle();
    float error = angle - rot;

    while (error < -Math.PI) error += 2 * Math.PI;
    while (error > Math.PI) error -= 2 * Math.PI;

    if (Math.abs(error) < limit) {
      body.setAngularVelocity(120 * Math.signum(error)* error*error / limit);
    }
  }
}
