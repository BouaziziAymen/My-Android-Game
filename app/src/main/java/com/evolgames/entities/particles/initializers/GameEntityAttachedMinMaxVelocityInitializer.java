package com.evolgames.entities.particles.initializers;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.initializer.IParticleInitializer;
import org.andengine.entity.sprite.UncoloredSprite;

public class GameEntityAttachedMinMaxVelocityInitializer
    implements IParticleInitializer<UncoloredSprite> {
  private final GameEntity gameEntity;
  private final Vector2 normal;
  private final Vector2 tangent;
  private final Vector2 worldPoint;
  private final float minX;
  private final float maxX;
  private final float minY;
  private final float maxY;

  public GameEntityAttachedMinMaxVelocityInitializer(
      GameEntity gameEntity, Vector2 normal, float minX, float maxX, float minY, float maxY) {
    this.gameEntity = gameEntity;
    this.normal = normal;
    this.tangent = new Vector2(-normal.y, normal.x);
    this.worldPoint = new Vector2();
    this.minX = minX;
    this.maxX = maxX;
    this.minY = minY;
    this.maxY = maxY;
  }

  @Override
  public void onInitializeParticle(Particle<UncoloredSprite> pParticle) {
    if (gameEntity.getBody() == null) {
      return;
    }
    this.worldPoint.set(pParticle.getEntity().getX() / 32f, pParticle.getEntity().getY() / 32f);
    float ta = (float) (minX + Math.random() * (maxX - minX));
    float na = (float) (minY + Math.random() * (maxY - minY));

    Vector2 localNormal = gameEntity.getBody().getWorldVector(normal).cpy();
    Vector2 localTangent = new Vector2(-localNormal.y, localNormal.x);
    Vector2 N = localNormal.cpy().mul(na);
    Vector2 T = localTangent.cpy().mul(ta);
    pParticle.getPhysicsHandler().setVelocity(N.x + T.x, N.y + T.y);
  }
}
