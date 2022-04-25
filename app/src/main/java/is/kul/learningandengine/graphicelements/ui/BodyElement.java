package is.kul.learningandengine.graphicelements.ui;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;
import is.kul.learningandengine.scene.UISignal.SignalName;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class BodyElement extends TextedButton{

	private final Button add,option;
public AtomicInteger layerId = new AtomicInteger();



int bodyID;
Button decale;
	public BodyElement(int ID) {
		super("Body"+ID,ResourceManager.getInstance().bodyButtonTextureRegion,Button.ButtonType.Selector, SignalName.BodyElementMain);
        bodyID = ID;
		VertexBufferObjectManager vbom = ResourceManager.getInstance().vbom;
	float hh = getHeight()/2;
        this.add = new Button(getWidth()+ResourceManager.getInstance().addTextureRegion.getWidth(),hh,
				ResourceManager.getInstance().addTextureRegion,vbom,Button.ButtonType.OneClick, SignalName.BodyElementAddLayerButton);

        this.decale = new Button(getWidth()+ResourceManager.getInstance().addTextureRegion.getWidth()+ResourceManager.getInstance().decaleTextureRegion.getWidth(),hh,
					ResourceManager.getInstance().decaleTextureRegion,vbom,Button.ButtonType.Selector, SignalName.BodyElementDecaleButton);

		this.option = new Button(getWidth()+ResourceManager.getInstance().addTextureRegion.getWidth()
				+ResourceManager.getInstance().decaleTextureRegion.getWidth()+ResourceManager.getInstance().smallOptionsTextureRegion.getWidth(),hh,
				ResourceManager.getInstance().smallOptionsTextureRegion,vbom, ButtonType.OneClick, SignalName.BodyOptionButton);


		attachChild(this.add);
        attachChild(this.decale);
		attachChild(this.option);

	}


	@Override
	public void diffuse(UISignal signal) {
	
		if(getParent()instanceof UIHandler){
		UIHandler ui = (UIHandler) getParent();
		signal.source = this;
		ui.diffuse(signal);
			
		}
	}


	@Override
	public boolean OnTouchScene(TouchEvent pSceneTouchEvent) {
	boolean touched = false;
	if(isVisible())
	if(super.OnTouchScene(pSceneTouchEvent))touched=true;
	if(this.add.OnTouchScene(pSceneTouchEvent))touched=true;
	if(this.decale.OnTouchScene(pSceneTouchEvent))touched=true;
	if(this.option.OnTouchScene(pSceneTouchEvent))touched=true;
	return touched;
	}


	public Button getDecale() {
		return decale;
	}
}
