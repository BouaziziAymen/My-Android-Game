package is.kul.learningandengine.graphicelements;

import is.kul.learningandengine.ResourceManager;

import org.andengine.entity.primitive.Line;

public class DCircleT extends DPoint {
float ray;
	public DCircleT(float x, float y, float scale, int order, int size) {
		super(x, y, scale, order,DFactory.DrawingType.TYPEJOINT1);
        this.ray = DFactory.POINT_RAY[size];
		Line   line = new Line(0, this.ray /scale,0,-this.ray /scale, 2,ResourceManager.getInstance().vbom);

line.setColor(0f, 1f, 0f);
        this.attachChild(line);



line = new Line(-this.ray /scale,0, this.ray /scale,0, 2,ResourceManager.getInstance().vbom);

line.setColor(0f, 1f, 0f);
        this.attachChild(line);

	}

	
	
	
	@Override
    public void scale(float scale){
    super.scale(scale);

    
	Line   line = new Line(0, this.ray /scale,0,-this.ray /scale, 2,ResourceManager.getInstance().vbom);

line.setColor(0f, 1f, 0f);
        this.attachChild(line);



line = new Line(-this.ray /scale,0, this.ray /scale,0, 2,ResourceManager.getInstance().vbom);

line.setColor(0f, 1f, 0f);
        this.attachChild(line);
    
    
	}


}
