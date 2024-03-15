package com.evolgames.userinterface.model.toolmodels;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.properties.LiquidSourceProperties;
import com.evolgames.entities.serialization.infos.LiquidSourceInfo;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.LiquidSourceShape;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.LiquidSourceField;
import com.evolgames.utilities.GeometryUtils;

public class LiquidSourceModel extends ProperModel<LiquidSourceProperties> {

  private final int bodyId;
  private final int liquidSourceId;
  private LiquidSourceShape liquidSourceShape;
  private LiquidSourceField liquidSourceField;
  private GameEntity containerEntity;
  private GameEntity sealEntity;

  public LiquidSourceModel(int bodyId, int liquidSourceId, String liquidSourceName) {
    super(liquidSourceName);
    this.bodyId = bodyId;
    this.liquidSourceId = liquidSourceId;
  }

  public LiquidSourceModel(int bodyId, int liquidSourceId, LiquidSourceShape liquidSourceShape) {
    super("Liquid Source " + liquidSourceId);
    this.properties =
        new LiquidSourceProperties(liquidSourceShape.getBegin(), liquidSourceShape.getDirection());
    this.liquidSourceShape = liquidSourceShape;
    this.bodyId = bodyId;
    this.liquidSourceId = liquidSourceId;
  }

  public void setContainerEntity(GameEntity containerEntity) {
    this.containerEntity = containerEntity;
  }

  public void setSealEntity(GameEntity sealEntity) {
    this.sealEntity = sealEntity;
  }

  public LiquidSourceShape getLiquidSourceShape() {
    return liquidSourceShape;
  }

  public void setLiquidSourceShape(LiquidSourceShape liquidSourceShape) {
    this.liquidSourceShape = liquidSourceShape;
  }

  public int getBodyId() {
    return bodyId;
  }

  public int getLiquidSourceId() {
    return liquidSourceId;
  }

  public LiquidSourceProperties getLiquidSourceProperties() {
    return this.properties;
  }

  public LiquidSourceField getLiquidSourceField() {
    return liquidSourceField;
  }

  public void setLiquidSourceField(LiquidSourceField liquidSourceField) {
    this.liquidSourceField = liquidSourceField;
  }
  public LiquidSourceInfo toLiquidSourceInfo(boolean mirrored) {
    LiquidSourceInfo liquidSourceInfo = new LiquidSourceInfo();
    Vector2 centredOrigin = this.properties
            .getLiquidSourceOrigin()
            .cpy()
            .sub(containerEntity.getCenter())
            .mul(1 / 32f);
    Vector2 direction = this.properties.getLiquidSourceDirection().cpy();
    if(mirrored){
      centredOrigin = GeometryUtils.mirrorPoint(centredOrigin);
      direction.x = -direction.x;
    }
    liquidSourceInfo.setLiquidSourceOrigin(centredOrigin);
    liquidSourceInfo.setLiquidDirection(direction);
    liquidSourceInfo.setExtent(this.properties.getExtent());
    liquidSourceInfo.setContainerEntity(this.containerEntity);
    liquidSourceInfo.setSealEntity(this.sealEntity);
    liquidSourceInfo.setLiquid(this.properties.getLiquid());
    return liquidSourceInfo;
  }
}
