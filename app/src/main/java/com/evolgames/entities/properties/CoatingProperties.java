package com.evolgames.entities.properties;

import com.evolgames.entities.blockvisitors.utilities.MyColorUtils;
import org.andengine.util.adt.color.Color;

public class CoatingProperties extends ColoredProperties {
  private final Color textureColor = new Color(0, 0, 0, 0);
  private final Color radianceColor = new Color(0, 0, 0, 0);
  private final Color flameColor1 = new Color(0, 0, 0);
  private final Color flameColor2 = new Color(0, 0, 0);
  private double nonBurnableChemicalEnergy;
  private double initialChemicalEnergy;
  private LayerProperties parentProperties;
  private boolean hasTexture, hasRadiance;
  private int row;
  private int column;
  private double temperature;
  private double burnRatio;
  private double chemicalEnergy;

  public CoatingProperties(
      int row,
      int column,
      double temperature,
      double burnRatio,
      double chemicalEnergy,
      LayerProperties parentProperties) {
    this.row = row;
    this.column = column;
    this.temperature = temperature;
    this.burnRatio = burnRatio;
    this.chemicalEnergy = chemicalEnergy;
    this.initialChemicalEnergy = chemicalEnergy;
    this.parentProperties = parentProperties;
    this.nonBurnableChemicalEnergy = chemicalEnergy*Math.random()*0.4f;
  }

  public CoatingProperties() {}

  public double getNonBurnableChemicalEnergy() {
    return nonBurnableChemicalEnergy;
  }

  public void setNonBurnableChemicalEnergy(double nonBurnableChemicalEnergy) {
    this.nonBurnableChemicalEnergy = nonBurnableChemicalEnergy;
  }

  public void updateColors() {
    MyColorUtils.setupFlameColor(flameColor1, getFlameTemperature());
    MyColorUtils.setupFlameColor(flameColor2, Math.max(0, temperature - 2000));
    hasTexture = MyColorUtils.setTextureColor(textureColor, burnRatio);
    hasRadiance = MyColorUtils.setRadianceColor(radianceColor, temperature);
    setupCoatingColor();
  }

  private void setupCoatingColor() {
    if (hasTexture && hasRadiance) {
      MyColorUtils.blendColors(getDefaultColor(), textureColor, radianceColor);
    } else if (hasTexture) {
      getDefaultColor().set(textureColor);
    } else if (hasRadiance) {
      getDefaultColor().set(radianceColor);
    }
  }

  public void applyDeltaTemperature(double delta) {
    if (temperature + delta < 2000000) {
      temperature += delta;
    } else {
      temperature = 2000000;
    }
  }

  public CoatingProperties copy() {
    CoatingProperties properties = new CoatingProperties();
    properties.setDefaultColor(getDefaultColor());
    properties.setParentProperties(parentProperties);
    properties.setRow(row);
    properties.setColumn(column);
    properties.setTemperature(temperature);
    properties.setBurnRatio(burnRatio);
    properties.setChemicalEnergy(chemicalEnergy);
    properties.setInitialChemicalEnergy(initialChemicalEnergy);
    properties.setNonBurnableChemicalEnergy(nonBurnableChemicalEnergy);
    return properties;
  }

  public double getIgnitionTemperature() {
    return parentProperties.getIgnitionTemperature();
  }

  public double getFlameTemperature() {
    return parentProperties.getFlameTemperature();
  }

  public boolean isCombustible() {
    return parentProperties.isCombustible();
  }

  public double getInitialChemicalEnergy() {
    return initialChemicalEnergy;
  }

  public void setInitialChemicalEnergy(double initialChemicalEnergy) {
    this.initialChemicalEnergy = initialChemicalEnergy;
  }

  public void setParentProperties(LayerProperties parentProperties) {
    this.parentProperties = parentProperties;
  }

  public int getRow() {
    return row;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public int getColumn() {
    return column;
  }

  public void setColumn(int column) {
    this.column = column;
  }

  public double getTemperature() {
    return temperature;
  }

  public void setTemperature(double temperature) {
    this.temperature = temperature;
  }

  public double getBurnRatio() {
    return burnRatio;
  }

  public void setBurnRatio(double burnRatio) {
    this.burnRatio = burnRatio;
  }

  public double getChemicalEnergy() {
    return chemicalEnergy;
  }

  public void setChemicalEnergy(double chemicalEnergy) {
    this.chemicalEnergy = chemicalEnergy;
  }

  public float getHeatResistance() {
    return parentProperties.getHeatResistance();
  }
}
