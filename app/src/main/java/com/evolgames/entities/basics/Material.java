package com.evolgames.entities.basics;


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
    private int juiceIndex;

    public Material(
            String name,
            int index,
            Color color,
            float density,
            float friction,
            float restitution,
            float tenacity,
            float hardness,
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
        this.juiceColor = Materials.getLiquidByIndex(juiceIndex).getDefaultColor();
        this.juiceFlammability = Materials.getLiquidByIndex(juiceIndex).getFlammability();
        this.combustible = combustible;
        this.ignitionTemperature = ignitionTemperature;
        this.flameTemperature = flameTemperature;
        this.energy = energy;
        this.flammable = flammable;
        this.flammability = flammability;
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
            boolean combustible,
            double ignitionTemperature,
            double flameTemperature,
            boolean flammable,
            float flammability,
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
        this.juiceColor = new Color(Color.WHITE);
        this.juiceFlammability = 0f;
        this.JuicinessLowerPressure = 0;
        this.JuicinessUpperPressure = 0;
        this.JuicinessDensity = 0;
        this.combustible = combustible;
        this.ignitionTemperature = ignitionTemperature;
        this.flameTemperature = flameTemperature;
        this.energy = energy;
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
}
