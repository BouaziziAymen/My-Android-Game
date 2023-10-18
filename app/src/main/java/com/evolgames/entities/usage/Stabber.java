package com.evolgames.entities.usage;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.hand.MoveToStabHandControl;
import com.evolgames.physics.WorldFacade;
import com.evolgames.physics.entities.TopographyData;
import com.evolgames.scenes.PlayerSpecialAction;

import java.util.List;

public class Stabber extends MeleeUse implements Penetrating {

    public static final float STAB_EXTENT = 4f;
    private Vector2 handLocalPosition;
    private MoveToStabHandControl handControl;

    @Override
    public void onStep(float deltaTime) {
      if(this.handControl!=null && this.handControl.isDead()){
          setActive(false);
      }
    }

    @Override
    public PlayerSpecialAction getAction() {
        return PlayerSpecialAction.Stab;
    }

    public void reset(Vector2 handLocalPosition, MoveToStabHandControl handControl) {
        this.handLocalPosition = handLocalPosition;
        this.handControl = handControl;
    }

    @Override
    public void onImpulseConsumed(WorldFacade worldFacade, Contact contact, Vector2 point, Vector2 normal, float actualAdvance, GameEntity penetrator, GameEntity penetrated, List<TopographyData> envData, List<TopographyData> penData, float collisionImpulse) {
       final float maxAdvance = STAB_EXTENT - handControl.getAdvance();
       final float possibleAdvance = Math.min(maxAdvance,actualAdvance);
        float massFraction = penetrator.getBody().getMass() / (penetrator.getBody().getMass() + penetrator.getBody().getMass());
        List<GameEntity> stabbedEntities = worldFacade.findReachedEntities(penData, envData, possibleAdvance);
        for(GameEntity stabbedEntity:stabbedEntities){
            worldFacade.freeze(stabbedEntity);
            for(GameEntity gameEntity:stabbedEntity.getParentGroup().getGameEntities()) {
                worldFacade.addNonCollidingPair(penetrator, gameEntity);
                this.getTargetGameEntities().add(gameEntity);
            }
        }
        worldFacade.computePenetrationPoints(normal, possibleAdvance, envData);
        penetrated.getBody().applyLinearImpulse(normal.cpy().mul(0.001f * collisionImpulse * massFraction), point);
        worldFacade.freeze(penetrator);
        Vector2 handWorldPoint = penetrator.getBody().getWorldPoint(this.handLocalPosition).cpy();
        handControl.setTarget(handWorldPoint.add(normal.cpy().mul(possibleAdvance)));
        this.setActive(false);
        penetrator.getBody().setFixedRotation(true);
        contact.setEnabled(false);
    }

    @Override
    public void onFree(WorldFacade worldFacade, Contact contact, Vector2 point, Vector2 normal, float actualAdvance, GameEntity penetrator, GameEntity penetrated, List<TopographyData> envData, List<TopographyData> penData, float consumedImpulse, float collisionImpulsee) {
        final float maxAdvance = STAB_EXTENT - handControl.getAdvance();
        final float possibleAdvance = Math.min(maxAdvance,actualAdvance);
        List<GameEntity> stabbedEntities = worldFacade.findReachedEntities(penData, envData, possibleAdvance);
        for(GameEntity stabbedEntity:stabbedEntities){
            worldFacade.freeze(stabbedEntity);
            worldFacade.addNonCollidingPair(penetrator,stabbedEntity);
            this.getTargetGameEntities().add(stabbedEntity);
        }
        worldFacade.freeze(penetrator);
        worldFacade.computePenetrationPoints(normal, possibleAdvance, envData);
        setActive(false);
        penetrator.getBody().setFixedRotation(true);
        contact.setEnabled(false);
    }

    @Override
    public void onCancel() {
       setActive(false);
       handControl.goBack();
    }
}
