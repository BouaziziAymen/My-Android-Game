package com.evolgames.scenes;

import android.content.Context;
import android.content.SharedPreferences;

import com.evolgames.entities.persistence.PersistenceCaretaker;
import com.evolgames.entities.persistence.PersistenceException;
import com.evolgames.activity.ResourceManager;
import com.evolgames.scenes.entities.SceneType;
import com.evolgames.userinterface.model.ToolModel;
import com.evolgames.userinterface.view.UserInterface;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.debug.Debug;
import org.xml.sax.SAXException;

public abstract class AbstractScene<T extends UserInterface<?>> extends Scene implements IOnSceneTouchListener{
  protected final Camera mCamera;

  private final HUD hud;
  private final SceneType sceneName;
  protected T userInterface;

  public AbstractScene(Camera pCamera, SceneType sceneName) {
    this.mCamera = pCamera;
    this.hud = new HUD();
    this.mCamera.setHUD(hud);
    this.sceneName = sceneName;
    try {
      PersistenceCaretaker.getInstance().create(this);
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    }
    setOnSceneTouchListener(this);
  }

  public SceneType getSceneName() {
    return sceneName;
  }

  public abstract void populate();

  public abstract void detach();

  public void onBackKeyPressed() {
    Debug.d("Back key pressed");
  }

    public ToolModel loadToolModel(String file, boolean editor, boolean assets) {
      ToolModel toolModel = null;
      try {
        toolModel = PersistenceCaretaker.getInstance().loadToolModel(file,editor,assets);
      } catch (IOException | ParserConfigurationException | SAXException | PersistenceException e) {
        e.printStackTrace();
      }
      return toolModel;
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

  public abstract void createUserInterface();

  @Override
  protected void onManagedUpdate(float pSecondsElapsed) {
    super.onManagedUpdate(pSecondsElapsed);
    if (userInterface != null) {
      userInterface.step();
    }
  }



  protected abstract void processTouchEvent(TouchEvent touchEvent, TouchEvent hudTouchEvent);

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
  @Override
  public boolean onSceneTouchEvent(Scene pScene, TouchEvent touchEvent) {
    float[] cameraSceneCoordinatesFromSceneCoordinates =
            mCamera.getCameraSceneCoordinatesFromSceneCoordinates(touchEvent.getX(), touchEvent.getY());
    TouchEvent hudTouchEvent =
            TouchEvent.obtain(
                    cameraSceneCoordinatesFromSceneCoordinates[0],
                    cameraSceneCoordinatesFromSceneCoordinates[1],
                    touchEvent.getAction(),
                    touchEvent.getPointerID(),
                    touchEvent.getMotionEvent());
    this.processTouchEvent(touchEvent, hudTouchEvent);
    return false;
  }


}
