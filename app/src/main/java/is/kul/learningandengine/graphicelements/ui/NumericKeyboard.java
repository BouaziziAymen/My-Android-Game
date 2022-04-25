package is.kul.learningandengine.graphicelements.ui;

import java.util.Locale;

import org.andengine.entity.Entity;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;

import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;

import android.util.Log;
import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;

public class NumericKeyboard extends Entity implements UIHandler {

	public NumericKeyboard(float X, float Y) {
		super(X, Y);


		String[] strings = { "_", "123", "456", "789", ".0-", "@" };

		int x = 0;
		int y = 50 * 4;
		for (int j = 0; j < strings.length; j++) {
			for (int i = 0; i < strings[j].length(); i++) {

				Button.ButtonType type = Button.ButtonType.OneClick;
				int n = 0;
				if (strings[j].charAt(i) == '&') {
					n = 3;
				}// SPACE
				else if (strings[j].charAt(i) == '@') {
					n = 4;
				}// DELETE
				else if (strings[j].charAt(i) == '#') {
					n = 4;
					type = Button.ButtonType.Selector;
				}// SHIFT
				else if (strings[j].charAt(i) == '_') {
					n = 3;
				}// MOVE

				float width = ResourceManager.getInstance().keyboardButtons
						.get(n).getWidth();

				KeyboardButton button = new KeyboardButton(
						strings[j].charAt(i), n, x + width / 2, y, type,
						ResourceManager.getInstance().vbom);
				x += button.getWidth();

                attachChild(button);
			}
			if (j == 4)
				x = 60;
			else
				x = 0;
			y -= 50;
		}
	}

	NumericInput input;

	

	public boolean isTouched(TouchEvent touch) {
		boolean bool = false;
		if (isVisible())
			for (int i = 0; i < this.getChildCount(); i++) {
				if (this.getChildByIndex(i).contains(touch.getX(), touch.getY())) {
					bool = true;
					break;
				}
			}

		return bool;
	}

	boolean uppercase;
	private CharSequence original;

	@Override
	public boolean OnTouchScene(TouchEvent pSceneTouchEvent) {
		boolean touched = false;
		if (this.isVisible()) {
			for (int i = 0; i < getChildCount(); i++) {

				if (getChildByIndex(i) instanceof UIHandler) {

					KeyboardButton button = (KeyboardButton) getChildByIndex(i);
					if (button.OnTouchScene(pSceneTouchEvent))
						touched = true;

					if (button.state == Button.State.PRESSED
							&& button.character == '_')
                        setPosition(
								pSceneTouchEvent.getX() - button.getWidth() / 2,
								pSceneTouchEvent.getY() - 50 * 4);

				}

			}
		}
		return touched;
	}

	public void setText(NumericInput input) {
        setType(input.type);
        this.original =  input.text.getText();
		input.text.setEnabled(true);
		this.input = input;
		this.input.text.setTextPoles("", "-");
	}

	KeyboardButton getButton(Character c){
		for (int i = 0; i < getChildCount(); i++) {

			KeyboardButton button = (KeyboardButton) getChildByIndex(i);
			if(button.character == c)return button;
		}
		return null;
	}
	
	@Override
	public void diffuse(UISignal signal) {
		char character = (Character) signal.data;

		if (character == '#' && signal.event == UISignal.Event.Clicked) {
            this.uppercase = true;
			for (int i = 0; i < mChildren.size(); i++) {

				KeyboardButton button = (KeyboardButton) getChildByIndex(i);
				if (button.character != '&' && button.character != '_'
						&& button.character != '#')
					button.text.setText(button.text.getText().toString()
							.toUpperCase(Locale.ENGLISH));
			}
		}

		if (character == '#' && signal.event == UISignal.Event.Unclicked) {
            this.uppercase = false;
			for (int i = 0; i < mChildren.size(); i++) {

				KeyboardButton button = (KeyboardButton) getChildByIndex(i);
				if (button.character != '&' && button.character != '_'
						&& button.character != '#' && button.character != '@')
					button.text.setText(button.text.getText().toString()
							.toLowerCase(Locale.ENGLISH));
			}
		}

		String c = "" + character;
		if (this.uppercase)
			c = c.toUpperCase(Locale.ENGLISH);

		if (signal.event == UISignal.Event.Clicked) {
			if (character != '_' && character != '#')

				if (this.isVisible())
                    this.input.appendCharacter(character);
		}

	}

	public void setType(NumType type){
		if(type==NumType.INT){
            getButton('.').setVisible(false);
		}
	
		
		if(type==NumType.POSINT){
            getButton('.').setVisible(false);
            getButton('-').setVisible(false);
		}
	}
	
	public void detachText() {
		

		if(this.input !=null){
			
			if(this.input.text.getText()==""|| this.input.text.getText()=="-"){
                this.input.text.text1 = this.original;
                this.input.text.text2 = this.original +"-";
                this.input.text.setText();
			}
            this.input.text.setEnabled(false);
            this.input = null;
            setVisible(false);
		}

	}
}