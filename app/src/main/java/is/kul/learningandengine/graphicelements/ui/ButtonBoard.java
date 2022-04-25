package is.kul.learningandengine.graphicelements.ui;

import org.andengine.input.touch.TouchEvent;

import android.util.Log;

import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;

public class ButtonBoard extends Board implements UIHandler {
	ButtonBoard.Type type;
	public ButtonBoard(float x, float y, int margin,
			Board.Type boardType,ButtonBoard.Type type,
			Button... entities) {
		super(x, y, margin, boardType, entities);
		this.type = type;
	}

	public void hide() {

		releaseAll();
        this.setVisible(false);
	}

	public enum Type {
		Dependant,
		Independant
	}
	public UISignal.SignalName getActiveSignal(){
		if(!this.isVisible())return null;
		for(int i=0;i<entities.length;i++){
			Button b = (Button)entities[i];
			if(b.isPressed())return b.bName;

		}
		return null;
	}
	public void resetButtons(){
		for(int i=0;i<entities.length;i++){
			Button b = (Button)entities[i];
			b.unclick();

		}
	}


	@Override
	public boolean OnTouchScene(TouchEvent pSceneTouchEvent) {
		boolean touched = false;
		if(isVisible()){
		for(int i = 0; i< mChildren.size(); i++){
			UIHandler button = (UIHandler) getChildByIndex(i);
		if(	button.OnTouchScene(pSceneTouchEvent))touched=true;
		}
		}
		return touched;
	}

	@Override
	public void diffuse(UISignal signal) {
		if(isVisible()){
		if(type ==ButtonBoard.Type.Dependant){
			if(signal.event==UISignal.Event.Clicked)
		for(int i = 0; i< mChildren.size(); i++){
			Button button = (Button) getChildByIndex(i);
			if(button.state == Button.State.PRESSED)
			if(button.bName!=signal.signalName)button.release();
		}
		}
	
		if(getParent() instanceof UIHandler)
			((UIHandler) getParent()).diffuse(signal);
		}
	}
	public void releaseAll(){
		for(int i = 0; i< mChildren.size(); i++){
			Button button = (Button) getChildByIndex(i);
			button.release();
		}
	}
	public void disable(){
		for(int i = 0; i< mChildren.size(); i++){
			Button button = (Button) getChildByIndex(i);
			button.setEnabled(false);
		}
	}

	public void enable(){
		for(int i = 0; i< mChildren.size(); i++){
			Button button = (Button) getChildByIndex(i);
			button.setEnabled(true);
		}
	}
	
}
