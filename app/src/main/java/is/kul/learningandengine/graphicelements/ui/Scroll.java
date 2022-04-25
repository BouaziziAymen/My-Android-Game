package is.kul.learningandengine.graphicelements.ui;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;
import is.kul.learningandengine.scene.UISignal.SignalName;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import android.util.Log;

public class Scroll extends Entity implements UIHandler{

	private final int max;
	private final Sprite knob;
	Entity knobBound;
	private final int rows;
	private float ratio;
	private float offset;
	private boolean selected;
	private int total;
	private final Button up;
	private final Button down;
	private boolean Active;

	Scroll(int rows, int max, int x, int y){
		super(-64*2+12,y,24,rows*64);
        this.setIgnoreUpdate(true);


        setZIndex(1000);
		
				
		 this.rows = rows;
	
		this.max = max;
		Sprite s1,s2;
        attachChild(s1 = new Sprite(0,rows*64+27/2F, ResourceManager.getInstance().upperTextureRegion,ResourceManager.getInstance().vbom));
        attachChild(s2 = new Sprite(0, -25/2F, ResourceManager.getInstance().lowerTextureRegion,ResourceManager.getInstance().vbom));
		s1.setZIndex(999);
		s2.setZIndex(999);
		
		for(int i=0;i<rows;i++){
			Sprite sprite = new Sprite(0,32+ i*64, ResourceManager.getInstance().trailTextureRegion,ResourceManager.getInstance().vbom);
            attachChild(sprite);
		}
		float ratio = 1f;
        this.knob = new Sprite(0, 64*rows*(1-ratio/2) , ResourceManager.getInstance().knobTextureRegion,ResourceManager.getInstance().vbom);
        this.knob.setHeight(64*rows*ratio);
        this.knobBound = new Entity(0, 0);
        this.knobBound.setY(64*rows*(1-ratio/2));
        this.knobBound.setHeight(64*rows*ratio);
        this.knobBound.setWidth(50);
        attachChild(this.knobBound);
        attachChild(this.knob);


        this.knob.setHeight(64*rows*ratio);
        this.knob.setY(64*rows*(1-ratio/2) );
        this.knob.setBlendingEnabled(false);

        this.knobBound.setY(64*rows*(1-ratio/2));
        this.knobBound.setHeight(64*rows*ratio);

        this.up = new Button(0,rows*64+27/2F,
			ResourceManager.getInstance().upButtonTextureRegion,ResourceManager.getInstance().vbom,Button.ButtonType.OneClick, SignalName.scrolluparrow);

        this.down = new Button(0, -27/2F,
			ResourceManager.getInstance().downButtonTextureRegion,ResourceManager.getInstance().vbom,Button.ButtonType.OneClick, SignalName.scrolldownarrow);

        attachChild(this.up);
        attachChild(this.down);
        sortChildren();
	}

	void update(int total){
		
	if(total> this.max){
        this.ratio = this.max /(float)total;
	
			float hph = (this.knob.getHeight()-64* this.rows * this.ratio)/2;
        this.knob.setHeight(64* this.rows * this.ratio);
        this.knob.setY(this.knob.getY()+hph);

        this.knobBound.setHeight(64* this.rows * this.ratio);
        this.knobBound.setY(this.knob.getY()+hph);
			
			if(this.knob.getY()+ this.knob.getHeight()/2> this.rows *64){
                this.knob.setY(this.rows *64- this.knob.getHeight()/2);
                this.knobBound.setY(this.knob.getY());
				}
			if(this.knob.getY()- this.knob.getHeight()/2<0){
                this.knob.setY(this.knob.getHeight()/2);
                this.knobBound.setY(this.knob.getY());
			}

        this.setVisible(true);
        Active = true;
		}  
	else {
        this.setVisible(false);
        this.Active = false;
	}

        setTotal(total);
		
		

	}
	@Override
	public void diffuse(UISignal signal) {
		
		if(getParent() instanceof UIHandler){
			
		if(signal.signalName== SignalName.scrolldownarrow){
            knob.setY(this.knob.getY()-25);
            update(this.getTotal());
			((UIHandler) getParent()).diffuse(new UISignal(SignalName.Scroller, UISignal.Event.changed,this));
		} else if(signal.signalName== SignalName.scrolluparrow){
            knob.setY(this.knob.getY()+25);
            update(this.getTotal());
			((UIHandler) getParent()).diffuse(new UISignal(SignalName.Scroller, UISignal.Event.changed,this));
		}
		else ((UIHandler) getParent()).diffuse(signal);
		}

		}
	@Override
	public boolean OnTouchScene(TouchEvent pSceneTouchEvent) {

	boolean touched = false;




	if(pSceneTouchEvent.isActionUp()||pSceneTouchEvent.isActionOutside()||pSceneTouchEvent.isActionCancel()){
        this.selected = false;
	}

		if(this.knobBound.contains(pSceneTouchEvent.getX(),pSceneTouchEvent.getY())&&pSceneTouchEvent.isActionDown()){
            this.offset = pSceneTouchEvent.getY()- this.knob.getSceneCenterCoordinates()[1];
	touched=true;
            this.selected = true;


		}
		if(pSceneTouchEvent.isActionMove()&& this.selected){
		float dy = pSceneTouchEvent.getY()- getSceneCenterCoordinates()[1]+ getHeight()/2;


            this.knob.setY(dy- this.offset);
            this.knobBound.setY(this.knob.getY());


		if(this.knob.getY()+ this.knob.getHeight()/2> this.rows *64){
            this.knob.setY(this.rows *64- this.knob.getHeight()/2);
            this.knobBound.setY(this.knob.getY());
			}
		if(this.knob.getY()- this.knob.getHeight()/2<0){
            this.knob.setY(this.knob.getHeight()/2);
            this.knobBound.setY(this.knob.getY());
		}

		touched=true;
            this.diffuse(new UISignal(SignalName.Scroller, UISignal.Event.changed,this));
		}
		
	
		if(this.up.OnTouchScene(pSceneTouchEvent))touched=true;
		if(this.down.OnTouchScene(pSceneTouchEvent))touched=true;
		
	
		return touched;
	}

	public float getScrollValue(){
		if(this.getTotal()> this.max){
		float x0= this.knob.getY()+ this.knob.getHeight()/2;
		float ratio = 1-x0/(this.rows *64);
		float span = ratio * this.getTotal() * 25;
		return span;
		} else return 0;
		
	}
	public boolean isActive(){
		return this.Active;
	}

	public int getTotal() {
		return this.total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
}
