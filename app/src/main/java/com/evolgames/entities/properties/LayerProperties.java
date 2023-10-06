package com.evolgames.entities.properties;

import com.evolgames.entities.factories.PropertiesFactory;

import org.andengine.util.adt.color.Color;

public class LayerProperties extends ColoredProperties {
    private String layerName;
    private int order;
    private int materialNumber;
    private boolean flammable;
    private double ignitionTemperature;
    private double chemicalEnergy;
    private double flameTemperature;
    private float density;
    private float restitution;
    private float friction;
    private float tenacity;
    private float hardness;
    private float sharpness = 0f;
    private boolean juicy;
    private float juicinessDensity;
    private float juicinessLowerPressure;
    private float juicinessUpperPressure;
    private Color juiceColor = new Color(Color.RED);
    private float heatResistance = 10f;

    public LayerProperties copy() {
        return PropertiesFactory.getInstance().createProperties(this);
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getMaterialNumber() {
        return materialNumber;
    }

    public void setMaterialNumber(int materialNumber) {
        this.materialNumber = materialNumber;
    }

    public boolean isFlammable() {
        return flammable;
    }

    public void setFlammable(boolean flammable) {
        this.flammable = flammable;
    }

    public double getIgnitionTemperature() {
        return ignitionTemperature;
    }

    public void setIgnitionTemperature(double ignitionTemperature) {
        this.ignitionTemperature = ignitionTemperature;
    }

    public double getChemicalEnergy() {
        return chemicalEnergy;
    }

    public void setChemicalEnergy(double chemicalEnergy) {
        this.chemicalEnergy = chemicalEnergy;
    }

    public double getFlameTemperature() {
        return flameTemperature;
    }

    public void setFlameTemperature(double flameTemperature) {
        this.flameTemperature = flameTemperature;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public float getRestitution() {
        return restitution;
    }

    public void setRestitution(float restitution) {
        this.restitution = restitution;
    }

    public float getFriction() {
        return friction;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public float getTenacity() {
        return tenacity;
    }

    public void setTenacity(float tenacity) {
        this.tenacity = tenacity;
    }

    public boolean isJuicy() {
        return juicy;
    }

    public void setJuicy(boolean juicy) {
        this.juicy = juicy;
    }

    public float getJuicinessDensity() {
        return juicinessDensity;
    }

    public void setJuicinessDensity(float juicinessDensity) {
        this.juicinessDensity = juicinessDensity;
    }


    public float getJuicinessLowerPressure() {
        return juicinessLowerPressure;
    }

    public void setJuicinessLowerPressure(float juicinessLowerPressure) {
        this.juicinessLowerPressure = juicinessLowerPressure;
    }

    public float getJuicinessUpperPressure() {
        return juicinessUpperPressure;
    }

    public void setJuicinessUpperPressure(float juicinessUpperPressure) {
        this.juicinessUpperPressure = juicinessUpperPressure;
    }

    public Color getJuiceColor() {
        return juiceColor;
    }

    public void setJuiceColor(Color juiceColor) {
        this.juiceColor = juiceColor;
    }

    public void setJuicinessVolume(float juicinessVolume) {
    }

    public float getHeatResistance() {
        return heatResistance;
    }

    public void setHeatResistance(float heatResistance) {
        this.heatResistance = heatResistance;
    }

    public float getSharpness() {
        return sharpness;
    }

    public void setSharpness(float sharpness) {
        this.sharpness = sharpness;
    }

    public float getHardness() {
        return hardness;
    }

    public void setHardness(float hardness) {
        this.hardness = hardness;
    }
}
