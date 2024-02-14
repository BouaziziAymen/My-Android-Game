package com.evolgames.entities.properties;

import com.badlogic.gdx.math.Vector2;

public class LiquidSourceProperties extends Properties {

  private Vector2 liquidSourceOrigin;
  private Vector2 liquidSourceDirection;
  private float extent = 0.05f;
  private int liquid;

  @SuppressWarnings("unused")
  public LiquidSourceProperties() {}

  public LiquidSourceProperties(Vector2 begin, Vector2 direction) {
    this.liquidSourceOrigin = begin.cpy();
    this.liquidSourceDirection = direction.cpy();
  }

  @Override
  public Properties copy() {
    return null;
  }

  public Vector2 getLiquidSourceOrigin() {
    return liquidSourceOrigin;
  }

  public void setLiquidSourceOrigin(Vector2 liquidSourceOrigin) {
    this.liquidSourceOrigin = liquidSourceOrigin;
  }

  public Vector2 getLiquidSourceDirection() {
    return liquidSourceDirection;
  }

  public void setLiquidSourceDirection(Vector2 liquidSourceDirection) {
    this.liquidSourceDirection = liquidSourceDirection;
  }

  public float getExtent() {
    return extent;
  }

  public void setExtent(float extent) {
    this.extent = extent;
  }

  public int getLiquid() {
    return liquid;
  }

  public void setLiquid(int liquidId) {
    this.liquid = liquidId;
  }
}
