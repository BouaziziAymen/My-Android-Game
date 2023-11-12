package com.evolgames.userinterface.view.basics;

import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.control.windowcontrollers.AdvancedWindowController;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.layouts.LinearLayout;
import java.util.ArrayList;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;

public class Panel extends LinearLayout {

  private Button<AdvancedWindowController<?>> closeButton;
  private Button<AdvancedWindowController<?>> acceptButton;

  public Panel(float pX, float pY, int pRows, boolean hasAcceptButton, boolean hasCloseButton) {
    super(pX, pY, Direction.Horizontal, -2);
    ArrayList<ITextureRegion> regions = ResourceManager.getInstance().panel;
    TiledTextureRegion acceptButtonRegion = ResourceManager.getInstance().panelButtonTextureRegion1;
    TiledTextureRegion closeButtonRegion = ResourceManager.getInstance().panelButtonTextureRegion2;

    if (hasCloseButton) {
      closeButton = new Button<>(closeButtonRegion, Button.ButtonType.OneClick, true);
      this.addToLayout(closeButton);
    }

    for (int i = 0; i < pRows; i++) {
      int regionKey;
      if (i == 0 && !hasCloseButton) regionKey = 0;
      else if (i == pRows - 1 && !hasAcceptButton) regionKey = 2;
      else regionKey = 1;
      Image sprite = new Image(regions.get(regionKey));
      addToLayout(sprite);
    }

    if (hasAcceptButton) {
      acceptButton = new Button<>(acceptButtonRegion, Button.ButtonType.OneClick, true);
      this.addToLayout(acceptButton);
    }
  }

  public Button<AdvancedWindowController<?>> getCloseButton() {
    return closeButton;
  }

  public Button<AdvancedWindowController<?>> getAcceptButton() {
    return acceptButton;
  }
}
