package com.evolgames.userinterface.view.windows.windowfields;

import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.view.basics.Container;
import com.evolgames.userinterface.view.basics.Image;

public class ColorSlot extends Container {

  private final Image inner;

  public ColorSlot() {
    super();
    Image slot = new Image(ResourceManager.getInstance().slotTextureRegion);
    inner = new Image(ResourceManager.getInstance().slotInnerTextureRegion);
    addElement(slot);
    addElement(inner);
    inner.setColor(1, 1, 0);
  }

  @Override
  public void setColor(float pRed, float pGreen, float pBlue) {
    inner.setColor(pRed, pGreen, pBlue);
  }
}
