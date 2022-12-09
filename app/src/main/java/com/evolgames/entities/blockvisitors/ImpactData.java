package com.evolgames.entities.blockvisitors;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.blocks.LayerBlock;

public class ImpactData {
    public ImpactData(Vector2 worldImpactPoint, float impactEnergy, LayerBlock impactedBlock) {
        this.worldImpactPoint = worldImpactPoint;
        this.impactEnergy = impactEnergy;
        this.impactedBlock = impactedBlock;
    }

    private final Vector2 worldImpactPoint;
    private final float impactEnergy;
    private final LayerBlock impactedBlock;

    public Vector2 getLocalImpactPoint() {
        return worldImpactPoint;
    }

    public float getImpactEnergy() {
        return impactEnergy;
    }

    public LayerBlock getImpactedBlock() {
        return impactedBlock;
    }
}
