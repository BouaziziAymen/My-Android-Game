package com.evolgames.entities.properties;

import androidx.annotation.NonNull;

import com.badlogic.gdx.math.Vector2;
import org.andengine.util.adt.color.Color;

public class StainProperties extends ColoredProperties {

  private Color color;
  private Vector2 localCenter;
  private int textureRegionIndex;
  private float rotation;

  @SuppressWarnings("unused")
  public StainProperties() {}

  public StainProperties(int textureRegionIndex, Vector2 localCenter, float rotation, Color color) {
    this.textureRegionIndex = textureRegionIndex;
    this.localCenter = new Vector2(localCenter);
    this.rotation = rotation;
    this.color = color;
  }

  @NonNull
  @Override
  public Object clone() {
    StainProperties clone = (StainProperties) super.clone();
    clone.setTextureRegionIndex(textureRegionIndex);
    clone.setLocalCenter(localCenter.cpy());
    clone.setRotation(rotation);
    clone.setDefaultColor(color);
    return clone;
  }

  public void setLocalCenter(Vector2 center) {
    this.localCenter = center;
  }

  public Color getColor() {
    return color;
  }

  public Vector2 getLocalCenter() {
    return localCenter;
  }

  public float getRotation() {
    return rotation;
  }

  public void setRotation(float rotation) {
    this.rotation = rotation;
  }

  public int getTextureRegionIndex() {
    return textureRegionIndex;
  }

  public void setTextureRegionIndex(int textureRegionIndex) {
    this.textureRegionIndex = textureRegionIndex;
  }
}
