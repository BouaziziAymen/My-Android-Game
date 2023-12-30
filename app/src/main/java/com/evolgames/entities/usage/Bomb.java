package com.evolgames.entities.usage;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.properties.usage.BombUsageProperties;
import com.evolgames.entities.properties.usage.TimeBombUsageProperties;
import com.evolgames.entities.serialization.infos.BombInfo;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.PlayScene;
import com.evolgames.scenes.entities.PlayerSpecialAction;
import com.evolgames.userinterface.model.toolmodels.BombModel;
import com.evolgames.userinterface.model.toolmodels.UsageModel;

import java.util.List;
import java.util.stream.Collectors;

public abstract class Bomb extends Use {

    private List<BombInfo> bombInfoList;
    protected boolean alive = true;
    protected transient GameEntity gameEntity;
    private String gameEntityUniqueId;

    @SuppressWarnings("unused")
    public Bomb() {
    }
    public Bomb(UsageModel<?> usageModel) {
        BombUsageProperties properties = (BombUsageProperties) usageModel.getProperties();
        this.bombInfoList =
                properties.getBombModelList().stream()
                        .map(BombModel::toBombInfo)
                        .collect(Collectors.toList());
    }

    protected abstract boolean isBombOn();

    private void detonate(WorldFacade worldFacade){
        for (BombInfo bombInfo : bombInfoList) {
            Body body = gameEntity.getBody();
            Vector2 pos =
                    bombInfo
                            .getBombPosition()
                            .cpy()
                            .sub(gameEntity.getCenter())
                            .mul(1 / 32f);
            Vector2 worldPos = body.getWorldPoint(pos).cpy();
            if (gameEntity.getScene() instanceof PlayScene) {
                //((PlayScene) gameEntity.getScene()).lockSaving();
            }
            worldFacade.createExplosion(
                    gameEntity,
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
        if (!this.alive||gameEntity==null||gameEntity.getBody()==null) {
            return;
        }
        if(isBombOn()){
            detonate(worldFacade);
        }
    }

    @Override
    public PlayerSpecialAction getAction() {
        return PlayerSpecialAction.Grenade;
    }

    public void setGameEntity(GameEntity gameEntity) {
        this.gameEntity = gameEntity;
        this.gameEntityUniqueId = gameEntity.getUniqueID();
    }

    public String getGameEntityUniqueId() {
        return gameEntityUniqueId;
    }
}
