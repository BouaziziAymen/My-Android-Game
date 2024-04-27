package com.evolgames.dollmutilationgame.physics;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.entities.blocks.LayerBlock;

public class Flag implements Comparable<Flag> {
    private final Vector2 point;
    private final float fraction;
    private final LayerBlock block;
    FlagType type;

    public Flag(LayerBlock block, Vector2 point, float fraction, FlagType type) {
        this.block = block;
        this.point = point;
        this.type = type;
        if (type == FlagType.Entering) {
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

    public LayerBlock getBlock() {
        return block;
    }

    public FlagType getType() {
        return type;
    }

    public String toString() {
        return point.toString() + type;
    }

    @Override
    public int compareTo(Flag o) {
        return Float.compare(fraction, o.fraction);
    }
}
