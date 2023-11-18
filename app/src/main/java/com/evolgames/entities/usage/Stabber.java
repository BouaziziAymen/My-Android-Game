package com.evolgames.entities.usage;

import static org.andengine.extension.physics.box2d.util.Vector2Pool.obtain;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.hand.MoveToStabHandControl;
import com.evolgames.physics.WorldFacade;
import com.evolgames.physics.entities.TopographyData;
import com.evolgames.scenes.entities.Hand;
import com.evolgames.scenes.entities.PlayerSpecialAction;
import java.util.List;

public class Stabber extends MeleeUse implements Penetrating {

  private Vector2 handLocalPosition;
  transient private Hand hand;
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
    return PlayerSpecialAction.Stab;
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
    List<GameEntity> stabbedEntities =
        worldFacade.findReachedEntities(penData, envData, actualAdvance);
    worldFacade.applyPointImpact(obtain(point), consumedImpulse * massFraction, penetrator);
    if (actualAdvance > 0.01f) {
      for (GameEntity stabbedEntity : stabbedEntities) {
        worldFacade.freeze(stabbedEntity);
        worldFacade.applyPointImpact(obtain(point), consumedImpulse * massFraction, stabbedEntity);
        for (GameEntity gameEntity : stabbedEntity.getParentGroup().getGameEntities()) {
          worldFacade.addNonCollidingPair(penetrator, gameEntity);
          this.getTargetGameEntities().add(gameEntity);
        }
      }
    }
    worldFacade.computePenetrationPoints(normal, actualAdvance, envData);
    penetrated
        .getBody()
        .applyLinearImpulse(normal.cpy().mul(0.1f * consumedImpulse * massFraction), point);
    worldFacade.freeze(penetrator);
    Vector2 handWorldPoint = penetrator.getBody().getWorldPoint(this.handLocalPosition).cpy();
    ((MoveToStabHandControl)hand.getHandControl()).setTarget(handWorldPoint.add(normal.cpy().mul(actualAdvance)));
    penetrator.getBody().setFixedRotation(true);
    this.setActive(false);
    contact.setEnabled(false);
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
    List<GameEntity> stabbedEntities =
        worldFacade.findReachedEntities(penData, envData, actualAdvance);
    for (GameEntity stabbedEntity : stabbedEntities) {
      worldFacade.freeze(stabbedEntity);
      worldFacade.addNonCollidingPair(penetrator, stabbedEntity);
      this.getTargetGameEntities().add(stabbedEntity);
    }
    worldFacade.freeze(penetrator);
    worldFacade.computePenetrationPoints(normal, actualAdvance, envData);
    setActive(false);
    penetrator.getBody().setFixedRotation(true);
    contact.setEnabled(false);
  }

  @Override
  public void onCancel() {
    setActive(false);
    ((MoveToStabHandControl)hand.getHandControl()).goBack();
  }

  public int getHandId() {
    return handId;
  }

  public Vector2 getHandLocalPosition() {
    return handLocalPosition;
  }

  public void setHandLocalPosition(Vector2 handLocalPosition) {
    this.handLocalPosition = handLocalPosition;
  }

  public Hand getHand() {
    return hand;
  }

  public void setHand(Hand hand) {
    this.hand = hand;
    this.handId = hand.getMousePointerId();
  }
}
