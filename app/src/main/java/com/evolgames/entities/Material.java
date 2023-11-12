package com.evolgames.entities;


import org.andengine.util.adt.color.Color;

public class Material {

  private final float tenacity;
  private final float density;
  private final float friction;
  private final float restitution;
  private final Color color;
  private final String name;
  private final int index;
  private final Color juiceColor;
  private final float JuicinessDensity;
  private final float JuicinessLowerPressure;
  private final float JuicinessUpperPressure;
  private final boolean juicy;
  private final float hardness;
  private final double ignitionTemperature;
  private final double flameTemperature;
  private final double energy;
  private final boolean flammable;
  private Liquid juice;

  public Material(
      String name,
      int index,
      Color color,
      float density,
      float friction,
      float restitution,
      float tenacity,
      float hardness,
      Liquid Juice,
      float jd,
      float jlp,
      float jup,
      boolean flammable,
      double ignitionTemperature,
      double flameTemperature,
      double energy) {
    this.juice = Juice;
    this.density = density;
    this.friction = friction;
    this.restitution = restitution;
    this.color = color;
    this.name = name;
    this.hardness = hardness;
    this.index = index;
    this.tenacity = tenacity;
    this.juicy = true;
    this.JuicinessLowerPressure = jlp;
    this.JuicinessUpperPressure = jup;
    this.JuicinessDensity = jd;
    this.juiceColor = Juice.getProperties().getDefaultColor();
    this.flammable = flammable;
    this.ignitionTemperature = ignitionTemperature;
    this.flameTemperature = flameTemperature;
    this.energy = energy;
  }

  public Material(
      String name,
      int index,
      Color color,
      float density,
      float friction,
      float restitution,
      float tenacity,
      float hardness,
      boolean flammable,
      double ignitionTemperature,
      double flameTemperature,
      double energy) {
    this.density = density;
    this.friction = friction;
    this.restitution = restitution;
    this.color = color;
    this.name = name;
    this.index = index;
    this.tenacity = tenacity;
    this.hardness = hardness;
    this.juicy = false;
    this.JuicinessLowerPressure = 0;
    this.JuicinessUpperPressure = 0;
    this.JuicinessDensity = 0;
    this.juiceColor = null;
    this.flammable = flammable;
    this.ignitionTemperature = ignitionTemperature;
    this.flameTemperature = flameTemperature;
    this.energy = energy;
  }

  public double getFlameTemperature() {
    return flameTemperature;
  }

  public double getEnergy() {
    return energy;
  }

  public double getIgnitionTemperature() {
    return ignitionTemperature;
  }

  public boolean isFlammable() {
    return flammable;
  }

  public boolean isJuicy() {
    return juicy;
  }

  public float getDensity() {
    return density;
  }

  public float getFriction() {
    return friction;
  }

  public float getRestitution() {
    return restitution;
  }

  public org.andengine.util.adt.color.Color getColor() {
    return color;
  }

  public String getName() {
    return name;
  }

  public int getIndex() {
    return index;
  }

  public float getTenacity() {
    return tenacity;
  }

  public float getJuicinessDensity() {
    return JuicinessDensity;
  }

  public float getJuicinessLowerPressure() {
    return JuicinessLowerPressure;
  }

  public float getJuicinessUpperPressure() {
    return JuicinessUpperPressure;
  }

  public org.andengine.util.adt.color.Color getJuiceColor() {
    return juiceColor;
  }

  public float getHardness() {
    return hardness;
  }
}
