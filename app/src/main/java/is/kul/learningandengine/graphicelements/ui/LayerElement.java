package is.kul.learningandengine.graphicelements.ui;

import is.kul.learningandengine.ResourceManager;

import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;
import is.kul.learningandengine.scene.UISignal.Event;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.andengine.entity.Entity;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;

public class LayerElement extends TextedButton {

    String name;
	public int layerID,bodyID;
	private final Button option;

    private Button decale;
    private final Button add;
    private final Button remove;
    private Button up;
    private Button down;


	
	public LayerElement( int bodyID, int layerID) {
		super("Layer"+layerID,ResourceManager.getInstance().layerButtonTextureRegion,Button.ButtonType.Selector, UISignal.SignalName.LayerButtonMain);
		this.layerID = layerID;
		this.bodyID = bodyID;

        name = "Layer"+layerID;
float halfHeight = getHeight()/2f;
VertexBufferObjectManager vbom = ResourceManager.getInstance().vbom;



        this.setDecale(new Button(getWidth()+ResourceManager.getInstance().decaleTextureRegion.getWidth()/2,halfHeight,
				ResourceManager.getInstance().decaleTextureRegion,vbom,Button.ButtonType.Selector, UISignal.SignalName.LayerButtonDecale));


        this.setUp(new Button(getWidth()-ResourceManager.getInstance().upButtonTextureRegion.getWidth()/2-1,halfHeight,
				ResourceManager.getInstance().upButtonTextureRegion,vbom,Button.ButtonType.OneClick, UISignal.SignalName.LayerButtonUpArrow));

        this.setDown(new Button(ResourceManager.getInstance().upButtonTextureRegion.getWidth()/2+1,halfHeight,
				ResourceManager.getInstance().downButtonTextureRegion,vbom,Button.ButtonType.OneClick, UISignal.SignalName.LayerButtonDownArrow));


        this.remove = new Button(getWidth()+ResourceManager.getInstance().decaleTextureRegion.getWidth()+ResourceManager.getInstance().removeTextureRegion.getWidth()/2,halfHeight,
				ResourceManager.getInstance().removeTextureRegion,vbom,Button.ButtonType.OneClick, UISignal.SignalName.LayerButtonRemove);


        this.add = new Button(-ResourceManager.getInstance().addTextureRegion.getWidth()/2,halfHeight,
				ResourceManager.getInstance().addTextureRegion,vbom,Button.ButtonType.OneClick, UISignal.SignalName.LayerButtonAddDecoration);


		this.option = new Button(-ResourceManager.getInstance().smallOptionsTextureRegion.getWidth()/2-ResourceManager.getInstance().addTextureRegion.getWidth(),halfHeight,
				ResourceManager.getInstance().smallOptionsTextureRegion,vbom,Button.ButtonType.OneClick, UISignal.SignalName.LayerButtonOptions);

		this.up.setZIndex(-1);
        this.down.setZIndex(-1);

        this.up.setScale(0.7f);
        this.down.setScale(0.7f);

        attachChild(this.add);
        attachChild(this.getDecale());
        attachChild(this.getUp());
        attachChild(this.getDown());
        attachChild(this.remove);
        attachChild(this.option);

        sortChildren();
        setActiveZone(0.6f);
		 // TODO Auto-generated constructor stub
	}

	
	public AtomicInteger decorationId = new AtomicInteger();

	

	@Override
	public void diffuse(UISignal signal) {
	
			if(getParent()instanceof UIHandler){
			signal.source = this;
			UIHandler ui = (UIHandler) getParent();
			if(signal.signalName== UISignal.SignalName.LayerButtonRemove)signal.confirmed = false;
			ui.diffuse(signal);	
				
			}
			}

	@Override
	public boolean OnTouchScene(TouchEvent pSceneTouchEvent) {
		
	boolean touched = false;
	if(isVisible()){
			if(super.OnTouchScene(pSceneTouchEvent))touched=true;
			if(this.getDown().OnTouchScene(pSceneTouchEvent))touched=true;
				if(this.getUp().OnTouchScene(pSceneTouchEvent))touched=true;
					if(this.remove.OnTouchScene(pSceneTouchEvent))touched=true;
					if(this.option.OnTouchScene(pSceneTouchEvent))touched=true;
					if(this.getDecale().OnTouchScene(pSceneTouchEvent))touched=true;
					if(this.add.OnTouchScene(pSceneTouchEvent))touched=true;
	}
		return touched;
	}

	public Button getUp() {
		return this.up;
	}

	public void setUp(Button up) {
		this.up = up;
	}

	public Button getDown() {
		return this.down;
	}

	public void setDown(Button down) {
		this.down = down;
	}

	public Button getDecale() {
		return this.decale;
	}

	public void setDecale(Button decale) {
		this.decale = decale;
	}


}
