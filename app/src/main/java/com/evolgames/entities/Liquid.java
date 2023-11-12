package com.evolgames.entities;

import com.evolgames.entities.properties.JuiceProperties;

public class Liquid {
  private final JuiceProperties properties;

  public Liquid(JuiceProperties properties) {
    this.properties = properties;
  }

  public JuiceProperties getProperties() {
    return properties;
  }
}
