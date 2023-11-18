package com.evolgames.entities.serialization;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.evolgames.entities.blocks.JointBlock;
import com.evolgames.entities.commandtemplate.commands.JointCreationCommand;

public class JointInfo {
  transient private JointBlock jointBlock;
  private JointDef.JointType jointType;
  private Vector2 anchorA;
  private Vector2 anchorB;
  private boolean collideConnected;
  // distance
  private float length;
  private float frequencyHz;
  private float dampingRatio;
  // mouse
  private Vector2 target;
  private float maxForce;
  // weld
  private float referenceAngle;
  // revolute
  private boolean enableLimit;
  private float lowerAngle;
  private float upperAngle;
  private boolean enableMotor;
  private float motorSpeed;
  private float maxMotorTorque;
  // prismatic
  private float lowerTranslation;
  private float upperTranslation;
  private float maxMotorForce;
  private String entity1;
  private String entity2;

  public void setJointBlock(JointBlock jointBlock) {
    this.jointBlock = jointBlock;
  }

  public String getEntity1() {
    return entity1;
  }

  public String getEntity2() {
    return entity2;
  }

  public JointDef getJointDef() {
    switch (jointType) {
      case Unknown:
        break;
      case RevoluteJoint:
        RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.localAnchorA.set(this.anchorA);
        revoluteJointDef.localAnchorB.set(this.anchorB);
        revoluteJointDef.collideConnected = collideConnected;
        revoluteJointDef.upperAngle = upperAngle;
        revoluteJointDef.lowerAngle = lowerAngle;
        revoluteJointDef.enableLimit = enableLimit;
        revoluteJointDef.motorSpeed = motorSpeed;
        revoluteJointDef.maxMotorTorque = maxMotorTorque;
        revoluteJointDef.enableMotor = enableMotor;
        revoluteJointDef.referenceAngle = referenceAngle;
        return revoluteJointDef;
      case PrismaticJoint:
        PrismaticJointDef prismaticJointDef = new PrismaticJointDef();
        prismaticJointDef.localAnchorA.set(this.anchorA);
        prismaticJointDef.localAnchorB.set(this.anchorB);
        prismaticJointDef.collideConnected = collideConnected;
        prismaticJointDef.lowerTranslation = lowerTranslation;
        prismaticJointDef.upperTranslation = upperTranslation;
        prismaticJointDef.enableLimit = enableLimit;
        prismaticJointDef.motorSpeed = motorSpeed;
        prismaticJointDef.maxMotorForce = maxMotorForce;
        prismaticJointDef.enableMotor = enableMotor;
        prismaticJointDef.referenceAngle = referenceAngle;
        break;
      case DistanceJoint:
        break;
      case PulleyJoint:
        break;
      case MouseJoint:
        MouseJointDef mouseJointDef = new MouseJointDef();
        mouseJointDef.target.set(target);
        mouseJointDef.collideConnected = collideConnected;
        mouseJointDef.dampingRatio = dampingRatio;
        mouseJointDef.frequencyHz = frequencyHz;
        mouseJointDef.maxForce = maxForce;
        return mouseJointDef;
      case GearJoint:
        break;
      case LineJoint:
        break;
      case WeldJoint:
        WeldJointDef weldJointDef = new WeldJointDef();
        weldJointDef.referenceAngle = referenceAngle;
        weldJointDef.collideConnected = collideConnected;
        weldJointDef.localAnchorA.set(this.anchorA);
        weldJointDef.localAnchorB.set(this.anchorB);
        return weldJointDef;
      case FrictionJoint:
        break;
    }
    return null;
  }

  public void prepareJointInfo() {
    this.entity1 = this.jointBlock.getCommand().getEntity1().getUniqueID();
    this.entity2 = this.jointBlock.getCommand().getEntity2().getUniqueID();
    this.jointType = this.jointBlock.getJointType();
    switch (this.jointBlock.getJointType()) {
      case Unknown:
        break;
      case RevoluteJoint:
        RevoluteJointDef revoluteJointDef = (RevoluteJointDef) this.jointBlock.getCommand().getJointDef();
        this.anchorA = revoluteJointDef.localAnchorA;
        this.anchorB = revoluteJointDef.localAnchorB;
        this.enableLimit = revoluteJointDef.enableLimit;
        this.lowerAngle = revoluteJointDef.lowerAngle;
        this.upperAngle = revoluteJointDef.upperAngle;
        this.referenceAngle = revoluteJointDef.referenceAngle;
        this.collideConnected = revoluteJointDef.collideConnected;
        this.enableMotor = revoluteJointDef.enableMotor;
        this.motorSpeed = revoluteJointDef.motorSpeed;
        this.maxMotorTorque = revoluteJointDef.maxMotorTorque;
        break;
      case PrismaticJoint:
        PrismaticJointDef prismaticJointDef = (PrismaticJointDef) this.jointBlock.getCommand().getJointDef();
        this.anchorA = prismaticJointDef.localAnchorA;
          this.anchorB= prismaticJointDef.localAnchorB;
        this.enableLimit = prismaticJointDef.enableLimit;
        this.lowerTranslation = prismaticJointDef.lowerTranslation;
        this.upperTranslation = prismaticJointDef.upperTranslation;
        this.referenceAngle = prismaticJointDef.referenceAngle;
        this.collideConnected = prismaticJointDef.collideConnected;
        this.enableMotor = prismaticJointDef.enableMotor;
        this.motorSpeed = prismaticJointDef.motorSpeed;
        this.maxMotorForce = prismaticJointDef.maxMotorForce;
        break;
      case DistanceJoint:
        break;
      case PulleyJoint:
        break;
      case MouseJoint:
        MouseJointDef mouseJointDef = (MouseJointDef) this.jointBlock.getCommand().getJointDef();
        this.collideConnected=mouseJointDef.collideConnected;
        this.target = this.jointBlock.getCommand().getJoint()!=null?((MouseJoint)this.jointBlock.getCommand().getJoint()).getTarget():mouseJointDef.target;
        this.dampingRatio = mouseJointDef.dampingRatio;
        this.frequencyHz = mouseJointDef.frequencyHz;
        this.maxForce = mouseJointDef.maxForce;
        break;
      case GearJoint:
        break;
      case LineJoint:
        break;
      case WeldJoint:
        WeldJointDef weldJointDef = (WeldJointDef) this.jointBlock.getCommand().getJointDef();
        this.collideConnected=weldJointDef.collideConnected;
        this.referenceAngle = weldJointDef.referenceAngle;
        this.anchorA = weldJointDef.localAnchorA;
        this.anchorB= weldJointDef.localAnchorB;
        break;
      case FrictionJoint:
        break;
    }
  }

}
