package com.evolgames.physics.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.evolgames.entities.EntityWithBody;
import com.evolgames.entities.GameEntity;
import com.evolgames.helpers.utilities.Utils;

import java.util.concurrent.atomic.AtomicInteger;

public class JointBlueprint {

    private static AtomicInteger jointCounter = new AtomicInteger();
    private JointDef jointDef;
    private Joint joint;
    private int jointId;
    private Vector2 advance;
    private boolean dead;
    private boolean connected;
    private EntityWithBody entity1;
    private EntityWithBody entity2;
    private boolean created = false;

    public JointBlueprint(JointDef jointDef, EntityWithBody entity1, EntityWithBody entity2, boolean connected) {
        this.jointDef = jointDef;
        this.entity1 = entity1;
        this.entity2 = entity2;
        this.jointId = jointCounter.getAndIncrement();
        this.connected = connected;
    }


    public Joint getJoint() {
        return joint;
    }

    public void setJoint(Joint joint) {
        this.joint = joint;
    }

    public void update() {

        jointDef.bodyA = entity1.getBody();
        jointDef.bodyB = entity2.getBody();

    }

    public JointDef getJointDef() {
        return jointDef;
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean b) {
        created = b;
    }

    public EntityWithBody getEntity1() {
        return entity1;
    }

    public void setEntity1(EntityWithBody gameEntity) {
        entity1 = gameEntity;
    }

    public EntityWithBody getEntity2() {
        return entity2;
    }

    public void setEntity2(EntityWithBody gameEntity) {
        entity2 = gameEntity;
    }

    public boolean isValid() {
        return (jointDef.bodyA != null && jointDef.bodyB != null);
    }


    public int getId() {

        return jointId;
    }

    public Vector2 getAdvance() {
        return advance;
    }

    public void setAdvance(Vector2 advance) {
        this.advance = advance;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {

        this.dead = dead;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
