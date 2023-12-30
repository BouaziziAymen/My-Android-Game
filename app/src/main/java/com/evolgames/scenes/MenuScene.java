package com.evolgames.scenes;

import com.evolgames.scenes.entities.SceneType;
import com.evolgames.userinterface.view.MenuUserInterface;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

public class MenuScene extends PhysicsScene<MenuUserInterface> {
  public MenuScene(Camera pCamera) {
    super(pCamera, SceneType.MENU);
  }

  @Override
  public void populate() {}

  @Override
  public void detach() {
    if(this.userInterface!=null){
      this.userInterface.detachSelf();
    }
  }

  @Override
  public void onPause() {
    this.detach();
  }

  @Override
  public void onResume() {
    createUserInterface();
  }

  @Override
  public void createUserInterface() {
    this.userInterface = new MenuUserInterface(this);
  }

  @Override
  protected void processTouchEvent(TouchEvent touchEvent, TouchEvent hudTouchEvent) {
    boolean hudTouched = this.userInterface.onTouchHud(hudTouchEvent, false);
  }

  public void goToScene(SceneType sceneType){
    ((MainScene)this.mParentScene).goToScene(sceneType);
  }
}
