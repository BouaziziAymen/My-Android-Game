package com.evolgames.entities.properties;

import com.badlogic.gdx.math.Vector2;

public class AmmoProperties extends Properties {

    private Vector2 ammoOrigin;
    private Vector2 ammoDirection;

    public AmmoProperties() {
    }

    public AmmoProperties(Vector2 begin, Vector2 direction) {
        this.ammoOrigin = begin.cpy();
        this.ammoDirection = direction.cpy();
    }

    @Override
    public Properties copy() {
        return null;
    }

    public Vector2 getAmmoOrigin() {
        return ammoOrigin;
    }

    public void setAmmoOrigin(Vector2 ammoOrigin) {
        this.ammoOrigin = ammoOrigin;
    }

    public Vector2 getAmmoDirection() {
        return ammoDirection;
    }

    public void setAmmoDirection(Vector2 ammoDirection) {
        this.ammoDirection = ammoDirection;
    }
}
