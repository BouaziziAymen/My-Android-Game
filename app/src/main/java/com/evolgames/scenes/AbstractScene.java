package com.evolgames.scenes;

import android.content.Context;
import android.content.SharedPreferences;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.scenes.entities.SceneType;
import com.evolgames.userinterface.view.UserInterface;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.scene.Scene;
import org.andengine.util.debug.Debug;

public abstract class AbstractScene<T extends UserInterface<?>> extends Scene {
  protected final Camera mCamera;

  private final HUD hud;
  private final SceneType sceneName;
  protected T userInterface;

  public AbstractScene(Camera pCamera, SceneType sceneName) {
    this.mCamera = pCamera;
    this.hud = new HUD();
    this.mCamera.setHUD(hud);
    this.sceneName = sceneName;
  }

  public SceneType getSceneName() {
    return sceneName;
  }

  public abstract void populate();

  public abstract void detach();

  public void onBackKeyPressed() {
    Debug.d("Back key pressed");
  }

  public abstract void onPause();

  public abstract void onResume();

  public Camera getCamera() {
    return mCamera;
  }

  public HUD getHud() {
    return hud;
  }

  public T getUserInterface() {
    return userInterface;
  }

  @Override
  protected void onManagedUpdate(float pSecondsElapsed) {
    super.onManagedUpdate(pSecondsElapsed);
    if (userInterface != null) {
      userInterface.step();
    }
  }

  protected void saveStringToPreferences(String key, String value) {
    SharedPreferences preferences =
        ResourceManager.getInstance()
            .activity
            .getSharedPreferences("RAG_DOLL_MUT", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = preferences.edit();
    editor.putString(key, value);
    editor.apply();
  }

  protected String loadStringFromPreferences(String key) {
    SharedPreferences preferences =
        ResourceManager.getInstance()
            .activity
            .getSharedPreferences("RAG_DOLL_MUT", Context.MODE_PRIVATE);
    return preferences.getString(key, "");
  }
}
