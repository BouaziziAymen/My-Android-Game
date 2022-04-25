package com.evolgames.entities.joint;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.evolgames.physics.entities.JointBlueprint;

public class JointKey {

    public JointBlueprint getJointPlan() {
        return command;
    }

    public KeyType getType() {
        return type;
    }

    public Vector2 getAnchor() {
        return anchor;
    }

    private JointBlueprint command;
private JointKey.KeyType type;
private Vector2 anchor;

public enum KeyType{
	A,B
}
public JointKey(JointBlueprint command, KeyType type, Vector2 anchor) {
    this.command = command;
    this.type = type;
    this.anchor = anchor;

}

public void translate(Vector2 translationToOrigin){
switch (command.getJointDef().type){
    case Unknown:
        break;
    case RevoluteJoint:
        RevoluteJointDef jointDef = (RevoluteJointDef) getJointPlan().getJointDef();
        if (getType() == JointKey.KeyType.A) {
            Vector2 anchor = jointDef.localAnchorA;
            jointDef.localAnchorA.set(anchor.x-translationToOrigin.x/32f,anchor.y-translationToOrigin.y/32f);
        }
        else if (getType() == JointKey.KeyType.B) {
            Vector2 anchor = jointDef.localAnchorB;
            jointDef.localAnchorB.set(anchor.x-translationToOrigin.x/32f,anchor.y-translationToOrigin.y/32f);
        }
        break;
    case PrismaticJoint:
        PrismaticJointDef prismaticJointDef = (PrismaticJointDef) getJointPlan().getJointDef();
        if (getType() == JointKey.KeyType.A) {
            Vector2 anchor = prismaticJointDef.localAnchorA;
            prismaticJointDef.localAnchorA.set(anchor.x-translationToOrigin.x/32f,anchor.y-translationToOrigin.y/32f);
        }
        else if (getType() == JointKey.KeyType.B) {
            Vector2 anchor = prismaticJointDef.localAnchorB;
            prismaticJointDef.localAnchorB.set(anchor.x-translationToOrigin.x/32f,anchor.y-translationToOrigin.y/32f);
        }
        break;
    case DistanceJoint:
        DistanceJointDef distanceJointDef = (DistanceJointDef) getJointPlan().getJointDef();
        if (getType() == JointKey.KeyType.A) {
            Vector2 anchor = distanceJointDef.localAnchorA;
            distanceJointDef.localAnchorA.set(anchor.x-translationToOrigin.x/32f,anchor.y-translationToOrigin.y/32f);
        }
        else if (getType() == JointKey.KeyType.B) {
            Vector2 anchor = distanceJointDef.localAnchorB;
            distanceJointDef.localAnchorB.set(anchor.x-translationToOrigin.x/32f,anchor.y-translationToOrigin.y/32f);
        }
        break;
    case PulleyJoint:
        break;
    case MouseJoint:
        MouseJointDef mouseJointDef = (MouseJointDef) getJointPlan().getJointDef();
        if (getType() == JointKey.KeyType.B) {
        Vector2 target = mouseJointDef.target;
            mouseJointDef.target.set(target.x-translationToOrigin.x/32f,target.y-translationToOrigin.y/32f);
    }
        break;
    case GearJoint:
        break;
    case LineJoint:
        break;
    case WeldJoint:
        WeldJointDef weldJointDef = (WeldJointDef) getJointPlan().getJointDef();
        if (getType() == JointKey.KeyType.A) {
            Vector2 anchor = weldJointDef.localAnchorA;
            weldJointDef.localAnchorA.set(anchor.x-translationToOrigin.x/32f,anchor.y-translationToOrigin.y/32f);
        }
        else if (getType() == JointKey.KeyType.B) {
            Vector2 anchor = weldJointDef.localAnchorB;
            weldJointDef.localAnchorB.set(anchor.x-translationToOrigin.x/32f,anchor.y-translationToOrigin.y/32f);
        }
        break;
    case FrictionJoint:
        break;
}
}





}
