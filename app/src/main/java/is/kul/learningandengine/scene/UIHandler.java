package is.kul.learningandengine.scene;

import org.andengine.input.touch.TouchEvent;

public interface UIHandler {
	void diffuse(UISignal signal);
    boolean OnTouchScene(TouchEvent pSceneTouchEvent);

}
