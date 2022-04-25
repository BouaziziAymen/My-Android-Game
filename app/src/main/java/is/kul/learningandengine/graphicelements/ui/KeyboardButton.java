package is.kul.learningandengine.graphicelements.ui;

import is.kul.learningandengine.ResourceManager;

import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;
import is.kul.learningandengine.scene.UISignal.SignalName;

import org.andengine.entity.text.Text;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class KeyboardButton extends Button  {
     char character;
	 Text text;
   
	public KeyboardButton(char c,int n,float pX, float pY,Button.ButtonType type,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, ResourceManager.getInstance().keyboardButtons.get(n), pVertexBufferObjectManager, type, SignalName.Keyboard);
		// TODO Auto-generated constructor stub
		String string="";
		if(c=='&')string="SPACE";
		else if(c=='#')string="SHIFT";
		else if(c=='@')string="DEL";
		else if(c=='_')string="MOVE";
		else string=""+c;
        character = c;
		if(c!='#'&&c!='@')
            attachChild(this.text =new Text(getWidth()/2, getHeight()/2, ResourceManager.getInstance().font, string, string.length(), pVertexBufferObjectManager));
		else
            attachChild(this.text =new Text(getWidth()/2, getHeight()/2, ResourceManager.getInstance().font2, string, string.length(), pVertexBufferObjectManager));
	}
	
	@Override
	public void diffuse(UISignal signal) {
		if(signal.event==UISignal.Event.Clicked) this.text.setColor(1f, 0, 0);else
			if(signal.event==UISignal.Event.Unclicked)
                this.text.setColor(1f, 1f, 1f);
		if (getParent() instanceof UIHandler) {

			((UIHandler) getParent()).diffuse(new UISignal(SignalName.Keyboard,signal.event,this, new Character(this.character)));
			
		}
	}



}
