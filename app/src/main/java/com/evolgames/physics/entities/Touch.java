package com.evolgames.physics.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.entities.blocks.LayerBlock;

public class Touch {
    private final Vector2 point;
    public LayerBlock block1;
    public LayerBlock block2;
    public Body body1;
    public Body body2;

    public Touch(LayerBlock block1, LayerBlock block2, Body body1, Body body2, Vector2 point) {
        this.block1 = block1;
        this.block2 = block2;
        this.body1 = body1;
        this.body2 = body2;
        this.point = point;
    }

    public boolean isEquivalent(Body body1, Body body2) {
        return ((this.body1 == body1 && this.body2 == body2)
                || (this.body1 == body2 && this.body2 == body1));
    }
    public boolean isEquivalent(LayerBlock block1, LayerBlock block2) {
        return ((this.block1 == block1 && this.block2 == block2)
                || (this.block1 == block2 && this.block2 == block1));
    }

    public Vector2 getPoint() {
        return point;
    }
}
