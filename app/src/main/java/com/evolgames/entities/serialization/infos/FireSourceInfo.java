package com.evolgames.entities.serialization.infos;

import com.badlogic.gdx.math.Vector2;

public class FireSourceInfo {
    private Vector2 fireSourceOrigin;
    private Vector2 fireDirection;

    public Vector2 getFireSourceOrigin() {
        return fireSourceOrigin;
    }

    public void setFireSourceOrigin(Vector2 fireSourceOrigin) {
        this.fireSourceOrigin = fireSourceOrigin;
    }

    public Vector2 getFireDirection() {
        return fireDirection;
    }

    public void setFireDirection(Vector2 fireDirection) {
        this.fireDirection = fireDirection;
    }
}
