package com.evolgames.entities.usage;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.scenes.GameScene;
import com.evolgames.scenes.PlayerSpecialAction;
import com.evolgames.scenes.Hand;

public class Throw extends Use{
    @Override
    public void onStep(float deltaTime) {}

    public void processThrow(GameScene gameScene,Hand hand, float angle, float speedRatio){
        float maxSpeed = 100f;
        float speed = speedRatio * maxSpeed;
        Body body = hand.getGrabbedEntity().getBody();
            body.setFixedRotation(false);
            gameScene.getPhysicsWorld().destroyJoint(hand.getMouseJoint());
            Vector2 u = new Vector2(0, 1);
            GeometryUtils.rotateVectorDeg(u, angle);
            body.setLinearVelocity(speed * u.x, speed* u.y);
            Projectile projectile = new Projectile();
            hand.getGrabbedEntity().getUseList().add(projectile);
            projectile.setActive(true);
            hand.onMouseJointDestroyed();
            hand.clearStack();
    }

    @Override
    public PlayerSpecialAction getAction() {
        return PlayerSpecialAction.Throw;
    }
}
