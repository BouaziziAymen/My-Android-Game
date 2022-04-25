package is.kul.learningandengine.graphicelements.ui;

import org.andengine.entity.Entity;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;

public class TextedAlphaNumericInput extends Entity implements LayoutElement,UIHandler {

    AlphaNumericInput alphanumericInput;

    public TextedAlphaNumericInput(String name,float x, float y, String value, int n) {
        super(x,y);

        Text text = new Text(x, y, ResourceManager.getInstance().font3, name, 30, ResourceManager.getInstance().vbom);
	this.attachChild(text);
	text.setX(text.getX()-text.getWidth()/2);
        this.alphanumericInput = new SimpleAlphaNumericInput(name,x, y, n);
        this.alphanumericInput.text.setX(text.getX()-text.getWidth()/2);
        this.alphanumericInput.setText(value);
        attachChild(this.alphanumericInput);
    }
    public String getValue(){
        return (String)this.alphanumericInput.text.getText();
    }
    boolean active = true;
    @Override
    public void setActive(boolean b) {
        this.active = b;

    }
    @Override
    public boolean isActive() {

        return this.active;
    }
    @Override
    public void diffuse(UISignal signal) {


        ((UIHandler) getParent()).diffuse(signal);

    }
    @Override
    public boolean OnTouchScene(TouchEvent touch) {
        boolean touched = false;
        if(isActive()){
            if(this.alphanumericInput.OnTouchScene(touch))touched = true;
        }
        return touched;
    }
    public void setValue(String value) {
        alphanumericInput.setText(value);

    }
}

