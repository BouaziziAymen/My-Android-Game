package com.evolgames.entities.cut;

import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class PointsFreshCut extends FreshCut{
    private final List<Vector2> points;
    private final Vector2 splashVelocity;

    public PointsFreshCut(List<Vector2> pointsData, float length, int limit, Vector2 velocity) {
        super(length,limit);
        this.points = pointsData;
        this.splashVelocity = velocity;
    }

    public List<Vector2> getPoints() {
        return points;
    }

    public Vector2 getSplashVelocity() {
        return splashVelocity;
    }
}
