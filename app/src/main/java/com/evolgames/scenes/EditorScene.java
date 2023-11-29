package com.evolgames.scenes;

import android.util.Log;

import com.evolgames.entities.Plotter;
import com.evolgames.entities.particles.persistence.PersistenceCaretaker;
import com.evolgames.scenes.entities.SceneType;
import com.evolgames.userinterface.control.CreationZoneController;
import com.evolgames.userinterface.control.KeyboardController;
import com.evolgames.userinterface.control.OutlineController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.BodySettingsWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.BombOptionController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.CasingOptionController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.DecorationSettingsWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ItemSaveWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ItemWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.JointSettingsWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.JointWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.LayerSettingsWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.LayerWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.OptionsWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ProjectileOptionController;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.ToolModel;
import com.evolgames.userinterface.view.EditorUserInterface;

import javax.xml.parsers.ParserConfigurationException;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.entity.Entity;
import org.andengine.entity.scene.background.EntityBackground;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.SurfaceScrollDetector;

public class EditorScene extends AbstractScene<EditorUserInterface>
    implements IAccelerationListener,
        ScrollDetector.IScrollDetectorListener,
        PinchZoomDetector.IPinchZoomDetectorListener {

  public static final String EDITOR_FILE = "editor_file";
  public static final String SAVE_MUT = "editor_auto_save.mut";
  public static int step;
  public static Plotter plotter;
  private final SurfaceScrollDetector mScrollDetector;
  private final PinchZoomDetector mPinchZoomDetector;
  private boolean scroll = false;
  private float mPinchZoomStartedCameraZoomFactor;
  private BodyModel groundModel;

  public EditorScene(Camera camera) {
    super(camera, SceneType.EDITOR);

    this.mScrollDetector = new SurfaceScrollDetector(this);

    this.mPinchZoomDetector = new PinchZoomDetector(this);
  }

  public BodyModel getGroundModel() {
    return groundModel;
  }

  @Override
  public void populate() {


  }

  private void init() {
    this.groundModel = new BodyModel(-1);
    this.groundModel.setModelName("Ground");

    Entity background = new Entity();
    this.setBackground(new EntityBackground(0, 0, 0, background));

    plotter  = new Plotter();
    plotter.setZIndex(200);
    this.attachChild(plotter);

    sortChildren();
  }

  @Override
  public void detach() {
    this.userInterface.detachSelf();
  }
  @Override
  public void createUserInterface() {
    String editorFile = this.loadStringFromPreferences(EDITOR_FILE);
    Log.e("XFile",editorFile);
    ToolModel toolModel = loadToolModel(editorFile);
    if(toolModel==null){
      toolModel = new ToolModel(this,4);
    }
    init();

    KeyboardController keyboardController = new KeyboardController();
    OutlineController outlineController = new OutlineController();
    LayerWindowController layerWindowController = new LayerWindowController();
    JointSettingsWindowController jointSettingsWindowController =
        new JointSettingsWindowController(this);
    JointWindowController jointWindowController = new JointWindowController();
    ProjectileOptionController projectileOptionController = new ProjectileOptionController(this);
    CasingOptionController ammoOptionController = new CasingOptionController();
    BombOptionController bombOptionController = new BombOptionController();
    ItemWindowController itemWindowController = new ItemWindowController();
    LayerSettingsWindowController layerSettingsWindowController =
        new LayerSettingsWindowController();
    BodySettingsWindowController bodySettingsWindowController = new BodySettingsWindowController();
    ItemSaveWindowController itemSaveWindowController = new ItemSaveWindowController();
    DecorationSettingsWindowController decorationSettingsWindowController =
        new DecorationSettingsWindowController();
    OptionsWindowController optionsWindowController = new OptionsWindowController();
    CreationZoneController creationZoneController = new CreationZoneController(this);

    userInterface =
        new EditorUserInterface(
            this,
            layerWindowController,
            jointWindowController,
            layerSettingsWindowController,
            bodySettingsWindowController,
            jointSettingsWindowController,
            itemWindowController,
            projectileOptionController,
            ammoOptionController,
            bombOptionController,
            itemSaveWindowController,
            decorationSettingsWindowController,
            optionsWindowController,
            outlineController,
            creationZoneController,
            keyboardController);

    // Dependency Injection
    jointSettingsWindowController.setToolModel(toolModel);
    jointSettingsWindowController.setKeyboardController(keyboardController);
    jointSettingsWindowController.setOutlineController(outlineController);
    layerWindowController.setOutlineController(outlineController);
    jointSettingsWindowController.setJointWindowController(jointWindowController);
    jointWindowController.setOutlineController(outlineController);
    jointWindowController.setJointSettingsWindowController(jointSettingsWindowController);
    bombOptionController.setKeyboardController(keyboardController);
    ammoOptionController.setKeyboardController(keyboardController);
    itemWindowController.setKeyboardController(keyboardController);
    projectileOptionController.setKeyboardController(keyboardController);
    itemWindowController.setAmmoOptionController(ammoOptionController);
    itemWindowController.setOutlineController(outlineController);
    itemWindowController.setBombOptionController(bombOptionController);
    itemWindowController.setProjectileOptionController(projectileOptionController);
    projectileOptionController.setItemWindowController(itemWindowController);
    ammoOptionController.setItemWindowController(itemWindowController);
    bombOptionController.setItemWindowController(itemWindowController);
    layerSettingsWindowController.setLayerWindowController(layerWindowController);
    layerSettingsWindowController.setKeyboardController(keyboardController);
    bodySettingsWindowController.setLayerWindowController(layerWindowController);
    bodySettingsWindowController.setKeyboardController(keyboardController);
    optionsWindowController.setItemSaveController(itemSaveWindowController);
    creationZoneController.setLayerWindowController(layerWindowController);
    creationZoneController.setJointWindowController(jointWindowController);
    creationZoneController.setItemWindowController(itemWindowController);
    optionsWindowController.setCreationZoneController(creationZoneController);
    optionsWindowController.setUserInterface(userInterface);
    itemSaveWindowController.setUserInterface(userInterface);
    jointWindowController.setUserInterface(userInterface);
    projectileOptionController.setUserInterface(userInterface);
    layerSettingsWindowController.setUserInterface(userInterface);
    bodySettingsWindowController.setUserInterface(userInterface);
    decorationSettingsWindowController.setUserInterface(userInterface);
    outlineController.setUserInterface(userInterface);
    itemWindowController.setUserInterface(userInterface);
    layerWindowController.setUserInterface(userInterface);
    layerSettingsWindowController.setUserInterface(userInterface);
    bodySettingsWindowController.setUserInterface(userInterface);
    creationZoneController.setUserInterface(userInterface);
    itemWindowController.setUserInterface(userInterface);
    userInterface.bindToolModel(toolModel);
  }

  @Override
  public void onPause() {
    this.saveStringToPreferences(EDITOR_FILE, SAVE_MUT);
    this.getUserInterface().saveToolModel("editor_auto_save.mut");
  }

  @Override
  public void onResume() {
    createUserInterface();
  }

  @Override
  protected void onManagedUpdate(float pSecondsElapsed) {
    super.onManagedUpdate(pSecondsElapsed);
    step++;
  }

  @Override
  protected void processTouchEvent(TouchEvent touchEvent, TouchEvent hudTouchEvent) {
    boolean hudTouched = userInterface.onTouchHud(hudTouchEvent, scroll);
    if (!hudTouched) {
      userInterface.onTouchScene(touchEvent, scroll);
    }

    if (mPinchZoomDetector != null) {
      mPinchZoomDetector.onTouchEvent(touchEvent);
      if (mPinchZoomDetector.isZooming()) {
        mScrollDetector.setEnabled(false);
      } else {
        if (!hudTouched) mScrollDetector.onTouchEvent(touchEvent);
      }
    } else {
      if (!hudTouched) {
        mScrollDetector.onTouchEvent(touchEvent);
      }
    }
  }

  @Override
  public void onAccelerationAccuracyChanged(AccelerationData pAccelerationData) {}

  @Override
  public void onAccelerationChanged(AccelerationData pAccelerationData) {}

  @Override
  public void onPinchZoomStarted(
      PinchZoomDetector pPinchZoomDetector, TouchEvent pSceneTouchEvent) {
    SmoothCamera cam = (SmoothCamera) this.mCamera;
    this.mPinchZoomStartedCameraZoomFactor = cam.getZoomFactor();
    userInterface.getCreationZoneController().setUpLocked(true);
  }

  @Override
  public void onPinchZoom(
      PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent, float pZoomFactor) {

    SmoothCamera cam = (SmoothCamera) this.mCamera;
    float zf = mPinchZoomStartedCameraZoomFactor * pZoomFactor;
    if (zf >= 1) {
      cam.setZoomFactor(zf);
      userInterface.updateZoom(zf);
    }
  }

  @Override
  public void onPinchZoomFinished(
      PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent, float pZoomFactor) {

    SmoothCamera cam = (SmoothCamera) this.mCamera;
    float zf = mPinchZoomStartedCameraZoomFactor * pZoomFactor;
    if (zf >= 1) {
      cam.setZoomFactor(zf);
      userInterface.updateZoom(zf);
    }
  }

  @Override
  public void onScrollStarted(
      ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
    scroll = true;
  }

  @Override
  public void onScroll(
      ScrollDetector pScrollDetector, int pPointerID, float pDistanceX, float pDistanceY) {

    SmoothCamera cam = (SmoothCamera) this.mCamera;

    float zoomFactor = cam.getZoomFactor();

    float deltaX = -pDistanceX / zoomFactor;

    float deltaY = pDistanceY / zoomFactor;

    mCamera.offsetCenter(deltaX, deltaY);
  }

  @Override
  public void onScrollFinished(
      ScrollDetector pScrollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
    scroll = false;
  }

  public void setScrollerEnabled(boolean pScrollerEnabled) {
    mScrollDetector.setEnabled(pScrollerEnabled);
  }

  public void setZoomEnabled(boolean pZoomEnabled) {
    mPinchZoomDetector.setEnabled(pZoomEnabled);
  }

  public EditorUserInterface getUserInterface() {
    return userInterface;
  }

  public void goToScene(SceneType sceneType){
    ((MainScene)this.mParentScene).goToScene(sceneType);
  }

}
