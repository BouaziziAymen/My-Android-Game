package is.kul.learningandengine.scene;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

import is.kul.learningandengine.helpers.Utils;

public class cutElement implements Comparable<cutElement>{
public Fixture getFixture(){
	return fixture;
}
	float intersectionFraction;
	public enum type {ENTERING,LEAVING,END}
	type TYPE;
	Fixture fixture;
	private Vector2 point;
	int fixtureId;
	Body body;
	public cutElement(Fixture fixture,Vector2 point, float fraction, boolean es){

        intersectionFraction = es ?fraction:1-fraction;
        setEorS(es);
		this.fixture = fixture;
        body = fixture.getBody();
        setPoint(point);
        fixtureId = (Integer) fixture.getUserData();
	}
	public boolean isCohesive(cutElement other){
        return body == other.body && fixtureId == other.fixtureId;
    }

	@Override
	public int compareTo(cutElement another) {
		if(Utils.vectorEquivalence(getPoint(), another.getPoint())){
			if(isEorS())return 1;else return -1;
		} else {
	 
        if(intersectionFraction <another.intersectionFraction)return -1;else return 1;
		
	}
	}
	public String toString(){
		char c = this.isEorS() ?'E':'S';
		return TYPE+""+ this.point;
	}

	public Vector2 getPoint() {
		return this.point;
	}

	public void setPoint(Vector2 point) {
		this.point = point;
	}

	public boolean isEorS() {
		return this.TYPE==type.ENTERING;
	}
	public boolean isEnd() {
		return this.TYPE==type.END;
	}
	public void setEorS(boolean eorS)
	{
		if(eorS)TYPE=type.ENTERING;else TYPE=type.LEAVING;
	}
}

