package is.kul.learningandengine.graphicelements;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.helpers.MathUtils;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Line;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

public class MirrorIndicator extends Entity {
	DPoint begin,end;
	float scale;
	public MirrorIndicator(Vector2 start){
		VertexBufferObjectManager vbom = ResourceManager.getInstance().vbom;

		 begin = new DPoint(start.x, start.y, 1f, 0, DFactory.DrawingType.TYPEPOINT);
		 end = new DPoint(start.x, start.y, 1f, 0, DFactory.DrawingType.TYPEPOINT);

		begin.setColor(1f,0f,0f);
		end.setColor(1f,0f,0f);
        attachChild(begin);
        attachChild(end);

        setZIndex(1000);
	
	Vector2 direction = new Vector2(begin.getX()-end.getX(),begin.getY()-end.getY());
	Vector2 unit1 =MathUtils.rotateZ(direction, (float) (Math.PI/6)).nor();
	Vector2 unit2 =MathUtils.rotateZ(direction, (float) (-Math.PI/6)).nor();
	   Line line = new Line(end.getX(), end.getY(), begin.getX(), begin.getY(), 3, vbom);
line.setColor(1f,0f,0f);
        attachChild(line);
	   
		Line stroke1 = new Line(begin.getX(), begin.getY(), begin.getX()+20*unit1.x, begin.getY()+20*unit1.y, 3, vbom);
	    Line stroke2 = new Line(begin.getX(), begin.getY(), begin.getX()+20*unit2.x, begin.getY()+20*unit2.y, 3, vbom);
	    stroke1.setColor(1f,1f,0f);
        attachChild(stroke1);
	    stroke2.setColor(1f,1f,0f);
        attachChild(stroke2);
	    
	    
	    Line stroke3 = new Line(end.getX(), end.getY(), end.getX()-20*unit1.x, end.getY()-20*unit1.y, 3, vbom);
	    Line stroke4 = new Line(end.getX(), end.getY(), end.getX()-20*unit2.x, end.getY()-20*unit2.y, 3, vbom);
	    stroke1.setColor(1f,1f,0f);
        attachChild(stroke3);
	    stroke2.setColor(1f,1f,0f);
        attachChild(stroke4);
    

	
	}
	public void update(Vector2 newPosition) {

        end.setPosition(newPosition.x,newPosition.y);


detachChildren();
        this.begin.setColor(1f,0f,0f);
        this.end.setColor(1f,0f,0f);
        this.attachChild(begin);
        this.attachChild(end);

		VertexBufferObjectManager vbom = ResourceManager.getInstance().vbom;
	  Line line = new Line(this.end.getX(), this.end.getY(), this.begin.getX(), this.begin.getY(), 3, vbom);
	  line.setColor(1f,0f,0f);
        attachChild(line);
	
	
	
	
		Vector2 direction = new Vector2(this.begin.getX()- this.end.getX(), this.begin.getY()- this.end.getY());
		Vector2 unit1 =MathUtils.rotateZ(direction, (float) (Math.PI/6)).nor();
		Vector2 unit2 =MathUtils.rotateZ(direction, (float) (-Math.PI/6)).nor();


		Line stroke1 = new Line(this.begin.getX(), this.begin.getY(), this.begin.getX()+20*unit1.x, this.begin.getY()+20*unit1.y, 3, vbom);
	    Line stroke2 = new Line(this.begin.getX(), this.begin.getY(), this.begin.getX()+20*unit2.x, this.begin.getY()+20*unit2.y, 3, vbom);
	    stroke1.setColor(1f,1f,0f);
        attachChild(stroke1);
	    stroke2.setColor(1f,1f,0f);
        attachChild(stroke2);
	    
	    
	    Line stroke3 = new Line(this.end.getX(), this.end.getY(), this.end.getX()-20*unit1.x, this.end.getY()-20*unit1.y, 3, vbom);
	    Line stroke4 = new Line(this.end.getX(), this.end.getY(), this.end.getX()-20*unit2.x, this.end.getY()-20*unit2.y, 3, vbom);
	    stroke1.setColor(1f,1f,0f);
        attachChild(stroke3);
	    stroke2.setColor(1f,1f,0f);
        attachChild(stroke4);
		
	}
}
