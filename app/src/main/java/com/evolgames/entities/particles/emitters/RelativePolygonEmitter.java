package com.evolgames.entities.particles.emitters;

import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.blocks.CoatingBlock;

import org.andengine.util.adt.transformation.Transformation;

import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RelativePolygonEmitter extends PolygonEmitter {

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
        this.randomPointPicker.calculateWeights();
        if (Math.abs(positionX - gameEntity.getX()) > 1
                || Math.abs(positionY - gameEntity.getY()) > 1
                || Math.abs(rotation - gameEntity.getRotation()) > 1) {
            this.randomPointPicker.computeTrianglesData();
            transformData();
        }
    }

    private void transformData() {
        if (gameEntity == null) {
            return;
        }
        Transformation transformation = gameEntity.getMesh().getLocalToSceneTransformation();
        this.randomPointPicker.transformData(transformation);
        positionX = gameEntity.getX();
        positionY = gameEntity.getY();
        rotation = gameEntity.getRotation();
    }
}
