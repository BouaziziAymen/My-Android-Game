package is.kul.learningandengine.entity;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;

import org.andengine.entity.IEntity;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;

import java.util.List;

import is.kul.learningandengine.basics.DCGameEntity;
import is.kul.learningandengine.basics.GameGroup;
import is.kul.learningandengine.helpers.Utils;
import is.kul.learningandengine.scene.GameScene;

public class Ragdoll extends GameGroup {

	public enum RagdollState{
		FALLENLEFT,FALLENRIGHT,DRAGGEDUP
		
	}
	
	private final PhysicsWorld physicsWorld;
	private final Grafcet brain;
	

	public Ragdoll(PhysicsWorld physicsWorld) {
		super("Ragdoll");
		
this.physicsWorld = physicsWorld;
        this.brain = new Grafcet
(new int[][]{{150,150},{100,100},{20000}},
		new int[][]{{10,-10},{10,10},{10}},	
		
		new int[][]{{0,1},{2,3},{4}},new int[]{2,2,1}, 3);
		
		
	}
	
	public void setBodyParts(DCGameEntity 
			head, DCGameEntity
			upperTorso,DCGameEntity lowerTorso, 
			DCGameEntity upperArmR,  DCGameEntity lowerArmR,
			DCGameEntity upperArmL,  DCGameEntity lowerArmL, 
			DCGameEntity upperLegR,  DCGameEntity lowerLegR, 
			DCGameEntity upperLegL, DCGameEntity lowerLegL, 
			DCGameEntity rightHand, DCGameEntity leftHand,
			DCGameEntity leftFoot,  DCGameEntity rightFoot){
		
		
		this.head = head;
		this.upperTorso =  upperTorso;
		this.lowerTorso = lowerTorso; 
		this.upperArmR = upperArmR;  
		this.lowerArmR = lowerArmR;
		this.upperArmL = upperArmL;  
		this.lowerArmL = lowerArmL;
		this.upperLegR = upperLegR; 
		this.lowerLegR = lowerLegR;
		this.upperLegL = upperLegL;  
		this.lowerLegL = lowerLegL;
		this.rightHand = rightHand; 
		this.leftHand = leftHand;
		this.leftFoot = leftFoot;  
		this.rightFoot = rightFoot;
		
		
		
	}
DCGameEntity 
head, 
upperTorso, lowerTorso, 
upperArmR,  lowerArmR,
upperArmL,  lowerArmL, 
upperLegR,  lowerLegR, 
upperLegL,  lowerLegL, 
rightHand,  leftHand,
leftFoot,   rightFoot;
float t;
private boolean footTouchingR;
private boolean footTouchingL;
@Override
protected void onManagedUpdate(float pSecondsElapsed) {
super.onManagedUpdate(pSecondsElapsed);
    this.t +=pSecondsElapsed;
    this.brain.run();


		
		
	
		
		
		if(true)
		
		for(int i = 0; i< this.brain.numStat[this.brain.current]; i++){
            this.performAction(this.brain.actions[this.brain.current][i], this.brain.speeds[this.brain.current][i]);

}

		

	
	float absAngle = Utils.normalise(this.upperTorso.getRotation());









List<Contact> list = this.physicsWorld.getContactList();
    this.footTouchingR = false;
    this.footTouchingL = false;
for(Contact contact:list){
	Body body1 = contact.getFixtureA().getBody();
	Body body2 = contact.getFixtureB().getBody();
    this.footTouchingR = true;


	if((body1== this.leftFoot.getBody()||body2== this.leftFoot.getBody())&&(body1== GameScene.ground.getEntityByIndex(0).getBody()||body2== GameScene.ground.getEntityByIndex(0).getBody()))
        this.footTouchingL = true;
}
if(this.footTouchingR){
	//upperTorso.getBody().setAngularVelocity(10);
	
}

}
private void performAction(int action, int amplitude) {
	if(true)return;
	if(action==0){
        this.equilibrate(this.upperTorso.getBody(),(float)(7*Math.PI/4),2000,(float)Math.PI/2);
		
		}
	if(action==1);
	
	if(action==2) this.lowerArmL.getBody().setAngularVelocity(-10);
	if(action==3){
        this.equilibrate(this.upperTorso.getBody(),(float)0,1000,(float)Math.PI/2);
        this.equilibrate(this.lowerTorso.getBody(),(float)0,1000,(float)Math.PI/2);

        this.equilibrate(this.upperLegR.getBody(),(float) 0,500,(float)Math.PI);
        this.equilibrate(this.upperLegL.getBody(),(float) 0,500,(float)Math.PI);
        this.equilibrate(this.lowerLegR.getBody(),(float) 0,500,(float)Math.PI);
        this.equilibrate(this.lowerLegL.getBody(),(float) 0,500,(float)Math.PI);
	}
	
	if(action==4){
	if(this.footTouchingR && this.footTouchingL){
        this.equilibrate(this.upperTorso.getBody(),(float)0,1000,(float)Math.PI/6);
        this.equilibrate(this.lowerTorso.getBody(),(float)0,1000,(float)Math.PI/6);

        this.equilibrate(this.upperLegR.getBody(),(float) 0,500,(float)Math.PI/4);
        this.equilibrate(this.upperLegL.getBody(),(float) 0,500,(float)Math.PI/4);
        this.equilibrate(this.lowerLegR.getBody(), this.upperLegR.getBody().getAngle(),500,(float)Math.PI/8);
        this.equilibrate(this.lowerLegL.getBody(), this.upperLegL.getBody().getAngle(),500,(float)Math.PI/8);
		
	}
	}
	
}
@Override
public void attachChild(IEntity entity){
	super.attachChild(entity);
	entity.setZIndex(this.ORBIT);
    this.ORBIT++;
}
private int ORBIT;
void equilibrate(Body body, float angle,int force,float limit){

	float rot = body.getAngle();
	 float totalRotation = angle - rot;
	
	  while ( totalRotation < -Math.PI) totalRotation += 2*Math.PI;
	  while ( totalRotation > Math.PI ) totalRotation -= 2*Math.PI;
	  if(Math.abs(totalRotation)<limit)
	 body.applyTorque( (float) (2*force*(totalRotation/Math.PI)) );
	RevoluteJoint j;
	
	 
}
boolean dragged;

public boolean onTouch(TouchEvent pSceneTouchEvent,
float pTouchAreaLocalX, float pTouchAreaLocalY)  {
return true;
}

public String toString(){
	String result;
	result="uarml"+upperArmL+"|uarmr"+upperArmR+"|larml"+lowerArmL+"|larmr"+lowerArmR+"|head"+head+"|utorso"+
			upperTorso+"|ltorso"+lowerTorso+"|ulegl"+upperLegL+"|ulegr"+upperLegR+"|llegl"+lowerLegL+"llegr"+lowerLegR+"|fl"+leftFoot+"|fr"
			+rightFoot;
return "{"+result+"}";

}


}
