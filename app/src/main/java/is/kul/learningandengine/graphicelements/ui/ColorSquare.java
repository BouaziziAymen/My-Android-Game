package is.kul.learningandengine.graphicelements.ui;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;

public class ColorSquare extends Entity implements UIHandler {
    private final Rectangle foreground;
    private final Rectangle background;

    ColorSquare(Color color,float x, float y){
        super(x,y);
        background = new Rectangle(0, 0, 26, 26, ResourceManager.getInstance().vbom);
        foreground = new Rectangle(0,0,23,23, ResourceManager.getInstance().vbom);
    this.attachChild(background);
    this.attachChild(foreground);
    background.setZIndex(0);
    foreground.setZIndex(1);
    this.sortChildren();
    setColor(color);
    }
    @Override
    public void setColor(Color c){
        foreground.setColor(c);
    }
    @Override
    public Color getColor(){
        return foreground.getColor();
    }
    @Override
    public void diffuse(UISignal signal) {
        if(getParent()instanceof UIHandler){
            ((UIHandler) getParent()).diffuse(signal);
        }
    }
    void select(){
        diffuse(new UISignal(UISignal.SignalName.ColorSquare, UISignal.Event.Clicked,this));
        selected = true;
        background.setColor(new Color(1f,0.5f,0f));
    }
    void unselect(){
        diffuse(new UISignal(UISignal.SignalName.ColorSquare, UISignal.Event.Unclicked,this));
        selected = false;
        background.setColor(new Color(Color.WHITE));
    }
boolean selected;
    @Override
    public boolean OnTouchScene(TouchEvent touch) {
        if(touch.isActionDown())
       if(foreground.contains(touch.getX(),touch.getY())){
           if(!selected)
           select();
           else unselect();
           return true;
       }

           return false;

    }

}
