package com.evolgames.scenes;

import static com.evolgames.physics.CollisionConstants.OBJECTS_MIDDLE_CATEGORY;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.GameGroup;
import com.evolgames.entities.GroupType;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.factories.BlockFactory;
import com.evolgames.entities.factories.GameEntityFactory;
import com.evolgames.entities.factories.MaterialFactory;
import com.evolgames.entities.factories.PropertiesFactory;
import com.evolgames.entities.factories.VerticesFactory;
import com.evolgames.entities.init.BodyInit;
import com.evolgames.entities.init.BodyInitImpl;
import com.evolgames.entities.init.TransformInit;
import com.evolgames.entities.particles.wrappers.FluxParticleWrapperWithPolygonEmitter;
import com.evolgames.entities.properties.LayerProperties;
import com.evolgames.entities.usage.Stabber;
import com.evolgames.scenes.entities.SceneType;
import com.evolgames.userinterface.view.EditorUserInterface;
import com.evolgames.userinterface.view.UserInterface;
import com.evolgames.userinterface.view.shapes.ImageShape;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    SmoothCamera cam = (SmoothCamera) this.mCamera;
    cam.setZoomFactor(1f);
    cam.setCenter(400,240);
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
    if (!sceneName.isEmpty()&&!sceneName.equals("MENU")) {
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

  public void onBackgroundImageLoaded() {
    ((EditorScene)scene).addImage();
  }

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
