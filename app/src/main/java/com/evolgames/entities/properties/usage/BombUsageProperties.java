package com.evolgames.entities.properties.usage;

import com.evolgames.entities.properties.Properties;
import com.evolgames.userinterface.model.toolmodels.BombModel;
import java.util.ArrayList;
import java.util.List;

public class BombUsageProperties extends Properties {

  private final List<BombModel> bombModelList;
  private List<Integer> bombIds;
  protected float delay = 5f;

  public BombUsageProperties() {
    this.bombModelList = new ArrayList<>();
  }

  public List<BombModel> getBombModelList() {
    return bombModelList;
  }

  public List<Integer> getBombIds() {
    return bombIds;
  }

  public void setBombIds(List<Integer> bombIds) {
    this.bombIds = bombIds;
  }

  public float getDelay() {
    return delay;
  }

  public void setDelay(float delay) {
    this.delay = delay;
  }
}
