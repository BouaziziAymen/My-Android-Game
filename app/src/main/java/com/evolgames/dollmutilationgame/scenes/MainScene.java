package com.evolgames.dollmutilationgame.scenes;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.scenes.entities.SceneType;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;

public class MainScene extends AbstractScene {

    private AbstractScene childScene;

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

        switch (sceneType) {
            case MENU:
                ResourceManager.getInstance().activity.installMenuUi();
                this.childScene = new MenuScene(this.mCamera);
                break;
            case PLAY:
                ResourceManager.getInstance().activity.installGameUi();
                this.childScene = new PlayScene(this.mCamera);
                break;
            case EDITOR:
                ResourceManager.getInstance().activity.installEditorUi();
                this.childScene = new EditorScene(this.mCamera);
                break;
        }

        if (this.childScene != null) {
            if (populate) {
                this.childScene.populate();
            }
            this.setChildScene(this.childScene, false, false, false);
        }
    }

    @Override
    public void onPause() {
        ResourceManager.getInstance().activity.saveStringToPreferences("SCENE", childScene.getSceneName().name());
        if (this.childScene != null) {
            this.childScene.onPause();
        }
    }

    @Override
    public void onResume() {
        String sceneName = ResourceManager.getInstance().activity.loadStringFromPreferences("SCENE");
        if (!sceneName.isEmpty() && !sceneName.equals("MENU")) {
            changeScene(SceneType.valueOf(sceneName), false);
        } else {
            changeScene(SceneType.MENU, true);
        }
        this.childScene.onResume();
    }

    @Override
    public void createUserInterface() {
    }

    @Override
    public void detach() {
        if (this.childScene != null) {
            this.childScene.detach();
        }
    }

    public void onBackgroundImageLoaded() {
        ((EditorScene) childScene).addImage();
    }

    public void goToScene(SceneType sceneType) {
        changeScene(sceneType, true);
        ResourceManager.getInstance().activity.saveStringToPreferences("SCENE", "");
        childScene.createUserInterface();
    }
}
