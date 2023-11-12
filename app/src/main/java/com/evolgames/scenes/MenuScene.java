package com.evolgames.scenes;

import com.evolgames.scenes.entities.SceneType;
import com.evolgames.userinterface.view.MenuUserInterface;
import org.andengine.engine.camera.Camera;

public class MenuScene extends PhysicsScene<MenuUserInterface> {
  public MenuScene(Camera pCamera) {
    super(pCamera, SceneType.MENU);
  }

  @Override
  public void populate() {}

  @Override
  public void detach() {}

  @Override
  public void onPause() {}

  @Override
  public void onResume() {}
}
