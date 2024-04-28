package com.evolgames.dollmutilationgame.entities.serialization.infos;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.entities.usage.Muzzle;
import com.evolgames.dollmutilationgame.entities.usage.Seal;

public class LiquidSourceInfo {
    private Vector2 liquidSourceOrigin;
    private Vector2 liquidDirection;
    private transient GameEntity containerEntity;
    private String containerEntityUniqueId;
    private int liquid;
    private float extent;
    private String sealEntityUniqueId;
    private Seal seal;
    private String fireSourceInfoUniqueId;

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
        if(this.containerEntity!=null) {
            this.containerEntityUniqueId = containerEntity.getUniqueID();
        }
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


    public String getLiquidSourceInfoUniqueId() {
        return this.fireSourceInfoUniqueId;
    }

    public void setLiquidSourceInfoUniqueId(String fireSourceInfoUniqueId) {
        this.fireSourceInfoUniqueId = fireSourceInfoUniqueId;
    }

    public void setSeal(Seal seal) {
        this.seal = seal;
        if(this.seal!=null&&seal.getSealEntity()!=null) {
            this.sealEntityUniqueId = seal.getSealEntity().getUniqueID();
        }
    }

    public Seal getSeal() {
        return seal;
    }

}
