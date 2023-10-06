package com.evolgames.entities.usage;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.hand.MoveWithRevertHandControl;
import com.evolgames.physics.WorldFacade;
import com.evolgames.physics.entities.Data;
import com.evolgames.scenes.PlayerSpecialAction;

import java.util.List;

public class Stabber extends MeleeUse implements Penetrating {

    private Vector2 handLocalPosition;
    private MoveWithRevertHandControl handControl;

    @Override
    public void onStep(float deltaTime) {

    }

    @Override
    public PlayerSpecialAction getAction() {
        return PlayerSpecialAction.Stab;
    }

    public void reset(Vector2 handLocalPosition, MoveWithRevertHandControl handControl) {
        this.handLocalPosition = handLocalPosition;
        this.handControl = handControl;
    }

    @Override
    public float getAvailableEnergy(float collisionEnergy) {
        return collisionEnergy;
    }

    @Override
    public void onEnergyConsumed(WorldFacade worldFacade, Contact contact, Vector2 point, Vector2 normal, float actualAdvance, GameEntity penetrator, GameEntity penetrated, List<Data> envData, List<Data> penData, float consumedEnergy) {
        float massFraction = penetrator.getBody().getMass() / (penetrator.getBody().getMass() + penetrator.getBody().getMass());
        List<GameEntity> stabbedEntities = worldFacade.findReachedEntities(penData, envData, actualAdvance);
        for(GameEntity stabbedEntity:stabbedEntities){
            worldFacade.addNonCollidingPair(penetrator,stabbedEntity);
            this.getTargetGameEntities().add(stabbedEntity);
        }
        worldFacade.computePenetrationPoints(normal, actualAdvance, envData);
        penetrated.getBody().applyLinearImpulse(normal.cpy().mul(0.001f * consumedEnergy * massFraction), point);
        worldFacade.freeze(penetrator);
        this.setActive(false);
        Vector2 handWorldPoint = penetrator.getBody().getWorldPoint(this.handLocalPosition).cpy();
        handControl.setTarget(handWorldPoint.add(normal.cpy().mul(actualAdvance)));
    }

    @Override
    public void onFree(WorldFacade worldFacade, Contact contact, Vector2 point, Vector2 normal, float actualAdvance, GameEntity penetrator, GameEntity penetrated, List<Data> envData, List<Data> penData, float consumedEnergy, float collisionEnergy) {
        List<GameEntity> stabbedEntities = worldFacade.findReachedEntities(penData, envData, actualAdvance);
        for(GameEntity stabbedEntity:stabbedEntities){
            worldFacade.addNonCollidingPair(penetrator,stabbedEntity);
            this.getTargetGameEntities().add(stabbedEntity);
        }
        worldFacade.computePenetrationPoints(normal, actualAdvance, envData);
        setActive(false);
    }

    @Override
    public void onCancel() {

    }
}
