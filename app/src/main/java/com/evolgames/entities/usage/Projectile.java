package com.evolgames.entities.usage;

import static org.andengine.extension.physics.box2d.util.Vector2Pool.obtain;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.commandtemplate.Invoker;
import com.evolgames.entities.hand.PlayerSpecialAction;
import com.evolgames.physics.WorldFacade;
import com.evolgames.physics.entities.TopographyData;
import com.evolgames.scenes.PhysicsScene;

import java.util.List;
import java.util.stream.Collectors;

public class Projectile extends Use implements Penetrating {


    private float impactFactor(){
        return projectileType==ProjectileType.BULLET?6:1;
    }
    private ProjectileType projectileType;

    @SuppressWarnings("unused")
    public Projectile() {
    }

    public Projectile(ProjectileType projectileType) {
        this.active = false;
        this.projectileType = projectileType;
    }

    @Override
    public void onStep(float deltaTime, WorldFacade worldFacade) {
    }

    @Override
    public List<PlayerSpecialAction> getActions() {
        return null;
    }

    @Override
    public void dynamicMirror(PhysicsScene<?> physicsScene) {

    }

    @Override
    public boolean inheritedBy(GameEntity biggestSplinter, float ratio) {
        return !(ratio < 0.1f);
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
                        / (penetrated.getMassOfGroup() + penetrator.getBody().getMass());

        List<GameEntity> overlappedEntities =
                worldFacade.findOverlappingEntities(penData, envData, actualAdvance);

        worldFacade.computePenetrationPoints(normal, actualAdvance, envData);


        Invoker.addCustomCommand(penetrated, () -> {
            if (penetrated.isAlive() && penetrated.getBody() != null) {
                penetrated.getBody().applyLinearImpulse(normal.cpy().mul(10f*impactFactor() * consumedImpulse * massFraction), obtain(point));
                worldFacade.applyPointImpact(obtain(point), 100f*impactFactor() * consumedImpulse * massFraction, penetrated);
            }
        });

        for (GameEntity overlappedEntity : overlappedEntities) {
            overlappedEntity
                    .getParentGroup()
                    .getGameEntities()
                    .forEach(t -> worldFacade.addNonCollidingPair(penetrator, t));
            if (projectileType == ProjectileType.SHARP_WEAPON) {
                worldFacade.mergeEntities(overlappedEntity, penetrator, normal.cpy().mul(-actualAdvance), point.cpy());
            } else {
                if(penetrator.isAlive()) {
                    penetrator.setAlive(false);
                    penetrator.getMesh().setVisible(false);
                    worldFacade.destroyGameEntity(penetrator, false, false);
                }
            }
        }

        setActive(false);
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
        float massFraction =
                penetrator.getBody().getMass()
                        / (penetrated.getMassOfGroup() + penetrator.getBody().getMass());
        for (GameEntity gameEntity : penetrated.getParentGroup().getEntities()) {
            worldFacade.addNonCollidingPair(gameEntity, penetrator);
        }
        worldFacade.computePenetrationPoints(normal, actualAdvance, envData);
        Invoker.addCustomCommand(penetrated, () -> {
            if (penetrated.isAlive() && penetrated.getBody() != null) {
                penetrated.getBody().applyLinearImpulse(normal.cpy().mul(10*impactFactor() * collisionImpulse * massFraction), obtain(point));
                worldFacade.applyPointImpact(obtain(point), 100f*impactFactor() * collisionImpulse * massFraction, penetrated);
            }
        });
        setActive(false);
        penetrator.setZIndex(-1);
        worldFacade.getPhysicsScene().sortChildren();
    }

    @Override
    public void onCancel() {
    }
}
