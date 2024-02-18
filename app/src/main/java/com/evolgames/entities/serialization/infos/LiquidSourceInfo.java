package com.evolgames.entities.serialization.infos;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;

public class LiquidSourceInfo {
    private Vector2 liquidSourceOrigin;
    private Vector2 liquidDirection;
    private transient GameEntity containerEntity;
    private String containerEntityUniqueId;
    private int liquid;
    private float extent;
    private transient GameEntity sealEntity;
    private String sealEntityUniqueId;

    public Vector2 getLiquidSourceOrigin() {
        return liquidSourceOrigin;
    }

    public void setLiquidSourceOrigin(Vector2 liquidSourceOrigin) {
        this.liquidSourceOrigin = liquidSourceOrigin;
    }

    public Vector2 getLiquidDirection() {
        return liquidDirection;
    }

    public void setLiquidDirection(Vector2 liquidDirection) {
        this.liquidDirection = liquidDirection;
    }

    public GameEntity getContainerEntity() {
        return containerEntity;
    }

    public void setContainerEntity(GameEntity containerEntity) {
        this.containerEntity = containerEntity;
        this.containerEntityUniqueId = containerEntity.getUniqueID();
    }

    public String getSealEntityUniqueId() {
        return sealEntityUniqueId;
    }

    public String getContainerEntityUniqueId() {
        return containerEntityUniqueId;
    }

    public int getLiquid() {
        return liquid;
    }

    public void setLiquid(int liquid) {
        this.liquid = liquid;
    }

    public float getExtent() {
        return extent;
    }

    public void setExtent(float extent) {
        this.extent = extent;
    }

    public void setSealEntity(GameEntity sealEntity) {
        this.sealEntity = sealEntity;
        this.sealEntityUniqueId = sealEntity.getUniqueID();
    }

    public GameEntity getSealEntity() {
        return sealEntity;
    }
}
