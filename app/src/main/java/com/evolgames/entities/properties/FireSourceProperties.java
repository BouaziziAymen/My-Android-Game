package com.evolgames.entities.properties;

import com.badlogic.gdx.math.Vector2;

public class FireSourceProperties  extends Properties {

    private Vector2 fireSourceOrigin;
    private Vector2 fireSourceDirection;

    public FireSourceProperties() {}

    public FireSourceProperties(Vector2 begin, Vector2 direction) {
        this.fireSourceOrigin = begin.cpy();
        this.fireSourceDirection = direction.cpy();
    }

    @Override
    public Properties copy() {
        return null;
    }

    public Vector2 getFireSourceOrigin() {
        return fireSourceOrigin;
    }

    public void setFireSourceOrigin(Vector2 fireSourceOrigin) {
        this.fireSourceOrigin = fireSourceOrigin;
    }

    public Vector2 getFireSourceDirection() {
        return fireSourceDirection;
    }

    public void setFireSourceDirection(Vector2 fireSourceDirection) {
        this.fireSourceDirection = fireSourceDirection;
    }

}
