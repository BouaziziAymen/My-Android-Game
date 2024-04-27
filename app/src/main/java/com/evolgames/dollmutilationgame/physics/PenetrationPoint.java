package com.evolgames.dollmutilationgame.physics;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.entities.blocks.LayerBlock;

public class PenetrationPoint implements Comparable<PenetrationPoint> {
    private final GameEntity entity;
    private final LayerBlock block;
    private final Vector2 point;
    private final boolean entering;
    private final float value;
    private final float weight;

    public PenetrationPoint(
            GameEntity entity,
            LayerBlock block,
            Vector2 point,
            float value,
            float weight,
            boolean entering) {
        this.entity = entity;
        this.block = block;
        this.point = point;
        this.entering = entering;
        this.value = value;
        this.weight = weight;
    }

    public GameEntity getEntity() {
        return entity;
    }

    public LayerBlock getBlock() {
        return block;
    }

    public Vector2 getPoint() {
        return point;
    }

    public boolean isEntering() {
        return entering;
    }

    public float getWeight() {
        return weight;
    }

    @Override
    public int compareTo(PenetrationPoint o) {
        return Float.compare(this.value, o.value);
    }
}
