package com.evolgames.scenes;

import com.evolgames.activity.ResourceManager;
import com.evolgames.scenes.entities.SceneType;
import com.evolgames.userinterface.view.UserInterface;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

public class MainScene extends AbstractScene<UserInterface<?>> {

    protected AbstractScene<?> scene;

    public MainScene(Camera pCamera) {
        super(pCamera, SceneType.MAIN);
    }

    @Override
    public void populate() {
    }

    public void changeScene(SceneType sceneType, boolean populate) {
        if (sceneType == SceneType.MAIN) {
            throw new UnsupportedOperationException("Cannot load main scene here");
        }
        SmoothCamera cam = (SmoothCamera) this.mCamera;
        cam.setZoomFactorDirect(1f);
        cam.setCenterDirect(400, 240);
        cam.setChaseEntity(null);
        this.clearChildScene();
        if (this.scene != null) {
            this.scene.detach();
        }

        switch (sceneType) {
            case MENU:
                ResourceManager.getInstance().activity.installMenuUi();
                this.scene = new MenuScene(this.mCamera);
                break;
            case PLAY:
                ResourceManager.getInstance().activity.installGameUi();
                this.scene = new PlayScene(this.mCamera);
                break;
            case EDITOR:
                ResourceManager.getInstance().activity.installEditorUi();
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
        String sceneName = loadStringFromPreferences("SCENE");
        if (!sceneName.isEmpty() && !sceneName.equals("MENU")) {
            changeScene(SceneType.valueOf(sceneName), false);
        } else {
            changeScene(SceneType.MENU, true);
        }
        this.scene.onResume();
    }

    @Override
    public void createUserInterface() {
    }

    @Override
    protected void processTouchEvent(TouchEvent touchEvent, TouchEvent hudTouchEvent) {
    }

    @Override
    public void detach() {
        if (this.scene != null) {
            this.scene.detach();
        }
    }

    public void onBackgroundImageLoaded() {
        ((EditorScene) scene).addImage();
    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        return false;
    }

    public void goToScene(SceneType sceneType) {
        changeScene(sceneType, true);
        this.saveStringToPreferences("SCENE", "");
        scene.createUserInterface();
    }


}
