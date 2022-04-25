package is.kul.learningandengine.graphicelements.ui;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

public class JointProperty {
	public enum Joint_Type {
		REVOLUTE,WELD,DISTANCE,PRISMATIC
		}
public JointProperty.Joint_Type type;
public boolean hasMotor;
public int ID, bodyA_ID,bodyB_ID;
public boolean collideConnected;
public float lower_limit;
public float upper_limit;
public float Rotation_Torque;
public float Rotation_Speed;
public boolean hasLimits;
public float frequency;
public float damping;
public Vector2 axe;
	public JointProperty(int ID,boolean hasMotor,float Nm, float Tm,boolean hasLimits, float l1, float l2, boolean connected, int ID1, int ID2) {
	this.ID  = ID;
        this.type = JointProperty.Joint_Type.REVOLUTE;
        collideConnected = connected;
	this.hasMotor = hasMotor;
        lower_limit = l1;
        upper_limit = l2;
        Rotation_Torque = Tm;
        Rotation_Speed = Nm;
	this.hasLimits = hasLimits;
        bodyA_ID = ID1;
        bodyB_ID = ID2;
	}

	public JointProperty(int ID,boolean hasMotor,float Nm, float Tm,boolean hasLimits, float lower_limit, float upper_limit, boolean connected, int ID1, int ID2, Vector2 axe) {
	this.ID  = ID;
        this.type = JointProperty.Joint_Type.PRISMATIC;
        collideConnected = connected;
	this.hasMotor = hasMotor;
        this.lower_limit = lower_limit;
        this.upper_limit = upper_limit;
        Rotation_Torque = Tm;
        Rotation_Speed = Nm;
	this.hasLimits = hasLimits;
        bodyA_ID = ID1;
        bodyB_ID = ID2;
	this.axe = axe;
	}


	public JointProperty(int ID, int ID1, int ID2) {
	this.ID  = ID;
        this.type = JointProperty.Joint_Type.WELD;


        bodyA_ID = ID1;
        bodyB_ID = ID2;
	}


	public JointProperty(int ID,float frequency, float damping,boolean collision,  int ID1, int ID2) {
	this.ID  = ID;
        this.type = JointProperty.Joint_Type.DISTANCE;
this.frequency = frequency;
this.damping=damping;
        collideConnected = collision;
        bodyA_ID = ID1;
        bodyB_ID = ID2;
	}
}


