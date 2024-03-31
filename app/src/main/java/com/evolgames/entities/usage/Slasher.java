package com.evolgames.entities.usage;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.hand.Hand;
import com.evolgames.entities.hand.PlayerSpecialAction;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.PhysicsScene;
import com.evolgames.utilities.GeometryUtils;
import com.evolgames.utilities.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Slasher extends MeleeUse {

    public static final float SLASH_CONSTANT = 0.25f;
    private final List<Vector2> path = new ArrayList<>();
    transient private GameEntity targetEntity;
    private String targetEntityId;
    private Vector2 target;
    private float sharpLength;
    private float sharpness;
    private float hardness;

    public void reset(GameEntity targetEntity, Vector2 target, float sharpLength, float sharpness, float hardness) {
        path.clear();
        this.targetEntity = targetEntity;
        this.target = target;
        this.sharpLength = sharpLength;
        this.sharpness = sharpness;
        this.hardness = hardness;
        this.targetEntityId = targetEntity.getUniqueID();
    }

    @Override
    public void onStep(float deltaTime, WorldFacade worldFacade) {
    }

    public void doSlash(Hand hand) {
        Vector2 p =
                hand.getGrabbedEntity().getBody().getWorldPoint(new Vector2(0, sharpLength / 2f)).cpy();
        path.add(p);
        GameEntity grabbedEntity = hand.getGrabbedEntity();
        if (path.size() >= 3 && Utils.PointInPolygon(target, path)) {
            Vector2 u = new Vector2(1, 0);
            GeometryUtils.rotateVectorDeg(u, (float) (Math.random() * 360));
            float cutHalfLength = SLASH_CONSTANT * sharpness * sharpLength;
            Vector2 point1 = target.cpy().add(cutHalfLength * u.x, cutHalfLength * u.y);
            Vector2 point2 = target.cpy().add(-cutHalfLength * u.x, -cutHalfLength * u.y);
            if (cutHalfLength > 0.05f) {
                hand.getGameScene().getWorldFacade().performScreenCut(point1, point2, hand.getGrabbedEntity(), hardness);
            }
            path.clear();
        }
        for (GameEntity gameEntity : targetEntity.getParentGroup().getGameEntities()) {
            hand.getGameScene().getWorldFacade().addNonCollidingPair(grabbedEntity, gameEntity);
            if (grabbedEntity.hasActiveUsage(Slasher.class)) {
                grabbedEntity.getUsage(Slasher.class).getTargetGameEntities().add(gameEntity);
            }
        }
    }

    @Override
    public List<PlayerSpecialAction> getActions() {
        return Collections.singletonList(PlayerSpecialAction.Slash);
    }

    @Override
    public void dynamicMirror(PhysicsScene<?> physicsScene) {

    }

    @Override
    public boolean inheritedBy(GameEntity biggestSplinter, float ratio) {
        return !(ratio < 0.5f);
    }

    public String getTargetEntityId() {
        return targetEntityId;
    }

    public void setTargetEntity(GameEntity targetEntity) {
        this.targetEntity = targetEntity;
    }
}
