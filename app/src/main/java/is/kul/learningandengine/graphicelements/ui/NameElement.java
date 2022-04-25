package is.kul.learningandengine.graphicelements.ui;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;

public class NameElement extends Entity implements LayoutElement,UIHandler {

	AlphaNumericInput alphanumericInput;




	private final Button rename;


	public NameElement(String title) {
		attachChild(alphanumericInput=new AlphaNumericInput(title,0,0,10,new Sprite(0,0,ResourceManager.getInstance().bodyButtonTextureRegion,ResourceManager.getInstance().vbom)));

		attachChild(this.rename = new Button(this.alphanumericInput.body.getWidth()/2+ResourceManager.getInstance().renameButtonTextureRegion.getWidth()/2+10, getHeight()/2,
				ResourceManager.getInstance().renameButtonTextureRegion,ResourceManager.getInstance().vbom,Button.ButtonType.Selector, UISignal.SignalName.Rename));
		alphanumericInput.body = null;

    }


	@Override
	public boolean OnTouchScene(TouchEvent touch) {
		boolean touched = false;
		if(isActive()){
		    if(this.rename.OnTouchScene(touch))touched=true;
			if(this.alphanumericInput.OnTouchScene(touch))touched = true;
		}
		return touched;
	}

	@Override
	public void diffuse(UISignal signal) {


		((UIHandler) getParent()).diffuse(signal);
if(signal.signalName== UISignal.SignalName.Rename){this.alphanumericInput.diffuse(signal);}
	}
	boolean active = true;
	@Override
	public void setActive(boolean b) {
		this.active = b;

	}
	@Override
	public boolean isActive() {

		return this.active;
	}

}
