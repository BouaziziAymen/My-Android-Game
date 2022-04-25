package is.kul.learningandengine.graphicelements;
import is.kul.learningandengine.ResourceManager;

import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Line;

import com.badlogic.gdx.math.Vector2;


public class DSquare extends DElement {


public int order;
public float scale;


float ray;
float thickness;
DFactory.DrawingType type;
public DSquare(float x, float y,float scale,DFactory.DrawingType type){
	super(x, y);
this.type = type;
this.scale = scale;
    thickness = DFactory.POINT_THICKNESS[type.getValue()];
    scale(scale);
float[][] vertices = new float[ 4][2];
    this.ray = DFactory.POINT_RAY[type.getValue()];
vertices[0][0] = -this.ray;
vertices[0][1] =-this.ray;
vertices[1][0] = this.ray;
vertices[1][1] =-this.ray;
vertices[2][0] = this.ray;
vertices[2][1] = this.ray;
vertices[3][0] =-this.ray;
vertices[3][1] = this.ray;


for(int i=0;i< 4;i++){
int ip = (i+1)%4;
Line  line = new Line(vertices[i][0],vertices[i][1],vertices[ip][0],vertices[ip][1], this.thickness,ResourceManager.getInstance().vbom);

line.setColor(0f, 1f, 0f);
    this.attachChild(line);
}
}


	@Override
    public void scale(float scale){
        setScale(1/scale);
	
	
	}
	@Override
    public void setRed(){
		for(IEntity line: mChildren)
			line.setColor(1f, 0f, 0f);
	}
	@Override
    public void setColor(float r, float g, float b){
		for(IEntity line: mChildren)
			line.setColor(r, g, b);
	}
	@Override
    public void setBlue(){
		for(IEntity line: mChildren)
			line.setColor(0f, 0f, 1f);
	}
	@Override
    public void setGreen(){
		for(IEntity line: mChildren)
			line.setColor(0f, 1f, 0f);
	}
	public int getOrder() {
		// TODO Auto-generated method stub
		return this.order;
	}
	@Override
    public Vector2 toVector(){
		return new Vector2(getX(), getY());
	}
	
	public DSquare copy(){

		return new DSquare(getX(), getY(), this.scale, type);
	}
}

