package is.kul.learningandengine.graphicelements.ui;

import org.andengine.entity.text.Text;
import org.andengine.entity.text.exception.OutOfCharactersException;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

public class FlickeringText extends Text {
float tMax;
FlickeringTextType type;
private Color color1;
private Color color2;
private Color currentColor;
CharSequence text1, text2, currentText;

public FlickeringText(float tMax, float pX, float pY, IFont pFont, CharSequence pText,
			int pCharactersMaximum,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		
		super(pX, pY, pFont, pText, pCharactersMaximum,
				 pVertexBufferObjectManager);
		this.tMax = tMax;
    type = FlickeringTextType.HIDING;
	}
float offset;
public FlickeringText(Color color1, Color color2,float tMax, float pX, float pY, IFont pFont, CharSequence pText1, CharSequence pText2,
		int pCharactersMaximum,
		VertexBufferObjectManager pVertexBufferObjectManager, float offset) {

	super(pX, pY, pFont, pText1, pCharactersMaximum,
			 pVertexBufferObjectManager);
    this.offset = offset;
	this.tMax = tMax;
    type = FlickeringTextType.MORPHING;
	this.color1 = color1;
	this.color2 = color2;
    currentColor = color1;
    this.text1 = pText1;
    this.text2 = pText2;
    currentText = pText1;
    setText();
}
boolean enabled;
public void setEnabled(boolean b){
    this.enabled = b;
  if(!b){
      currentText = this.text1;
      currentColor = this.color1;
      setColor(this.currentColor);
      setText();
	
  }

}
public boolean isEnabled(){
  return this.enabled;
}

public void setTextPoles(CharSequence pText1,CharSequence pText2) {

    text1 = pText1;
    text2 = pText2;
    this.currentText = this.text1;
    setText();

}
public void setText(){
    setText(this.currentText);
    setX(this.getWidth()/2+offset);
}
	@Override
    protected void onManagedUpdate(float pSecondsElapsed) {
		if(this.enabled){
            this.time +=pSecondsElapsed;
		if(this.time > this.tMax){
            this.time = this.time - this.tMax;
			if(this.type ==FlickeringTextType.HIDING)
                setVisible(!isVisible());
			else{
				if(this.currentColor == this.color1) this.currentColor = this.color2;else this.currentColor = this.color1;
                setColor(this.currentColor);
				
				if(this.currentText == this.text1) this.currentText = this.text2;else this.currentText = this.text1;
                setText();
			}
		}
		}
		
	}
float time;
}
