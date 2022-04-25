package is.kul.learningandengine.graphicelements.ui;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;

import org.andengine.entity.Entity;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;

import android.util.Log;

public class TextedNumericInput extends Entity implements LayoutElement,UIHandler {

	private final Text text;
	NumericInput numericInput;
NumType type;
	public TextedNumericInput(String name,float x, float y, float value, float f1, float f2, NumType type) {
		super(x,y);
this.type = type;
		 text = new Text(0, 0, ResourceManager.getInstance().font3, name, 30, ResourceManager.getInstance().vbom);
	this.attachChild(text);
	text.setX(-text.getWidth()/2);
        this.numericInput = new NumericInput(name,text.getWidth(), 0, f1,f2,type);
        this.numericInput.text.setX(text.getX()-text.getWidth()/2);
        this.numericInput.setFloatValue(value);
        attachChild(this.numericInput);
	}
public <T extends Number> T getValueWithType(){

	if(type==NumType.FLOAT||type==NumType.POSFLOAT)
	return (T)Float.valueOf((String)this.numericInput.text.getText());
	else if(type==NumType.INT||type==NumType.POSINT)
		return (T)Integer.valueOf((String)this.numericInput.text.getText());
	else return null;
}
public float getValue(){
		return Float.parseFloat((String)this.numericInput.text.getText());
}
	public int getIntValue(){
		return Integer.parseInt((String)this.numericInput.text.getText());
	}


boolean active = true;
@Override
public void setActive(boolean b) {
    this.active = b;
	
}
@Override
public boolean isActive() {
	
	return this.active;
}
@Override
public void diffuse(UISignal signal) {
	

	((UIHandler) getParent()).diffuse(signal);
	
}
@Override
public boolean OnTouchScene(TouchEvent touch) {
	boolean touched = false;
	if(isActive()&&isVisible()){
	if(this.numericInput.OnTouchScene(touch))touched = true;
	}
	return touched;
}
public void setValue(float value) {
    numericInput.setFloatValue(value);
	
}
}

