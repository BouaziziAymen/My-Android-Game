package is.kul.learningandengine.graphicelements.ui;

import org.andengine.util.adt.color.Color;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.scene.UIHandler;
import is.kul.learningandengine.scene.UISignal;

public class ColorButtonElement extends TextedButton{

	private final ColorSlot colorSlot;
	public ColorButtonElement(Color color) {
		super("Select Color", ResourceManager.smallButton, Button.ButtonType.Selector, UISignal.SignalName.ColorButton);
		// TODO Auto-generated constructor stub
        this.text.setX(-70);
	

		float[] circle_vertices = new float[ 12*3+3];
		 float RAY = 8;
		 
		
		for(int i = 0; i < 12; i++) 
		{ 
			float theta = (float) (2* Math.PI* (float)i / (float)12);//get the current angle 

			circle_vertices[3*i] = (float) (RAY * Math.cos(theta));//calculate the x component 
			circle_vertices[3*i+1] = (float) (RAY * Math.sin(theta));//calculate the y component 
			circle_vertices[3*i+2] = (float) (RAY * Math.sin(theta));
}
colorSlot = new ColorSlot(50,getHeight()/2,color);
this.attachChild(colorSlot);
		
	}

	@Override
	public void diffuse(UISignal signal) {

			if(getParent()instanceof UIHandler){
			signal.source = this;
			UIHandler ui = (UIHandler) getParent();
			ui.diffuse(signal);	
				
			}
			}
	public void updateColor(Color color) {
        this.colorSlot.setColor(color);
        unhighlight();
		
	}
}
