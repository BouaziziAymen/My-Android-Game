package is.kul.learningandengine.graphicelements.ui;

import android.util.Log;

import org.andengine.entity.Entity;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;

public class TextPlace extends Entity implements UIHandler,
		LayoutElement {




	FlickeringText text;
	String textString = "";
	private boolean selected;
Entity body;
void setOffset(int n) {
	this.text.offset = n;
}
	public TextPlace(String value, float x, float y, Entity background, float offset) {

body = background;
this.textString = value;
        this.text = new FlickeringText(Color.WHITE, Color.RED, 0.5f, 0, 0,
				ResourceManager.getInstance().font2, this.textString, this.textString
						+ "-", 100, ResourceManager.getInstance().vbom,offset);

        this.text.setEnabled(false);

        attachChild(this.text);

		attachChild(background);
		text.setZIndex(999);
        sortChildren();

	}

	@Override
	public void diffuse(UISignal signal) {
		UIHandler ui = (UIHandler) getParent();

		ui.diffuse(signal);

	}

	@Override
	public boolean OnTouchScene(TouchEvent touch) {

			if (touch.isActionDown()) {
				if(body!=null)
			if (body.contains(touch.getX(), touch.getY())) {


                this.selected = !this.selected;
					if (this.selected)
                        diffuse(new UISignal(UISignal.SignalName.TextField,
								UISignal.Event.Clicked, this));
					 else diffuse(new UISignal(UISignal.SignalName.TextField,
								UISignal.Event.Unclicked, this));

				return true;
			}

		}


		return false;
	}

	private boolean isActive;

	@Override
	public void setActive(boolean b) {
        isActive = b;

	}

	@Override
	public boolean isActive() {
		// TODO Auto-generated method stub
		return this.isActive;
	}


}
