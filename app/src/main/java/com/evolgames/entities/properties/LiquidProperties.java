package com.evolgames.entities.properties;

import org.andengine.util.adt.color.Color;

public class LiquidProperties extends Properties {

  private final int liquidId;
  private final String JuiceName;
  private final Color defaultColor;
  private final boolean flammable;
  private final float flammability;
  private final float ignitionTemperature;

  public LiquidProperties(int id, String name, boolean flammable, float flammability, float ignitionTemperature, Color color) {
    this.defaultColor = color;
    this.JuiceName = name;
    this.liquidId = id;
    this.flammable = flammable;
    this.flammability = flammability;
    this.ignitionTemperature = ignitionTemperature;
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

  public boolean isFlammable() {
    return flammable;
  }

  public float getIgnitionTemperature() {
    return ignitionTemperature;
  }

  public float getFlammability() {
    return flammability;
  }
}
