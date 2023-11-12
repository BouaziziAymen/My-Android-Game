package com.evolgames.userinterface.model.jointmodels;

import com.badlogic.gdx.physics.box2d.JointDef;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.view.shapes.indicators.jointindicators.JointShape;

public class JointModel {
  private final int jointId;
  private final String jointName;
  private JointDef jointDef;
  private JointShape jointShape;
  private boolean selected;
  private BodyModel bodyModel1;
  private BodyModel bodyModel2;

  public JointModel(int jointId, JointDef jointDef, JointShape jointShape) {
    this.jointShape = jointShape;
    this.jointId = jointId;
    this.jointDef = jointDef;
    this.jointName = "Joint" + jointId;
  }

  public JointModel(int jointId, JointDef jointDef) {
    this.jointId = jointId;
    this.jointDef = jointDef;
    this.jointName = "Joint" + jointId;
  }

  public BodyModel getBodyModel1() {
    return bodyModel1;
  }

  public void setBodyModel1(BodyModel bodyModel1) {
    this.bodyModel1 = bodyModel1;
  }

  public BodyModel getBodyModel2() {
    return bodyModel2;
  }

  public void setBodyModel2(BodyModel bodyModel2) {
    this.bodyModel2 = bodyModel2;
  }

  public JointDef getJointDef() {
    return jointDef;
  }

  public void setJointDef(JointDef jointDef) {
    this.jointDef = jointDef;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  public JointDef.JointType getJointType() {
    return jointDef.type;
  }

  public JointShape getJointShape() {
    return jointShape;
  }

  public void setJointShape(JointShape jointShape) {
    this.jointShape = jointShape;
  }

  public int getJointId() {
    return jointId;
  }

  public void selectJoint() {
    setSelected(true);
    jointShape.select();
  }

  public void deselect() {
    setSelected(false);
    if (jointShape != null) {
      jointShape.release();
    }
  }

  public String getJointName() {
    return jointName;
  }
}
