package com.evolgames.userinterface.view.windows.windowfields;

import com.evolgames.activity.ResourceManager;
import com.evolgames.userinterface.view.basics.Container;
import com.evolgames.userinterface.view.basics.Image;

import org.andengine.util.adt.color.Color;

public class ColorSlot extends Container {

  private final Image inner;
  private final Color color = new Color(1f,1f,1f);

  public ColorSlot() {
    super();
    Image slot = new Image(ResourceManager.getInstance().slotTextureRegion);
    inner = new Image(ResourceManager.getInstance().slotInnerTextureRegion);
    addElement(slot);
    addElement(inner);
    inner.setColor(color.getRed(),color.getGreen(),color.getBlue());
  }

  @Override
  public void setColor(float pRed, float pGreen, float pBlue) {
    inner.setColor(pRed, pGreen, pBlue);
    color.set(pRed,pGreen,pBlue);
  }

  public Color getColor() {
    return color;
  }
}
