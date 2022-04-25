package is.kul.learningandengine.graphicelements;

import is.kul.learningandengine.graphicelements.ui.Coordinated;

public abstract class JointPoint extends DElement implements Coordinated {


	public JointPoint(float x, float y){
		super(x,y);
	}

	@Override
	public void updatePosition(float newX, float newY) {
		super.updatePosition(newX, newY);

		JointIndicator indicator = (JointIndicator) getParent();
indicator.updateLine();
		
	}
	@Override
	public void resetColor(){
		if(((JointIndicator) getParent()).selected) this.setRed();
		else this.setGreen();
	}

}
