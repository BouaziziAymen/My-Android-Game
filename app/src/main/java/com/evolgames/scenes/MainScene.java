package com.evolgames.scenes;

import android.content.Context;
import android.content.SharedPreferences;

import com.evolgames.gameengine.ResourceManager;
import com.evolgames.scenes.entities.SceneType;
import com.evolgames.userinterface.view.UserInterface;

import org.andengine.engine.camera.Camera;

public class MainScene extends AbstractScene<UserInterface<?>> {

  int step = 0;
  protected AbstractScene<?> scene;

  public MainScene(Camera pCamera) {
    super(pCamera, SceneType.MAIN);
  }

  @Override
  protected void onManagedUpdate(float pSecondsElapsed) {
    super.onManagedUpdate(pSecondsElapsed);
    step++;
    if (step > 599 && scene != null && scene.getSceneName() != SceneType.PLAY) {
      changeScene(SceneType.PLAY,true);
    }
  }

  @Override
  public void populate() {
    changeScene(SceneType.EDITOR,true);
  }



  public void changeScene(SceneType sceneType, boolean populate) {
    this.clearChildScene();
    if (this.scene != null) {
      this.scene.detach();
    }
    switch (sceneType) {
      case MENU:
        break;
      case PLAY:
        this.scene = new GameScene(this.mCamera);
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
    saveStringToPreferences("SCENE",scene.getSceneName().name());
    if(this.scene!=null){
      this.scene.onPause();
    }
  }

  @Override
  public void onResume() {
 //String sceneName="";
 String sceneName=loadStringFromPreferences("SCENE");
    if(!sceneName.isEmpty()){
      changeScene(SceneType.PLAY,false);
      this.scene.onResume();
    }
  }

  @Override
  public void detach() {
    if(this.scene!=null){
      this.scene.detach();
    }
  }

  public void onBackgroundImageLoaded() {}
}
