package com.evolgames.entities.properties;

import com.badlogic.gdx.physics.box2d.JointDef;

public class JointProperties extends Properties {

  private JointDef jointDef;

  public JointProperties(){}

  public JointProperties(JointDef jointDef) {
    this.jointDef = jointDef;
  }

  public JointDef getJointDef() {
    return jointDef;
  }

  @Override
  public Properties copy() {
    return null;
  }

}
