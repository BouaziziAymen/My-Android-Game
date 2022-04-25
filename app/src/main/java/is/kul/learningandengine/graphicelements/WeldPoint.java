package is.kul.learningandengine.graphicelements;

public class WeldPoint extends JointPoint {
	DSquare center;
	public WeldPoint(float x, float y, float scale) {
		super(x,y);

        this.center = new DSquare(0, 0, scale,DFactory.DrawingType.TYPEJOINT1);

        attachChild(this.center);
        setZIndex(9999);
	}

	@Override
	public void setBlue() {
        this.center.setBlue();
		
		
	}

	@Override
	public void setGreen() {
        this.center.setGreen();
		
	}

	@Override
	public void setRed() {
        this.center.setRed();
	
		
	}

	@Override
	void scale(float scale) {
        center.scale(scale);
		
	}
	
}
