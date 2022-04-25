package is.kul.learningandengine.graphicelements.ui;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;

import is.kul.learningandengine.ResourceManager;

public class BaseLamp extends Base {

    public BaseLamp(float x, float y,int size){
        super(x,y,size,ResourceManager.getInstance().lamp);

        }

}
