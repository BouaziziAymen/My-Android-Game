package com.evolgames.dollmutilationgame.entities.particles.emitters;

import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.utilities.GeometryUtils;

import org.andengine.entity.particle.emitter.BaseParticleEmitter;

public abstract class DataEmitter extends BaseParticleEmitter {

    protected final float[] data;
    private final GameEntity gameEntity;

    DataEmitter(float pCenterX, float pCenterY, float[] data, GameEntity gameEntity) {
        super(pCenterX, pCenterY);
        this.data = data;
        this.gameEntity = gameEntity;
    }

    protected abstract void prepareData();

    public void update() {
        if (gameEntity == null) {
            return;
        }
        float x = gameEntity.getX();
        float y = gameEntity.getY();
        float rot = gameEntity.getRotation();
        GeometryUtils.transformation.setToIdentity();
        GeometryUtils.transformation.preTranslate(x, y);
        GeometryUtils.transformation.preRotate(-rot);
        prepareData();
        GeometryUtils.transformation.transform(data);
    }
    public void updatePosition(float x, float y) {
        GeometryUtils.transformation.setToIdentity();
        GeometryUtils.transformation.preTranslate(x, y);
        prepareData();
        GeometryUtils.transformation.transform(data);
    }

}
