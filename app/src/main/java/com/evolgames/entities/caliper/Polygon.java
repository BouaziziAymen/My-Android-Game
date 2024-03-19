package com.evolgames.entities.caliper; // NAME: Justin Ward

// ALGORITHM PAPER: Rotating Calipers
// COURSE TITLE: Data Structures and Algorithms
// COURSE NUMBER:CPSC 4355
// TERM: Summer 2019

import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class Polygon {
    protected List<Vector2> points;
    protected Vector2 center;

    public Polygon() {
        center = new Vector2();
    }

    public Polygon(List<Vector2> points) {
        this.points = points;
        center = new Vector2();
        calcCenter();
    }


    public void rotate(float theta) {
        rotate(theta, center);
    }

    public void rotate(float theta, Vector2 pivot) {
        for (int i = 0; i < points.size(); i++) {
            Vector2 p = points.get(i);
            float x = p.x;
            float y = p.y;
            x -= pivot.x;
            y -= pivot.y;
            float tx = x;
            float sinTheta = (float) Math.sin(theta);
            float cosTheta = (float) Math.cos(theta);
            x = x * cosTheta - y * sinTheta;
            y = tx * sinTheta + y * cosTheta;
            x += pivot.x;
            y += pivot.y;
            p.set(x, y);
        }
    }

    public Vector2 getPoint(int index) {
        return points.get(index);
    }

    public Vector2 getCenter() {
        return center;
    }

    public Vector2 getEdge(int index) {
        int next = (index + 1) % pointCount();
        Vector2 p1 = points.get(index);
        Vector2 p2 = points.get(next);

        return p2.cpy().sub(p1);
    }

    protected void calcCenter() {
        float x = 0;
        float y = 0;
        int n = points.size();

        for (int i = 0; i < n; i++) {
            Vector2 p = points.get(i);
            x += p.x;
            y += p.y;
        }

        center.set(x / n, y / n);
    }

    public int pointCount() {
        return points.size();
    }

    public int edgeCount() {
        if (points.size() == 1) {
            return 0;
        } else if (points.size() == 2) {
            return 1;
        } else {
            return points.size();
        }
    }

    public boolean contains(Vector2 p) {
        for (int i = 0; i < pointCount(); i++) {
            Vector2 normal = getEdge(i).cpy().nor();
            p = p.cpy().sub(center);
            if (normal.dot(p) > 0) {
                return false;
            }
        }
        return true;
    }
}
