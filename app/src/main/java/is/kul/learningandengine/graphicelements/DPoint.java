package is.kul.learningandengine.graphicelements;
import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.helpers.Utils;

import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Line;

import com.badlogic.gdx.math.Vector2;


public class DPoint extends DElement {

public int order;
float scale;
DFactory.DrawingType type;
Vector2 point;
public DPoint(float x, float y,float scale,int order, DFactory.DrawingType type){
	super(x, y);
point = new Vector2(x,y);
this.order = order;
this.scale = scale;
this.type = type;
    scale(scale);
float[][] vertices = new float[ DFactory.N_POINT_VERTICES[type.getValue()]][2];
for(int i = 0; i < DFactory.N_POINT_VERTICES[type.getValue()]; i++) 
{ 
vertices[i][0] =DFactory.circle_vertices[type.getValue()][i][0] ;
vertices[i][1] =DFactory.circle_vertices[type.getValue()][i][1] ;
}

for(int i=0;i< DFactory.N_POINT_VERTICES[type.getValue()];i++){
int ip = (i+1)% DFactory.N_POINT_VERTICES[type.getValue()];
Line  line = new Line(vertices[i][0],vertices[i][1],vertices[ip][0],vertices[ip][1], DFactory.POINT_THICKNESS[type.getValue()],ResourceManager.getInstance().vbom);

line.setColor(0f, 1f, 0f);
    this.attachChild(line);
}
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

	@Override
    public Vector2 toVector(){
		return new Vector2(getX(), getY());
	}
	
	public DPoint copy(){
		return new DPoint(getX(), getY(), this.scale, this.order, type);
	}


	public void rotate(Vector2 origin, float angle) {
		Vector2 u = new Vector2(getX()-origin.x,getY()-origin.y);
		Utils.rotateVector2(u,angle);
		float newX = u.x+origin.x;
		float newY = u.y+origin.y;

		setPosition(newX, newY);
	}

	public void decale(float dx, float dy) {
        setPosition(getX()+dx,getY()+dy);
	}

@Override
public void updatePosition(float dx, float dy){
	super.updatePosition(dx,dy);
	point.set(getX(),getY());
}


	@Override
    public void scale(float scale) {
        setScale(1/scale);
		
	}


}

