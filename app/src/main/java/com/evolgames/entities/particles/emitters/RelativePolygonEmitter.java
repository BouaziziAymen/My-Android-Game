package com.evolgames.entities.particles.emitters;

import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.CoatingBlock;

import org.andengine.util.adt.transformation.Transformation;

import java.util.stream.Collectors;

import is.kul.learningandengine.helpers.Predicate;

public abstract class RelativePolygonEmitter extends PolygonEmitter {

    private float positionX;
    private float positionY;
    private float rotation;

    protected GameEntity gameEntity;

    public RelativePolygonEmitter(GameEntity entity,Predicate<CoatingBlock> predicate) {
        super(entity.getBlocks().stream().flatMap(t->t.getBlockGrid().getCoatingBlocks().stream()).collect(Collectors.toList()),predicate);
        this.gameEntity = entity;
    }

    public void onStep() {
        calculateWeights();
        if (Math.abs(positionX - gameEntity.getMesh().getX()) > 1 || Math.abs(positionY - gameEntity.getMesh().getY()) > 1 || Math.abs(rotation - gameEntity.getMesh().getRotation()) > 1) {
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
