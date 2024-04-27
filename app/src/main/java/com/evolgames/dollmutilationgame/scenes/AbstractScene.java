package com.evolgames.dollmutilationgame.scenes;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.entities.persistence.PersistenceCaretaker;
import com.evolgames.dollmutilationgame.entities.persistence.PersistenceException;
import com.evolgames.dollmutilationgame.scenes.entities.SceneType;
import com.evolgames.dollmutilationgame.userinterface.model.ToolModel;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.scene.Scene;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

public abstract class AbstractScene extends Scene {
    protected final Camera mCamera;
    private final Vector2 cameraPositionBeforeChase = new Vector2();

    private final HUD hud;
    private final SceneType sceneName;
    private GameEntity chasedEntity;
    private boolean returnToStart;

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
    }

    public SceneType getSceneName() {
        return sceneName;
    }

    public abstract void populate();

    public abstract void detach();


    public ToolModel loadToolModel(String file, boolean editor, boolean assets) {
        ToolModel toolModel;
        if (file != null && !file.isEmpty()) {
            try {
                toolModel = PersistenceCaretaker.getInstance().loadToolModel(file, editor, assets);
                return toolModel;
            } catch (IOException | ParserConfigurationException | SAXException |
                     PersistenceException e) {
                return new ToolModel(this);
            }
        }
        return null;
    }

    public abstract void onPause();

    public abstract void onResume();

    public Camera getCamera() {
        return mCamera;
    }

    public HUD getHud() {
        return hud;
    }

    public abstract void createUserInterface();

    public GameEntity getChasedEntity() {
        return chasedEntity;
    }

    public void resetChasedEntity() {
        this.chasedEntity = null;
        ResourceManager.getInstance().firstCamera.setChaseEntity(null);
    }

    public void chaseEntity(GameEntity entity, boolean returnToStart) {
        this.returnToStart = returnToStart;
        this.cameraPositionBeforeChase.set(mCamera.getCenterX(), mCamera.getCenterY());
        if (entity == null) {
            return;
        }
        this.chasedEntity = entity;
        ResourceManager.getInstance().firstCamera.setChaseEntity(entity.getMesh());
    }

    public void releaseChasedEntity() {
        resetChasedEntity();
        if (cameraPositionBeforeChase != null && returnToStart) {
            ((SmoothCamera) ResourceManager.getInstance().firstCamera).setCenterDirect(cameraPositionBeforeChase.x, cameraPositionBeforeChase.y);
        }
    }
}
