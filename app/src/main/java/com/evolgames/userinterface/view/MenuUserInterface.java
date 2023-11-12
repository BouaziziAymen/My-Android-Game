package com.evolgames.userinterface.view;

import com.evolgames.scenes.MenuScene;
import org.andengine.input.touch.TouchEvent;

public class MenuUserInterface extends UserInterface<MenuScene> {
  protected MenuUserInterface(MenuScene scene) {
    super(scene);
  }

  @Override
  public void onTouchScene(TouchEvent pTouchEvent, boolean scroll) {}
}
