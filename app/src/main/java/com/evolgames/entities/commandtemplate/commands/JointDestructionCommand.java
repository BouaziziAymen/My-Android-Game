package com.evolgames.entities.commandtemplate.commands;

import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.JointBlock;
import com.evolgames.entities.commandtemplate.Invoker;
import org.andengine.extension.physics.box2d.PhysicsWorld;

public class JointDestructionCommand extends Command {
  private final Joint joint;

  public JointDestructionCommand(Joint joint) {
    this.joint = joint;
  }

  @Override
  protected void run() {
    PhysicsWorld physicsWorld = Invoker.scene.getPhysicsWorld();
    physicsWorld
        .getJoints()
        .forEachRemaining(
            j -> {
              if (j == joint) {
                physicsWorld.destroyJoint(joint);
                if (this.joint instanceof MouseJoint) {
                  GameEntity entity = (GameEntity) this.joint.getBodyB().getUserData();
                  entity
                      .getBlocks()
                      .forEach(
                          b ->
                              b.getAssociatedBlocks()
                                  .removeIf(
                                      e -> {
                                        if (e instanceof JointBlock) {
                                          JointBlock jointBlock = (JointBlock) e;
                                          if (jointBlock.getCommand() != null) {
                                            return joint == jointBlock.getCommand().getJoint();
                                          }
                                        }
                                        return false;
                                      }));

                  Invoker.scene.onDestroyMouseJoint((MouseJoint) joint);
                }
              }
            });
  }

  @Override
  protected boolean isReady() {
    return joint != null;
  }
}
