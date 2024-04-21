package com.evolgames.entities.usage;

import static com.evolgames.physics.WorldFacade.calculateVolumeRatio;
import static org.andengine.extension.physics.box2d.util.Vector2Pool.obtain;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.evolgames.activity.ResourceManager;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.hand.Hand;
import com.evolgames.entities.hand.PlayerSpecialAction;
import com.evolgames.physics.WorldFacade;
import com.evolgames.physics.entities.TopographyData;
import com.evolgames.scenes.PhysicsScene;

import java.util.Collections;
import java.util.List;

public class Smasher extends MeleeUse implements Penetrating {
    private transient Hand hand;
    @Override
    public void onStep(float deltaTime, WorldFacade worldFacade) {
        if (this.hand != null) {
            if (!this.hand.getHandControlStack().isEmpty() && this.hand.getHandControlStack().peek().isDead()) {
                setActive(false);
            }
        }
    }


    @Override
    public List<PlayerSpecialAction> getActions() {
        return Collections.singletonList(PlayerSpecialAction.Smash);
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
        float massFraction =
                penetrator.getBody().getMass()
                        / (penetrator.getBody().getMass() + penetrator.getBody().getMass());
        worldFacade.computePenetrationPoints(normal, actualAdvance, envData, consumedImpulse);
        penetrated
                .getBody()
                .applyLinearImpulse(normal.cpy().mul(0.1f * consumedImpulse * massFraction), point);
        worldFacade.applyPointImpact(obtain(point), consumedImpulse * massFraction, penetrated);
        worldFacade.freeze(penetrator);
        hand.getHandControlStack().peek().setDead(true);
        if (actualAdvance < 0.05f) {
            ResourceManager.getInstance().tryPlaySound(ResourceManager.getInstance().bluntSound, calculateVolumeRatio(consumedImpulse), 2);
        }
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
        float massFraction =
                penetrator.getBody().getMass()
                        / (penetrator.getBody().getMass() + penetrator.getBody().getMass());
        hand.getHandControlStack().peek().setDead(true);
        worldFacade.applyPointImpact(obtain(point), collisionImpulse * massFraction, penetrated);
        worldFacade.computePenetrationPoints(normal, actualAdvance, envData, collisionImpulse);
        if (actualAdvance < 0.05f) {
            ResourceManager.getInstance().tryPlaySound(ResourceManager.getInstance().bluntSound, calculateVolumeRatio(collisionImpulse), 2);
        }
        setActive(false);
    }

    @Override
    public void onCancel() {
        setActive(false);
        hand.getHandControlStack().peek().setDead(true);
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    @Override
    public boolean inheritedBy(GameEntity biggestSplinter, float ratio) {
        return !(ratio < 0.3f);
    }

}
