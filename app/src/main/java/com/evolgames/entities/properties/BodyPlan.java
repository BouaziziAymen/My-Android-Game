package com.evolgames.entities.properties;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.BlockA;

import java.util.ArrayList;

public class BodyPlan {
    private BodyDef.BodyType type;
    private GameEntity gameEntity;
    private Vector2 linearVelocity;
    private float angularVelocity;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getRot() {


        return rot;
    }

    private float x, y,rot;

    public Vector2 getLinearVelocity() {
        return linearVelocity;
    }

    public void setLinearVelocity(Vector2 linearVelocity) {
        this.linearVelocity = linearVelocity;
    }

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public BodyPlan(float x, float y, float rot, GameEntity mesh, BodyDef.BodyType type, Vector2 linearVelocity, float angularVelocity){

        this.type = type;
        this.gameEntity = mesh;
        this.x = x;
        this.y = y;
        this.rot = rot;
        this.linearVelocity = linearVelocity;
        this.angularVelocity = angularVelocity;


    }

    public BodyPlan(float x, float y, float rot, GameEntity mesh, BodyDef.BodyType type){
        this.type = type;
        this.gameEntity = mesh;
        this.x = x;
        this.y = y;
        this.rot = rot;


    }
    public ArrayList<BlockA> getBlocks() {
        return getGameEntity().getBlocks();
    }

    public BodyDef.BodyType getType() {
        return type;
    }

    public GameEntity getGameEntity() {
        return gameEntity;
    }

}
