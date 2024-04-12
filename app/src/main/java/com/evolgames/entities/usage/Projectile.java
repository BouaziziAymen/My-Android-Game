package com.evolgames.entities.usage;

import static org.andengine.extension.physics.box2d.util.Vector2Pool.obtain;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.evolgames.activity.ResourceManager;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.commandtemplate.Invoker;
import com.evolgames.entities.hand.PlayerSpecialAction;
import com.evolgames.physics.WorldFacade;
import com.evolgames.physics.entities.TopographyData;
import com.evolgames.scenes.PhysicsScene;

import org.andengine.audio.sound.Sound;

import java.util.List;

public class Projectile extends Use implements Penetrating {


    private ProjectileType projectileType;

    @SuppressWarnings("unused")
    public Projectile() {
    }

    public Projectile(ProjectileType projectileType) {
        this.active = false;
        this.projectileType = projectileType;
    }

    private float impactFactor() {
        return projectileType == ProjectileType.BULLET || projectileType == ProjectileType.METEOR ? 10f : 1f;
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
            float consumedImpulse, LayerBlock penetratorBlock) {
        float massFraction =
                penetrator.getBody().getMass()
                        / (penetrated.getMassOfGroup() + penetrator.getBody().getMass());

        List<GameEntity> overlappedEntities =
                worldFacade.findOverlappingEntities(penData, envData, actualAdvance);

        worldFacade.computePenetrationPoints(normal, actualAdvance, envData, consumedImpulse);
        if (projectileType == ProjectileType.BULLET) {
            penetrator.setAlive(false);
            penetrator.setVisible(false);
            worldFacade.destroyGameEntity(penetrator, false, false);
        }

        if (projectileType == ProjectileType.SHARP_WEAPON) {
            penetrated.getBody().applyLinearImpulse(normal.cpy().mul(consumedImpulse * massFraction), obtain(point));
        }
        Invoker.addCustomCommand(penetrated, () -> {
            if (penetrated.isAlive() && penetrated.getBody() != null) {
                worldFacade.applyPointImpact(obtain(point), 20f * impactFactor() * consumedImpulse * massFraction, penetrated);
            }
        });
        if (projectileType == ProjectileType.SHARP_WEAPON) {

        for (GameEntity overlappedEntity : overlappedEntities) {
                worldFacade.mergeEntities(overlappedEntity, penetrator, normal.cpy().mul(-actualAdvance), point.cpy());
            }
        }

        setActive(false);
        penetrator.setZIndex(-1);
        worldFacade.getPhysicsScene().sortChildren();
        if(projectileType!=ProjectileType.METEOR) {
            if (!overlappedEntities.isEmpty()) {
                worldFacade.freeze(penetrator);
            }
        }
        onImpact(consumedImpulse * 4, penetrator, penetratorBlock);
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
                        / (penetrated.getMassOfGroup() + penetrator.getBody().getMass());
        for (GameEntity gameEntity : penetrated.getParentGroup().getEntities()) {
            worldFacade.addNonCollidingPair(gameEntity, penetrator);
        }
        worldFacade.computePenetrationPoints(normal, actualAdvance, envData, collisionImpulse);
        if (projectileType == ProjectileType.SHARP_WEAPON) {
            penetrated.getBody().applyLinearImpulse(normal.cpy().mul(5f*collisionImpulse * massFraction), obtain(point));
        }
        Invoker.addCustomCommand(penetrated, () -> {
            if (penetrated.isAlive() && penetrated.getBody() != null) {
                worldFacade.applyPointImpact(obtain(point), 20f * impactFactor() * collisionImpulse * massFraction, penetrated);
            }
        });
        onImpact(collisionImpulse * 4, penetrator, penetratorBlock);
        setActive(false);
        penetrator.setZIndex(-1);
        worldFacade.getPhysicsScene().sortChildren();
    }

    private void onImpact(float impulse, GameEntity penetrator, LayerBlock block) {
        if (penetrator.hasUsage(ImpactBomb.class)) {
            ImpactBomb impactBomb = penetrator.getUsage(ImpactBomb.class);
            impactBomb.onImpact(impulse, block.getId());
        }
    }

    @Override
    public void onCancel() {
    }
}
