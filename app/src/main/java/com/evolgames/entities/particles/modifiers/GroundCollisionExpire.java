package com.evolgames.entities.particles.modifiers;

import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.modifier.IParticleModifier;
import org.andengine.entity.sprite.UncoloredSprite;

public class GroundCollisionExpire implements IParticleModifier<UncoloredSprite> {
  private final int groundY;

  public GroundCollisionExpire(int groundY) {
    this.groundY = groundY;
  }

  @Override
  public void onInitializeParticle(Particle<UncoloredSprite> pParticle) {}

  @Override
  public void onUpdateParticle(Particle<UncoloredSprite> pParticle) {
    if (pParticle.getEntity().getY()
        < groundY) {
      pParticle.setExpired(true);
    }
  }
}
