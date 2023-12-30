package com.evolgames.entities.usage;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.serialization.infos.DragInfo;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.entities.PlayerSpecialAction;
import com.evolgames.userinterface.model.toolmodels.DragModel;

public class Drag extends Use {

  private DragInfo dragInfo;
  private transient GameEntity draggedEntity;
  private String draggedEntityUniqueId;

  @SuppressWarnings("unused")
  public Drag(){}

  public Drag(DragModel dragModel) {
    this.dragInfo = dragModel.toDragInfo();
  }

  public String getDraggedEntityUniqueId() {
    return draggedEntityUniqueId;
  }

  public GameEntity getDraggedEntity() {
    return draggedEntity;
  }

  public void setDraggedEntity(GameEntity draggedEntity) {
    this.draggedEntity = draggedEntity;
    this.draggedEntityUniqueId = draggedEntity.getUniqueID();
  }

  @Override
  public void onStep(float deltaTime, WorldFacade worldFacade) {
    if(draggedEntity==null){
      return;
    }
    Body body = draggedEntity.getBody();
    if (body != null) {
      Vector2 normal = dragInfo.getDragNormal();
      Vector2 begin = dragInfo.getDragOrigin();
      Vector2 center =
          body.getWorldPoint(begin.cpy().sub(draggedEntity.getCenter()).mul(1 / 32f)).cpy();
      Vector2 velocity = body.getLinearVelocity();
      Vector2 projectedNormal = body.getWorldVector(normal);
      float velocityProjection = velocity.dot(projectedNormal);
      if (velocityProjection > 0 || dragInfo.isSymmetrical()) {
        float forceFactor =
            -0.001f*dragInfo.getMagnitude()*Math.signum(velocityProjection)
                * dragInfo.getExtent()
                * velocityProjection
                * velocityProjection
                * draggedEntity.getBody().getMass();
        Vector2 force = projectedNormal.cpy().mul(forceFactor);

        // PlayScene.plotter.drawVector(center.cpy().mul(32f), projectedNormal.cpy().mul(32),
        // Color.RED);
        body.applyForce(force.x, force.y, center.x, center.y);
      }
    }
  }

  @Override
  public PlayerSpecialAction getAction() {
    return null;
  }
}
