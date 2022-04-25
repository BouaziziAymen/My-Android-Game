package com.evolgames.entities.properties;

import org.andengine.util.adt.color.Color;

public class JuiceProperties extends PropertiesWithColor {
    public String getJuiceName() {
        return JuiceName;
    }

    private String JuiceName;
    public JuiceProperties(String name,Color color){
      JuiceName = name;
        setDefaultColor(color);
      float[] properties = new float[]{0.1f,0.1f,0.5f,80};
      setProperties(properties);
    }
}
