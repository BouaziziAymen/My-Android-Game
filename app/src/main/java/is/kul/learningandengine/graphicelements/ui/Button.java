package is.kul.learningandengine.graphicelements.ui;

import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;
import java.util.Iterator;

import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;
import is.kul.learningandengine.scene.UISignal.SignalName;

public class Button extends TiledSprite implements UIHandler {
	
	public enum ButtonType {
		OneClick, Selector, OneOnly
	}
	Button.State state;
	Button.ButtonType type;
	SignalName bName;


	public Button(float pX, float pY, ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager, Button.ButtonType type,
			SignalName bName) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		this.bName = bName;
		this.type = type;
        this.state = Button.State.NORMAL;
	}

	public Button(float pX, float pY, ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager, Button.ButtonType type,
			SignalName bName,boolean b) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		this.bName = bName;
		this.type = type;
		if(b)
            changeState(Button.State.PRESSED);
		else changeState(Button.State.DISABLED);
	}



	public enum State {
		NORMAL, PRESSED, DISABLED
    }

	public boolean isPressed(){
		return state ==Button.State.PRESSED;
	}

	@Override
	public void diffuse(UISignal signal) {
		if (getParent() instanceof UIHandler) {

			((UIHandler) getParent()).diffuse(signal);
		}
	}



	@Override
    public boolean OnTouchScene(TouchEvent pSceneTouchEvent) {
if(state == Button.State.DISABLED)return false;
		if (!isEnabled()){
            changeState(Button.State.DISABLED);
		return false;
		}
		boolean inside = false;

if(contains(pSceneTouchEvent.getX(), pSceneTouchEvent.getY()))inside=true;



	if (this.type == Button.ButtonType.Selector || this.type == ButtonType.OneOnly) {

		if(inside&&pSceneTouchEvent.isActionDown()) {
			if(state==State.NORMAL) {
				changeState(Button.State.PRESSED);
				setSignal(new UISignal(this.bName, UISignal.Event.Clicked, this));
			} else if(this.type == ButtonType.Selector){
				changeState(Button.State.NORMAL);
				setSignal(new UISignal(this.bName, UISignal.Event.Unclicked, this));
			}
		}
	}


	if (this.type == Button.ButtonType.OneClick) {
		if (pSceneTouchEvent.isActionDown()&&inside) {
            changeState(Button.State.PRESSED);
            setSignal(new UISignal(this.bName, UISignal.Event.Clicked,this));
		}
		if(state==State.PRESSED)
		if (pSceneTouchEvent.isActionUp()||pSceneTouchEvent.isActionCancel()) {
			changeState(Button.State.NORMAL);
			setSignal(new UISignal(this.bName, UISignal.Event.Unclicked,this));
	}
	}


return inside;

	}
	class PendingSignal{
		UISignal signal;
		int timer;
		boolean dead=false;
		PendingSignal(UISignal signal){
			timer = 0;
			this.signal = signal;
		}
		void update(){
			timer++;
			if(timer>6){diffuse(signal);dead=true;}
		}
	}

	void setSignal(UISignal signal){
		this.pendingSignals.add(new PendingSignal(signal));
	}
	ArrayList<PendingSignal> pendingSignals = new ArrayList<PendingSignal>();


	@Override
	public void onManagedUpdate(float dt){
super.onManagedUpdate(dt);
		Iterator<PendingSignal> iterator = pendingSignals.iterator();
while(iterator.hasNext()){
	PendingSignal p = iterator.next();
	p.update();
	if(p.dead)iterator.remove();
}
	}

	boolean enabled = true;


	private boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		if(enabled) {
			changeState(State.NORMAL);

		} else changeState(State.DISABLED);
	}

	void changeState(Button.State pState) {
		if (pState == state) {
			return;
		}

        this.state = pState;
		if (this.state == Button.State.NORMAL)
            setCurrentTileIndex(0);
		else if (this.state == Button.State.DISABLED)
            setCurrentTileIndex(2);
		else
            setCurrentTileIndex(1);

	}

	protected void click() {
        changeState(Button.State.PRESSED);
		this.diffuse(new UISignal(this.bName, UISignal.Event.Clicked,this));
	}

	protected void unclick() {
		changeState(Button.State.NORMAL);
		this.diffuse(new UISignal(this.bName, UISignal.Event.Unclicked,this));
	}

	protected void release() {
		changeState(Button.State.NORMAL);
		this.diffuse(new UISignal(this.bName, UISignal.Event.Released,this));
	}
}
