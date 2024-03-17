package com.evolgames.entities.blockvisitors;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.blocks.LayerBlock;

public class ImpactData {
    private GameEntity gameEntity;
    private Vector2 localImpactPoint;
    private Vector2 worldPoint;
    private float impactImpulse;
    private LayerBlock impactedBlock;

    public ImpactData() {
    }

    public ImpactData(
            GameEntity gameEntity, LayerBlock impactedBlock, Vector2 worldPoint, float impactImpulse) {
        this.localImpactPoint = gameEntity.getBody().getLocalPoint(worldPoint).cpy();
        this.worldPoint = worldPoint;
        this.impactedBlock = impactedBlock;
        this.gameEntity = gameEntity;
        this.impactImpulse = impactImpulse;
    }

    public Vector2 getLocalImpactPoint() {
        return localImpactPoint;
    }

    public void setLocalImpactPoint(Vector2 localImpactPoint) {
        this.localImpactPoint = localImpactPoint;
    }

    public float getImpactImpulse() {
        return impactImpulse;
    }

    public void setImpactImpulse(float impactImpulse) {
        this.impactImpulse = impactImpulse;
    }

    public LayerBlock getImpactedBlock() {
        return impactedBlock;
    }

    public void setImpactedBlock(LayerBlock impactedBlock) {
        this.impactedBlock = impactedBlock;
    }

    public Vector2 getWorldPoint() {
        return worldPoint;
    }

    public void setWorldPoint(Vector2 worldPoint) {
        this.worldPoint = worldPoint;
    }

    public GameEntity getGameEntity() {
        return gameEntity;
    }

    public void setGameEntity(GameEntity gameEntity) {
        this.gameEntity = gameEntity;
    }
}
