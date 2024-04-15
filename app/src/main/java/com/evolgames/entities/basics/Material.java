package com.evolgames.entities.basics;


import com.evolgames.entities.factories.MaterialFactory;
import com.evolgames.entities.factories.Materials;

import org.andengine.util.adt.color.Color;

public class Material {

    private final float tenacity;
    private final float density;
    private final float friction;
    private final float restitution;
    private final Color color;
    private final String name;
    private final int index;
    private final float JuicinessDensity;
    private final float JuicinessLowerPressure;
    private final float JuicinessUpperPressure;
    private final boolean juicy;
    private final Color juiceColor;
    private final float juiceFlammability;
    private final float hardness;
    private final double ignitionTemperature;
    private final double flameTemperature;
    private final double energy;
    private final boolean combustible;
    private final boolean flammable;

    private final float flammability;
    private final float heatResistance;
    private int juiceIndex;

    public Material(
            String name,
            int index,
            Color color,
            float heatResistance,
            int juiceIndex,
            float jd,
            float jlp,
            float jup,
            boolean combustible,
            double ignitionTemperature,
            double flameTemperature,
            boolean flammable,
            float flammability,
            double energy) {
        this.juiceIndex = juiceIndex;
        float[] values = MaterialFactory.materialProperties.get(index);
        this.density = values[0] * 4;
        this.restitution = values[1];
        this.friction = values[2];
        this.tenacity = values[3];
        this.hardness = values[4];
        this.color = color;
        this.name = name;

        this.heatResistance = heatResistance;
        this.index = index;

        this.juicy = jd > 0f;
        this.JuicinessLowerPressure = jlp;
        this.JuicinessUpperPressure = jup;
        this.JuicinessDensity = jd;
        this.juiceColor = new Color(Materials.getLiquidByIndex(juiceIndex).getDefaultColor());
        this.juiceFlammability = Materials.getLiquidByIndex(juiceIndex).getFlammability();
        this.combustible = combustible;
        this.ignitionTemperature = ignitionTemperature;
        this.flameTemperature = flameTemperature;
        this.energy = 3f * energy;
        this.flammable = flammable;
        this.flammability = flammability;
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

    public boolean isCombustible() {
        return combustible;
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

    public Color getColor() {
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

    public Color getJuiceColor() {
        return juiceColor;
    }

    public float getHardness() {
        return hardness;
    }

    public int getJuiceIndex() {
        return juiceIndex;
    }

    public void setJuiceIndex(int juiceIndex) {
        this.juiceIndex = juiceIndex;
    }

    public boolean isFlammable() {
        return flammable;
    }

    public float getFlammability() {
        return flammability;
    }

    public float getJuiceFlammability() {
        return juiceFlammability;
    }

    public float getHeatResistance() {
        return heatResistance;
    }

}
