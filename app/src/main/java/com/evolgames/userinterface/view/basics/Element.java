package com.evolgames.userinterface.view.basics;

import com.evolgames.userinterface.view.sections.Section;
import com.evolgames.userinterface.view.Color;
import com.evolgames.userinterface.view.inputs.bounds.Bounds;
import com.evolgames.userinterface.view.windows.WindowPartIdentifier;
import com.evolgames.userinterface.view.windows.windowfields.ErrorDisplay;

public abstract class Element implements Comparable<Element> {
  protected float mWidth;
  protected float mHeight;
  protected float scaleX = 1;
  protected float scaleY = 1;
  float mRed = 1;
  float mGreen = 1;
  float mBlue = 1;
  private int id = 0;
  private float lowerBottomX;
  private float lowerBottomY;
  private Bounds bounds;
  private Element parent;
  private boolean updated;
  private WindowPartIdentifier windowPartIdentifier;
  private boolean isVisible = true;
  private boolean isScaled;
  private Section section;
  private float padding;
  private int depth;
  private ErrorDisplay errorDisplay;
  private boolean shaded;

  protected Element(float pX, float pY) {
    this.lowerBottomX = pX;
    this.lowerBottomY = pY;
  }

  Element(float pX, float pY, float pWidth, float pHeight) {
    this(pX, pY);
    this.mWidth = pWidth;
    this.mHeight = pHeight;
  }

  public Element() {}

  public float getRed() {
    return mRed;
  }

  public float getGreen() {
    return mGreen;
  }

  public float getBlue() {
    return mBlue;
  }

  public float getPadding() {
    return padding;
  }

  public void setPadding(float padding) {
    this.padding = padding;
  }

  private int getDepth() {
    return depth;
  }

  public void setDepth(int zIndex) {
    this.depth = zIndex;
    setUpdated(true);
  }

  public Section getSection() {
    return section;
  }

  public void setSection(Section section) {
    this.section = section;
  }

  public WindowPartIdentifier getWindowPartIdentifier() {
    return windowPartIdentifier;
  }

  public void setWindowPartIdentifier(WindowPartIdentifier windowPartIdentifier) {
    this.windowPartIdentifier = windowPartIdentifier;
  }

  public boolean isUpdated() {

    return updated;
  }

  public void setUpdated(boolean updated) {
    this.updated = updated;
  }

  public Element getParent() {
    return parent;
  }

  protected void setParent(Element parent) {
    this.parent = parent;
  }

  public abstract void drawSelf();

  public float getLowerBottomX() {
    return lowerBottomX;
  }

  public void setLowerBottomX(float lowerBottomX) {
    this.lowerBottomX = lowerBottomX;
  }

  private boolean hasBounds() {
    return bounds != null;
  }

  public float getLowerBottomY() {
    return lowerBottomY;
  }

  public void setLowerBottomY(float lowerBottomY) {
    this.lowerBottomY = lowerBottomY;
  }

  public void setPosition(float pX, float pY) {
    setLowerBottomX(pX);
    setLowerBottomY(pY);
  }

  public void setColor(float pRed, float pGreen, float pBlue) {
    setUpdated(true);
    this.mRed = pRed;
    this.mGreen = pGreen;
    this.mBlue = pBlue;
  }

  public float getWidth() {
    return mWidth;
  }

  public void setWidth(float mWidth) {
    this.mWidth = mWidth;
  }

  public float getHeight() {
    return mHeight;
  }

  public void setHeight(float mHeight) {
    this.mHeight = mHeight;
  }

  public Bounds getBounds() {
    return bounds;
  }

  public void setBounds(Bounds bounds) {
    this.bounds = bounds;
  }

  public boolean isInBounds(float pX, float pY) {
    if (bounds == null) return false;
    return bounds.isInBounds(pX, pY);
  }

  protected boolean hasParent() {
    return parent != null;
  }

  public float getAbsoluteX() {
    if (hasParent()) {
      return this.getLowerBottomX() + parent.getAbsoluteX();
    } else return this.getLowerBottomX();
  }

  public float getAbsoluteY() {
    if (hasParent()) {
      return this.getLowerBottomY() + parent.getAbsoluteY();
    } else return this.getLowerBottomY();
  }

  public boolean isScaled() {
    return isScaled;
  }

  public void setScale(float pScaleX, float pScaleY) {
    this.scaleX = pScaleX;
    this.scaleY = pScaleY;
    isScaled = true;
    setUpdated(true);
  }

  public boolean isVisible() {
    return isVisible;
  }

  public void setVisible(boolean visible) {
    if (visible != isVisible) {
      setUpdated(true);
    }
    isVisible = visible;
  }

  @Override
  public int compareTo(Element o) {
    return Integer.compare(o.getDepth(), getDepth());
  }

  @Override
  public String toString() {
    return "" + getClass().getSimpleName();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public ErrorDisplay getErrorDisplay() {
    return errorDisplay;
  }

  public void setErrorDisplay(ErrorDisplay errorDisplay) {
    this.errorDisplay = errorDisplay;
  }

  public boolean isShaded() {
    return shaded;
  }

  public void setShaded(boolean shaded) {
    this.shaded = shaded;
  }

  public void setColor(Color color) {
    setColor(color.getRed(), color.getGreen(), color.getBlue());
  }

  public void updateZoom(float pZoomFactor) {}

}
