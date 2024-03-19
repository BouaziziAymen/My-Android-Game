package com.evolgames.entities.factories;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.evolgames.entities.basics.Material;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.utilities.BlockUtils;

import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import java.util.ArrayList;
import java.util.List;

public class BodyFactory {
    private static final BodyFactory INSTANCE = new BodyFactory();
    private PhysicsWorld physicsWorld;

    public static BodyFactory getInstance() {
        return INSTANCE;
    }

    public void create(PhysicsWorld physicsWorld) {
        this.physicsWorld = physicsWorld;
    }

    public Body createBoxBody(float x, float y, float w, float h, BodyDef.BodyType type) {
        FixtureDef fixtureDef = new FixtureDef();
        Material flesh = MaterialFactory.getInstance().getMaterialByIndex(MaterialFactory.FLESH);
        fixtureDef.density = flesh.getDensity();
        fixtureDef.friction = flesh.getFriction();
        fixtureDef.restitution = flesh.getRestitution();
        fixtureDef.filter.maskBits = 0x0;
        return PhysicsFactory.createBoxBody(physicsWorld, x, y, w, h, 0, type, fixtureDef);
    }

    public Body createBody(List<LayerBlock> blocks, BodyDef.BodyType type) {

        Body body = physicsWorld.createBody(new BodyDef());
        body.setType(type);
        for (LayerBlock block : blocks) {
            createFixtures(block, body);
        }

        return body;
    }

    public void createFixtures(LayerBlock block, Body body) {
        float density = block.getProperties().getDensity();
        float elasticity = block.getProperties().getRestitution();
        float friction = block.getProperties().getFriction();
        ArrayList<Vector2> rVertices = BlockUtils.bodyVertices(block);

        ArrayList<ArrayList<Vector2>> bodyVertices = BlockUtils.decompose(rVertices);

        for (ArrayList<Vector2> fixtureVertices : bodyVertices) {
            FixtureDef fixtureDef = PhysicsFactory.createFixtureDef(density, elasticity, friction);

            PolygonShape fixtureShape = new PolygonShape();

            Vector2[] points = fixtureVertices.toArray(new Vector2[0]);
            fixtureShape.set(points);
            fixtureDef.shape = fixtureShape;
            Fixture fixture = body.createFixture(fixtureDef);
            fixture.setUserData(block);
        }
    }
}
