package com.evolgames.entities.blockvisitors;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.LayerBlock;

public class ImpactData {
    private final GameEntity gameEntity;
    private final Vector2 worldImpactPoint;
    private final float impactEnergy;
    private final LayerBlock impactedBlock;

    public ImpactData(GameEntity gameEntity, LayerBlock impactedBlock, Vector2 worldImpactPoint, float impactEnergy) {
        this.worldImpactPoint = worldImpactPoint;
        this.impactEnergy = impactEnergy;
        this.impactedBlock = impactedBlock;
        this.gameEntity = gameEntity;
    }

    public Vector2 getLocalImpactPoint() {
        return worldImpactPoint;
    }

    public float getImpactEnergy() {
        return impactEnergy;
    }

    public LayerBlock getImpactedBlock() {
        return impactedBlock;
    }

    public GameEntity getGameEntity() {
        return gameEntity;
    }
}
