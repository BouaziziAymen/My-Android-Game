package com.evolgames.scenes;

import static com.evolgames.physics.CollisionConstants.OBJECTS_BACK_CATEGORY;
import static com.evolgames.physics.CollisionConstants.OBJECTS_FRONT_CATEGORY;
import static com.evolgames.physics.CollisionConstants.OBJECTS_MIDDLE_CATEGORY;

import android.content.Context;
import android.content.SharedPreferences;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.GameGroup;
import com.evolgames.entities.GroupType;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.factories.GameEntityFactory;
import com.evolgames.entities.factories.ItemCategoryFactory;
import com.evolgames.entities.init.BodyInit;
import com.evolgames.entities.init.BodyInitImpl;
import com.evolgames.entities.init.BulletInit;
import com.evolgames.entities.init.TransformInit;
import com.evolgames.entities.particles.persistence.PersistenceCaretaker;
import com.evolgames.entities.particles.persistence.PersistenceException;
import com.evolgames.entities.particles.wrappers.explosion.ExplosiveParticleWrapper;
import com.evolgames.entities.properties.ProjectileProperties;
import com.evolgames.entities.usage.Shooter;
import com.evolgames.entities.usage.Slasher;
import com.evolgames.entities.usage.Smasher;
import com.evolgames.entities.usage.Stabber;
import com.evolgames.entities.usage.Throw;
import com.evolgames.entities.usage.TimeBomb;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.helpers.utilities.BlockUtils;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.helpers.utilities.ToolUtils;
import com.evolgames.physics.PhysicsConstants;
import com.evolgames.scenes.entities.SceneType;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.BodyUsageCategory;
import com.evolgames.userinterface.model.LayerModel;
import com.evolgames.userinterface.model.ToolModel;
import com.evolgames.userinterface.model.jointmodels.JointModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.view.UserInterface;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
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

    public ToolModel loadToolModel(String file) {
      ToolModel toolModel = null;
      try {
        toolModel = PersistenceCaretaker.getInstance().loadToolModel(file);
        toolModel.setToolCategory(ItemCategoryFactory.getInstance().getItemCategoryByIndex(2));
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
