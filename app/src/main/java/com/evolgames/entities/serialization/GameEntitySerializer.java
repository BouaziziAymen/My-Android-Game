package com.evolgames.entities.serialization;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.SpecialEntityType;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.factories.GameEntityFactory;
import com.evolgames.entities.init.AngularVelocityInit;
import com.evolgames.entities.init.BodyInit;
import com.evolgames.entities.init.BodyInitImpl;
import com.evolgames.entities.init.BulletInit;
import com.evolgames.entities.init.LinearVelocityInit;
import com.evolgames.entities.init.RecoilInit;
import com.evolgames.entities.init.TransformInit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameEntitySerializer {
  public static transient Map<String, GameEntity> entities = new HashMap<>();

  private InitInfo initInfo;
  private List<LayerBlock> layerBlocks;
  private BodyDef.BodyType bodyType;
  private SpecialEntityType specialEntityType;
  private String name;
  private Vector2 center;
  private String uniqueId;

  @SuppressWarnings("unused")
  GameEntitySerializer() {}

  GameEntitySerializer(GameEntity gameEntity) {
    this.layerBlocks = gameEntity.getBlocks();
    this.initInfo = gameEntity.getCreationInitInfo();
    this.bodyType = gameEntity.getBodyType();
    this.name = gameEntity.getName();
    this.uniqueId = gameEntity.getUniqueID();
    this.center = gameEntity.getCenter();
    this.specialEntityType = gameEntity.getType();
  }

  public void afterLoad() {
    for (LayerBlock layerBlock : this.layerBlocks) {
      layerBlock.refillGrid();
    }
  }

  public GameEntity create() {
    BodyInit bodyInit = getBodyInit();
    return GameEntityFactory.getInstance()
        .createGameEntity(
            initInfo.getX(),
            initInfo.getY(),
            initInfo.getAngle(),
            bodyInit,
            layerBlocks,
            bodyType,
            name);
  }

  private BodyInit getBodyInit() {
    BodyInit bodyInit;
    BodyInitImpl bodyInitImpl = new BodyInitImpl(initInfo.getFilter());
    bodyInit = new BulletInit(bodyInitImpl, initInfo.isBullet());
    bodyInit = new TransformInit(bodyInit, initInfo.getX(), initInfo.getY(), initInfo.getAngle());
    if (initInfo.getLinearVelocity() != null) {
      bodyInit = new LinearVelocityInit(bodyInit, initInfo.getLinearVelocity());
    }
    if (initInfo.getAngularVelocity() != 0) {
      bodyInit = new AngularVelocityInit(bodyInit, initInfo.getAngularVelocity());
    }
    if (initInfo.getMuzzleVelocity() != null) {
      bodyInit =
          new RecoilInit(
              bodyInit,
              null,
              initInfo.getRecoil(),
              initInfo.getMuzzleVelocity(),
              initInfo.getPoint());
    }
    return bodyInit;
  }

  public void afterCreate(GameEntity gameEntity) {
    gameEntity.setCenter(this.center);
    gameEntity.redrawStains();
    gameEntity.setUniqueId(this.uniqueId);
    gameEntity.setType(this.specialEntityType);
    entities.put(gameEntity.getUniqueID(), gameEntity);
  }

  @SuppressWarnings("unused")
  public void setLayerBlocks(ArrayList<LayerBlock> layerBlocks) {
    this.layerBlocks = layerBlocks;
  }
}
