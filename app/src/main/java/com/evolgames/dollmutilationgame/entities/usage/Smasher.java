package com.evolgames.dollmutilationgame.entities.usage;

import static org.andengine.extension.physics.box2d.util.Vector2Pool.obtain;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.evolgames.dollmutilationgame.entities.hand.Hand;
import com.evolgames.dollmutilationgame.entities.hand.PlayerSpecialAction;
import com.evolgames.dollmutilationgame.physics.WorldFacade;
import com.evolgames.dollmutilationgame.physics.entities.TopographyData;
import com.evolgames.dollmutilationgame.scenes.PhysicsScene;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.entities.blocks.LayerBlock;

import java.util.ArrayList;
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
        List<PlayerSpecialAction> list = new ArrayList<>();
        list.add(PlayerSpecialAction.None);
        list.add(PlayerSpecialAction.Smash);
        return list;
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
        worldFacade.computePenetrationSpecialEffects(normal, point,actualAdvance, envData, consumedImpulse);
        penetrated
                .getBody()
                .applyLinearImpulse(normal.cpy().mul(0.1f * consumedImpulse * massFraction), point);
        worldFacade.applyPointImpact(obtain(point), consumedImpulse * massFraction, penetrated);

        hand.getHandControlStack().peek().setDead(true);
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
        worldFacade.computePenetrationSpecialEffects(normal,point, actualAdvance, envData, collisionImpulse);
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
