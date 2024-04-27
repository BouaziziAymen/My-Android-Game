package com.evolgames.dollmutilationgame.entities.cut;

import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class PointsFreshCut extends FreshCut {
    private List<CutPoint> points;


    @SuppressWarnings("unused")
    public PointsFreshCut() {
    }

    public PointsFreshCut(List<CutPoint> pointsData, float length, int limit, Vector2 velocity) {
        super(length, limit, velocity);
        this.points = pointsData;
    }

    public List<CutPoint> getPoints() {
        return points;
    }
}
