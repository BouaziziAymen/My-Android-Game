package is.kul.learningandengine.scene;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

import java.util.ArrayList;

import is.kul.learningandengine.basics.DCGameEntity;

public class MyRayCastCallback implements RayCastCallback{
boolean ES;
ArrayList<cutElement> cutElements;
private DCGameEntity specificEntity;
private int specificBlockID;
	private  boolean convert;

	@Override
public float reportRayFixture(Fixture fixture, Vector2 point,
                              Vector2 normal, float fraction) {
	Body body = fixture.getBody();
	boolean ok = true;
	if(specificEntity !=null){
    ok = false;
	int ID = (Integer) fixture.getUserData();
	DCGameEntity entity = (DCGameEntity) body.getUserData();
	if((specificEntity ==entity&&!specificInverted)||(specificEntity !=entity&&specificInverted)){
	if(specificBlockID ==-1|| specificBlockID ==ID)ok = true;
	}	
	}
	if(ok){
		Vector2 ex = null;
		if(this.convert)
	    ex = body.getLocalPoint(point).cpy().mul(32f);
		else ex =point.cpy();
	cutElement element = new cutElement(fixture, ex, fraction, this.ES);
        this.cutElements.add(element);
	}
return 1;
}
boolean specificInverted;
	public void setDirection(boolean dir){
        ES = dir;
	}
	public void setElements(ArrayList<cutElement> cutElements, DCGameEntity specificEntity,int specificBlockID, boolean convert) {
		this.specificEntity = null;
		this.specificBlockID = -1;
		this.convert = convert;
		if(specificEntity!=null){
			this.specificEntity = specificEntity;
			if(specificBlockID!=-1){
				this.specificBlockID = specificBlockID;
			}
		}
		
		this.cutElements = cutElements;
		
	}
	

	
}
