package com.evolgames.physics.entities.explosions;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.particles.wrappers.DataExplosiveParticleWrapper;
import com.evolgames.scenes.PhysicsScene;
import com.evolgames.scenes.PlayScene;

public class Explosion {
  private final PhysicsScene<?> gameScene;
  private final Vector2 center;
  private final float force;
  private final float heat;
  private final float velocity;
  private final Vector2 vector = new Vector2();
  private final GameEntity source;

  private DataExplosiveParticleWrapper explosionParticleWrapper;
  private int time = 0;
  private boolean alive = true;

  public Explosion(
      PhysicsScene<?> gameScene,
      GameEntity source,
      Vector2 center,
      float particles,
      float force,
      float speed,
      float heat,
      float fireRatio,
      float smokeRatio,
      float sparkRatio) {
    this.gameScene = gameScene;
    this.source = source;
    this.center = center;
    this.force = force;
    this.heat = heat;
    this.velocity = (1000 + speed * 1000);

    if (fireRatio > 0.1f || smokeRatio > 0.1f || sparkRatio > 0.1f) {
      Vector2 c = center.cpy().mul(32f);
      this.explosionParticleWrapper =
          this.gameScene
              .getWorldFacade()
              .createPointFireSource(
                  null,
                  new float[] {c.x, c.y, c.x, c.y},
                  velocity,
                  fireRatio,
                  smokeRatio,
                  sparkRatio,
                  particles,
                  2000,
                  false);
    }
  }

  public boolean isAlive() {
    return alive;
  }

  public void update() {
    if (!alive) {
      return;
    }
    if (time < 1) {
      gameScene
          .getWorldFacade()
          .performFlux(
              center,
              (gameEntity, impacts) -> {
                impacts.forEach(
                    i -> {
                      Vector2 p = i.getWorldPoint();
                      vector.set(p.x - center.x, p.y - center.y);
                      float d = Math.max(1f, vector.len());
                      i.setImpactImpulse(100000f * force / (d));
                      vector.mul(i.getImpactImpulse() / 10000f);
                      gameEntity.getBody().applyLinearImpulse(vector.x, vector.y, p.x, p.y);
                    });
                gameScene.getWorldFacade().applyImpacts(gameEntity, impacts);
                gameScene.getWorldFacade().applyImpactHeat(heat, impacts);
              },
              source);
    }
    if (time > 10) {
      this.explosionParticleWrapper.stopFinal();
      if (gameScene instanceof PlayScene) {
        ((PlayScene) gameScene).unlockSaving();
      }
      alive = false;
    }

    time++;
  }

  public Vector2 getCenter() {
    return center;
  }

  public float getVelocity() {
    return velocity;
  }
}
