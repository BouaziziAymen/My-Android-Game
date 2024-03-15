package com.evolgames.entities.usage;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.properties.usage.BombUsageProperties;
import com.evolgames.entities.serialization.infos.BombInfo;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.PlayScene;
import com.evolgames.entities.hand.PlayerSpecialAction;
import com.evolgames.userinterface.model.toolmodels.BombModel;
import com.evolgames.userinterface.model.toolmodels.UsageModel;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Bomb extends Use {

    private List<BombInfo> bombInfoList;
    protected boolean alive = true;

    @SuppressWarnings("unused")
    public Bomb() {
    }
    public Bomb(UsageModel<?> usageModel,boolean mirrored) {
        BombUsageProperties properties = (BombUsageProperties) usageModel.getProperties();
        this.bombInfoList =
                properties.getBombModelList().stream()
                        .map(m->m.toBombInfo(mirrored))
                        .collect(Collectors.toList());
    }

    protected abstract boolean isBombOn();

    protected abstract boolean isActivated();

    private void detonate(WorldFacade worldFacade){
        for (BombInfo bombInfo : bombInfoList) {
            Body body = bombInfo.getCarrierEntity().getBody();
            Vector2 pos =
                    bombInfo
                            .getBombPosition();
            Vector2 worldPos = body.getWorldPoint(pos).cpy();

            worldFacade.createExplosion(
                    bombInfo.getCarrierEntity(),
                    worldPos.x,
                    worldPos.y,
                    bombInfo.getFireRatio(),
                    bombInfo.getSmokeRatio(),
                    bombInfo.getSparkRatio(),
                    60f*bombInfo.getParticles(),
                    bombInfo.getForce(),
                    bombInfo.getHeat(),
                    bombInfo.getSpeed(),1f,0f);
        }
        this.alive = false;
    }
    @Override
    public void onStep(float deltaTime, WorldFacade worldFacade) {
        if (!this.alive) {
            return;
        }
        if(isBombOn()){
            detonate(worldFacade);
        }
    }

    @Override
    public List<PlayerSpecialAction> getActions() {
         if(!isActivated()){
             return Collections.singletonList(PlayerSpecialAction.Trigger);
         }
         return null;
    }

    public List<BombInfo> getBombInfoList() {
        return bombInfoList;
    }
}
