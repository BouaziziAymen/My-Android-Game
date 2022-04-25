package com.evolgames.entities.properties;

import com.evolgames.helpers.utilities.MyColorUtils;

import org.andengine.util.adt.color.Color;

public class CoatingProperties extends BlockProperties {
    private final double initialChemicalEnergy;
    private Color textureColor = new Color(0, 0, 0, 0);
    private Color radianceColor = new Color(0, 0, 0, 0);
    private Color flameColor1 = new Color(0, 0, 0);
    private Color flameColor2 = new Color(0, 0, 0);
    private boolean hasTexture, hasRadiance;
    private int row;
    private int column;
    private double temperature;
    private double burnRatio;
    private double chemicalEnergy;
    private BlockAProperties properties;

    public CoatingProperties(BlockAProperties properties, int row, int column, double temperature, double burnRatio, double chemicalEnergy) {
        this.row = row;
        this.column = column;
        this.temperature = temperature;
        this.burnRatio = burnRatio;
        this.chemicalEnergy = chemicalEnergy;
        this.initialChemicalEnergy = chemicalEnergy;
        this.properties = properties;
    }

    private CoatingProperties(BlockAProperties properties, int row, int column, double temperature, double burnRatio, double chemicalEnergy, double initialChemicalEnergy) {
        this.row = row;
        this.column = column;
        this.temperature = temperature;
        this.burnRatio = burnRatio;
        this.chemicalEnergy = chemicalEnergy;
        this.initialChemicalEnergy = initialChemicalEnergy;
        this.properties = properties;
    }

    public Color getFlameColor1() {
        return flameColor1;
    }

    public Color getFlameColor2() {
        return flameColor2;
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

    public double getInitialChemicalEnergy() {
        return initialChemicalEnergy;
    }

    public double getChemicalEnergy() {
        return chemicalEnergy;
    }

    public void setChemicalEnergy(double chemicalEnergy) {
        this.chemicalEnergy = chemicalEnergy;
    }

    public void updateColors() {
        MyColorUtils.setupFlameColor(flameColor1, getFlameTemperature());
        MyColorUtils.setupFlameColor(flameColor2, Math.max(0, temperature - 2000));
        hasTexture = MyColorUtils.setTextureColor(textureColor, burnRatio);
        hasRadiance = MyColorUtils.setRadianceColor(radianceColor, temperature);
        setupCoatingColor();

    }

    private void setupCoatingColor() {
        if (hasTexture && hasRadiance)
            MyColorUtils.blendColors(getDefaultColor(), textureColor, radianceColor);
        else if (hasTexture)
            getDefaultColor().set(textureColor);
        else if (hasRadiance)
            getDefaultColor().set(radianceColor);

    }

    public void applyDeltaTemperature(double delta) {
        if (temperature + delta < 2000000)
            temperature += delta;
        else temperature = 2000000;
    }

    public boolean isFlammable() {
        return properties.isFlammable();
    }

    public CoatingProperties copy() {
        return new CoatingProperties(properties, row, column, temperature, burnRatio, chemicalEnergy, initialChemicalEnergy);
    }

    public double getBurnRatio() {

        return burnRatio;
    }

    public void setBurnRatio(double burnRatio) {
        this.burnRatio = burnRatio;
    }

    public double getIgnitionTemperature() {
        return properties.getIgnitionTemperature();
    }
    public double getFlameTemperature(){
        return properties.getFlameTemperature();
    }

    @Override
    public BlockProperties getCopy() {
        return null;
    }
}
