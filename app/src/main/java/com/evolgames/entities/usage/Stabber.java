package com.evolgames.entities.usage;

import static org.andengine.extension.physics.box2d.util.Vector2Pool.obtain;

import android.support.annotation.Nullable;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.hand.MoveToStabHandControl;
import com.evolgames.physics.WorldFacade;
import com.evolgames.physics.entities.TopographyData;
import com.evolgames.scenes.entities.Hand;
import com.evolgames.scenes.entities.PlayerSpecialAction;

import java.util.Collections;
import java.util.List;

public class Stabber extends MeleeUse implements Penetrating {

  private Vector2 handLocalPosition;
  private transient Hand hand;
  private int handId;

  @Override
  public void onStep(float deltaTime, WorldFacade worldFacade) {
    if (this.hand != null) {
      if (!this.hand.getHandControlStack().isEmpty()
          && this.hand.getHandControlStack().peek().isDead()) {
        setActive(false);
      }
    }
  }

  @Override
  public List<PlayerSpecialAction> getActions() {
    return Collections.singletonList(PlayerSpecialAction.Stab);
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
    MoveToStabHandControl moveToStabHandControl =   ((MoveToStabHandControl) hand.getHandControlStack().peek());

    float possibleAdvance = Math.min(Hand.STAB_ADVANCE-moveToStabHandControl.getCurrentAdvance(), actualAdvance);

    List<GameEntity> stabbedEntities =
        worldFacade.findReachedEntities(penData, envData, possibleAdvance);
    worldFacade.applyPointImpact(
        obtain(point),
            0.1f*consumedImpulse
            * penetrator.getBody().getMass()
            / (penetrated.getMassOfGroup() + penetrator.getBody().getMass()),
        penetrator);
    if (possibleAdvance > 0.01f) {
      for (GameEntity stabbedEntity : stabbedEntities) {
        float massFraction = getMassFraction(penetrator, stabbedEntity);
        worldFacade.applyPointImpact(obtain(point), 0.1f*consumedImpulse * massFraction, stabbedEntity);
        for (GameEntity gameEntity : stabbedEntity.getParentGroup().getGameEntities()) {
            this.targetGameEntities.add(gameEntity);
            worldFacade.addNonCollidingPair(penetrator, gameEntity);
        }
      }
    }
    worldFacade.computePenetrationPoints(normal, possibleAdvance, envData);
    worldFacade.freeze(penetrator);
    Vector2 handWorldPoint = penetrator.getBody().getWorldPoint(this.handLocalPosition).cpy();
    moveToStabHandControl.setTarget(handWorldPoint.add(normal.cpy().mul(possibleAdvance)));
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
      float collisionImpulse) {

    MoveToStabHandControl moveToStabHandControl =   ((MoveToStabHandControl) hand.getHandControlStack().peek());

    float possibleAdvance = Math.min(Hand.STAB_ADVANCE-moveToStabHandControl.getCurrentAdvance(), actualAdvance);

    List<GameEntity> stabbedEntities =
        worldFacade.findReachedEntities(penData, envData, possibleAdvance);
    for (GameEntity stabbedEntity : stabbedEntities) {
      float massFraction = getMassFraction(penetrator, stabbedEntity);
      worldFacade.freeze(stabbedEntity);
      worldFacade.applyPointImpact(obtain(point), 0.1f*collisionImpulse * massFraction, stabbedEntity);
      worldFacade.addNonCollidingPair(penetrator, stabbedEntity);
      for (GameEntity gameEntity : stabbedEntity.getParentGroup().getGameEntities()) {
        this.targetGameEntities.add(gameEntity);
        worldFacade.addNonCollidingPair(penetrator, gameEntity);
      }
    }
    worldFacade.computePenetrationPoints(normal, possibleAdvance, envData);
    setActive(false);
    penetrator.getBody().setFixedRotation(true);
    contact.setEnabled(false);
  }

  private float getMassFraction(GameEntity penetrator, GameEntity stabbedEntity) {
    return penetrator.getBody().getMass()
            / (penetrator.getBody().getMass() + stabbedEntity.getBody().getMass());
  }

  @Override
  public void onCancel() {
    setActive(false);
    ((MoveToStabHandControl) hand.getHandControlStack().peek()).goBack();
  }

  public int getHandId() {
    return handId;
  }

  public void setHandLocalPosition(Vector2 handLocalPosition) {
    this.handLocalPosition = handLocalPosition;
  }

  public Hand getHand() {
    return hand;
  }

  public void setHand(@Nullable Hand hand) {
    this.hand = hand;
    this.handId = hand != null ? hand.getMousePointerId() : -1;
  }
}
