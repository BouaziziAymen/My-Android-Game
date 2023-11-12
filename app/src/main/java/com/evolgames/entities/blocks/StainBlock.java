package com.evolgames.entities.blocks;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.factories.MeshFactory;
import com.evolgames.entities.properties.StainProperties;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.helpers.utilities.Utils;
import org.andengine.opengl.texture.region.ITextureRegion;

public final class StainBlock extends AssociatedBlock<StainBlock, StainProperties> {

  private float[] data;
  private int priority;

  public ITextureRegion getTextureRegion() {
    return ResourceManager.getInstance()
        .stainTextureRegions
        .getTextureRegion(getProperties().getTextureRegionIndex());
  }

  public float getLocalCenterX() {
    return getProperties().getLocalCenter().x;
  }

  public float getLocalCenterY() {
    return getProperties().getLocalCenter().y;
  }

  public float getLocalRotation() {
    return getProperties().getRotation();
  }

  @Override
  protected void calculateArea() {}

  @Override
  protected boolean shouldCalculateArea() {
    return false;
  }

  @Override
  protected StainBlock createChildBlock() {
    return new StainBlock();
  }

  public float[] getData() {
    return data;
  }

  public void setData(float[] meshData) {
    data = meshData;
  }

  public void computeData() {
    data = MeshFactory.getInstance().getStainData(this);
  }

  @Override
  public void translate(Vector2 translationVector) {
    Utils.translatePoints(getVertices(), translationVector);
    computeTriangles();
    getProperties()
        .setLocalCenter(
            getLocalCenterX() - translationVector.x, getLocalCenterY() - translationVector.y);
    computeData();
  }

  @Override
  protected StainBlock getThis() {
    return this;
  }

  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }
}
