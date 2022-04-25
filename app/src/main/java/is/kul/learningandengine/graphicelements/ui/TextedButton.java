package is.kul.learningandengine.graphicelements.ui;

import is.kul.learningandengine.ResourceManager;

import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

public class TextedButton extends Button implements LayoutElement {
	Rectangle activeZone;
	public TextedButton(String name,
			ITiledTextureRegion pTiledTextureRegion,
			Button.ButtonType type, UISignal.SignalName bName) {
		super(0,0,pTiledTextureRegion,ResourceManager.getInstance().vbom,type,bName);
		float w = getWidth();
		float h = getHeight();
        this.text = new Text(w/2,h/2,ResourceManager.getInstance().font2, name, 100, ResourceManager.getInstance().vbom);

        attachChild(this.text);
	}



	public TextedButton(String name,
						ITiledTextureRegion pTiledTextureRegion,
						Button.ButtonType type, UISignal.SignalName bName,boolean flag) {
		super(0,0,pTiledTextureRegion,ResourceManager.getInstance().vbom,type,bName);
		float w = getWidth();
		float h = getHeight();
		this.text = new Text(0,0,ResourceManager.getInstance().font2, name, 100, ResourceManager.getInstance().vbom);
text.setPosition(-text.getWidth()/2,h/2);
		attachChild(this.text);


	}
	public void setActiveZone(float ratio){

		float w = getWidth();
		float h = getHeight();
        this.activeZone = new Rectangle(w/2,h/2,w*ratio,h, ResourceManager.getInstance().vbom);
        attachChild(this.activeZone);
        this.activeZone.setVisible(false);
	}



	@Override
	public boolean OnTouchScene(TouchEvent pSceneTouchEvent) {
		if(isVisible()){
		if(this.activeZone !=null){
	if(this.activeZone.contains(pSceneTouchEvent.getX(), pSceneTouchEvent.getY()))
		return super.OnTouchScene(pSceneTouchEvent);
	else return false;
		} else return super.OnTouchScene(pSceneTouchEvent);
		}
		return false;

	}
private boolean isActive = true;
Text text;
@Override
public void setActive(boolean b){
    this.isActive = b;
    setVisible(b);
	
}

public void unhighlight(){

    this.resetTextColor();
	changeState(State.NORMAL);
}

public void resetTextColor(){
    this.text.setColor(1f, 1f, 1f);
}

public void hightlight(){

    this.text.setColor(0f,1f,0f);
	changeState(State.PRESSED);
}

@Override
public boolean isActive() {
	return this.isActive;
}
	


}
