package com.evolgames.entities.properties;

import android.util.Log;

import org.andengine.util.adt.color.Color;

public class Material {

    private final float Tenacity;
    private final float Density;
    private final float Friction;
    private final float Restitution;
    private final Color Color;
    private final String Name;
    private final int Index;
    private final Color juiceColor;
    private final float JuicinessDensity;
    private final float JuicinessLowerPressure;
    private final float JuicinessUpperPressure;
    private final boolean Juicy;
    private Liquid Juice;
    private double ignitionTemperature;
    private double flameTemperature;
    private double energy;
    private boolean Flammable;

    public double getFlameTemperature() {
        return flameTemperature;
    }

    public double getEnergy() {
        return energy;
    }

    public Material(String name, int index, Color color, float density, float friction, float restitution, float tenacity, Liquid Juice, float jd, float jlp, float jup, boolean flammable, double ignitionTemperature, double flameTemperature, double energy) {
        this.Juice = Juice;
        Density = density * 4;
        Friction = friction;
        Restitution = restitution;
        Color = color;
        Name = name;
        Index = index;
        Tenacity = tenacity;
        Juicy = true;
        JuicinessLowerPressure = jlp;
        JuicinessUpperPressure = jup;
        JuicinessDensity = jd;
        this.juiceColor = Juice.getProperties().getDefaultColor();
        this.Flammable = flammable;
        this.ignitionTemperature = ignitionTemperature;
        this.flameTemperature = flameTemperature;
        this.energy = energy;
    }
    public Material(String name, int index, Color color, float density, float friction, float restitution, float tenacity,boolean flammable, double ignitionTemperature,double flameTemperature,double energy) {
        Density = density * 4;
        Friction = friction;
        Restitution = restitution;
        Color = color;
        Name = name;
        Index = index;
        Tenacity = tenacity;
        Juicy = false;
        JuicinessLowerPressure = 0;
        JuicinessUpperPressure = 0;
        JuicinessDensity = 0;
        this.juiceColor = null;
        this.Flammable = flammable;
        this.ignitionTemperature = ignitionTemperature;
        this.flameTemperature = flameTemperature;
        this.energy = energy;
    }


    public double getIgnitionTemperature() {
        return ignitionTemperature;
    }

    public boolean isFlammable() {
        return Flammable;
    }

    public boolean isJuicy() {
        return Juicy;
    }

    public float getDensity() {
        return Density;
    }

    public float getFriction() {
        return Friction;
    }

    public float getRestitution() {
        return Restitution;
    }


    public org.andengine.util.adt.color.Color getColor() {
        return Color;
    }

    public String getName() {
        return Name;
    }

    public int getIndex() {
        return Index;
    }

    public float getTenacity() {
        return Tenacity;
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


}
