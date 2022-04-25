package is.kul.learningandengine.scene;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.WorldManifold;

import is.kul.learningandengine.basics.Block;
import is.kul.learningandengine.basics.DCGameEntity;

public class ContactWrapper {

	DCGameEntity entityA, entityB;
	int time;
	public boolean continuous;
	public boolean dead;
	Fixture fixtureA, fixtureB;
	int numberOfContactPoints;
	Vector2[] points;
	public Block block1;
	public Block block2;
	float impulse;
	void setPoints(Vector2[] pts){
        this.points = new Vector2[this.numberOfContactPoints];
		for(int i = 0; i< this.numberOfContactPoints; i++) this.points[i] =pts[i].cpy();
	}

	ContactWrapper(Contact contact, int time, ContactImpulse impulse){
		this.time = time;
		//float[] nimp = impulse.getNormalImpulses();
		//float[] timp = impulse.getTangentImpulses();

        this.fixtureA = contact.getFixtureB();
        this.fixtureB = contact.getFixtureA();

		WorldManifold manifold = contact.getWorldManifold();
        this.numberOfContactPoints = manifold.getNumberOfContactPoints();

		//	this.impulse =(float) Math.sqrt(nimp[0]*nimp[0]+ timp[0]*timp[0]);


        this.points = new Vector2[this.numberOfContactPoints];
		for(int i = 0; i< this.numberOfContactPoints; i++) this.points[i] = manifold.getPoints()[i].cpy();
		Body body1 = this.fixtureA.getBody();
		Body body2 = this.fixtureB.getBody();
        entityA = (DCGameEntity) body1.getUserData();
        entityB = (DCGameEntity) body2.getUserData();
        this.block1 = this.entityA.getBlock((Integer) this.fixtureA.getUserData());
        this.block2 = this.entityB.getBlock((Integer) this.fixtureB.getUserData());
        this.continuous = false;

		//Log.e("contact", "contact:"+entity1.ID+"/"+entity2.ID+"at"+World.onStep);
	}
	public boolean equivalent(ContactWrapper other){
		if(entityA ==other.entityA&& entityB ==other.entityB)return true;
        return entityA == other.entityB && entityB == other.entityA;
    }
	public String toString(){
		return "("+ this.entityA.ID+"/"+ this.entityB.ID+"at"+ this.time +")";

	}

}
