package com.evolgames.dollmutilationgame.entities.usage;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.evolgames.dollmutilationgame.entities.hand.Hand;
import com.evolgames.dollmutilationgame.entities.hand.PlayerSpecialAction;
import com.evolgames.dollmutilationgame.physics.CollisionUtils;
import com.evolgames.dollmutilationgame.physics.WorldFacade;
import com.evolgames.dollmutilationgame.scenes.PhysicsScene;
import com.evolgames.dollmutilationgame.utilities.GeometryUtils;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.entities.commandtemplate.Invoker;

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
        body.setBullet(true);
        body.getFixtureList().forEach(fixture -> {
            Filter data = fixture.getFilterData();
            data.groupIndex = CollisionUtils.THROWN_ENTITY_GROUP_INDEX;
            fixture.setFilterData(data);
        });
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
