package com.evolgames.scenes;

import com.evolgames.scenes.entities.SceneType;
import com.evolgames.userinterface.view.UserInterface;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

public class MainScene extends AbstractScene<UserInterface<?>> {

  protected AbstractScene<?> scene;

  public MainScene(Camera pCamera) {
    super(pCamera, SceneType.MAIN);
  }

  @Override
  public void populate() {}

  public void changeScene(SceneType sceneType, boolean populate) {
    if (sceneType == SceneType.MAIN) {
      throw new UnsupportedOperationException("Cannot load main scene here");
    }
    this.clearChildScene();
    if (this.scene != null) {
      this.scene.detach();
    }
    switch (sceneType) {
      case MENU:
        this.scene = new MenuScene(this.mCamera);
        break;
      case PLAY:
        this.scene = new PlayScene(this.mCamera);
        break;
      case EDITOR:
        this.scene = new EditorScene(this.mCamera);
        break;
    }
    if (this.scene != null) {
      if (populate) {
        this.scene.populate();
      }
      this.setChildScene(this.scene, false, false, false);
    }
  }

  @Override
  public void onPause() {
    saveStringToPreferences("SCENE", scene.getSceneName().name());
    if (this.scene != null) {
      this.scene.onPause();
    }
  }

  @Override
  public void onResume() {
 //String sceneName="EDITOR";
   String sceneName = loadStringFromPreferences("SCENE");
    if (!sceneName.isEmpty()) {
      changeScene(SceneType.valueOf(sceneName), false);
    } else {
      changeScene(SceneType.MENU, true);
    }
    this.scene.onResume();
  }

  @Override
  public void createUserInterface() {}

  @Override
  protected void processTouchEvent(TouchEvent touchEvent, TouchEvent hudTouchEvent) {}

  @Override
  public void detach() {
    if (this.scene != null) {
      this.scene.detach();
    }
  }

  public void onBackgroundImageLoaded() {}

  @Override
  public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
    return false;
  }

  public void goToScene(SceneType sceneType) {
    changeScene(sceneType, true);
    this.saveStringToPreferences("SCENE", "");
    this.scene.createUserInterface();
  }
}
