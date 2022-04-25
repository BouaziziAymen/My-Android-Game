package is.kul.learningandengine.graphicelements.ui;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.FontUtils;

import android.graphics.Paint;
import android.util.Log;

public class MessageBox extends Entity implements LayoutElement, UIHandler {

	
	private final Button button;
	private final String string;
	private final int width;
	private final Text text;
	public MessageBox(float x, float y, int length){
		super(x,y);
	Sprite sprite = new Sprite(0, 0, ResourceManager.getInstance().messageBoxRegions.get(0), ResourceManager.getInstance().vbom);
        attachChild(sprite);
	float d = sprite.getWidth();
	for(int i=0;i<length;i++){
		sprite = new Sprite(d, 0, ResourceManager.getInstance().messageBoxRegions.get(1), ResourceManager.getInstance().vbom);
		d+=sprite.getWidth()-1;
        attachChild(sprite);
	}
	sprite = new Sprite(d-ResourceManager.getInstance().messageBoxRegions.get(2).getWidth()/2, 0, ResourceManager.getInstance().messageBoxRegions.get(2), ResourceManager.getInstance().vbom);
        attachChild(sprite);
        this.button = new Button(-4, 0, ResourceManager.getInstance().messageTextureRegion, ResourceManager.getInstance().vbom, Button.ButtonType.Selector, UISignal.SignalName.messageBox);
        attachChild(this.button);


        this.string = "This game is the raccoon princess and angel slime's extreme adventure story. If you can clear all stage level, then you can say to your friends I'm pretty good at game control.Challenge to be the best slime driver!";
        this.text = new Text(20, 0, ResourceManager.getInstance().font3, this.string, 500, ResourceManager.getInstance().vbom);
        this.text.getWidth();

        attachChild(this.text);

        width = length * 32 - length;

	}

	@Override
	public void diffuse(UISignal signal) {
		UIHandler ui = (UIHandler) getParent();
		
		ui.diffuse(signal);
	}

	@Override
	public boolean OnTouchScene(TouchEvent pSceneTouchEvent) {
        return this.button.OnTouchScene(pSceneTouchEvent);
    }

	boolean active;
	@Override
	public void setActive(boolean b) {
        active = b;
	}

	@Override
	public boolean isActive() {
		return this.active;
	}
	int t;
	int counter;
	@Override
    protected void onManagedUpdate(float pSecondsElapsed) {
		if(this.counter %10==0){
		//n is the number of characters that can be fit counting from start and going
		int n = 0;
		float x = 0;
		for(int i = this.t; i< this.string.length(); i++){
			x+= FontUtils.measureText(ResourceManager.getInstance().font3, ""+ this.string.charAt(i));
			if(x> this.width)break;
			n++;
		}
		String part = this.string.substring(this.t, Math.min(this.t +n, this.string.length()));
            this.text.setText(part);
            this.text.setX(this.text.getWidth()/2+12);
            this.t++;
            this.t = this.t % this.string.length();
		}
        this.counter++;
	}
}
