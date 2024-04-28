package com.evolgames.dollmutilationgame.entities.usage;

import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.entities.hand.PlayerSpecialAction;
import com.evolgames.dollmutilationgame.entities.serialization.infos.LiquidSourceInfo;
import com.evolgames.dollmutilationgame.entities.serialization.infos.ProjectileInfo;
import com.evolgames.dollmutilationgame.physics.WorldFacade;
import com.evolgames.dollmutilationgame.scenes.PhysicsScene;

import java.util.List;

public class Seal extends Use{

    private String liquidSourceInfoUniqueId;
    private transient GameEntity sealEntity;
    private transient LiquidSourceInfo liquidSourceInfo;

    @SuppressWarnings("unused")
    public Seal() {
    }

    public Seal(GameEntity sealEntity, LiquidSourceInfo liquidSourceInfo) {
        this.liquidSourceInfo = liquidSourceInfo;
        this.liquidSourceInfoUniqueId = liquidSourceInfo.getLiquidSourceInfoUniqueId();
        this.sealEntity = sealEntity;
        this.active = true;
    }

    public String getLiquidSourceInfoUniqueId() {
        return this.liquidSourceInfoUniqueId;
    }

    public GameEntity getSealEntity() {
        return sealEntity;
    }

    public void setSealEntity(GameEntity sealEntity) {
        this.sealEntity = sealEntity;
    }

    public void setLiquidSourceInfo(LiquidSourceInfo liquidSourceInfo) {
        this.liquidSourceInfo = liquidSourceInfo;
    }

    @Override
    public void onStep(float deltaTime, WorldFacade worldFacade) {

    }

    @Override
    public List<PlayerSpecialAction> getActions() {
        return null;
    }

    @Override
    public void dynamicMirror(PhysicsScene physicsScene) {

    }

    @Override
    public boolean inheritedBy(GameEntity biggestSplinter, float ratio) {
        if (ratio > 0.9f) {
            this.sealEntity = biggestSplinter;
            if(this.liquidSourceInfo!=null){
                liquidSourceInfo.setSeal(this);
            }
            return true;
        } else {
            this.setActive(false);
            return false;
        }
    }
}
