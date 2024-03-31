package com.evolgames.entities.usage;

import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.hand.PlayerSpecialAction;
import com.evolgames.entities.serialization.infos.ProjectileInfo;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.PhysicsScene;

import java.util.List;

public class Muzzle extends Use {

    private String projectileInfoUniqueId;
    private transient GameEntity muzzleEntity;

    @SuppressWarnings("Unused")
    public Muzzle() {
    }

    public Muzzle(GameEntity muzzleEntity, ProjectileInfo projectileInfo) {
        this.muzzleEntity = muzzleEntity;
        this.projectileInfoUniqueId = projectileInfo.getProjectileInfoUniqueId();
        this.active = true;
    }

    public String getProjectileInfoUniqueId() {
        return projectileInfoUniqueId;
    }

    public GameEntity getMuzzleEntity() {
        return muzzleEntity;
    }

    public void setMuzzleEntity(GameEntity muzzleEntity) {
        this.muzzleEntity = muzzleEntity;
    }

    @Override
    public void onStep(float deltaTime, WorldFacade worldFacade) {

    }

    @Override
    public List<PlayerSpecialAction> getActions() {
        return null;
    }

    @Override
    public void dynamicMirror(PhysicsScene<?> physicsScene) {

    }

    @Override
    public boolean inheritedBy(GameEntity biggestSplinter, float ratio) {
        if (ratio > 0.9f) {
            this.muzzleEntity = biggestSplinter;
            return true;
        } else {
            this.setActive(false);
            return false;
        }
    }
}
