package com.evolgames.entities.usage;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.hand.PlayerSpecialAction;
import com.evolgames.entities.serialization.infos.DragInfo;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.PhysicsScene;
import com.evolgames.userinterface.model.toolmodels.DragModel;
import com.evolgames.utilities.GeometryUtils;

import java.util.List;

public class Drag extends Use {

    private DragInfo dragInfo;

    @SuppressWarnings("unused")
    public Drag() {
    }

    public Drag(DragModel dragModel, boolean mirrored) {
        this.dragInfo = dragModel.toDragInfo(mirrored);
    }


    @Override
    public void onStep(float deltaTime, WorldFacade worldFacade) {
        if (dragInfo.getDraggedEntity() == null) {
            return;
        }
        Body body = dragInfo.getDraggedEntity().getBody();
        if (body != null) {
            Vector2 normal = dragInfo.getDragNormal();
            Vector2 center = body.getWorldPoint(dragInfo.getDragOrigin()).cpy();
            Vector2 velocity = body.getLinearVelocity();
            Vector2 projectedNormal = body.getWorldVector(normal).cpy().nor();
            float velocityProjection = velocity.dot(projectedNormal);
            if (velocityProjection > 0 || dragInfo.isSymmetrical()) {
                float forceFactor =
                        -0.001f * dragInfo.getMagnitude() * Math.signum(velocityProjection)
                                * dragInfo.getExtent()
                                * velocityProjection
                                * velocityProjection
                                * dragInfo.getDraggedEntity().getBody().getMass();
                Vector2 force = projectedNormal.cpy().mul(forceFactor);
                body.applyForce(force.x, force.y, center.x, center.y);
            }
        }
    }

    @Override
    public List<PlayerSpecialAction> getActions() {
        return null;
    }

    @Override
    public void dynamicMirror(PhysicsScene physicsScene) {
        dragInfo.getDragOrigin().set(GeometryUtils.mirrorPoint(dragInfo.getDragOrigin()));
    }

    @Override
    public boolean inheritedBy(GameEntity heir, float ratio) {
        if (ratio < 0.8f) {
            return false;
        }
        dragInfo.setDraggedEntity(heir);
        return true;
    }

    public DragInfo getDragInfo() {
        return dragInfo;
    }
}
