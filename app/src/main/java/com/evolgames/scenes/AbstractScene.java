package com.evolgames.scenes;

import com.evolgames.gameengine.GameActivity;
import com.evolgames.gameengine.ResourceManager;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;


public abstract class AbstractScene extends Scene {
    protected ResourceManager res = ResourceManager.getInstance();
    protected Engine engine = this.res.engine;
    protected GameActivity activity = this.res.activity;
    protected VertexBufferObjectManager vbom = this.res.vbom;
    protected Camera mainCamera = this.res.firstCamera;
    public abstract void populate();
    public void destroy() {
    }
    public void onBackKeyPressed() {
        Debug.d("Back key pressed");
    }
    public abstract void onPause();
    public abstract void onResume();

    public Camera getMainCamera() {
        return mainCamera;
    }
}
