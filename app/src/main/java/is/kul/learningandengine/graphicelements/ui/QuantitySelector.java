package is.kul.learningandengine.graphicelements.ui;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;

import android.util.Log;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;

public class QuantitySelector extends Quantity implements UIHandler {
Entity front;
private final int size;
int color;
private final float v0;
private final float v1;
private int numberOfStrokes;
private final int strokeSize;
private float value;
	public QuantitySelector(float initialValue,int strokeSize, float x, float y, int size, int color, float v0, float v1) {
		super(x, y, size, color);
		// TODO Auto-generated constructor stub
	this.size = size;
	this.color = color;
        this.front = new Entity();

        attachChild(this.front);
	this.strokeSize = strokeSize;
	this.v0 = v0;
	this.v1 = v1;
        setValue(initialValue);

	}
	public float computeValue(){
        this.value = this.numberOfStrokes /(float) this.size * (this.v1 - this.v0) + this.v0;
	
		return this.value;
	}

	@Override
	public void diffuse(UISignal signal) {
		if(getParent() instanceof UIHandler){
			((UIHandler) getParent()).diffuse(signal);
		}
	}
float getValue(){
    this.computeValue();
	return value;
}
	@Override
	public boolean OnTouchScene(TouchEvent touch) {
		boolean touched = false;
		if(isVisible()){
	float[] pos = getSceneCenterCoordinates();
		if(contains(touch.getX(),touch.getY())){
			float ratio=(touch.getX()-pos[0])/ this.width;
            this.numberOfStrokes = this.strokeSize * (int) Math.floor(ratio* this.size /(float) this.strokeSize);
            this.createFront();
		
			touched = true;
		} 
		else
		if(Math.abs(pos[1]-touch.getY())<6.5f&&touch.getX()>pos[0]+ this.width &&touch.getX()<pos[0]+ this.width +32){
            this.numberOfStrokes = this.size;
            this.createFront();
			
			touched=true;
		}
		}
		if(touched) diffuse(new UISignal(UISignal.SignalName.QuantitySelector, UISignal.Event.changed, this));
		return touched;
	}
	void setValue(float value){
        this.numberOfStrokes = (int)Math.floor ((value - this.v0)* this.size /(this.v1 - this.v0));
        this.createFront();
	}
	void createFront(){
        this.front.detachChildren();
	
		for(int i = 0; i< this.numberOfStrokes; i++){
			int k;
			if(i==0)k=0;else if(i== this.numberOfStrokes -1)k=2; else k=1;
			Sprite sprite = new Sprite(i*8+6.5f-i, 0, ResourceManager.getInstance().quantity.get(this.color).get(k), 	ResourceManager.getInstance().vbom);
            this.front.attachChild(sprite);
		}
		
	}

}
