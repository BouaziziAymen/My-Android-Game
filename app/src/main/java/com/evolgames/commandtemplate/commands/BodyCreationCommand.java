package com.evolgames.commandtemplate.commands;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.evolgames.commandtemplate.Invoker;
import com.evolgames.entities.GameEntity;
import com.evolgames.factories.BodyFactory;

import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsWorld;

public class BodyCreationCommand extends Command {
    private final GameEntity entity;
    private final BodyDef.BodyType type;
    private final float x;
    private final float y;
    private final float rotation;
    private final Vector2 linearVelocity;
    private final float angularVelocity;
    private final boolean isBullet;
    private final Filter filter;

    public BodyCreationCommand(GameEntity entity, BodyDef.BodyType type, float x, float y, float rot, Vector2 linearVelocity, float angularVelocity, boolean isBullet, Filter filter) {
        this.entity = entity;
        this.type = type;
        this.x = x;
        this.y = y;
        this.rotation = rot;
        this.linearVelocity = linearVelocity;
        this.angularVelocity = angularVelocity;
        this.isBullet = isBullet;
        this.filter = filter;

    }

    @Override
    protected void run() {
        Body body = BodyFactory.getInstance().createBody(entity.getBlocks(), type);
        body.setBullet(isBullet);
        PhysicsConnector physicsConnector = new PhysicsConnector(entity.getMesh(), body);
        PhysicsWorld physicsWorld = Invoker.gameScene.getPhysicsWorld();
        physicsWorld.registerPhysicsConnector(physicsConnector);
        entity.setBody(body);
        body.setTransform(x, y, rotation);
        entity.setVisible(true);

        if (linearVelocity != null) {
            body.setLinearVelocity(linearVelocity);
            body.setAngularVelocity(angularVelocity);
        }
        if(filter!=null) for(Fixture f:body.getFixtureList())f.setFilterData(filter);
    }

    public GameEntity getGameEntity() {
        return entity;
    }
}
