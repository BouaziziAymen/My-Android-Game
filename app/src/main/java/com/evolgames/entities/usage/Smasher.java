package com.evolgames.entities.usage;

import static org.andengine.extension.physics.box2d.util.Vector2Pool.obtain;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.evolgames.entities.GameEntity;
import com.evolgames.physics.WorldFacade;
import com.evolgames.physics.entities.TopographyData;
import com.evolgames.scenes.entities.Hand;
import com.evolgames.scenes.entities.PlayerSpecialAction;
import java.util.List;

public class Smasher extends MeleeUse implements Penetrating {

  private transient Hand hand;
  private int handId;

  @Override
  public void onStep(float deltaTime) {
    if (this.hand != null) {
      if (this.hand.getHandControl() != null && this.hand.getHandControl().isDead()) {
        setActive(false);
      }
    }
  }


  @Override
  public PlayerSpecialAction getAction() {
    return PlayerSpecialAction.Smash;
  }

  @Override
  public void onImpulseConsumed(
      WorldFacade worldFacade,
      Contact contact,
      Vector2 point,
      Vector2 normal,
      float actualAdvance,
      GameEntity penetrator,
      GameEntity penetrated,
      List<TopographyData> envData,
      List<TopographyData> penData,
      float consumedImpulse) {
    float massFraction =
        penetrator.getBody().getMass()
            / (penetrator.getBody().getMass() + penetrator.getBody().getMass());
    worldFacade.computePenetrationPoints(normal, actualAdvance, envData);
    penetrated
        .getBody()
        .applyLinearImpulse(normal.cpy().mul(0.1f * consumedImpulse * massFraction), point);
    worldFacade.applyPointImpact(obtain(point), consumedImpulse * massFraction, penetrated);
    worldFacade.freeze(penetrator);
    hand.getHandControl().setDead(true);
    this.setActive(false);
  }

  @Override
  public void onFree(
      WorldFacade worldFacade,
      Contact contact,
      Vector2 point,
      Vector2 normal,
      float actualAdvance,
      GameEntity penetrator,
      GameEntity penetrated,
      List<TopographyData> envData,
      List<TopographyData> penData,
      float consumedImpulse,
      float collisionImpulse) {
    float massFraction =
        penetrator.getBody().getMass()
            / (penetrator.getBody().getMass() + penetrator.getBody().getMass());
    hand.getHandControl().setDead(true);
    worldFacade.applyPointImpact(obtain(point), consumedImpulse * massFraction, penetrated);
    worldFacade.computePenetrationPoints(normal, actualAdvance, envData);
    setActive(false);
  }

  @Override
  public void onCancel() {
    setActive(false);
    hand.getHandControl().setDead(true);
  }

    public int getHandId() {
        return handId;
    }

  public void setHand(Hand hand) {
    this.hand = hand;
    this.handId = hand.getMousePointerId();
  }
}
