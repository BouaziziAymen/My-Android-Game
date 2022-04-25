package is.kul.learningandengine.graphicelements.ui;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;

import java.util.ArrayList;

import is.kul.learningandengine.ResourceManager;

public class Base extends Entity {
    Entity background;

    protected int width;
    public Base(float x, float y, int size, ArrayList<ITextureRegion> regions){
        super(x,y);
        this.background = new Entity();

        this.width = 0;
        float x0 = regions.get(0).getWidth()/2;
        float pas = regions.get(1).getWidth();
        for(int i=0;i<size;i++){
            int k;
            if(i==0)k=0;else if(i==size-1)k=2;else k = 1;
            Sprite sprite = new Sprite(x0+i*pas-i, 0, regions.get(k), ResourceManager.getInstance().vbom);
            this.background.attachChild(sprite);
            this.width += sprite.getWidth()-1;

        }

        attachChild(this.background);
    }
}
