package is.kul.learningandengine.graphicelements.ui;

import org.andengine.input.touch.TouchEvent;

import android.graphics.Color;
import android.util.Log;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.graphicelements.LayerProperty;
import is.kul.learningandengine.scene.UISignal;
import is.kul.learningandengine.scene.UISignal.SignalName;


public class ColorSelectorWindow extends OrganizedWindow {
	private final ColorPanel panel;
	ColorSelector selector;
private final TextedSelector valueSelector;
private final TextedSelector alphaSelector;

private Window caller;

	public ColorSelectorWindow(float X, float Y) {
		super(X, Y, 4, 6, ResourceManager.getInstance().vbom, false);
        this.selector = new ColorSelector(0,40);
        this.selector.setReferencePosition(X, Y+40);
        attachChild(this.selector);
        this.valueSelector = new TextedSelector(1f,2, 25, 1, "VALUE  ", -80, -100, 0, 1f, 5);
        this.alphaSelector = new TextedSelector(1f,2, 25, 1, "ALPHA  ", -80, -130, 0, 1f, 5);
        attachChild(this.valueSelector);
        attachChild(this.alphaSelector);
         panel = new ColorPanel(0,-32*4-64-32);
		this.attachChild(panel);



	}

	@Override
	public float getWidth(){
		return panel.width;
	}
	@Override
    public void setColor(int argb){
		float[] hsv = new float[3];
		Color.RGBToHSV(Color.red(argb),Color.green(argb),Color.blue(argb), hsv);
		float alpha = Color.alpha(argb)/255f;
        this.selector.setColor(hsv, alpha);
        this.alphaSelector.setValue(alpha);
        this.valueSelector.setValue(hsv[2]);
	
	}
	
	@Override
	public void diffuse(UISignal signal) {
		Log.e("signal", "colorselctorwindow"+signal.signalName);
		super.diffuse(signal);
		UserInterface ui = (UserInterface) getParent();

		if(signal.signalName== SignalName.foldwindow&&signal.event==UISignal.Event.Clicked){
            selector.setVisible(true);
            alphaSelector.setVisible(true);
            valueSelector.setVisible(true);
		}

	if(signal.signalName== SignalName.foldwindow&&signal.event==UISignal.Event.Unclicked){
        selector.setVisible(false);
        alphaSelector.setVisible(false);
        valueSelector.setVisible(false);
		}



		if(signal.signalName==UISignal.SignalName.QuantitySelector){
			TextedSelector selector = (TextedSelector)signal.source;
			if(selector== this.alphaSelector){
				float alpha = selector.getValue();

				this.selector.updateAlpha(alpha);
			}
            if(selector == this.valueSelector){
            	float value = selector.getValue();
            	this.selector.updateValue(value);
			}
		}
		if(signal.signalName== UISignal.SignalName.ColorSquare&&signal.event== UISignal.Event.Clicked){
			ColorSquare square = (ColorSquare)signal.source;
			org.andengine.util.adt.color.Color color = square.getColor();
			selector.setColor(color);
			Log.e("diffuse","color");

		}
		if(signal.signalName== SignalName.YesPanel&&signal.event==UISignal.Event.Clicked){
			ui.signal_updateLayerColor(this.selector.color);

			ui.hideColorSelector();
		}
		if(signal.signalName== SignalName.NoPanel&&signal.event==UISignal.Event.Clicked){
			ui.hideColorSelector();
		}
	}
	
	
@Override
public boolean OnTouchScene(TouchEvent pSceneTouchEvent) {
super.OnTouchScene(pSceneTouchEvent);
if(this.isVisible())
if(this.window.isVisible()){
	if(this.panel.OnTouchScene(pSceneTouchEvent))return true;
		if(this.selector.OnTouchScene(pSceneTouchEvent))return true;
		if(this.valueSelector.OnTouchScene(pSceneTouchEvent))return true;
		if(this.alphaSelector.OnTouchScene(pSceneTouchEvent))return true;

}

		return false;
	}


	public void setCaller(Window caller) {
		this.caller = caller;
	}

	public Window getCaller() {
		return caller;
	}

    @Override
    public void setPosition(float pX, float pY) {
        this.selector.setReferencePosition(pX, pY+40);
        super.setPosition(pX, pY);
    }

	public void addColorToPanel(org.andengine.util.adt.color.Color color) {
		panel.addColor(color);
	}
}