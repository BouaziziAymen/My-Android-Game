package is.kul.learningandengine.graphicelements;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Line;
import org.andengine.util.adt.color.Color;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.helpers.MathUtils;

public class DVector extends Entity {
	public static final int UNIT = 10;
	private final Line line;
	private final Line stroke1;
	private final Line stroke2;
	Vector2 point1,point2;

	public DVector(Vector2 point1, Vector2 point2, Color color, int thickness){
	Vector2 u = point2.cpy().sub(point1);
	this.point1 = point1;
	this.point2 = point2;
        this.line = new Line(point1.x,point1.y,point2.x,point2.y,thickness,ResourceManager.getInstance().vbom);
        this.line.setColor(color);
        attachChild(this.line);


Vector2 unit1 =MathUtils.rotateZ(u.cpy().mul(-1), (float) (-Math.PI/6)).nor();
Vector2 unit2 =MathUtils.rotateZ(u.cpy().mul(-1), (float) (Math.PI/6)).nor();


        this.stroke1 = new Line(point2.x, point2.y, point2.x+ DVector.UNIT *unit1.x, point2.y+ DVector.UNIT *unit1.y, thickness, ResourceManager.getInstance().vbom);
        this.stroke2 = new Line(point2.x, point2.y, point2.x+ DVector.UNIT *unit2.x, point2.y+ DVector.UNIT *unit2.y, thickness, ResourceManager.getInstance().vbom);
        this.stroke1.setColor(color);
        attachChild(this.stroke1);
        this.stroke2.setColor(color);
        attachChild(this.stroke2);
	}
	public void update(Vector2 point1, Vector2 point2) {
		this.point1.set(point1);
		this.point2.set(point2);
		Vector2 u = point2.cpy().sub(point1);
        this.line.setPosition(point1.x,point1.y,point2.x,point2.y);
	Vector2 unit1 =MathUtils.rotateZ(u.cpy().mul(-1), (float) (-Math.PI/6)).nor();
	Vector2 unit2 =MathUtils.rotateZ(u.cpy().mul(-1), (float) (Math.PI/6)).nor();
        this.stroke1.setPosition(point2.x, point2.y, point2.x+1/scale* DVector.UNIT *unit1.x, point2.y+1/scale* DVector.UNIT *unit1.y);
        this.stroke2.setPosition(point2.x, point2.y, point2.x+1/scale* DVector.UNIT *unit2.x, point2.y+1/scale* DVector.UNIT *unit2.y);

		
	}
	float scale;
	public void scale(float scale) {
		this.scale = scale;
        Vector2 u = point2.cpy().sub(point1);
        this.line.setPosition(point1.x,point1.y,point2.x,point2.y);
        Vector2 unit1 =MathUtils.rotateZ(u.cpy().mul(-1), (float) (-Math.PI/6)).nor();
        Vector2 unit2 =MathUtils.rotateZ(u.cpy().mul(-1), (float) (Math.PI/6)).nor();
        this.stroke1.setPosition(point2.x, point2.y, point2.x+1/scale*DVector.UNIT *unit1.x, point2.y+ 1/scale*DVector.UNIT *unit1.y);
        this.stroke2.setPosition(point2.x, point2.y, point2.x+1/scale*DVector.UNIT *unit2.x, point2.y+ 1/scale*DVector.UNIT *unit2.y);

	}
}
