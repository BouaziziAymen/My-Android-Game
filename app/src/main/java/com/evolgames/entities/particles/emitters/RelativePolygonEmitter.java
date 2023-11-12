package com.evolgames.entities.particles.emitters;

import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.CoatingBlock;

import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.andengine.util.adt.transformation.Transformation;

public abstract class RelativePolygonEmitter extends PolygonEmitter {

  protected GameEntity gameEntity;
  private float positionX;
  private float positionY;
  private float rotation;

  public RelativePolygonEmitter(GameEntity entity, Predicate<CoatingBlock> predicate) {
    super(
        entity.getBlocks().stream()
            .flatMap(t -> t.getBlockGrid().getCoatingBlocks().stream())
            .collect(Collectors.toList()),
        predicate);
    this.gameEntity = entity;
  }

  public void onStep() {
    calculateWeights();
    if (Math.abs(positionX - gameEntity.getMesh().getX()) > 1
        || Math.abs(positionY - gameEntity.getMesh().getY()) > 1
        || Math.abs(rotation - gameEntity.getMesh().getRotation()) > 1) {
      computeTrianglesData();
      transformData();
    }
  }

  private void transformData() {
    Transformation transformation = gameEntity.getMesh().getLocalToSceneTransformation();
    transformation.transform(trianglesData);
    positionX = gameEntity.getMesh().getX();
    positionY = gameEntity.getMesh().getY();
    rotation = gameEntity.getMesh().getRotation();
  }
}
