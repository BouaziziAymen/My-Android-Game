package com.evolgames.entities.commandtemplate.commands;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.commandtemplate.Invoker;
import com.evolgames.entities.factories.BodyFactory;
import com.evolgames.entities.init.BodyInit;

import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsWorld;

public class BodyCreationCommand extends Command {
    public static final float DEFAULT_DAMPING = 0.1f;
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
        Body body = BodyFactory.getInstance().createBody(entity.getBlocks(), type);
        PhysicsConnector physicsConnector = new PhysicsConnector(entity.getMesh(), body);
        PhysicsWorld physicsWorld = Invoker.scene.getPhysicsWorld();
        physicsWorld.registerPhysicsConnector(physicsConnector);
        entity.setBody(body);
        entity.setVisible(true);
        Invoker.scene.attachChild(entity.getMesh());
        body.setLinearDamping(DEFAULT_DAMPING);
        body.setAngularDamping(DEFAULT_DAMPING);
        bodyInit.initialize(body);
        entity.createJuiceSources();
        if(entity.isOutlined()){
            entity.outlineEntity();
        }
    }

    public GameEntity getGameEntity() {
        return entity;
    }
}
