package com.evolgames.scenes;

import android.content.Context;
import android.content.SharedPreferences;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.activity.ResourceManager;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.persistence.PersistenceCaretaker;
import com.evolgames.entities.persistence.PersistenceException;
import com.evolgames.scenes.entities.SceneType;
import com.evolgames.userinterface.model.ToolModel;
import com.evolgames.userinterface.view.UserInterface;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.debug.Debug;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

public abstract class AbstractScene<T extends UserInterface<?>> extends Scene implements IOnSceneTouchListener {
    protected final Camera mCamera;
    private Vector2 cameraPositionBeforeChase = new Vector2();

    private final HUD hud;
    private final SceneType sceneName;
    protected T userInterface;
    private GameEntity chasedEntity;

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
        ToolModel toolModel;
        if (file != null && !file.isEmpty()) {
            try {
                toolModel = PersistenceCaretaker.getInstance().loadToolModel(file, editor, assets);
                return toolModel;
            } catch (IOException | ParserConfigurationException | SAXException |
                     PersistenceException e) {
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

    public GameEntity getChasedEntity() {
        return chasedEntity;
    }
public void resetChasedEntity(){
    this.chasedEntity = null;
    ResourceManager.getInstance().firstCamera.setChaseEntity(null);
}
    public void chaseEntity(GameEntity entity) {
        this.cameraPositionBeforeChase.set(mCamera.getCenterX(),mCamera.getCenterY());
        if (entity == null) {
            return;
        }
        this.chasedEntity = entity;
        ResourceManager.getInstance().firstCamera.setChaseEntity(entity.getMesh());
    }

    public void releaseChasedEntity() {
     resetChasedEntity();
        if(cameraPositionBeforeChase!=null) {
            ((SmoothCamera)ResourceManager.getInstance().firstCamera).setCenterDirect(cameraPositionBeforeChase.x, cameraPositionBeforeChase.y);
        }
    }
}
