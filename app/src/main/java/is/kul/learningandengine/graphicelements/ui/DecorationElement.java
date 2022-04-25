package is.kul.learningandengine.graphicelements.ui;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;
import is.kul.learningandengine.scene.UISignal.SignalName;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;

public class DecorationElement extends TextedButton {


int decorationID,bodyID,layerID;
Button remove,option;
	public DecorationElement(int bodyID, int layerID, int decorationID, String name) {
		super(name,ResourceManager.getInstance().decorButtonTextureRegion,Button.ButtonType.Selector, SignalName.DecorationButtonMain);
		this.decorationID = decorationID;
		this.bodyID = bodyID;
		this.layerID = layerID;
		float halfHeight = getHeight()/2f;

		this.option = new Button(getWidth()+20,halfHeight,
				ResourceManager.getInstance().smallOptionsTextureRegion,ResourceManager.getInstance().vbom,Button.ButtonType.OneClick, UISignal.SignalName.DecorationButtonOptions);


		remove = new Button(-15,halfHeight,
				ResourceManager.getInstance().removeTextureRegion,ResourceManager.getInstance().vbom,Button.ButtonType.OneClick, UISignal.SignalName.DecorationButtonRemove);
this.attachChild(remove);
		this.attachChild(option);
	}

	@Override
	public void diffuse(UISignal signal) {
		if(getParent()instanceof UIHandler){
			signal.source = this;
			UIHandler ui = (UIHandler) getParent();
			if(signal.signalName== SignalName.DecorationButtonRemove)signal.confirmed = false;
			ui.diffuse(signal);	
				
			}
	}
	@Override
	public boolean OnTouchScene(TouchEvent pSceneTouchEvent) {

		boolean touched = false;
		if (isVisible()) {
			if (super.OnTouchScene(pSceneTouchEvent)) touched = true;
			if (this.remove.OnTouchScene(pSceneTouchEvent)) touched = true;
			if (this.option.OnTouchScene(pSceneTouchEvent)) touched = true;

		}
		return touched;
	}

}
