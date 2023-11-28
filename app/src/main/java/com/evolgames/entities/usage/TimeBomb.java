package com.evolgames.entities.usage;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.properties.usage.TimeBombUsageProperties;
import com.evolgames.entities.usage.infos.BombInfo;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.PlayScene;
import com.evolgames.scenes.entities.PlayerSpecialAction;
import com.evolgames.userinterface.model.toolmodels.BombModel;
import com.evolgames.userinterface.model.toolmodels.UsageModel;
import java.util.List;
import java.util.stream.Collectors;

public class TimeBomb extends Use {

  private List<BombInfo> bombInfoList;
  private float countDown;
  private boolean alive = true;
  private boolean count;
  transient private GameEntity gameEntity;
  private String gameEntityUniqueId;

  public TimeBomb() {
  }
  public TimeBomb(UsageModel<?> usageModel) {
    TimeBombUsageProperties properties = (TimeBombUsageProperties) usageModel.getProperties();
    this.bombInfoList =
        properties.getBombModelList().stream()
            .map(BombModel::toBombInfo)
            .collect(Collectors.toList());
    this.countDown = properties.getDelay();
  }

  @Override
  public void onStep(float deltaTime, WorldFacade worldFacade) {
    if (!this.alive||gameEntity==null) {
      return;
    }
    if (count) {
      countDown -= deltaTime;
      if (countDown < 0) {
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
            ((PlayScene) gameEntity.getScene()).lockSaving();
          }
          worldFacade.createExplosion(
              gameEntity,
              worldPos.x,
              worldPos.y,
              bombInfo.getFireRatio(),
              bombInfo.getSmokeRatio(),
              bombInfo.getSparkRatio(),
              bombInfo.getParticles(),
              bombInfo.getForce(),
              bombInfo.getHeat(),
              bombInfo.getSpeed());
        }
        this.alive = false;
      }
    }
  }

  public void onTriggerReleased() {
    count = true;
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
