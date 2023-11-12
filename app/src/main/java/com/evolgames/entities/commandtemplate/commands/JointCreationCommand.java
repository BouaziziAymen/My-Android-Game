package com.evolgames.entities.commandtemplate.commands;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.JointBlock;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.commandtemplate.Invoker;
import com.evolgames.entities.properties.JointProperties;
import com.evolgames.helpers.utilities.BlockUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import org.andengine.extension.physics.box2d.PhysicsWorld;

public class JointCreationCommand extends Command {

  private final JointDef jointDef;
  private GameEntity entity1;
  private GameEntity entity2;
  private Joint joint;
  private JointBlock jointBlock1;
  private JointBlock jointBlock2;

  public JointCreationCommand(
          JointDef jointDef, GameEntity entity1, GameEntity entity2,JointBlock jointBlock1, JointBlock jointBlock2) {
    this.jointDef = jointDef;
    this.entity1 = entity1;
    this.entity2 = entity2;
    this.jointBlock1 = jointBlock1;
    this.jointBlock2 = jointBlock2;
  }


  @Override
  protected void run() {
    PhysicsWorld physicsWorld = Invoker.scene.getPhysicsWorld();
    if (this.joint != null && this.joint instanceof MouseJoint) {
      MouseJointDef mouseJointDef = (MouseJointDef) this.jointDef;
      Vector2 point =
          mouseJointDef
              .bodyB
              .getWorldPoint(jointBlock2.getVertices().get(0).cpy().mul(1 / 32f))
              .cpy();
      mouseJointDef.target.set(point);
    }
    if (jointDef.type == JointDef.JointType.WeldJoint) {
      List<GameEntity> list =
          entity1.getParentGroup().getCommands().stream()
              .filter(e -> e instanceof JointCreationCommand)
              .map(e -> (JointCreationCommand) e)
              .filter(e -> e.entity1 == entity1 || e.entity2 == entity1)
              .map(e -> e.entity1 == entity1 ? e.entity2 : e.entity1)
              .collect(Collectors.toList());
      for (GameEntity gameEntity : list) {
        Invoker.scene.getWorldFacade().addNonCollidingPair(entity2, gameEntity);
      }
    }
    this.joint = physicsWorld.createJoint(jointDef);
    if (this.joint instanceof MouseJoint) {
      Invoker.scene.setMouseJoint((MouseJoint) joint, entity2);
    }
    if (jointBlock1==null) {
      addJointBlocks();
    }
    setAborted(true);
  }

  private void addJointBlocks() {
    jointBlock1 = new JointBlock();
    jointBlock2 = new JointBlock();
    Vector2 anchorA = null;
    Vector2 anchorB = null;

    switch (jointDef.type) {
      case Unknown:
      case GearJoint:
      case MouseJoint:
        MouseJointDef mouseJointDef = ((MouseJointDef) jointDef);
        anchorB = jointDef.bodyB.getLocalPoint(mouseJointDef.target).cpy().mul(32);
      case LineJoint:
      case PulleyJoint:
      case DistanceJoint:
      case FrictionJoint:
        break;
      case RevoluteJoint:
        Log.e("Joint","Create blocks: "+((RevoluteJointDef) jointDef).localAnchorA+","+((RevoluteJointDef) jointDef).localAnchorB+" "+this);
        anchorA = ((RevoluteJointDef) jointDef).localAnchorA.cpy().mul(32f);
        anchorB = ((RevoluteJointDef) jointDef).localAnchorB.cpy().mul(32f);
        break;
      case PrismaticJoint:
        anchorA = ((PrismaticJointDef) jointDef).localAnchorA.cpy().mul(32f);
        anchorB = ((PrismaticJointDef) jointDef).localAnchorB.cpy().mul(32f);
        break;
      case WeldJoint:
        anchorA = ((WeldJointDef) jointDef).localAnchorA.cpy().mul(32f);
        anchorB = ((WeldJointDef) jointDef).localAnchorB.cpy().mul(32f);
        break;
    }
    String jointUniqueId = UUID.randomUUID().toString();
    if (anchorA != null) {
      jointBlock1.initialization(
          this,
          jointUniqueId,
          jointDef.type,
          new ArrayList<>(Collections.singletonList(anchorA)),
          new JointProperties(jointDef),
          0,
          JointBlock.Position.A);
      LayerBlock layerBlock1 = BlockUtils.getNearestBlock(anchorA, entity1.getBlocks());
     layerBlock1.addAssociatedBlock(jointBlock1);
    }
    if (anchorB != null) {
      jointBlock2.initialization(
          this,
          jointUniqueId,
          jointDef.type,
          new ArrayList<>(Collections.singletonList(anchorB)),
          new JointProperties(jointDef),
          0,
          JointBlock.Position.B);
      LayerBlock layerBlock2 = BlockUtils.getNearestBlock(anchorB, entity2.getBlocks());
     layerBlock2.addAssociatedBlock(jointBlock2);
    }
  }

  @Override
  protected boolean isReady() {
    jointDef.bodyA = entity1.getBody();
    jointDef.bodyB = entity2.getBody();
    return isBodyAlive(jointDef.bodyA) && isBodyAlive(jointDef.bodyB);
  }

  public void substitute(GameEntity splinter, JointBlock jointBlock, JointBlock.Position position) {
    switch (position) {
      case A:
        entity1 = splinter;
        jointBlock1 = jointBlock;
        jointBlock1.setUniqueId1(splinter.getUniqueID());
        jointBlock2.setUniqueId1(splinter.getUniqueID());
        jointBlock1.setUniqueId2(entity2.getUniqueID());
        jointBlock2.setUniqueId2(entity2.getUniqueID());
        break;
      case B:
        entity2 = splinter;
        jointBlock2 = jointBlock;
        jointBlock1.setUniqueId1(entity1.getUniqueID());
        jointBlock2.setUniqueId1(entity1.getUniqueID());
        jointBlock1.setUniqueId2(splinter.getUniqueID());
        jointBlock2.setUniqueId2(splinter.getUniqueID());
        break;
    }
  }

  public void updateAnchor(Vector2 anchor, JointBlock.Position position) {
    switch (jointDef.type) {
      case Unknown:
        break;
      case RevoluteJoint:
        RevoluteJointDef revoluteJointDef = (RevoluteJointDef) jointDef;
        if (position == JointBlock.Position.A) {
          revoluteJointDef.localAnchorA.set(anchor.cpy().mul(1 / 32f));
        } else {
          revoluteJointDef.localAnchorB.set(anchor.cpy().mul(1 / 32f));
        }
        break;
      case PrismaticJoint:
        PrismaticJointDef prismaticJointDef = (PrismaticJointDef) jointDef;
        if (position == JointBlock.Position.A) {
          prismaticJointDef.localAnchorA.set(anchor.cpy().mul(1 / 32f));
        } else {
          prismaticJointDef.localAnchorB.set(anchor.cpy().mul(1 / 32f));
        }
        break;
      case DistanceJoint:
        break;
      case PulleyJoint:
        break;
      case MouseJoint:
        MouseJointDef mouseJointDef = (MouseJointDef) jointDef;
        mouseJointDef.target.set(((MouseJoint) this.joint).getTarget());
        break;
      case GearJoint:
        break;
      case LineJoint:
        break;
      case WeldJoint:
        WeldJointDef weldJointDef = (WeldJointDef) jointDef;
        if (position == JointBlock.Position.A) {
          weldJointDef.localAnchorA.set(anchor.cpy().mul(1 / 32f));
        } else {
          weldJointDef.localAnchorB.set(anchor.cpy().mul(1 / 32f));
        }
        break;
      case FrictionJoint:
        break;
    }
    jointBlock1.updateJointInfo();
    jointBlock2.updateJointInfo();
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

  public Joint getJoint() {
    return joint;
  }

  public GameEntity getEntity1() {
    return entity1;
  }

  public GameEntity getEntity2() {
    return entity2;
  }

  public void removeBlocks(){
    if (jointBlock1 != null) {
      entity1.getBlocks().forEach(b->b.getAssociatedBlocks().remove(jointBlock1));
    }
    if (jointBlock2 != null) {
      entity2.getBlocks().forEach(b->b.getAssociatedBlocks().remove(jointBlock2));
    }
  }


}
