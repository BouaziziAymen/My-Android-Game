package com.evolgames.entities.cut;

import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class PointsFreshCut extends FreshCut{
    private final List<Vector2> points;

    public PointsFreshCut(List<Vector2> pointsData, float length, int limit, Vector2 velocity) {
        super(length,limit, velocity);
        this.points = pointsData;
    }

    public List<Vector2> getPoints() {
        return points;
    }

}
