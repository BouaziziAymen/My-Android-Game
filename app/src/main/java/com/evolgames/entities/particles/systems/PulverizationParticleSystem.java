package com.evolgames.entities.particles.systems;

import com.evolgames.entities.blocks.CoatingBlock;
import com.evolgames.entities.particles.emitters.PolygonEmitter;
import com.evolgames.activity.ResourceManager;
import com.evolgames.utilities.MyColorUtils;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.util.adt.color.Color;

public class PulverizationParticleSystem extends FlowingParticleSystem {
  public PulverizationParticleSystem(
      PolygonEmitter emitter, float rateMin, float rateMax, int particlesMax) {
    super(emitter, rateMin, rateMax, particlesMax, ResourceManager.getInstance().pixelParticle);
  }

  @Override
  protected void onParticleSpawned(Particle<UncoloredSprite> particle) {
    super.onParticleSpawned(particle);
    CoatingBlock coatingBlock = relativePolygonEmitter.getActiveCoatingBlock();
    Color parentColor = coatingBlock.getParent().getProperties().getDefaultColor();
    MyColorUtils.blendColors(
        parentColor, parentColor, coatingBlock.getProperties().getDefaultColor());
    particle.getEntity().setColor(parentColor);
  }
}
