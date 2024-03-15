package com.evolgames.entities.serialization.infos;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.basics.GameEntity;

public class DragInfo {

    private Vector2 dragOrigin;
    private Vector2 dragNormal;
    private float extent;
    private boolean symmetrical;
    private float magnitude;

    private transient GameEntity draggedEntity;
    private String draggedEntityUniqueId;

    public Vector2 getDragOrigin() {
        return dragOrigin;
    }

    public void setDragOrigin(Vector2 dragOrigin) {
        this.dragOrigin = dragOrigin;
    }

    public Vector2 getDragNormal() {
        return dragNormal;
    }

    public void setDragNormal(Vector2 dragNormal) {
        this.dragNormal = dragNormal;
    }

    public float getExtent() {
        return extent;
    }

    public void setExtent(float extent) {
        this.extent = extent;
    }

    public boolean isSymmetrical() {
        return symmetrical;
    }

    public void setSymmetrical(boolean symmetrical) {
        this.symmetrical = symmetrical;
    }

    public void setMagnitude(float magnitude) {
        this.magnitude = magnitude;
    }

    public float getMagnitude() {
        return magnitude;
    }

    public GameEntity getDraggedEntity() {
        return draggedEntity;
    }

    public void setDraggedEntity(GameEntity draggedEntity) {
        this.draggedEntity = draggedEntity;
        if(draggedEntity!=null) {
            this.draggedEntityUniqueId = draggedEntity.getUniqueID();
        }
    }

    public String getDraggedEntityUniqueId() {
        return draggedEntityUniqueId;
    }

}
