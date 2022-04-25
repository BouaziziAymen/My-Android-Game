package is.kul.learningandengine.graphicelements.ui;

import org.andengine.input.touch.TouchEvent;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.scene.UISignal;

public class OnoffWindowMainElement extends WindowMainElement {

	private final Button onoff;
public Button getButton(){
	return onoff;
}
	public OnoffWindowMainElement(String name) {
		super(name);
        this.onoff = new Button(text.getWidth()/2+ResourceManager.getInstance().onoffTextureRegion.getWidth()/2+5, 0, ResourceManager.getInstance().onoffTextureRegion, ResourceManager.getInstance().vbom, Button.ButtonType.Selector, UISignal.SignalName.ONOFF);
        attachChild(this.onoff);
		
	}
public boolean isOn(){
	return this.onoff.isPressed();
			
}
	
	@Override
	public boolean OnTouchScene(TouchEvent pSceneTouchEvent) {
	if(this.isVisible())
		return this.onoff.OnTouchScene(pSceneTouchEvent);
	else return false;
	}
	public void click() {
        onoff.click();
		
	}
}
