package is.kul.learningandengine.polygonarithmetic;

import com.badlogic.gdx.math.Vector2;

public class Twin {
public Vector2 point;
int index;
Twin(Compass compass1, Compass compass2){
Vector2 v1 = compass1.point;
Vector2 v2 = compass2.point;
Vector2 dir = v2.cpy().sub(v1);
Vector2 udir = dir.cpy().nor();
Vector2 normal = new Vector2(-udir.y,udir.x).nor().mul(0.01f);
this.point = v1.cpy().add(dir.mul(0.5f)).add(normal);
 index = compass1.POSITION.SOUTH;
}

Twin(Compass compass1, Compass compass2, boolean FLAG){
	this(compass1,compass2);
	index = compass1.POSITION.WEST;
}
public String toString(){
	return point.toString();
}
}
