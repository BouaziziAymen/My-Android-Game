package com.evolgames.entities.usage;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.entities.commandtemplate.Invoker;
import com.evolgames.utilities.GeometryUtils;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.entities.Hand;
import com.evolgames.scenes.entities.PlayerSpecialAction;

import java.util.Collections;
import java.util.List;

public class Throw extends Use {

  private float angle;
  private float throwSpeed;

  @Override
  public void onStep(float deltaTime, WorldFacade worldFacade) {}

  public void processThrow(Hand hand) {
    Body body = hand.getGrabbedEntity().getBody();
    body.setFixedRotation(false);
    Invoker.addJointDestructionCommand(hand.getGrabbedEntity().getParentGroup(),hand.getMouseJoint());
    Vector2 u = new Vector2(0, 1);
    GeometryUtils.rotateVectorDeg(u, angle);
    body.setLinearVelocity( throwSpeed* u.x, throwSpeed * u.y);
    Projectile projectile = new Projectile(ProjectileType.SHARP_WEAPON);
    hand.getGrabbedEntity().getUseList().add(projectile);
    projectile.setActive(true);
    hand.clearStack();
  }

  @Override
  public List<PlayerSpecialAction> getActions() {
    return Collections.singletonList(PlayerSpecialAction.Throw);
  }

  public void reset(float angle, float speed) {
    this.angle = angle;
    this.throwSpeed = speed;
  }
}
