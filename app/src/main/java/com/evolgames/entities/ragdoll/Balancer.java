package com.evolgames.entities.ragdoll;

import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.entities.EntityWithBody;

public class Balancer {

    private EntityWithBody entity;
    private float limit;
    private float angle;

    public Balancer(EntityWithBody entity, float limit, float angle) {
        this.entity = entity;
        this.limit = limit;
        this.angle = angle;
    }



    public EntityWithBody getEntity() {
        return entity;
    }

    public void equilibrate(){
        Body body = entity.getBody();
        float rot = body.getAngle();
        float error = angle - rot;

        while (error < -Math.PI) error += 2 * Math.PI;
        while (error > Math.PI) error -= 2 * Math.PI;

        if (Math.abs(error) < limit) {
                body.setAngularVelocity(15*error / limit);
        }
    }
}
