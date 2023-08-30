package com.evolgames.userinterface.model.toolmodels;

import com.evolgames.entities.GameEntity;
import com.evolgames.entities.properties.BombProperties;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.BombShape;

public class BombModel extends ProperModel<BombProperties> {

    private BombShape bombShape;
    private final int bodyId;
    private final int bombId;
    private GameEntity gameEntity;
    public BombModel(int bodyId, int bombId) {
        super("Bomb"+bombId);
        this.bodyId = bodyId;
        this.bombId = bombId;
    }
    public BombModel(int bodyId, int bombId, BombShape bombShape) {
        super("Bomb"+bombId);
        this.bodyId = bodyId;
        this.bombId = bombId;
        this.bombShape = bombShape;
        this.properties = new BombProperties();
    }

    public BombShape getBombShape() {
        return bombShape;
    }

    public int getBodyId() {
        return bodyId;
    }

    public int getBombId() {
        return bombId;
    }

    public void setBombShape(BombShape bombShape) {
        this.bombShape = bombShape;
    }

    public GameEntity getGameEntity() {
        return gameEntity;
    }

    public void setGameEntity(GameEntity gameEntity) {
        this.gameEntity = gameEntity;
    }
}
