package is.kul.learningandengine.scene;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import is.kul.learningandengine.GameActivity;
import is.kul.learningandengine.ResourceManager;

public abstract class AbstractScene extends Scene {
protected ResourceManager res = ResourceManager.getInstance();
protected Engine engine = this.res.engine;
protected GameActivity activity = this.res.activity;
protected VertexBufferObjectManager vbom = this.res.vbom;
protected Camera camera = this.res.camera;
public abstract void populate();
public void destroy() {
}
public void onBackKeyPressed() {
Debug.d("Back key pressed");
}
public abstract void onPause();
public abstract void onResume();
}
