package com.evolgames.entities.properties;

import com.badlogic.gdx.math.Vector2;

public class AmmoProperties extends Properties{

    private final Vector2 ammoOrigin;
    private final Vector2 ammoDirection;

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

    public Vector2 getAmmoDirection() {
        return ammoDirection;
    }
}
