package com.evolgames.userinterface.model.toolmodels;

import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.HandShape;

public class HandModel extends ProperModel {
  private final HandShape handShape;
  private final int handId;
  private final int bodyId;

  public HandModel(int bodyId, int handId, HandShape handShape) {
    super("Hand" + handId);
    this.handShape = handShape;
    this.bodyId = bodyId;
    this.handId = handId;
  }

  public int getHandId() {
    return handId;
  }

  public int getBodyId() {
    return bodyId;
  }

  public HandShape getHandShape() {
    return handShape;
  }
}
