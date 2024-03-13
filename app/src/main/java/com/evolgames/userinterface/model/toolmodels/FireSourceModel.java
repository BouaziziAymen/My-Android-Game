package com.evolgames.userinterface.model.toolmodels;

import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.properties.FireSourceProperties;
import com.evolgames.entities.serialization.infos.FireSourceInfo;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.FireSourceShape;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.FireSourceField;

public class FireSourceModel extends ProperModel<FireSourceProperties> {

  private final int bodyId;
  private final int fireSourceId;
  private FireSourceShape fireSourceShape;
  private FireSourceField fireSourceField;
  private GameEntity muzzleEntity;

  public FireSourceModel(int bodyId, int fireSourceId, String fireSourceName) {
    super(fireSourceName);
    this.bodyId = bodyId;
    this.fireSourceId = fireSourceId;
  }

  public FireSourceModel(int bodyId, int fireSourceId, FireSourceShape fireSourceShape) {
    super("Fire Source " + fireSourceId);
    this.properties =
        new FireSourceProperties(fireSourceShape.getBegin(), fireSourceShape.getDirection());
    this.fireSourceShape = fireSourceShape;
    this.bodyId = bodyId;
    this.fireSourceId = fireSourceId;
  }

  public void setMuzzleEntity(GameEntity muzzleEntity) {
    this.muzzleEntity = muzzleEntity;
  }

  public FireSourceShape getFireSourceShape() {
    return fireSourceShape;
  }

  public void setFireSourceShape(FireSourceShape fireSourceShape) {
    this.fireSourceShape = fireSourceShape;
  }

  public int getBodyId() {
    return bodyId;
  }

  public int getFireSourceId() {
    return fireSourceId;
  }

  public FireSourceProperties getFireSourceProperties() {
    return this.properties;
  }

  public FireSourceField getFireSourceField() {
    return fireSourceField;
  }

  public void setFireSourceField(FireSourceField fireSourceField) {
    this.fireSourceField = fireSourceField;
  }


  public FireSourceInfo toFireSourceInfo() {
    FireSourceInfo fireSourceInfo = new FireSourceInfo();
    fireSourceInfo.setFireSourceOrigin(this.properties.getFireSourceOrigin());
    fireSourceInfo.setFireDirection(this.properties.getFireSourceDirection());
    fireSourceInfo.setFireRatio(this.properties.getFireRatio());
    fireSourceInfo.setSmokeRatio(this.properties.getSmokeRatio());
    fireSourceInfo.setSparkRatio(this.properties.getSparkRatio());
    fireSourceInfo.setHeat(this.properties.getHeatRatio());
    fireSourceInfo.setParticles(this.properties.getParticles());
    fireSourceInfo.setSpeed(this.properties.getSpeedRatio());
    fireSourceInfo.setInFirePartSize(this.properties.getInFirePartSize());
    fireSourceInfo.setFinFirePartSize(this.properties.getFinFirePartSize());
    fireSourceInfo.setMuzzleEntity(this.muzzleEntity);
    fireSourceInfo.setExtent(this.properties.getExtent());
    return fireSourceInfo;
  }
}
