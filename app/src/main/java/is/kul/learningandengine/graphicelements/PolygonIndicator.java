package is.kul.learningandengine.graphicelements;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.helpers.MathUtils;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Line;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

public class PolygonIndicator extends Entity {
DPoint begin,end;
float scale;
 int n;




    public PolygonIndicator(Vector2 begin, Vector2 end, int n){
	this.n = n;
	this.scale = scale;
	this.begin = new DPoint(begin.x, begin.y, scale,0, DFactory.DrawingType.TYPEJOINT1);
	this.end = new DPoint(end.x, end.y, scale, 1,DFactory.DrawingType.TYPEJOINT1);
	this.begin.setRed();
	this.end.setRed();
        Line line = new Line(begin.x, begin.y, end.x, end.y, 3, ResourceManager.getInstance().vbom);
    line.setColor(1f,0f,0f);
    attachChild(line);
    attachChild(this.begin);
    attachChild(this.end);
    setZIndex(1000);
	Vector2 direction = new Vector2(begin.x-end.x,begin.y-end.y);
	Vector2 unit1 =MathUtils.rotateZ(direction, (float) (Math.PI/6)).nor();
	Vector2 unit2 =MathUtils.rotateZ(direction, (float) (-Math.PI/6)).nor();


    Line stroke1 = new Line(end.x, end.y, end.x+20*unit1.x, end.y+20*unit1.y, 3, ResourceManager.getInstance().vbom);
    Line stroke2 = new Line(end.x, end.y, end.x+20*unit2.x, end.y+20*unit2.y, 3, ResourceManager.getInstance().vbom);
    stroke1.setColor(1f,1f,0f);
    attachChild(stroke1);
    stroke2.setColor(1f,1f,0f);
    attachChild(stroke2);
}
public void updateIndicator(float nx, float ny,VertexBufferObjectManager vbom){
    end.setPosition(nx,ny);
    end.setX(nx);
    end.setY(ny);
    end.setRed();
    detachChildren();


    Line line = new Line(this.begin.getX(), this.begin.getY(), nx, ny, 3, vbom);
    line.setColor(1f,0f,0f);
    attachChild(line);
    attachChild(end);
    setZIndex(1000);
	Vector2 direction = new Vector2(this.begin.getX()- this.end.getX(), this.begin.getY()- this.end.getY());
	Vector2 unit1 =MathUtils.rotateZ(direction, (float) (Math.PI/6)).nor();
	Vector2 unit2 =MathUtils.rotateZ(direction, (float) (-Math.PI/6)).nor();


    Line stroke1 = new Line(this.end.getX(), this.end.getY(), this.end.getX()+20*unit1.x, this.end.getY()+20*unit1.y, 3, vbom);
    Line stroke2 = new Line(this.end.getX(), this.end.getY(), this.end.getX()+20*unit2.x, this.end.getY()+20*unit2.y, 3, vbom);
    stroke1.setColor(1f,1f,0f);
    attachChild(stroke1);
    stroke2.setColor(1f,1f,0f);
    attachChild(stroke2);
	
}

public void hide() {

    setVisible(false);
}
}
