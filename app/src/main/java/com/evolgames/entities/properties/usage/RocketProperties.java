package com.evolgames.entities.properties.usage;

import com.evolgames.entities.properties.Properties;
import com.evolgames.userinterface.model.toolmodels.FireSourceModel;
import java.util.ArrayList;
import java.util.List;

public class RocketProperties extends Properties {
  private final List<FireSourceModel> fireSourceModelList;
  private List<Integer> fireSourceModelListIds;
  private float power = 0.5f;
  private int fuel = 10;

  public RocketProperties() {
    this.fireSourceModelList = new ArrayList<>();
  }

  public float getPower() {
    return power;
  }

  public void setPower(float power) {
    this.power = power;
  }

  public int getFuel() {
    return fuel;
  }

  public void setFuel(int fuel) {
    this.fuel = fuel;
  }

  @Override
  public Properties copy() {
    return null;
  }

  public List<FireSourceModel> getFireSourceModelList() {
    return fireSourceModelList;
  }

  public List<Integer> getFireSourceModelListIds() {
    return fireSourceModelListIds;
  }

  public void setFireSourceModelListIds(List<Integer> fireSourceModelListIds) {
    this.fireSourceModelListIds = fireSourceModelListIds;
  }
}
