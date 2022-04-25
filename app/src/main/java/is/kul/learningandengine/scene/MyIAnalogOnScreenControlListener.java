package is.kul.learningandengine.scene;

import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;


public class MyIAnalogOnScreenControlListener implements AnalogOnScreenControl.IAnalogOnScreenControlListener {

	@Override
	public void onControlChange(BaseOnScreenControl pBaseOnScreenControl,
			float pValueX, float pValueY) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onControlClick(AnalogOnScreenControl pAnalogOnScreenControl) {
		// TODO Auto-generated method stub
		
	}
	/*
public MyIAnalogOnScreenControlListener(){
	
}
DElement moveable;
void setMoveableElement(DElement moveable){
	this.moveable = moveable;
}
@Override
public void onControlChange(
		final BaseOnScreenControl pBaseOnScreenControl,
		final float pValueX, final float pValueY) {
	
		if (moveable != null && !moveable.dead) {
			float newX = moveable.x + 3 * pValueX
					/ (float) model.scale;
			float newY = moveable.y + 3 * pValueY
					/ (float) model.scale;
			// TEST IF POINT POSITION IS OK
			if(moveable instanceof DPoint){
		
				DPoint dpoint = (DPoint)moveable;
			if (model.getPoints().testPosition(dpoint, newX, newY)
					&& (Math.abs(pValueX) > 0.1f || Math
							.abs(pValueY) > 0.1f)) {
	moveable.moveElement(newX,newY);

				model.updateGameGroup();
			}
		 
		
		} else {
			
			moveable.moveElement(newX,newY);
			
			
		}
	}

}

@Override
public void onControlClick(
		final AnalogOnScreenControl pAnalogOnScreenControl) {
	if (moveable != null && !moveable.dead) {
		moveable.registerEntityModifier(new SequenceEntityModifier(
			new ScaleModifier(0.25f, 1, 2f), new ScaleModifier(
					0.25f, 2f, 1)));
}
}
*/
}
