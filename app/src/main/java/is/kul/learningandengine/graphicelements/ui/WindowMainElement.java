package is.kul.learningandengine.graphicelements.ui;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.graphicelements.ui.Button.ButtonType;
import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;
import org.andengine.entity.Entity;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class WindowMainElement extends Entity implements UIHandler,LayoutElement {

	protected Text text;

	public WindowMainElement( String name) {
        VertexBufferObjectManager vbom = ResourceManager.getInstance().vbom;
        this.text =	new Text(0, 0, ResourceManager.getInstance().font2, name, vbom);

        attachChild(this.text);
	
		
		
		 // TODO Auto-generated constructor stub
	}

	
	@Override
	public void diffuse(UISignal signal) {
	
		if(getParent()instanceof UIHandler){
			 ((UIHandler) getParent()).diffuse(signal);
		}
	}





	@Override
	public void setActive(boolean b) {
        this.isActive = b;
		
	}

boolean isActive;
	@Override
	public boolean isActive() {
		// TODO Auto-generated method stub
		return this.isActive;
	}


	@Override
	public boolean OnTouchScene(TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		return false;
	}


}
