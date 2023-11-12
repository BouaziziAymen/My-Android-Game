package com.evolgames.entities.factories;

import com.evolgames.entities.Material;
import com.evolgames.entities.properties.LayerProperties;
import org.andengine.util.adt.color.Color;

public class PropertiesFactory {
  static final PropertiesFactory INSTANCE = new PropertiesFactory();

  public static PropertiesFactory getInstance() {
    return INSTANCE;
  }

  public LayerProperties createProperties(Material material) {
    LayerProperties properties = new LayerProperties();
    properties.setJuicy(material.isJuicy());
    properties.setFlammable(material.isFlammable());
    properties.setDensity(material.getDensity());
    properties.setRestitution(material.getRestitution());
    properties.setFriction(material.getFriction());
    properties.setTenacity(material.getTenacity());
    properties.setHardness(material.getHardness());
    properties.setSharpness(0f);
    properties.setJuicinessDensity(material.getJuicinessDensity());
    properties.setJuicinessLowerPressure(material.getJuicinessLowerPressure());
    properties.setJuicinessUpperPressure(material.getJuicinessUpperPressure());
    properties.setMaterialNumber(material.getIndex());

    if (material.getJuiceColor() != null) {
      properties.setJuiceColor(new Color(material.getJuiceColor()));
    }
    properties.setDefaultColor(new Color(material.getColor()));
    properties.setIgnitionTemperature(material.getIgnitionTemperature());
    properties.setFlameTemperature(material.getFlameTemperature());
    properties.setChemicalEnergy(material.getEnergy());

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
    properties.setFlammable(original.isFlammable());
    properties.setMaterialNumber(original.getMaterialNumber());
    if (original.getJuiceColor() != null) {
      properties.setJuiceColor(new Color(original.getJuiceColor()));
    }
    properties.setIgnitionTemperature(original.getIgnitionTemperature());
    properties.setFlameTemperature(original.getFlameTemperature());
    properties.setChemicalEnergy(original.getChemicalEnergy());
    properties.setSharpness(original.getSharpness());
    return properties;
  }
}
