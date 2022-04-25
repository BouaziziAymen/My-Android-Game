package is.kul.learningandengine.basics;

import org.andengine.entity.Entity;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;

public class NonRotatingChild extends Entity{
protected float x0;
protected float y0;
float theta0;

private DCGameEntity parent;
public NRType type;
protected NonRotatingChild(float x, float y, float theta, DCGameEntity parent, NRType type){
    setParent(parent);
	this.type = type;
    this.x0 = x;
    this.y0 = y;

    this.theta0 = theta;
}
@Override
public DCGameEntity getParent() {
	return this.parent;
}
public void setParent(DCGameEntity parent) {
	this.parent = parent;
}
public Vector2 getPixelCenterSpeed(){
	if(this.parent !=null){
		Vector2 local = new Vector2(this.x0, this.y0).mul(1/32f);
	return this.parent.getBody().getLinearVelocityFromLocalPoint(local).cpy().mul(32f);
	}
	return null;
}

}
