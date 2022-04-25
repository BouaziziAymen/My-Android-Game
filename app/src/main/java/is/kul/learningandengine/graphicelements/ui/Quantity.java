package is.kul.learningandengine.graphicelements.ui;

import is.kul.learningandengine.ResourceManager;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;


public class Quantity extends Base {

	public Quantity(float x, float y,int size, int color){
		super(x,y,size,ResourceManager.getInstance().quantity.get(0));

	}
	
	
	
	
	@Override
	public boolean contains(float pX, float pY) {
		for(int i = 0; i< this.background.getChildCount(); i++){
			Sprite sprite = (Sprite) this.background.getChildByIndex(i);
			if(sprite.contains(pX, pY))return true;
		}
		return false;
	}

}
