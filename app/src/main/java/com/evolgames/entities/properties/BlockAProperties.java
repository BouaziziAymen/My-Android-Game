package com.evolgames.entities.properties;

import com.badlogic.gdx.physics.box2d.Filter;
import com.evolgames.factories.PropertiesFactory;

import org.andengine.util.adt.color.Color;

public class BlockAProperties extends BlockProperties {


    private CollisionOption collisionOption;
    private int order;

    private Color juiceColor;
    private int materialNumber;
    private String layerName;
    private boolean Juicy;
    private boolean Flammable;
    private Filter filter;
    private double ignitionTemperature;
    private double chemicalEnergy;
    private double flameTemperature;

    public int getMaterialNumber() {
        return materialNumber;
    }

    public void setMaterialNumber(int materialNumber) {
        this.materialNumber = materialNumber;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public double getFlameTemperature() {
        return flameTemperature;
    }

    public void setFlameTemperature(double flameTemperature) {
        this.flameTemperature = flameTemperature;
    }

    public double getIgnitionTemperature() {
        return ignitionTemperature;
    }

    public void setIgnitionTemperature(double ignitionTemperature) {
        this.ignitionTemperature = ignitionTemperature;
    }

    public boolean isFlammable() {
        return Flammable;
    }

    public void setFlammable(boolean flammable) {
        Flammable = flammable;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }


    public boolean isJuicy() {
        return Juicy;
    }

    public void setJuicy(boolean juicy) {
        Juicy = juicy;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public float getDensity() {
        return properties[0];
    }

    public void setDensity(float density) {
        properties[0] = density;
    }

    public float getRestitution() {
        return properties[1];
    }

    public float getFriction() {
        return properties[2];
    }

    public CollisionOption getCollisionOption() {
        return collisionOption;
    }

    public void setCollisionOption(CollisionOption collisionOption) {
        this.collisionOption = collisionOption;
    }


    public float getJuicinessDensity() {
        return properties[4];
    }

    public void setJuicinessDensity(float juicinessVolume) {
        this.properties[4] = juicinessVolume;
    }

    public float getJuicinessLowerPressure() {
        return properties[5];
    }

    public void setJuicinessLowerPressure(float juicinessLowerPressure) {
        this.properties[5] = juicinessLowerPressure;
    }

    public float getJuicinessUpperPressure() {
        return properties[6];
    }

    public void setJuicinessUpperPressure(float juicinessUpperPressure) {
        this.properties[6] = juicinessUpperPressure;
    }

    public BlockAProperties getCopy() {
        return PropertiesFactory.getInstance().createProperties(this);
    }

    public float getTenacity() {
        return properties[3];
    }

    public void setTenacity(float tenacity) {
        properties[3] = tenacity;
    }


    public Color getJuiceColor() {
        return juiceColor;
    }

    public void setJuiceColor(Color juiceColor) {
        this.juiceColor = juiceColor;
    }

    public double getChemicalEnergy() {
        return chemicalEnergy;
    }

    public void setChemicalEnergy(double chemicalEnergy) {
        this.chemicalEnergy = chemicalEnergy;
    }
}
