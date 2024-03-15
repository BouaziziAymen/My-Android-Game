package com.evolgames.userinterface.model.toolmodels;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.properties.BombProperties;
import com.evolgames.entities.serialization.infos.BombInfo;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.BombShape;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.BombField;
import com.evolgames.utilities.GeometryUtils;

public class BombModel extends ProperModel<BombProperties> {

  private final int bodyId;
  private final int bombId;
  private BombShape bombShape;
  private BombField bombField;

  private transient GameEntity carrierEntity;

  public BombModel(int bodyId, int bombId) {
    super("Bomb" + bombId);
    this.bodyId = bodyId;
    this.bombId = bombId;
  }

  public BombModel(int bodyId, int bombId, BombShape bombShape) {
    super("Bomb" + bombId);
    this.bodyId = bodyId;
    this.bombId = bombId;
    this.bombShape = bombShape;
    this.properties = new BombProperties();
  }

  public BombShape getBombShape() {
    return bombShape;
  }

  public void setBombShape(BombShape bombShape) {
    this.bombShape = bombShape;
  }

  public int getBodyId() {
    return bodyId;
  }

  public int getBombId() {
    return bombId;
  }

  public BombField getBombField() {
    return bombField;
  }

  public void setBombField(BombField bombField) {
    this.bombField = bombField;
  }

  public BombInfo toBombInfo(boolean mirrored) {
    BombInfo bombInfo = new BombInfo();
    Vector2 centredPosition = this.properties
            .getBombPosition()
            .cpy()
            .sub(carrierEntity.getCenter())
            .mul(1 / 32f);
    if(mirrored){
      centredPosition = GeometryUtils.mirrorPoint(centredPosition);
    }
    bombInfo.setCarrierEntity(this.carrierEntity);
    bombInfo.setBombPosition(centredPosition);
    bombInfo.setFireRatio(this.properties.getFireRatio());
    bombInfo.setSmokeRatio(this.properties.getSmokeRatio());
    bombInfo.setSparkRatio(this.properties.getSparkRatio());
    bombInfo.setForce(this.properties.getForce());
    bombInfo.setHeat(this.properties.getHeat());
    bombInfo.setParticles(this.properties.getParticles());
    bombInfo.setSpeed(this.properties.getSpeed());
    return bombInfo;
  }

  public void setCarrierEntity(GameEntity carrierEntity) {
    this.carrierEntity = carrierEntity;
  }
}
