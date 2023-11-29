package com.evolgames.entities.commandtemplate.commands;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.JointBlock;
import com.evolgames.entities.commandtemplate.Invoker;
import java.util.concurrent.atomic.AtomicBoolean;
import org.andengine.extension.physics.box2d.PhysicsWorld;

public class JointCreationCommand extends Command {

  private final JointDef jointDef;
  private final GameEntity entity1;
  private final GameEntity entity2;
  private final JointBlock mainBlock;

  public JointCreationCommand(
          JointDef jointDef, GameEntity entity1, GameEntity entity2, JointBlock main) {
    this.jointDef = jointDef;
    this.entity1 = entity1;
    this.entity2 = entity2;
    this.mainBlock = main;
  }


  @Override
  protected void run() {
    PhysicsWorld physicsWorld = Invoker.scene.getPhysicsWorld();
    Joint joint = physicsWorld.createJoint(jointDef);
    joint.setUserData(this.mainBlock);
    if (joint instanceof MouseJoint) {
      Invoker.scene.setMouseJoint((MouseJoint) joint, entity2, (MouseJointDef) jointDef);
    }
  }

  @Override
  protected boolean isReady() {
    jointDef.bodyA = entity1.getBody();
    jointDef.bodyB = entity2.getBody();
    return isBodyAlive(jointDef.bodyA) && isBodyAlive(jointDef.bodyB);
  }

  public JointDef getJointDef() {
    return jointDef;
  }

  private boolean isBodyAlive(Body body) {
    if (body == null) {
      return false;
    }
    AtomicBoolean result = new AtomicBoolean(false);
    Invoker.scene
        .getPhysicsWorld()
        .getBodies()
        .forEachRemaining(
            body1 -> {
              if (body1 == body) {
                result.set(true);
              }
            });
    return result.get();
  }

}
