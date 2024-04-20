package com.evolgames.entities.usage;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.commandtemplate.Invoker;
import com.evolgames.entities.hand.Hand;
import com.evolgames.entities.hand.PlayerSpecialAction;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.PhysicsScene;
import com.evolgames.utilities.GeometryUtils;

import java.util.Collections;
import java.util.List;

public class Throw extends Use {

    private float angle;
    private float throwSpeed;

    @Override
    public void onStep(float deltaTime, WorldFacade worldFacade) {
    }

    public void processThrow(Hand hand) {
        Body body = hand.getGrabbedEntity().getBody();
        body.setFixedRotation(false);
        Invoker.addJointDestructionCommand(hand.getGrabbedEntity().getParentGroup(), hand.getMouseJoint());
        Vector2 u = new Vector2(0, 1);
        GeometryUtils.rotateVectorDeg(u, angle);
        body.setLinearVelocity(throwSpeed * u.x, throwSpeed * u.y);
        Projectile projectile = new Projectile(ProjectileType.SHARP_WEAPON);
        hand.getGrabbedEntity().getUseList().add(projectile);
        projectile.setActive(true);
        hand.clearStack();
    }

    @Override
    public List<PlayerSpecialAction> getActions() {
        return Collections.singletonList(PlayerSpecialAction.Throw);
    }

    @Override
    public void dynamicMirror(PhysicsScene physicsScene) {

    }

    public void reset(float angle, float speed) {
        this.angle = angle;
        this.throwSpeed = speed;
    }

    @Override
    public boolean inheritedBy(GameEntity biggestSplinter, float ratio) {
        return !(ratio < 0.4f);
    }
}
