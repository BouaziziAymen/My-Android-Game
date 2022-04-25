package is.kul.learningandengine.graphicelements.ui;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;
import is.kul.learningandengine.scene.UISignal.SignalName;

import java.util.ArrayList;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class LayerOptionElement extends TextedButton{
	public LayerOptionElement(String title) {
		super(title,ResourceManager.getInstance().bodyButtonTextureRegion,Button.ButtonType.Selector, SignalName.LayerOptionButtonMain);
		 // TODO Auto-generated constructor stub
	}


	@Override
	public void diffuse(UISignal signal) {
		if(getParent()instanceof UIHandler){
			signal.source = this;
			UIHandler ui = (UIHandler) getParent();
			ui.diffuse(signal);	
				
			}

		
	}




}
