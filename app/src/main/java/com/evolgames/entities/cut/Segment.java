package com.evolgames.entities.cut;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.blocks.BlockA;

public class Segment {
    private final BlockA block;
    private Vector2 p1;
    private Vector2 p2;
    CutType type;

    public BlockA getBlock() {
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

    public Segment(Vector2 p1, Vector2 p2, BlockA block, CutType type){
        this.p1 = p1;
        this.p2 = p2;
        this.block = block;
        this.type = type;
    }
}
