package is.kul.learningandengine.basics;

import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

public class JointBluePrint {
public JointBluePrint(JointDef def, JointBluePrint.Type type) {
    definition = def;
		this.type = type;

	}
enum Type{
	Continuous,Punctual
}



JointBluePrint.Type type;
JointDef definition;
JointBluePrint split(){
	RevoluteJointDef newdef = new RevoluteJointDef();
	newdef.bodyA = this.definition.bodyA;
	newdef.bodyB = this.definition.bodyB;
	newdef.localAnchorA.set(((RevoluteJointDef) this.definition).localAnchorA);
	newdef.localAnchorB.set(((RevoluteJointDef) this.definition).localAnchorB);
	newdef.lowerAngle = ((RevoluteJointDef) this.definition).lowerAngle;
	newdef.upperAngle = ((RevoluteJointDef) this.definition).upperAngle;
	newdef.enableLimit = ((RevoluteJointDef) this.definition).enableLimit;
return new JointBluePrint(newdef,JointBluePrint.Type.Continuous);
}

}
