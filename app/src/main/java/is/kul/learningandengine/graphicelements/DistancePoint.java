package is.kul.learningandengine.graphicelements;

public class DistancePoint extends JointPoint {

	DCircleT circle;
	public DistancePoint(float x, float y, float scale, int order) {
		super(x, y);
        this.circle = new DCircleT(0, 0, scale, order,1);
        attachChild(this.circle);
        setZIndex(9999);
	}

	@Override
	public void setBlue() {
        this.circle.setBlue();
		
	}

	@Override
	public void setGreen() {
        this.circle.setGreen();
		
	}

	@Override
	public void setRed() {
        this.circle.setRed();
		
	}

	@Override
	void scale(float scale) {
        circle.scale(scale);
		
	}

}
