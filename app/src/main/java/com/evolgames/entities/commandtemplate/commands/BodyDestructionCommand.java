package com.evolgames.entities.commandtemplate.commands;

import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.blocks.JointBlock;
import com.evolgames.entities.commandtemplate.Invoker;
import org.andengine.extension.physics.box2d.PhysicsWorld;

public class BodyDestructionCommand extends Command {
  private final GameEntity entity;

  public BodyDestructionCommand(GameEntity entity) {
    this.entity = entity;
  }

  @Override
  protected void run() {
    PhysicsWorld physicsWorld = Invoker.scene.getPhysicsWorld();
    entity
        .getBody()
        .getJointList()
        .forEach(
            jointEdge -> {
              Joint joint = jointEdge.joint;
              JointBlock jointBlock = (JointBlock) joint.getUserData();
              if (jointBlock.getJointType() == JointDef.JointType.MouseJoint) {
                Invoker.scene.onDestroyMouseJoint((MouseJoint) joint);
              }
            });
    physicsWorld.destroyBody(entity.getBody());
    entity.setBody(null);
    entity.detach();
  }

  @Override
  protected boolean isReady() {
    return entity.getBody() != null;
  }

  public GameEntity getGameEntity() {
    return entity;
  }
}
