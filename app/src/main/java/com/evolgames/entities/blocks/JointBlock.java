package com.evolgames.entities.blocks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.commandtemplate.commands.JointCreationCommand;
import com.evolgames.entities.cut.Cut;
import com.evolgames.entities.properties.JointProperties;
import com.evolgames.entities.properties.Properties;
import com.evolgames.entities.serialization.JointInfo;
import com.evolgames.helpers.utilities.Utils;
import java.util.ArrayList;

public class JointBlock extends AssociatedBlock<JointBlock, JointProperties> {

  private JointDef.JointType jointType;
  private transient JointCreationCommand command;
  private JointInfo jointInfo;
  private Position position;
  private String jointUniqueId;

  public void recreate(GameEntity newEntity) {
    this.command.substitute(newEntity, position);
    this.command.setAborted(false);
  }

  public void initialization(
      JointCreationCommand jointCreationCommand,
      String jointUniqueId,
      JointDef.JointType jointType,
      ArrayList<Vector2> vertices,
      Properties properties,
      int id,
      Position position) {
    super.initialization(vertices, properties, id);
    this.position = position;
    this.jointType = jointType;
    this.command = jointCreationCommand;
    this.jointUniqueId = jointUniqueId;
    this.jointInfo = new JointInfo();
    this.jointInfo.setJointBlock(this);
  }



  @Override
  protected boolean shouldRectify() {
    return false;
  }

  @Override
  protected void calculateArea() {}

  @Override
  protected boolean shouldCalculateArea() {
    return false;
  }

  @Override
  protected JointBlock createChildBlock() {
    return new JointBlock();
  }

  @Override
  protected boolean shouldArrangeVertices() {
    return false;
  }

  @Override
  protected boolean shouldCheckShape() {
    return false;
  }

  @Override
  protected JointBlock getThis() {
    return this;
  }

  public JointCreationCommand getCommand() {
    return command;
  }

  public JointDef.JointType getJointType() {
    return jointType;
  }

  @Override
  public void performCut(Cut cut) {}

  @Override
  public void translate(Vector2 translationVector) {
    Utils.translatePoints(this.getVertices(), translationVector);
    command.updateAnchor(getVertices().get(0), position);
  }

  public String getJointUniqueId() {
    return jointUniqueId;
  }

  public JointInfo getJointInfo() {
    return jointInfo;
  }

  public void setCommand(JointCreationCommand command) {
    this.command = command;
  }

  public enum Position {
    A,
    B
  }
}
