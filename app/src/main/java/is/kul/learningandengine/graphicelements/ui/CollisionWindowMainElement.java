package is.kul.learningandengine.graphicelements.ui;

import org.andengine.input.touch.TouchEvent;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.scene.UISignal;

public class CollisionWindowMainElement extends WindowMainElement  {

    private final Button add;


    public CollisionWindowMainElement(String name) {
        super(name);
        this.add = new Button(this.text.getX()+ this.text.getWidth()/2+32+ ResourceManager.getInstance().addTextureRegion.getWidth(),0,
                ResourceManager.getInstance().addTextureRegion,ResourceManager.getInstance().vbom,Button.ButtonType.OneClick, UISignal.SignalName.AddCategoryButton);


        attachChild(this.add);

    }


    @Override
    public boolean OnTouchScene(TouchEvent pSceneTouchEvent) {
        boolean touched = false;
        if(isVisible())

            if(this.add.OnTouchScene(pSceneTouchEvent))touched=true;
        return touched;
    }
    void close(){
        this.add.setEnabled(false);
    }
}
