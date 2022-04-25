package is.kul.learningandengine.graphicelements.ui;

import android.util.Log;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import java.util.ArrayList;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;

public class Panel extends Entity implements UIHandler {
    private final float x0;
    private final ArrayList<ITextureRegion> regions;
    private Button closer,opener;
    int width;

    Panel(float x, float y,int rows,boolean hasButton1,boolean hasButton2){
        super(x,y);
        regions = ResourceManager.getInstance().panel;
        ITiledTextureRegion buttonRegion1 = ResourceManager.getInstance().panelButtonTextureRegion1;
        ITiledTextureRegion buttonRegion2 = ResourceManager.getInstance().panelButtonTextureRegion2;

        this.width = 0;
        if(hasButton2){
            opener = new Button(buttonRegion2.getWidth()/2+2, 0, buttonRegion2, ResourceManager.getInstance().vbom, Button.ButtonType.OneClick, UISignal.SignalName.NoPanel);
            this.attachChild(opener);
            width+=buttonRegion2.getWidth()-2;
        }
         x0 = (hasButton2)?width+regions.get(0).getWidth()/2:regions.get(0).getWidth()/2;
        float pas = regions.get(1).getWidth();
        rows = rows +((hasButton1)?0:1)+((hasButton2)?0:1);
        for(int i=0;i<rows;i++){
            int k = 1;
            if(i==0&&!hasButton2)k=0;

            else if(i==rows-1&&!hasButton1)k=2;
            Sprite sprite = new Sprite(x0+i*pas-i, 0, regions.get(k), ResourceManager.getInstance().vbom);
            this.attachChild(sprite);
            this.width += pas-1;

        }

        if(hasButton1){
            closer = new Button(width + buttonRegion1.getWidth() / 2-2, 0, buttonRegion1, ResourceManager.getInstance().vbom, Button.ButtonType.OneClick, UISignal.SignalName.YesPanel);
            this.attachChild(closer);
            width+=buttonRegion1.getWidth()-2;
        }
        this.setX(getX()-width/2);

    }
    void attachChild(Entity entity, int room){
        entity.setX(room*32+x0-room);
        this.attachChild(entity);
    }


    @Override
    public void diffuse(UISignal signal) {
        UserInterface ui = (UserInterface) getParent();
        ui.diffuse(signal);

    }

    @Override
    public boolean OnTouchScene(TouchEvent touch) {
        if(this.isVisible()) {
            if (this.closer != null) {
                if (this.closer.OnTouchScene(touch)) return true;
            }
            if (this.opener != null) {
                if (this.opener.OnTouchScene(touch)) return true;
            }
        }
return false;
    }

}
