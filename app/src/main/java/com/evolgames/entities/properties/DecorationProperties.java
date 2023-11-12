package com.evolgames.entities.properties;

import org.andengine.util.adt.color.Color;

public class DecorationProperties extends ColoredProperties {
  public DecorationProperties(Color color) {
    setDefaultColor(color);
  }

  @SuppressWarnings("unused")
  public DecorationProperties() {
  }

  @Override
  public ColoredProperties copy() {
    return new DecorationProperties(getDefaultColor());
  }
}
