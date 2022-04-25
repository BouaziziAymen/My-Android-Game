package is.kul.learningandengine.graphicelements.ui;

import is.kul.learningandengine.ResourceManager;

import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;
import is.kul.learningandengine.scene.UISignal.Event;
import is.kul.learningandengine.scene.UISignal.SignalName;

import java.util.ArrayList;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;

public class JointElement extends TextedButton {


String name;

	private final Button remove;


	public int ID;
	private final Button option;


	
	public JointElement(int ID) {
		super("Joint"+ID,ResourceManager.getInstance().bodyButtonTextureRegion,Button.ButtonType.Selector, SignalName.JointButtonMain);
		this.ID = ID;

		VertexBufferObjectManager vbom = ResourceManager.getInstance().vbom;


        this.option = new Button(ResourceManager.getInstance().bodyButtonTextureRegion.getWidth()+ResourceManager.getInstance().smallOptionsTextureRegion.getWidth()/2+10, getHeight()/2,
				ResourceManager.getInstance().smallOptionsTextureRegion,vbom,Button.ButtonType.OneClick, SignalName.JointButtonOptions);


        this.remove = new Button(ResourceManager.getInstance().bodyButtonTextureRegion.getWidth()+ResourceManager.getInstance().smallOptionsTextureRegion.getWidth()+ResourceManager.getInstance().removeTextureRegion.getWidth()/2+20, getHeight()/2,
				ResourceManager.getInstance().removeTextureRegion,vbom,Button.ButtonType.OneClick, SignalName.JointButtonRemove);


        attachChild(this.remove);
        attachChild(this.option);
		
		 // TODO Auto-generated constructor stub
	}


	
	
	@Override
	public void diffuse(UISignal signal) {
			

		if(getParent()instanceof UIHandler){
		signal.source = this;
		UIHandler ui = (UIHandler) getParent();
		if(signal.signalName== UISignal.SignalName.JointButtonRemove)signal.confirmed = false;
		ui.diffuse(signal);	
			
		}
			}

	@Override
	public boolean OnTouchScene(TouchEvent pSceneTouchEvent) {
		
	boolean touched = false;
	if(isVisible()){
	
		if(super.OnTouchScene(pSceneTouchEvent))touched=true;	
		
					
					if(this.remove.OnTouchScene(pSceneTouchEvent))touched=true;
					if(this.option.OnTouchScene(pSceneTouchEvent))touched=true;
	}
		return touched;
	}


}
