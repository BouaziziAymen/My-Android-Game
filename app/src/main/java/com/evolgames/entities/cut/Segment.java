package com.evolgames.entities.cut;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.blocks.LayerBlock;

public class Segment {
    private final LayerBlock block;
    private final Vector2 p1;
    private final Vector2 p2;
    CutType type;

    public Segment(Vector2 p1, Vector2 p2, LayerBlock block, CutType type) {
        this.p1 = p1;
        this.p2 = p2;
        this.block = block;
        this.type = type;
    }

    public LayerBlock getBlock() {
        return block;
    }

    public Vector2 getP1() {
        return p1;
    }

    public Vector2 getP2() {
        return p2;
    }

    public CutType getType() {
        return type;
    }
}
