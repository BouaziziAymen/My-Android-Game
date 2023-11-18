package com.evolgames.entities.hand;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.usage.Shooter;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.helpers.utilities.MathUtils;
import com.evolgames.scenes.entities.Hand;
import com.evolgames.scenes.entities.PlayerSpecialAction;

public class HoldHandControl extends HandControl {

  @SuppressWarnings("unused")
  public HoldHandControl() {
  }
  public HoldHandControl(Hand hand) {
    super(hand);
  }

  @Override
  public void run() {
    super.run();
    GameEntity weapon = hand.getGrabbedEntity();
    if (weapon == null || weapon.getBody() == null) {
      return;
    }
    float angle = getAngle();

    Body body = weapon.getBody();
    if (!isDead()) {
      if (body != null) {
        float rot = body.getAngle();
        float error = angle - rot;

        while (error < -Math.PI) error += 2 * Math.PI;
        while (error > Math.PI) error -= 2 * Math.PI;
        if (Math.abs(error) > 0.005f) {
          body.setAngularVelocity(15 * error);
        }
      }
    }
  }

  private float getAngle() {
    GameEntity weapon = hand.getGrabbedEntity();
    PlayerSpecialAction action = hand.getGameScene().getSpecialAction();
    float angle = 0;
    if (action == PlayerSpecialAction.Slash
        || action == PlayerSpecialAction.Throw
        || action == PlayerSpecialAction.None) {
      angle = 0 * MathUtils.degreesToRadians;
    } else if (action == PlayerSpecialAction.Stab) {
      angle = 90 * MathUtils.degreesToRadians;
    } else if (action == PlayerSpecialAction.Shoot) {
      if(weapon!=null){
      Shooter shooter = weapon.getUsage(Shooter.class);
      Vector2 target = shooter.getTarget();
        if (target != null) {
          Vector2 U = target.cpy().sub(weapon.getBody().getPosition()).nor();
          angle = GeometryUtils.calculateAngle(U.x, U.y) * MathUtils.degreesToRadians;
        }
      }
    }
    return angle;
  }
}
