package com.evolgames.entities.usage;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.helpers.utilities.Utils;
import com.evolgames.scenes.GameScene;
import com.evolgames.scenes.PlayerSpecialAction;
import com.evolgames.scenes.Hand;

import java.util.List;

public class Slasher extends MeleeUse {

    @Override
    public void onStep(float deltaTime) {
    }

    public void doSlash(GameScene gameScene, Hand hand,GameEntity targetEntity, List<Vector2> path, Vector2 target, float sharpLength, float sharpness, float hardness) {
        Vector2 p = hand.getGrabbedEntity().getBody().getWorldPoint(new Vector2(0, sharpLength / 2f)).cpy();
        path.add(p);
        GameEntity grabbedEntity = hand.getGrabbedEntity();
        if (path.size() >= 3 && Utils.PointInPolygon(target, path)) {
            Vector2 u = new Vector2(1, 0);
            GeometryUtils.rotateVectorDeg(u, (float) (Math.random() * 360));
            float cutHalfLength = 0.5f * sharpness * sharpLength;
            Vector2 point1 = target.cpy().add(cutHalfLength * u.x, cutHalfLength * u.y);
            Vector2 point2 = target.cpy().add(-cutHalfLength * u.x, -cutHalfLength * u.y);
            if (cutHalfLength > 0.05f) {
                gameScene.getWorldFacade().performScreenCut(point1, point2, hand.getGrabbedEntity(), hardness);
            }
            path.clear();
        }
        for (GameEntity gameEntity : targetEntity.getParentGroup().getGameEntities()) {
            gameScene.getWorldFacade().addNonCollidingPair(grabbedEntity, gameEntity);
            if (grabbedEntity.hasActiveUsage(Slasher.class)) {
                grabbedEntity.getUsage(Slasher.class).getTargetGameEntities().add(gameEntity);
            }
        }
    }

    @Override
    public PlayerSpecialAction getAction() {
        return PlayerSpecialAction.Slash;
    }

}
