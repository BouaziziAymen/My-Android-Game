package com.evolgames.entities.commandtemplate.commands;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.commandtemplate.Invoker;
import com.evolgames.entities.factories.BodyFactory;
import com.evolgames.entities.init.BodyInit;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsWorld;

public class BodyCreationCommand extends Command {
  private final GameEntity entity;
  private final BodyDef.BodyType type;
  private final BodyInit bodyInit;

  public BodyCreationCommand(GameEntity entity, BodyDef.BodyType type, BodyInit bodyInit) {
    this.entity = entity;
    this.type = type;
    this.bodyInit = bodyInit;
  }

  @Override
  protected void run() {
    Log.e("Mirror","Begin create body entity:"+entity);
    Body body = BodyFactory.getInstance().createBody(entity.getBlocks(), type);
    PhysicsConnector physicsConnector = new PhysicsConnector(entity.getMesh(), body);
    PhysicsWorld physicsWorld = Invoker.scene.getPhysicsWorld();
    physicsWorld.registerPhysicsConnector(physicsConnector);
    entity.setBody(body);
    entity.setVisible(true);
    bodyInit.initialize(body);
    entity.createJuiceSources();
    body.setLinearDamping(0.1f);
    body.setAngularDamping(0.1f);
  }

  public GameEntity getGameEntity() {
    return entity;
  }
}
