package com.evolgames.entities.properties;

import org.andengine.util.adt.color.Color;

public class JuiceProperties extends Properties {

  private final int juiceId;
  private final String JuiceName;
  private final Color defaultColor;
  private final boolean flammable;
  private final float ignitionTemperature;

  public JuiceProperties(int id,String name, boolean flammable, float ignitionTemperature, Color color) {
    this.defaultColor = color;
    this.JuiceName = name;
    this.juiceId = id;
    this.flammable = flammable;
    this.ignitionTemperature = ignitionTemperature;
  }

  @Override
  public Properties copy() {
    return null;
  }

  public String getJuiceName() {
    return JuiceName;
  }

  public Color getDefaultColor() {
    return defaultColor;
  }
  public int getJuiceId() {
    return juiceId;
  }

  public boolean isFlammable() {
    return flammable;
  }

  public float getIgnitionTemperature() {
    return ignitionTemperature;
  }
}
