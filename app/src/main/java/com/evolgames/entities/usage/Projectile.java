package com.evolgames.entities.usage;

import static org.andengine.extension.physics.box2d.util.Vector2Pool.obtain;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.basics.GameGroup;
import com.evolgames.entities.hand.PlayerSpecialAction;
import com.evolgames.physics.WorldFacade;
import com.evolgames.physics.entities.TopographyData;
import com.evolgames.scenes.PhysicsScene;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Projectile extends Use implements Penetrating {


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

        Map<GameGroup, List<GameEntity>> groups =
                overlappedEntities.stream().collect(Collectors.groupingBy(GameEntity::getParentGroup));
        List<GameEntity> list =
                groups.values().stream()
                        .map(e -> e.stream().findFirst().get())
                        .distinct()
                        .collect(Collectors.toList());

        for (GameEntity overlappedEntity : list) {
            overlappedEntity
                    .getParentGroup()
                    .getGameEntities()
                    .forEach(t -> worldFacade.addNonCollidingPair(penetrator, t));

            worldFacade.mergeEntities(
                    overlappedEntity, penetrator, normal.cpy().mul(-actualAdvance), point.cpy());
            if (projectileType != ProjectileType.SHARP_WEAPON) {
                worldFacade.scheduleGameEntityToDestroy(penetrator, 10);
            }

            worldFacade.applyPointImpact(obtain(point), consumedImpulse * massFraction, overlappedEntity);
        }

        if (!list.isEmpty()) {
            worldFacade.freeze(penetrator);
            penetrator.setZIndex(-1);
            penetrator.getScene().sortChildren();
            setActive(false);
        }
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
        worldFacade.addNonCollidingPair(penetrated, penetrator);
        worldFacade.computePenetrationPoints(normal, actualAdvance, envData);
        worldFacade.applyPointImpact(obtain(point), collisionImpulse * massFraction, penetrated);
        setActive(false);
    }

    @Override
    public void onCancel() {
    }
}
