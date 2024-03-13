package com.evolgames.entities.basics;

import org.andengine.util.adt.color.Color;

public class Liquid  {

  private final int liquidId;
  private final String JuiceName;
  private final Color defaultColor;
  private final float flammability;

  public Liquid(int id, String name, float flammability, Color color) {
    this.defaultColor = color;
    this.JuiceName = name;
    this.liquidId = id;
    this.flammability = flammability;
  }

  public String getJuiceName() {
    return JuiceName;
  }

  public Color getDefaultColor() {
    return defaultColor;
  }
  public int getLiquidId() {
    return liquidId;
  }

  public float getFlammability() {
    return flammability;
  }
}
