package com.evolgames.entities.particles.modifiers;

import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.util.adt.color.Color;

public class MyColorParticleInitializer<T> extends ColorParticleInitializer {

  public MyColorParticleInitializer(float pRed, float pGreen, float pBlue) {
    super(pRed, pGreen, pBlue);
    // TODO Auto-generated constructor stub
  }

  public void updateColor(Color first) {

    mMinValue = first.getRed();
    mMaxValue = first.getRed();
    mMinValueB = first.getGreen();
    mMaxValueB = first.getGreen();
    mMinValueC = first.getBlue();
    mMaxValueC = first.getBlue();
  }
}
