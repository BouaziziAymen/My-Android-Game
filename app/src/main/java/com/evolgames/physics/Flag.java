package com.evolgames.physics;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.blocks.BlockA;

public class Flag implements Comparable<Flag> {
    FlagType type;
    private Vector2 point;
    private float fraction;
    private BlockA block;

    public Flag(BlockA block, Vector2 point, FlagType type) {
        this.block = block;
        this.point = point;
        this.type = type;
    }

    public Flag(BlockA block, Vector2 point, float fraction, FlagType type) {
        this.block = block;
        this.point = point;
        this.type = type;
     if(type==FlagType.Entering){
         this.fraction = fraction;
     } else {
         this.fraction = 1 - fraction;
     }
    }

    public Vector2 getPoint() {
        return point;
    }

    public float getFraction() {
        return fraction;
    }

    public BlockA getBlock() {
        return block;
    }

    public FlagType getType() {
        return type;
    }

    public String toString(){
        return point.toString()+type;
    }

    @Override
    public int compareTo(Flag o) {
        if(fraction==o.fraction)return 0;
        else if(fraction<o.fraction)return -1;
        else return 1;
    }
}
