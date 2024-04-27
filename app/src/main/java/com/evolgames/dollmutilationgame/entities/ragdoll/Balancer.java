package com.evolgames.dollmutilationgame.entities.ragdoll;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;

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
            if(!problem) {
                body.setAngularVelocity(100 * Math.signum(error) * error * error / limit);
            }
        }
    }
}
