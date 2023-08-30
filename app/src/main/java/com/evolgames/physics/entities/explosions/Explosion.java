package com.evolgames.physics.entities.explosions;

import static org.andengine.extension.physics.box2d.util.Vector2Pool.obtain;
import static java.util.stream.Collectors.groupingBy;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blockvisitors.ImpactData;
import com.evolgames.entities.particles.wrappers.PointExplosiveParticleWrapper;
import com.evolgames.scenes.GameScene;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@FunctionalInterface
interface HitInterface {
    void rayHit(GameEntity entity, Vector2 direction, Vector2 end, float impulseValue);
}

public class Explosion {
    private final GameScene gameScene;
    private final ArrayList<ImpactData> impacts = new ArrayList<>();
    private final Vector2 center;
    private final float force;
    private final float heat;
    private final float velocity;

    private PointExplosiveParticleWrapper explosionParticleWrapper;
    private int time = 0;
    private boolean alive = true;

    public Explosion(GameScene gameScene, Vector2 center, float particles, float force, float speed, float heat, float fireRatio, float smokeRatio, float sparkRatio) {
        this.gameScene = gameScene;
        this.center = center;
        this.force = force;
        this.heat = heat;
        this.velocity = (1000 + speed * 2000);

        if (fireRatio > 0.1f || smokeRatio > 0.1f || sparkRatio > 0.1f) {
            this.explosionParticleWrapper = gameScene.getWorldFacade().createPointFireSource(null, center.cpy().mul(32f), velocity, fireRatio, smokeRatio, sparkRatio, particles, 2000, false);
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public void update() {
        if (!alive) return;
        if (time < 60) {
            if(time==5){
             //   this.runExplosion();
            }
            this.explosionParticleWrapper.setSpawnEnabled(false);
        }
        this.time++;
    }

    private void detectImpacts(HitInterface hitInterface) {
        impacts.clear();
        gameScene.getWorldFacade().performFlux(center, (layerBlock, entity, direction, start, end, dA) -> {
            float impulseValue = 1000 * force * dA;
            if (hitInterface != null) {
                hitInterface.rayHit(entity, direction, end, impulseValue);
            }
            impacts.add(new ImpactData(entity, layerBlock, obtain(end), impulseValue));
        }, false);
    }

    private void applyImpulse(GameEntity entity, Vector2 direction, Vector2 end, float impulseValue) {
        entity.getBody().applyLinearImpulse(direction.x * impulseValue / 60 / 60, direction.y * impulseValue / 60, end.x, end.y);
    }

    private void runExplosion() {
        detectImpacts(this::applyImpulse);
        Map<GameEntity, List<ImpactData>> map = impacts.stream()
                .collect(groupingBy(ImpactData::getGameEntity));
        map.forEach((e, imp) -> {
            gameScene.getWorldFacade().applyImpacts(e, imp);
            gameScene.getWorldFacade().applyImpactHeat(imp, heat);
        });
    }

    public Vector2 getCenter() {
        return center;
    }

    public float getVelocity() {
        return velocity;
    }
}