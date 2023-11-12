package com.evolgames.entities.hand;

import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.scenes.entities.Hand;

public class SwingHandControl extends HandControl {
  private final float speed;
  private final Hand hand;

  public SwingHandControl(Hand hand, int speed, float ratio) {
    super((int) (ratio / Math.abs(speed)));
    this.speed = speed;
    this.hand = hand;
  }

  @Override
  public void run() {
    super.run();
    if (hand.getMouseJoint() == null) {
      return;
    }
    Body body = hand.getMouseJoint().getBodyB();
    if (body != null) {
      if (!isDead()) {
        body.setAngularVelocity(speed);
      } else {
        body.setAngularVelocity(0f);
      }
    }
  }
}
