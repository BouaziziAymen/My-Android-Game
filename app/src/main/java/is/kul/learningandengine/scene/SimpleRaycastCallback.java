package is.kul.learningandengine.scene;

import org.andengine.util.adt.color.Color;

import is.kul.learningandengine.basics.Block;
import is.kul.learningandengine.basics.DCGameEntity;
import is.kul.learningandengine.basics.Grain;
import is.kul.learningandengine.helpers.Utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

public class SimpleRaycastCallback implements RayCastCallback {
Block block;	
 float temperature;
public Vector2 center;
	
public void reset(){
    this.block = null;
}

	@Override
	public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
		if(fixture.getBody().getType()==BodyDef.BodyType.StaticBody)return 0;
		DCGameEntity entity = (DCGameEntity) fixture.getBody().getUserData();

        this.block = entity.getBlock((Integer) fixture.getUserData());
	Vector2 reflectionPoint=	entity.getBody().getLocalPoint(point).cpy().mul(32f);
	
	if(!Utils.isOnBorder(reflectionPoint, this.block.VERTICES))return 0;
	
	
	Grain nearest = this.block.grid.getNearestBorderGrain(reflectionPoint);
	float d = point.dst(this.center);
	float coef = d * d * 100;
	if(this.temperature >nearest.temperature)nearest.applyHeat((this.temperature -nearest.temperature)/coef);
	
	//GameScene.plotter.drawPoint(point.cpy().mul(32), Color.RED, 1, 0);
		return 0;
	}
public Block getBlock(){
	return this.block;
}
}
