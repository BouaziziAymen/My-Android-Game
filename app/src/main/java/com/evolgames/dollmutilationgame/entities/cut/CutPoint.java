package com.evolgames.dollmutilationgame.entities.cut;

import com.badlogic.gdx.math.Vector2;

public class CutPoint {
    private Vector2 point;
    private float weight;


    @SuppressWarnings("unused")
    public CutPoint() {
    }

    public CutPoint(Vector2 point, float weight) {
        this.point = point;
        this.weight = weight;
    }

    public Vector2 getPoint() {
        return point;
    }

    public void setPoint(Vector2 point) {
        this.point = point;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
