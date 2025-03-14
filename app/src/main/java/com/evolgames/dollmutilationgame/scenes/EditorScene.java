package com.evolgames.dollmutilationgame.scenes;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.control.CreationAction;
import com.evolgames.dollmutilationgame.userinterface.control.CreationZoneController;
import com.evolgames.dollmutilationgame.userinterface.control.KeyboardController;
import com.evolgames.dollmutilationgame.userinterface.control.OutlineController;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.BodySettingsWindowController;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.BombOptionController;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.CasingOptionController;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.DecorationSettingsWindowController;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.DragOptionController;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.FireSourceOptionController;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.ItemSaveWindowController;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.ItemWindowController;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.JointSettingsWindowController;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.JointWindowController;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.LayerSettingsWindowController;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.LayerWindowController;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.LiquidSourceOptionController;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.OptionsWindowController;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.ProjectileOptionController;
import com.evolgames.dollmutilationgame.entities.Plotter;
import com.evolgames.dollmutilationgame.helpers.ItemMetaData;
import com.evolgames.dollmutilationgame.helpers.XmlHelper;
import com.evolgames.dollmutilationgame.scenes.entities.SceneType;
import com.evolgames.dollmutilationgame.userinterface.model.ToolModel;
import com.evolgames.dollmutilationgame.userinterface.view.EditorUserInterface;
import com.evolgames.dollmutilationgame.userinterface.view.Screen;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.entity.Entity;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.EntityBackground;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.SurfaceScrollDetector;

public class EditorScene extends AbstractScene
        implements IAccelerationListener,
        ScrollDetector.IScrollDetectorListener,
        PinchZoomDetector.IPinchZoomDetectorListener, IOnSceneTouchListener {

    public static int step;
    public static Plotter plotter;
    private final SurfaceScrollDetector mScrollDetector;
    private final PinchZoomDetector mPinchZoomDetector;
    private float mPinchZoomStartedCameraZoomFactor;
    private boolean hudLocked;
    private EditorUserInterface userInterface;

    public EditorScene(Camera camera) {
        super(camera, SceneType.EDITOR);

        this.mScrollDetector = new SurfaceScrollDetector(this);

        this.mPinchZoomDetector = new PinchZoomDetector(this);
        this.setOnSceneTouchListener(this);
    }

    @Override
    public void populate() {


    }

    private void init() {

        Entity background = new Entity();
        this.setBackground(new EntityBackground(0.3f, 0.3f, 0.3f, background));
        //this.setBackground(new EntityBackground(0, 0, 0, background));
        plotter = new Plotter();
        plotter.setZIndex(200);
        this.attachChild(plotter);

        sortChildren();
    }

    @Override
    public void detach() {
        userInterface.detachSelf();
        ResourceManager.getInstance().disposeOfEditorResources();
        System.gc();
    }

    @Override
    public void createUserInterface() {
       ResourceManager.getInstance().loadUserInterfaceImages();

        ItemMetaData itemMetaData = ResourceManager.getInstance().getEditorItem();
        ToolModel toolModel;
        if (itemMetaData == null) {
            throw new IllegalStateException("Entered into editor without a name");
        }
        toolModel = loadToolModel(itemMetaData.getTemplateName() != null ? itemMetaData.getTemplateName() : itemMetaData.getFileName(), true, !itemMetaData.isUserCreated());

        if (toolModel == null) {
            toolModel = new ToolModel(this);
            toolModel.getProperties().setToolName(itemMetaData.getName());
            toolModel.setCategory(itemMetaData.getItemCategory());
        } else {
            toolModel.getProperties().setToolName(itemMetaData.getName());
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
        FireSourceOptionController fireSourceOptionController = new FireSourceOptionController();
        LiquidSourceOptionController liquidSourceOptionController = new LiquidSourceOptionController();
        DragOptionController dragOptionController = new DragOptionController();
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
                        fireSourceOptionController,
                        liquidSourceOptionController,
                        dragOptionController,
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
        itemWindowController.setFireSourceOptionController(fireSourceOptionController);
        itemWindowController.setLiquidSourceOptionController(liquidSourceOptionController);
        itemWindowController.setDragOptionController(dragOptionController);
        projectileOptionController.setItemWindowController(itemWindowController);
        ammoOptionController.setItemWindowController(itemWindowController);
        bombOptionController.setItemWindowController(itemWindowController);
        fireSourceOptionController.setItemWindowController(itemWindowController);
        liquidSourceOptionController.setItemWindowController(itemWindowController);
        dragOptionController.setItemWindowController(itemWindowController);
        layerSettingsWindowController.setLayerWindowController(layerWindowController);
        layerSettingsWindowController.setKeyboardController(keyboardController);
        bodySettingsWindowController.setLayerWindowController(layerWindowController);
        bodySettingsWindowController.setKeyboardController(keyboardController);
        bodySettingsWindowController.setOutlineController(outlineController);
        optionsWindowController.setItemSaveController(itemSaveWindowController);
        creationZoneController.setLayerWindowController(layerWindowController);
        creationZoneController.setJointWindowController(jointWindowController);
        creationZoneController.setItemWindowController(itemWindowController);
        optionsWindowController.setCreationZoneController(creationZoneController);
        optionsWindowController.setUserInterface(userInterface);
        optionsWindowController.setKeyboardController(keyboardController);
        itemSaveWindowController.setUserInterface(userInterface);
        itemSaveWindowController.setKeyboardController(keyboardController);
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
        ResourceManager.getInstance().disposeOfEditorResources();
        CreationAction creationAction =this.userInterface.getCreationZoneController().getAction();
        Screen screen = this.userInterface.getSelectedScreen()!=null?this.userInterface.getSelectedScreen():Screen.NONE;
        ResourceManager.getInstance().activity.saveStringToPreferences("editor_creation_action", creationAction.name());
        ResourceManager.getInstance().activity.saveStringToPreferences("editor_screen", screen.name());
        ResourceManager.getInstance().activity.saveStringToPreferences("saved_tool_filename", XmlHelper.convertToXmlFormat(this.userInterface.getToolModel().getProperties().getToolName()));
        userInterface.saveToolModel();
    }

    @Override
    public void onResume() {
        String actionString = ResourceManager.getInstance().activity.loadStringFromPreferences("editor_creation_action");
        String screenString = ResourceManager.getInstance().activity.loadStringFromPreferences("editor_screen");
        CreationAction creationAction = CreationAction.fromName(actionString);
        Screen screen = Screen.fromName(screenString);
        createUserInterface();
        userInterface.setBoardsState(screen, creationAction);
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        step++;
        this.userInterface.step();
    }

    protected void processTouchEvent(TouchEvent touchEvent, TouchEvent hudTouchEvent) {
        boolean hudTouched = false;
        if(touchEvent.getPointerID()==0) {
            if (!hudLocked) {
                hudTouched = userInterface.onTouchHud(hudTouchEvent);
            }
            if (!hudTouched) {
                userInterface.onTouchScene(touchEvent);
            }
        }
        if (mPinchZoomDetector != null) {
            mPinchZoomDetector.onTouchEvent(touchEvent);
            if (mPinchZoomDetector.isZooming()) {
                mScrollDetector.setEnabled(false);
            } else {
                if (!hudTouched) {
                    mScrollDetector.onTouchEvent(touchEvent);
                }
            }
        } else {
            if (!hudTouched) {
                mScrollDetector.onTouchEvent(touchEvent);
            }
        }
    }

    @Override
    public void onAccelerationAccuracyChanged(AccelerationData pAccelerationData) {
    }

    @Override
    public void onAccelerationChanged(AccelerationData pAccelerationData) {
    }

    @Override
    public void onPinchZoomStarted(
            PinchZoomDetector pPinchZoomDetector, TouchEvent pSceneTouchEvent) {
        SmoothCamera cam = (SmoothCamera) this.mCamera;
        this.mPinchZoomStartedCameraZoomFactor = cam.getZoomFactor();
        userInterface.lockInteraction();
    }

    @Override
    public void onPinchZoom(
            PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent, float pZoomFactor) {

        SmoothCamera cam = (SmoothCamera) this.mCamera;
        float zf = mPinchZoomStartedCameraZoomFactor * pZoomFactor;
        if (zf >= 0.1f) {
            cam.setZoomFactor(zf);
            userInterface.updateZoom(zf);
        }
    }

    @Override
    public void onPinchZoomFinished(
            PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent, float pZoomFactor) {

        SmoothCamera cam = (SmoothCamera) this.mCamera;
        float zf = mPinchZoomStartedCameraZoomFactor * pZoomFactor;
        if (zf >= 0.1f) {
            cam.setZoomFactor(zf);
            userInterface.updateZoom(zf);
        }
        userInterface.getCreationZoneController().resetScrollAndZoom();
        userInterface.unlockInteraction();
    }

    @Override
    public void onScrollStarted(
            ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
        userInterface.lockInteraction();
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
        userInterface.unlockInteraction();
    }

    public void setScrollerEnabled(boolean pScrollerEnabled) {
        mScrollDetector.setEnabled(pScrollerEnabled);
    }

    public void setZoomEnabled(boolean pZoomEnabled) {
        mPinchZoomDetector.setEnabled(pZoomEnabled);
    }


    public void addImage() {
        userInterface.addImage();
    }

    public void setHudLocked(boolean hudLocked) {
        this.hudLocked = hudLocked;
    }

    public EditorUserInterface getUserInterface() {
        return this.userInterface;
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
