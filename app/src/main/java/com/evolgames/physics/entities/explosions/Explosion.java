package com.evolgames.physics.entities.explosions;

import static org.andengine.extension.physics.box2d.util.Vector2Pool.obtain;
import static java.util.stream.Collectors.groupingBy;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blockvisitors.ImpactData;
import com.evolgames.scenes.GameScene;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Explosion {
    private final GameScene gameScene;
    private final ArrayList<ImpactData> impacts = new ArrayList<>();
    private final Vector2 vector = new Vector2();
    private final Vector2 center;
    private final float energy;
    private int time = 0;
    private boolean alive = true;

    public boolean isAlive() {
        return alive;
    }

    public Explosion(GameScene gameScene, Vector2 center, float energy) {
        this.gameScene = gameScene;
        this.center = center;
        this.energy = energy;
    }

    public void update() {
        if (time < 60) {
            runExplosion(time%60==0,time%10==0,time%60==0);
        } else {
            alive = false;
        }
        time++;
    }
    private void detectImpacts(HitInterface hitInterface){
        impacts.clear();
        gameScene.getWorldFacade().performFlux(center, (layerBlock, entity, direction, start, end, angle) -> {
            float length2 = vector.set(end.x - start.x, end.y - start.y).len2();
            float dA = (float) (angle / (2 * Math.PI));
            float impulseValue = energy * dA / (length2+dA);
            if(hitInterface!=null) {
                hitInterface.rayHit(entity, direction, end, impulseValue);
            }
            impacts.add(new ImpactData(entity, layerBlock, obtain(end), impulseValue));
        }, false);
    }

    private void applyImpulse(GameEntity entity, Vector2 direction, Vector2 end, float impulseValue) {
        entity.getBody().applyLinearImpulse(direction.x * impulseValue / 60, direction.y * impulseValue / 60, end.x, end.y);
    }

    private void runExplosion(boolean heat, boolean forces, boolean collisions) {
       if(forces||heat||collisions) {
           if(forces) {
               detectImpacts(this::applyImpulse);
           } else {
               detectImpacts(null);
           }
       }
        Map<GameEntity, List<ImpactData>> map = impacts.stream()
                .collect(groupingBy(ImpactData::getGameEntity));
            map.forEach((e, imp) ->{
                if(collisions) {
                    gameScene.getWorldFacade().applyImpacts(e, imp);
                }
                if(heat) {
                    gameScene.getWorldFacade().applyImpactHeat(imp);
                }
            } );

           //

    }

    public Vector2 getCenter() {
        return center;
    }

    public float getEnergy() {
        return energy;
    }
}
@FunctionalInterface
interface HitInterface {
    void rayHit(GameEntity entity, Vector2 direction, Vector2 end, float impulseValue);
}