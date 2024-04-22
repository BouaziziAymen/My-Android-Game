package com.evolgames.entities.usage;

import static org.andengine.extension.physics.box2d.util.Vector2Pool.obtain;

import androidx.annotation.Nullable;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.hand.Hand;
import com.evolgames.entities.hand.MoveToStabHandControl;
import com.evolgames.entities.hand.PlayerSpecialAction;
import com.evolgames.physics.WorldFacade;
import com.evolgames.physics.entities.TopographyData;
import com.evolgames.scenes.PhysicsScene;

import java.util.Collections;
import java.util.List;

public class Stabber extends MeleeUse implements Penetrating {

    private Vector2 handLocalPosition;
    private transient Hand hand;
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
    public void dynamicMirror(PhysicsScene physicsScene) {

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
            float consumedImpulse, LayerBlock penetratorBlock) {
        MoveToStabHandControl moveToStabHandControl = ((MoveToStabHandControl) hand.getHandControlStack().peek());

        float possibleAdvance = Math.min(Hand.STAB_ADVANCE - moveToStabHandControl.getCurrentAdvance(), actualAdvance);

        List<GameEntity> stabbedEntities =
                worldFacade.findReachedEntities(penData, envData, possibleAdvance);
        worldFacade.applyPointImpact(
                obtain(point),
                0.1f * consumedImpulse
                        * penetrator.getBody().getMass()
                        / (penetrated.getMassOfGroup() + penetrator.getBody().getMass()),
                penetrator);
        if (possibleAdvance > 0.01f) {
            for (GameEntity stabbedEntity : stabbedEntities) {
                for (GameEntity gameEntity : stabbedEntity.getParentGroup().getGameEntities()) {
                    this.targetGameEntities.add(gameEntity);
                    worldFacade.addNonCollidingPair(penetrator, gameEntity);
                }
            }
        }
        worldFacade.computePenetrationPoints(normal, possibleAdvance, envData, consumedImpulse);
        worldFacade.freeze(penetrator);
        Vector2 handWorldPoint = penetrator.getBody().getWorldPoint(this.handLocalPosition).cpy();
        moveToStabHandControl.setTarget(handWorldPoint.add(normal.cpy().mul(possibleAdvance)));
        penetrator.getBody().setFixedRotation(true);
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
            float collisionImpulse, LayerBlock penetratorBlock) {

        MoveToStabHandControl moveToStabHandControl = ((MoveToStabHandControl) hand.getHandControlStack().peek());

        float possibleAdvance = Math.min(Hand.STAB_ADVANCE - moveToStabHandControl.getCurrentAdvance(), actualAdvance);

        List<GameEntity> stabbedEntities =
                worldFacade.findReachedEntities(penData, envData, possibleAdvance);
        for (GameEntity stabbedEntity : stabbedEntities) {
            worldFacade.freeze(stabbedEntity);
            worldFacade.addNonCollidingPair(penetrator, stabbedEntity);
            for (GameEntity gameEntity : stabbedEntity.getParentGroup().getGameEntities()) {
                this.targetGameEntities.add(gameEntity);
                worldFacade.addNonCollidingPair(penetrator, gameEntity);
            }
        }
        worldFacade.computePenetrationPoints(normal, possibleAdvance, envData, collisionImpulse);
        setActive(false);
        penetrator.getBody().setFixedRotation(true);
    }

    @Override
    public void onCancel() {
        setActive(false);
        ((MoveToStabHandControl) hand.getHandControlStack().peek()).goBack();
    }

    public void setHandLocalPosition(Vector2 handLocalPosition) {
        this.handLocalPosition = handLocalPosition;
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(@Nullable Hand hand) {
        this.hand = hand;
    }

    @Override
    public boolean inheritedBy(GameEntity biggestSplinter, float ratio) {
        return !(ratio < 0.5f);
    }


}
