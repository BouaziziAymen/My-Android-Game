package com.evolgames.entities.usage;

import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.hand.PlayerSpecialAction;
import com.evolgames.entities.serialization.infos.ProjectileInfo;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.PhysicsScene;

import java.util.List;

public class Muzzle extends Use{

    private int projectileIndex;
    @SuppressWarnings("Unused")
    public Muzzle(){}

    public Muzzle(int projectileIndex) {
       this.projectileIndex = projectileIndex;
    }

    public int getProjectileIndex() {
        return projectileIndex;
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
        return ratio>0.9f;
    }
}
