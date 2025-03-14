package com.evolgames.dollmutilationgame.entities.factories;

import com.evolgames.dollmutilationgame.entities.basics.Material;
import com.evolgames.dollmutilationgame.entities.properties.LayerProperties;

import org.andengine.util.adt.color.Color;

public class PropertiesFactory {
    static final PropertiesFactory INSTANCE = new PropertiesFactory();

    public static PropertiesFactory getInstance() {
        return INSTANCE;
    }

    public LayerProperties createProperties(Material material) {
        LayerProperties properties = new LayerProperties();
        properties.setJuicy(material.isJuicy());
        properties.setCombustible(material.isCombustible());
        properties.setDensity(material.getDensity());
        properties.setRestitution(material.getRestitution());
        properties.setFriction(material.getFriction());
        properties.setTenacity(material.getTenacity());
        properties.setHardness(material.getHardness());
        properties.setJuiceIndex(material.getJuiceIndex());
        properties.setJuicinessDensity(material.getJuicinessDensity());
        properties.setJuicinessLowerPressure(material.getJuicinessLowerPressure());
        properties.setJuicinessUpperPressure(material.getJuicinessUpperPressure());
        properties.setMaterialNumber(material.getIndex());
        properties.setJuiceIndex(material.getJuiceIndex());
        properties.setDefaultColor(new Color(material.getColor()));
        properties.setIgnitionTemperature(material.getIgnitionTemperature());
        properties.setFlameTemperature(material.getFlameTemperature());
        properties.setChemicalEnergy(material.getEnergy());
        properties.setFlammable(material.isFlammable());
        properties.setFlammability(material.getFlammability());
        properties.setJuiceColor(new Color(material.getJuiceColor()));
        properties.setJuiceFlammability(material.getJuiceFlammability());
        properties.setHeatResistance(material.getHeatResistance());
        return properties;
    }

    public LayerProperties createProperties(LayerProperties original) {
        LayerProperties properties = new LayerProperties();
        properties.setDensity(original.getDensity());
        properties.setRestitution(original.getRestitution());
        properties.setFriction(original.getFriction());
        properties.setTenacity(original.getTenacity());
        properties.setHardness(original.getHardness());
        properties.setJuicinessDensity(original.getJuicinessDensity());
        properties.setJuicinessLowerPressure(original.getJuicinessLowerPressure());
        properties.setJuicinessUpperPressure(original.getJuicinessUpperPressure());
        properties.setDefaultColor(new Color(original.getDefaultColor()));
        properties.setOrder(original.getOrder());
        properties.setJuicy(original.isJuicy());
        properties.setJuiceIndex(original.getJuiceIndex());
        properties.setCombustible(original.isCombustible());
        properties.setMaterialNumber(original.getMaterialNumber());
        properties.setIgnitionTemperature(original.getIgnitionTemperature());
        properties.setFlameTemperature(original.getFlameTemperature());
        properties.setChemicalEnergy(original.getChemicalEnergy());
        properties.setSharpness(original.getSharpness());
        if (original.getJuiceColor() != null) {
            properties.setJuiceColor(new Color(original.getJuiceColor()));
        }
        properties.setJuiceFlammability(original.getJuiceFlammability());
        properties.setHeatResistance(original.getHeatResistance());
        return properties;
    }
}
