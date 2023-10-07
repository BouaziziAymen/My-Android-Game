package com.evolgames.entities.usage;

import static org.andengine.extension.physics.box2d.util.Vector2Pool.obtain;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.evolgames.entities.GameEntity;
import com.evolgames.physics.WorldFacade;
import com.evolgames.physics.entities.TopographyData;
import com.evolgames.scenes.PlayerSpecialAction;

import java.util.List;

public class Projectile extends Use implements Penetrating {
    public Projectile() {
        this.active = true;
    }

    @Override
    public void onStep(float deltaTime) {
    }


    @Override
    public PlayerSpecialAction getAction() {
        return null;
    }

    @Override
    public float getAvailableEnergy(float collisionImpulse) {
        return collisionImpulse;
    }

    @Override
    public void onEnergyConsumed(WorldFacade worldFacade, Contact contact, Vector2 point, Vector2 normal, float actualAdvance, GameEntity penetrator, GameEntity penetrated, List<TopographyData> envData, List<TopographyData> penData, float consumedEnergy) {
        float massFraction = penetrator.getBody().getMass() / (penetrator.getBody().getMass() + penetrator.getBody().getMass());
        worldFacade.freeze(penetrator);
        worldFacade.computePenetrationPoints(normal,point, actualAdvance, envData,penData);
        setActive(false);
        List<GameEntity> list = worldFacade.findOverlappingEntities(penData, envData, actualAdvance);
        for (GameEntity overlappedEntity : list) {
            worldFacade.applyPointImpact(obtain(point), 1 / 50f * consumedEnergy * massFraction, overlappedEntity);
            penetrated.getBody().applyLinearImpulse(normal.cpy().mul(0.001f * consumedEnergy * massFraction), point);
            worldFacade.mergeEntities(overlappedEntity, penetrator, normal.cpy().mul(-actualAdvance), point);
        }

    }


    @Override
    public void onFree(WorldFacade worldFacade, Contact contact, Vector2 point, Vector2 normal, float actualAdvance, GameEntity penetrator, GameEntity penetrated, List<TopographyData> envData, List<TopographyData> penData, float consumedEnergy, float collisionEnergy) {
        float massFraction = penetrator.getBody().getMass() / (penetrator.getBody().getMass() + penetrator.getBody().getMass());
        worldFacade.addGameEntityToDestroy(penetrator, false);
        worldFacade.computePenetrationPoints(normal,point, actualAdvance, envData,penData);
        worldFacade.applyPointImpact(obtain(point), (float) (Math.sqrt(consumedEnergy) * massFraction), penetrated);
        setActive(false);
    }

    @Override
    public void onCancel() {

    }
}
