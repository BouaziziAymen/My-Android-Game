package com.evolgames.userinterface.model.toolmodels;

import com.evolgames.entities.properties.BombProperties;
import com.evolgames.entities.serialization.infos.BombInfo;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.BombShape;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.BombField;

public class BombModel extends ProperModel<BombProperties> {

  private final int bodyId;
  private final int bombId;
  private BombShape bombShape;
  private BombField bombField;

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

  public BombInfo toBombInfo() {
    BombInfo pi = new BombInfo();
    pi.setBombPosition(this.properties.getBombPosition());
    pi.setFireRatio(this.properties.getFireRatio());
    pi.setSmokeRatio(this.properties.getSmokeRatio());
    pi.setSparkRatio(this.properties.getSparkRatio());
    pi.setForce(this.properties.getForce());
    pi.setHeat(this.properties.getHeat());
    pi.setParticles(this.properties.getParticles());
    pi.setSpeed(this.properties.getSpeed());
    return pi;
  }
}
