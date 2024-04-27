package com.evolgames.dollmutilationgame.entities.usage;

import static org.andengine.extension.physics.box2d.util.Vector2Pool.obtain;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.evolgames.dollmutilationgame.entities.hand.PlayerSpecialAction;
import com.evolgames.dollmutilationgame.physics.WorldFacade;
import com.evolgames.dollmutilationgame.physics.entities.TopographyData;
import com.evolgames.dollmutilationgame.scenes.PhysicsScene;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.entities.blocks.LayerBlock;
import com.evolgames.dollmutilationgame.entities.commandtemplate.Invoker;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Projectile extends Use implements Penetrating {


    public static final float IMPULSE_FACTOR = 10f;
    private ProjectileType projectileType;

    @SuppressWarnings("unused")
    public Projectile() {
    }

    public Projectile(ProjectileType projectileType) {
        this.active = false;
        this.projectileType = projectileType;
    }

    private float impactFactor() {
        return projectileType == ProjectileType.BULLET || projectileType == ProjectileType.METEOR ? 100f : 20f;
    }

    @Override
    public void onStep(float deltaTime, WorldFacade worldFacade) {
    }

    @Override
    public List<PlayerSpecialAction> getActions() {
        return null;
    }

    @Override
    public void dynamicMirror(PhysicsScene physicsScene) {

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

        worldFacade.computePenetrationSpecialEffects(normal,point, actualAdvance, envData, consumedImpulse);
        if (projectileType == ProjectileType.BULLET) {
            penetrator.setVisible(false);
            worldFacade.destroyGameEntity(penetrator, true);
        }

        if (projectileType == ProjectileType.SHARP_WEAPON||projectileType == ProjectileType.METEOR) {
            penetrated.getBody().applyLinearImpulse(normal.cpy().mul(consumedImpulse * massFraction), obtain(point));
        }
        Invoker.addCustomCommand(penetrated, () -> {
            if (penetrated.isAlive() && penetrated.getBody() != null) {
                worldFacade.applyPointImpact(obtain(point), impactFactor() * consumedImpulse * massFraction, penetrated);
                worldFacade.applyPointImpact(obtain(point), impactFactor() * consumedImpulse * massFraction, penetrator,penetratorBlock);
            }
        });
        if (projectileType == ProjectileType.SHARP_WEAPON) {
            Optional<GameEntity> aDynamic = overlappedEntities.stream().filter(e -> e.getBody().getType() == BodyDef.BodyType.DynamicBody).collect(Collectors.toList()).stream().findFirst();
            Optional<GameEntity> aStatic = overlappedEntities.stream().filter(e -> e.getBody().getType() == BodyDef.BodyType.StaticBody).collect(Collectors.toList()).stream().findFirst();
            List<GameEntity> merged = new ArrayList<>();
            aDynamic.ifPresent(merged::add);
            aStatic.ifPresent(merged::add);
            for (GameEntity overlappedEntity : merged) {
                Float min = null;
                Vector2 base = null;
                for(int i=0;i<envData.size();i++) {
                    TopographyData e = envData.get(i);
                    if(e!=null) {
                        Float d = e.getFirstContact(overlappedEntity);
                        if (d!=null&&(min == null || min < d)) {
                            min = d;
                            base = e.getBase();
                        }
                    }
                }
                if(base!=null) {
                    Vector2 inter = base.cpy().add(min * normal.x, min * normal.y);
                    worldFacade.mergeEntities(overlappedEntity, penetrator, normal.cpy().mul(-actualAdvance), inter.cpy());
                }
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
        onImpact(consumedImpulse * 4, penetrator, penetratorBlock,worldFacade);
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
        worldFacade.computePenetrationSpecialEffects(normal, point,actualAdvance, envData, collisionImpulse);

        if (projectileType == ProjectileType.SHARP_WEAPON||projectileType == ProjectileType.METEOR) {
            penetrated.getBody().applyLinearImpulse(normal.cpy().mul(collisionImpulse * massFraction), obtain(point));
        }
        Invoker.addCustomCommand(penetrated, () -> {
            if (penetrated.isAlive() && penetrated.getBody() != null) {
                worldFacade.applyPointImpact(obtain(point), impactFactor() * collisionImpulse * massFraction, penetrated);
                worldFacade.applyPointImpact(obtain(point), impactFactor() * collisionImpulse * massFraction, penetrator);
            }
        });
        onImpact(collisionImpulse * 4, penetrator, penetratorBlock,worldFacade);
        setActive(false);
        penetrator.setZIndex(-1);
        worldFacade.getPhysicsScene().sortChildren();
    }

    private void onImpact(float impulse, GameEntity penetrator, LayerBlock block, WorldFacade worldFacade) {
        if (penetrator.hasUsage(ImpactBomb.class)) {
            ImpactBomb impactBomb = penetrator.getUsage(ImpactBomb.class);
            impactBomb.onImpact(impulse, block.getId(),worldFacade);
        }
    }

    @Override
    public void onCancel() {
    }
}
