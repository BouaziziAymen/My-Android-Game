package com.evolgames.entities.ragdoll;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.evolgames.entities.basics.EntityWithBody;
import com.evolgames.entities.basics.GameEntity;

import org.andengine.util.system.SystemUtils;

public class Balancer {

    private final GameEntity entity;
    private final float limit;
    private final float angle;

    public Balancer(GameEntity entity, float limit, float angle) {
        this.entity = entity;
        this.limit = limit;
        this.angle = angle;
    }

    public GameEntity getEntity() {
        return entity;
    }

    public void equilibrate() {
        Body body = entity.getBody();
        float rot = body.getAngle();
        float error = angle - rot;

        while (error < -Math.PI) error += 2 * Math.PI;
        while (error > Math.PI) error -= 2 * Math.PI;

        if (Math.abs(error) < limit) {
            boolean problem = false;
            for (JointEdge jointEdge : body.getJointList()) {
                float reat = jointEdge.joint.getReactionTorque(60f);
                if (reat > 100) {
                    problem = true;
                }
            }
            if(!problem)
            body.setAngularVelocity(100 * Math.signum(error) * error * error / limit);
        }
    }
}
