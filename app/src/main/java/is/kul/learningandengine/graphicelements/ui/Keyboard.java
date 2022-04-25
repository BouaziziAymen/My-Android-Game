package is.kul.learningandengine.graphicelements.ui;

import java.util.Locale;

import is.kul.learningandengine.GameActivity;
import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;

public class Keyboard extends Entity implements UIHandler{
TextPlace input;
	public Keyboard(float X, float Y,VertexBufferObjectManager pVertexBufferObjectManager) {
		super(X, Y);
		
	 String[] strings= {
	"_",
    "0123456789",
	"azertyuiop",
	"qsdfghjklm",
	 "#wxcvbn@",
	   ".,&+-"};

	
	int x= 0;
	int y=50*4;
	for(int j=0;j<strings.length;j++){
	for(int i=0;i<strings[j].length();i++){
		
		Button.ButtonType type = Button.ButtonType.OneClick;
		int n = 0;
		if(strings[j].charAt(i)=='&'){n=3;}//SPACE
		else if(strings[j].charAt(i)=='@'){n=4;}//DELETE
		else 	if(strings[j].charAt(i)=='#'){n=4;type=Button.ButtonType.Selector;}//SHIFT
		else if(strings[j].charAt(i)=='_'){n=3;}//MOVE
	
		float width = ResourceManager.getInstance().keyboardButtons.get(n).getWidth();
		
		KeyboardButton button=new KeyboardButton(strings[j].charAt(i),n,x+width/2, y,type, pVertexBufferObjectManager);
	x+=button.getWidth();


        attachChild(button);
	}
	 if(j==4)x=60;
	else x= 0;
	y-=50;
	}
	
	
	}

	@Override
	public void diffuse(UISignal signal) {
	char character = (Character)signal.data;

	if(character=='#'&&signal.event==UISignal.Event.Clicked){
        this.uppercase = true;
		for(int i = 0; i< mChildren.size(); i++){

			KeyboardButton button = (KeyboardButton) getChildByIndex(i);
			if(button.character!='&'&&button.character!='_'&&button.character!='#')
			button.text.setText(button.text.getText().toString().toUpperCase(Locale.ENGLISH));
		}
	}

	if(character=='#'&&signal.event==UISignal.Event.Unclicked){
        this.uppercase = false;
		for(int i = 0; i< mChildren.size(); i++){

			KeyboardButton button = (KeyboardButton) getChildByIndex(i);
			if(button.character!='&'&&button.character!='_'&&button.character!='#'&&button.character!='@')
			button.text.setText(button.text.getText().toString().toLowerCase(Locale.ENGLISH));
		}
	}

		String c = ""+ character;
		if(this.uppercase)c=c.toUpperCase(Locale.ENGLISH);

		if(signal.event==UISignal.Event.Clicked){
			if(character !='_'&& character !='#')

if(this.isVisible())
                this.appendCharacter(c.charAt(0));

		}
		
	
	
		
	}

	
	void appendCharacter(char c){

		String textString = (String) this.input.text.getText();
		
		
		if(c=='@'){ if (textString != null && textString.length() > 0) {
			textString = textString.substring(0, textString.length()-1);}
		    } else if(c=='&')textString+=" ";
		    else
		textString+=c;
		if(textString.length()<= this.max){
            this.input.text.setTextPoles(textString, textString+"-");
		}
		
	}
	public boolean isTouched(TouchEvent touch){
		boolean bool = false;
		if(isVisible()){
		for(int i = 0; i< this.getChildCount(); i++){
			if(this.getChildByIndex(i).contains(touch.getX(), touch.getY())){bool = true;break;}
			}
		}
		return bool;
		}
	
	boolean uppercase;
	private float offset;
	private int max;
	@Override
	public boolean OnTouchScene(TouchEvent pSceneTouchEvent) {
		boolean touched = false;
		
	touched = isTouched(pSceneTouchEvent);
	if(this.isVisible()){
			for(int i = 0; i< getChildCount(); i++){
				KeyboardButton button = (KeyboardButton) getChildByIndex(i);
				button.OnTouchScene(pSceneTouchEvent);
		if(button.state==Button.State.PRESSED&&button.character=='_') setPosition(pSceneTouchEvent.getX()-button.getWidth()/2, pSceneTouchEvent.getY()-50*4);
			
			
			}
			
			
		}
		
		return touched;
	}

	public void setText(AlphaNumericInput input) {
		this.input = input;
		this.input.text.setTextPoles("", "-");
		max = input.max;
	}

	public void detachText(){
        input = null;
        setVisible(false);
	}

}
