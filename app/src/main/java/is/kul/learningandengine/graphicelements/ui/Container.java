package is.kul.learningandengine.graphicelements.ui;

import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;

import org.andengine.entity.Entity;
import org.andengine.input.touch.TouchEvent;

import android.util.Log;

public class Container extends Entity implements UIHandler {
float limit1,limit2;
	public Container(float limit1, float limit2){
        this.limit1 = limit1;
		this.limit2 = limit2;
	}
	public boolean containsEntity(Entity entity){
		return mChildren.indexOf(entity)!=-1;
	}
	@Override
	public void diffuse(UISignal signal) {
		if(getParent()instanceof UIHandler){

			((UIHandler) getParent()).diffuse(signal);
		}
		
	}

	@Override
	public boolean OnTouchScene(TouchEvent pSceneTouchEvent) {

		for(int i = 0; i< getChildCount(); i++)
			if(getChildByIndex(i)instanceof UIHandler){
				if(((UIHandler) getChildByIndex(i)).OnTouchScene(pSceneTouchEvent))return true;
			}
		return false;
	}
	
	public  void update(){
		
		for(int i = 0; i< getChildCount(); i++){
			
		boolean vis1,vis2;
            vis1 = getY() + getChildByIndex(i).getY() <= this.limit1;

            vis2 = getY() + getChildByIndex(i).getY() >= this.limit2;
if(getChildByIndex(i) instanceof LayoutElement){
	boolean isActive = ((LayoutElement) getChildByIndex(i)).isActive();
    getChildByIndex(i).setVisible(vis1&&vis2&&isActive);

}
		}
	}

}
