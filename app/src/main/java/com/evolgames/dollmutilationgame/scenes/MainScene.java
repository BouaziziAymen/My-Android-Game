package com.evolgames.dollmutilationgame.scenes;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.scenes.entities.SceneType;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;

public class MainScene extends AbstractScene {

    protected AbstractScene scene;

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
        ResourceManager.getInstance().activity.saveStringToPreferences("SCENE", scene.getSceneName().name());
        if (this.scene != null) {
            this.scene.onPause();
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
        this.scene.onResume();
    }

    @Override
    public void createUserInterface() {
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

    public void goToScene(SceneType sceneType) {
        changeScene(sceneType, true);
        ResourceManager.getInstance().activity.saveStringToPreferences("SCENE", "");
        scene.createUserInterface();
    }
}
