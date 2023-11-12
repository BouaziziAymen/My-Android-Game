package com.evolgames.entities.particles.modifiers;

import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.modifier.IParticleModifier;
import org.andengine.entity.sprite.UncoloredSprite;

public class GroundCollisionStop implements IParticleModifier<UncoloredSprite> {
  private final int groundY;

  public GroundCollisionStop(int groundY) {
    this.groundY = groundY;
  }

  @Override
  public void onInitializeParticle(Particle<UncoloredSprite> pParticle) {}

  @Override
  public void onUpdateParticle(Particle<UncoloredSprite> pParticle) {
    if (pParticle.getEntity().getY()
            - (pParticle.getEntity().getHeight() / 3f * pParticle.getEntity().getScaleX())
        < groundY) {
      pParticle.getPhysicsHandler().setEnabled(false);
    }
  }
}
