package com.evolgames.userinterface.view.shapes.indicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.scenes.EditorScene;
import com.evolgames.userinterface.view.Color;
import com.evolgames.userinterface.view.Colors;
import org.andengine.entity.primitive.LineStrip;
import org.andengine.extension.physics.box2d.util.Vector2Pool;

public class LineShape {
  protected boolean validated;
  protected boolean selected = false;
  protected EditorScene creationScene;
  protected LineStrip lineStrip;
  protected Vector2 end;
  protected Vector2 begin;
  protected Vector2 direction;
  protected float mRed = 1, mGreen = 1, mBlue = 1;
  private boolean congruentEndpoints = false;
  private boolean indicatorsVisible;
  private boolean visible = true;

  public LineShape(Vector2 begin, EditorScene scene) {
    creationScene = scene;
    this.end = Vector2Pool.obtain(begin);
    this.begin = Vector2Pool.obtain(begin);
    this.lineStrip = new LineStrip(0, 0, 5, 100, ResourceManager.getInstance().vbom);
    creationScene.attachChild(lineStrip);
    lineStrip.setZIndex(2);
    creationScene.sortChildren();
  }

  protected boolean isIndicatorsVisible() {
    return indicatorsVisible;
  }

  protected void setIndicatorsVisible(boolean indicatorsVisible) {
    this.indicatorsVisible = indicatorsVisible;
  }

  public boolean isAborted() {
    return !validated;
  }

  public boolean isCongruentEndpoints() {
    return congruentEndpoints;
  }

  public void setCongruentEndpoints(boolean congruentEndpoints) {
    this.congruentEndpoints = congruentEndpoints;
  }

  public boolean isSelected() {
    return selected;
  }

  public void select() {
    selected = true;
    Color green = Colors.palette1_light_green;
    setColor(green);
  }

  public void release() {
    selected = false;
    setColor(1, 1, 1);
  }

  public void setColor(Color color) {
    setColor(color.getRed(), color.getGreen(), color.getBlue());
  }

  public void setColor(float r, float g, float b) {
    mRed = r;
    mGreen = g;
    mBlue = b;
    if (lineStrip != null) {
      lineStrip.setColor(mRed, mGreen, mBlue);
    }
  }

  public void drawLine() {
    while (lineStrip.getIndex() != 0) {
      lineStrip.shift();
      lineStrip.setIndex(lineStrip.getIndex() - 1);
    }
    lineStrip.add(begin.x, begin.y);
    lineStrip.add(end.x, end.y);
    lineStrip.setColor(mRed, mGreen, mBlue);
    System.out.println("#######Projectile Shape visible line:" + visible);
    lineStrip.setVisible(visible);
  }

  protected void onExtremityUpdated() {
    validated = true;
    direction = Vector2Pool.obtain(end).sub(begin).nor();
    drawLine();
  }

  public void detach() {
    if (lineStrip != null) {
      lineStrip.detachSelf();
    }
  }

  public void updateEnd(float x, float y) {
    end.set(x, y);
    this.onExtremityUpdated();
  }

  public void updateBegin(float x, float y) {
    begin.set(x, y);
    this.onExtremityUpdated();
  }

  public boolean isVisible() {
    return visible;
  }

  public void setVisible(boolean b) {
    if (lineStrip != null) {
      lineStrip.setVisible(b);
    }
    this.visible = b;
  }

  public void showLimitsElements() {
    setIndicatorsVisible(true);
  }

  public void hideLimitsElements() {
    setIndicatorsVisible(false);
  }
}
