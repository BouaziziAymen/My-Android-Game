package is.kul.learningandengine.graphicelements.ui;


import android.util.Log;

import java.util.HashMap;

import is.kul.learningandengine.scene.GameScene;
import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;


public class NumericInput extends TextPlace {

	NumType type;
    float f0;
	float f1;
	HashMap<ElementName,Object> saved;
public void save(ElementName key){
	if(saved==null) saved = new HashMap<ElementName,Object>();

	if(type==NumType.INT||type==NumType.POSINT)
		saved.put(key,this.getIntValue());
	else saved.put(key,this.getFloatValue());
}
public void restoreValue(ElementName key){
	if(saved==null)return;

Object object=  saved.get(key);

if(object!=null){
	if(type==NumType.INT||type==NumType.POSINT)
		this.setIntValue((Integer)object);
	else this.setFloatValue((Float)object);
}

}

	public NumericInput(String name,float x, float y, float f0, float f1, NumType type) {
		super(name, x,y,new Quantity(0,0,String.valueOf(f1).length()+((type==NumType.FLOAT||type==NumType.POSFLOAT)?2:0),1),0);
		Quantity quantity = (Quantity)body;
		this.setOffset(quantity.width/6);
        setLimits(f0, f1);
		this.type = type;
		this.sortChildren();

	}

private void setLimits(float f0, float f1){
this.f0 = f0;
this.f1 = f1;
}
void setIntValue(int value){
	String textString;
	
	 textString = Integer.toString(value);


    this.text.setTextPoles(textString,textString+"-");
}

void setFloatValue(float value){
	String textString;
	
	 textString = String.valueOf(value);


    this.text.setTextPoles(textString,textString+"-");


}

void appendCharacter(char c) {
	
	String textString = (String) this.text.text1;
	boolean proceed = true;

	if (c == '@') {
		if (textString != null && textString.length() > 0) {
			textString = textString.substring(0, textString.length() - 1);
		}
	} else if (c == '&')
		textString += " ";


	if (c == '@' || c == '&')
		proceed = false;
	if (c == '.') {
		if (textString.length() == 0)
			proceed = false;
		if (textString.length() > 0)
			if (textString.lastIndexOf('.') != -1)
				proceed = false;
	}

	if (proceed) {
		textString += c;
		if (textString.length() > 0){
			try {
				if (Float.parseFloat(textString) > this.f1) {
					if (this.type == NumType.FLOAT)
						textString = String.valueOf(this.f1);
					else if (this.type == NumType.INT || this.type == NumType.POSINT)
						textString = String.valueOf(Math.round(this.f1));
				}
			} catch (Throwable t){if(textString.length()>1)textString = removeLastChar(textString);}

		}
	}
    this.text.setTextPoles(textString, textString+"-");


}
	public String removeLastChar(String s) {
		if (s == null || s.length() == 0) {
			return s;
		}
		return s.substring(0, s.length()-1);
	}
@Override
public void diffuse(UISignal signal) {
	UIHandler ui = (UIHandler) getParent();

	ui.diffuse(signal);
	if (signal.signalName == UISignal.SignalName.TextField && signal.event == UISignal.Event.Clicked) {

		GameScene.ui.signal_numericKeyboard_On(this);

	}

	if (signal.signalName == UISignal.SignalName.TextField && signal.event == UISignal.Event.Unclicked) {

		GameScene.ui.signal_numericKeyboard_Off();
	}
}

public float getFloatValue() {
	
	return Float.parseFloat((String) text.text1);
} 
public int getIntValue() {
	
	return Integer.parseInt((String) text.text1);
}


}
