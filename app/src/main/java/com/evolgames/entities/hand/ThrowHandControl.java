package com.evolgames.entities.hand;

import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.helpers.utilities.MathUtils;
import com.evolgames.scenes.entities.Hand;

public class ThrowHandControl extends HandControl {

  private final Hand hand;
  private final float angle;
  private final AngleChanger angleChanger;
  private boolean thrown = false;

  public ThrowHandControl(Hand hand, float angle) {
    super();
    this.angle = angle;
    this.angleChanger = new AngleChanger(hand.getGrabbedEntity(), angle);
    this.hand = hand;
  }

  @Override
  public void run() {
    super.run();
    if (hand.getMouseJoint() == null) {
      return;
    }
    Body body = hand.getMouseJoint().getBodyB();
    if (!isDead()) {
      assert body != null;
      float rot = body.getAngle();
      float error =
          GeometryUtils.calculateShortestDirectedDistance(rot * MathUtils.radiansToDegrees, angle);
      this.angleChanger.run();
      if (!(Math.abs(error) > 5f)) {
        if (!thrown) {
          setDead(true);
          thrown = true;
          if (this.runnable != null) {
            this.runnable.run();
          }
        }
      }
    }
  }
}
