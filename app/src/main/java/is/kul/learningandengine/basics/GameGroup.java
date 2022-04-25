package is.kul.learningandengine.basics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

import org.andengine.entity.Entity;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

import java.util.ArrayList;
import java.util.HashSet;

import is.kul.learningandengine.ResourceManager;
import is.kul.learningandengine.factory.BasicFactory;
import is.kul.learningandengine.graphicelements.JointIndicator;
import is.kul.learningandengine.graphicelements.ui.JointProperty;
import is.kul.learningandengine.helpers.Utils;

public class GameGroup extends Entity {

String groupName;
public Entity entities;
HashSet<JointBluePrint> joints;
public GameGroup(String groupName){
	this.groupName = groupName;
    this.entities = new Entity();
    attachChild(this.entities);
    this.joints = new HashSet<JointBluePrint>();
}
public void updateGameGroup(){
	for(int i = 0; i< getEntityCount(); i++)
        getEntityByIndex(i).updateDCGameEntity();
}
public int getEntityCount(){
	return this.entities.getChildCount();
}
public void updateJoints(){
	//WHICH ENTITY HAS KEY1 THEN CHECK IF KEY IS ALIVE
	//WHICH ENTITY HAS KEY2
	//UPDATE JOINT TO TIE BOTH ENTITIES
	//REPACE JOINT OR ADD NEW JOINT
}

public int getNumberOfGrains(){
	int n = 0;
	for(int i = 0; i< this.entities.getChildCount(); i++)
		n+= getEntityByIndex(i).getNumberOfGrains();
	return n;
}

public DCGameEntity getEntityByIndex(int index){
	return (DCGameEntity) this.entities.getChildByIndex(index);
}

public ArrayList<DCGameEntity> getEntities(){
	ArrayList<DCGameEntity> result  = new ArrayList<DCGameEntity>();
	for(int i = 0; i< this.entities.getChildCount(); i++){
		result.add((DCGameEntity) this.entities.getChildByIndex(i));
	}
	return result;
}

public void margeWithGroup(GameGroup grouping){
	ArrayList<DCGameEntity> ent = grouping.getEntities();
	for(DCGameEntity e:ent)e.detachSelf();
    addAll(ent);
	
}
public int getNumberOfEntities(){
	return this.entities.getChildCount();
}
public void recreateJoint(final JointBluePrint bp){

	ResourceManager.getInstance().activity.runOnUpdateThread(new Runnable() {
																 @Override
																 public void run() {

																	 BasicFactory.createJoint(bp.definition);

																 }


															 }
	);



	
		
	
}

public GameGroup.pairtype0 distance(Vector2 anchor, int ID){
	float DMIN = Float.MAX_VALUE;
	DCGameEntity result = null;
	int BLOCKID = -1;
	for(int k=0;k<entities.getChildCount();k++){
		DCGameEntity entity = (DCGameEntity)entities.getChildByIndex(k);
		if(entity.INITIAL==ID) {

			ArrayList<Block> blocks = entity.blocks;
			float dmin = Float.MAX_VALUE;
			int blockid = -1;
			for (Block b : blocks) {
				float distance = b.distance(anchor);
					if (distance < dmin){ dmin = distance; blockid = b.ID;}
				}

			if(dmin<DMIN){BLOCKID = blockid;DMIN=dmin;result=entity;}
		}
	}
	return new GameGroup.pairtype0(result,BLOCKID);
}
class pairtype0{
	DCGameEntity entity;
	int block;
	pairtype0(	DCGameEntity entity,
	int block){
		this.entity = entity;
		this.block = block;
	}
}
public boolean contains(DCGameEntity e){
	for(int i = 0; i< entities.getChildCount(); i++) {
		DCGameEntity entity = (DCGameEntity) this.entities.getChildByIndex(i);
if(entity==e)return true;
	}
	return false;
}

public float getMass(){
	float mass = 0;
	for(int i = 0; i< entities.getChildCount(); i++){
		DCGameEntity entity = (DCGameEntity) this.entities.getChildByIndex(i);

			mass+=entity.getBody().getMass();


	}

	return mass;
}
public void addAll(ArrayList<DCGameEntity> childrenEntities) {

				for(int i=0;i<childrenEntities.size();i++){

                    this.addChildWithoutSort(childrenEntities.get(i));

				}


    this.entities.sortChildren();





}
public void addChildWithoutSort(DCGameEntity child){
    this.entities.attachChild(child);
	child.setParentGroup(this);

}
public void addChild(DCGameEntity child){
    this.entities.attachChild(child);
	child.setParentGroup(this);
    this.entities.sortChildren();


}
public void createJoint(Vector2 begin, Vector2 end, JointProperty property){


Vector2 v1 = begin.cpy().mul(1/32f);
Vector2 v2 = end.cpy().mul(1/32f);


GameGroup.pairtype0 pair1 = distance( begin, property.bodyA_ID);
int blockn1 = pair1.block;
DCGameEntity entity1 = pair1.entity;
GameGroup.pairtype0 pair2 = distance( end, property.bodyB_ID);
int blockn2 = pair2.block;
DCGameEntity entity2 = pair2.entity;


JointDef def = null;
if(property.type==JointProperty.Joint_Type.REVOLUTE){

RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
revoluteJointDef.bodyA = entity1.getBody();
revoluteJointDef.bodyB = entity2.getBody();
revoluteJointDef.localAnchorA.set(v1);
revoluteJointDef.localAnchorB.set(v2);

revoluteJointDef.collideConnected = property.collideConnected;
revoluteJointDef.enableMotor = property.hasMotor;
revoluteJointDef.lowerAngle=property.lower_limit;
revoluteJointDef.upperAngle = property.upper_limit;
revoluteJointDef.enableLimit = property.hasLimits;
revoluteJointDef.motorSpeed = property.Rotation_Speed;
revoluteJointDef.maxMotorTorque = property.Rotation_Torque;
def = revoluteJointDef;
BasicFactory.createJoint(revoluteJointDef);
}else if(property.type==JointProperty.Joint_Type.PRISMATIC){

	PrismaticJointDef prismaticJointDef = new PrismaticJointDef();
	prismaticJointDef.bodyA = entity1.getBody();
	prismaticJointDef.bodyB = entity2.getBody();
	prismaticJointDef.localAnchorA.set(v1);
	prismaticJointDef.localAnchorB.set(v2);

	prismaticJointDef.collideConnected = property.collideConnected;
	prismaticJointDef.enableMotor = property.hasMotor;
	prismaticJointDef.lowerTranslation = property.lower_limit/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
	prismaticJointDef.upperTranslation = property.upper_limit/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;


	prismaticJointDef.enableLimit = property.hasLimits;
	prismaticJointDef.motorSpeed = property.Rotation_Speed;
	prismaticJointDef.maxMotorForce = property.Rotation_Torque;
	prismaticJointDef.localAxis1.set(property.axe);
	def = prismaticJointDef;
	BasicFactory.createJoint(prismaticJointDef);


	}


else if (property.type==JointProperty.Joint_Type.WELD){

WeldJointDef weldJointDef = new WeldJointDef();
weldJointDef.bodyA = entity1.getBody();
weldJointDef.bodyB = entity2.getBody();
weldJointDef.localAnchorA.set(v1);
weldJointDef.localAnchorB.set(v2);
weldJointDef.collideConnected = false;
def = weldJointDef;
BasicFactory.createJoint(weldJointDef);
}
else if (property.type==JointProperty.Joint_Type.DISTANCE){
	
DistanceJointDef distanceJointDef = new DistanceJointDef();	
distanceJointDef.bodyA = entity1.getBody();
distanceJointDef.bodyB = entity2.getBody();
distanceJointDef.localAnchorA.set(v1);
distanceJointDef.localAnchorB.set(v2);	
distanceJointDef.collideConnected = true;
distanceJointDef.length=v1.dst(v2);
distanceJointDef.frequencyHz = property.frequency;
distanceJointDef.dampingRatio = property.damping;
def = distanceJointDef;
BasicFactory.createJoint(distanceJointDef);
}
	
	
	
	
	
	
	
	JointBluePrint bprint = new JointBluePrint(def, JointBluePrint.Type.Punctual);
    this.joints.add(bprint);
	JointKey key1 = new JointKey(bprint, JointKey.KeyType.A,begin);
	JointKey key2 = new JointKey(bprint, JointKey.KeyType.B,end);
	entity1.addKey(key1,blockn1);
	entity2.addKey(key2,blockn2);


}

public void registerJoint(JointDef def){
	Vector2 vA = null, vB = null;
	if(def instanceof RevoluteJointDef){
		RevoluteJointDef rdef = (RevoluteJointDef)def;
		vA = rdef.localAnchorA.cpy().mul(32);
		vB = rdef.localAnchorB.cpy().mul(32);
	} else if (def instanceof WeldJointDef){
		WeldJointDef rdef = (WeldJointDef)def;
		vA = rdef.localAnchorA.cpy().mul(32);
		vB = rdef.localAnchorB.cpy().mul(32);

	} else if(def instanceof PrismaticJointDef){
		PrismaticJointDef rdef = (PrismaticJointDef)def;
		vA = rdef.localAnchorA.cpy().mul(32);
		vB = rdef.localAnchorB.cpy().mul(32);
	}else if(def instanceof DistanceJointDef){
		DistanceJointDef rdef = (DistanceJointDef)def;
		vA = rdef.localAnchorA.cpy().mul(32);
		vB = rdef.localAnchorB.cpy().mul(32);
	}

	DCGameEntity entity1 = (DCGameEntity) def.bodyA.getUserData();
	DCGameEntity entity2 = (DCGameEntity) def.bodyB.getUserData();
	JointBluePrint bprint = new JointBluePrint(def, JointBluePrint.Type.Punctual);
	JointKey key1 = new JointKey(bprint, JointKey.KeyType.A,vA);
	JointKey key2 = new JointKey(bprint, JointKey.KeyType.B,vB);
	entity1.addKey(key1,0);
	entity2.addKey(key2,0);

}
public void createJoint(JointBluePrint bp){
	BasicFactory.createJointAndRegister(bp.definition, this);

}

public void registerJoint(JointDef def, Vector2 lA1, Vector2 lA2,Vector2 lB1,Vector2 lB2){


	DCGameEntity entity1 = (DCGameEntity) def.bodyA.getUserData();
	DCGameEntity entity2 = (DCGameEntity) def.bodyB.getUserData();
	JointBluePrint bprint = new JointBluePrint(def, JointBluePrint.Type.Continuous);
	JointKey key1 = new JointKey(bprint, JointKey.KeyType.A,lA1);
    JointKey key2 = new JointKey(bprint, JointKey.KeyType.B,lB1);

	entity1.addKey(key1,0);
	entity2.addKey(key2,0);


	JointBluePrint cbprint = bprint.split();
    joints.add(cbprint);

	JointKey ckey1 = new JointKey(cbprint, JointKey.KeyType.A,lA2);
	JointKey ckey2 = new JointKey(cbprint, JointKey.KeyType.B,lB2);
    entity1.addKey(ckey1,0);
	entity2.addKey(ckey2,0);
	
	
}

public void createJoint(JointIndicator ind) {
    createJoint(ind.begin.toVector(), ind.end.toVector(), ind.getProperty());

	
}






}
