package com.evolgames.entities.particles.modifiers;

import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.modifier.IParticleModifier;

public class QuadraticBezierParticleModifier<T extends IEntity> implements IParticleModifier<T> {
  // Control points for the Bezier curve
  private final float mX1;
  private final float mY1;
  private final float mX2;
  private final float mY2;

  // Constructor to set the control points for the curve
  public QuadraticBezierParticleModifier(float x1, float y1, float x2, float y2) {
    mX1 = x1;
    mY1 = y1;
    mX2 = x2;
    mY2 = y2;
  }

  @Override
  public void onInitializeParticle(Particle<T> p) {
    // Set the initial position of the particle to the starting point of the curve
    p.getEntity().setPosition(mX1, mY1);
  }

  @Override
  public void onUpdateParticle(Particle<T> p) {
    // Calculate the new position of the particle using the quadratic Bezier curve formula
    float t = p.getExpireTime() / p.getLifeTime();
    float x = (1 - t) * (1 - t) * mX1 + 2 * (1 - t) * t * mX2 + t * t * mX2;
    float y = (1 - t) * (1 - t) * mY1 + 2 * (1 - t) * t * mY2 + t * t * mY2;
    p.getEntity().setPosition(x, y);
  }
}
