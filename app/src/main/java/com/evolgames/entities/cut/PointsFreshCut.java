package com.evolgames.entities.cut;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.blocks.LayerBlock;

import java.util.List;

public class PointsFreshCut extends FreshCut{
    private final float length;
    private final List<Vector2> points;
    private final Vector2 splashVelocity;

    public PointsFreshCut(List<Vector2> pointsData, float length, Vector2 velocity, LayerBlock block) {
        this.points = pointsData;
        this.length = length;
        this.splashVelocity = velocity;
        this.limit =  computeLimit(block);
    }

    public PointsFreshCut(List<Vector2> pointsData, float length, Vector2 velocity, float limit) {
        this.points = pointsData;
        this.length = length;
        this.splashVelocity = velocity;
        this.limit = limit;
    }

    @Override
    public float getLength() {
        return length;
    }

    public List<Vector2> getPoints() {
        return points;
    }

    public Vector2 getSplashVelocity() {
        return splashVelocity;
    }
}
