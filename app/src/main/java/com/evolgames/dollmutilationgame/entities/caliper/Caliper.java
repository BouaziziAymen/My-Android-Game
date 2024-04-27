package com.evolgames.dollmutilationgame.entities.caliper; // NAME: Justin Ward

// ALGORITHM PAPER: Rotating Calipers
// COURSE TITLE: Data Structures and Algorithms
// COURSE NUMBER:CPSC 4355
// TERM: Summer 2019

import com.badlogic.gdx.math.Vector2;

public final class Caliper {
    // Generates the minimum bounding box of the given polygon
    // Operates in O(n log n + m^2) time
    // where n is the number of random points
    // and m is the number of edges in the convex hull
    public static Rectangle minimumBox(Polygon polygon) {
        Rectangle[] rects = new Rectangle[polygon.edgeCount()];

        // Iterate over the edges of the convex hull
        for (int i = 0; i < polygon.edgeCount(); i++) {
            Vector2 edge = polygon.getEdge(i);
            // Rotate the polygon so that the current edge is parallel to a major axis
            // The y-Axis in this use case
            float theta = (float) Math.acos(edge.cpy().nor().y);
            polygon.rotate(theta);
            // Calculate a bounding box
            rects[i] = boundingBox(polygon);
            polygon.rotate(-theta);
            rects[i].rotate(-theta, polygon.getCenter());
        }

        float minArea = Float.MAX_VALUE;
        Rectangle box = rects[0];

        // Find the bounding box with the smallest area, this is the minimum bounding box
        for (int i = 0; i < rects.length; i++) {
            float area = rects[i].area();
            if (area < minArea) {
                minArea = area;
                box = rects[i];
            }
        }

        // We're done!  ♪~ ᕕ(ᐛ)ᕗ
        return box;
    }

    public static Rectangle boundingBox(Polygon polygon) {
        float minX = Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;
        float maxY = Float.MIN_VALUE;

        for (int i = 0; i < polygon.pointCount(); i++) {
            Vector2 p = polygon.getPoint(i);
            if (minX > p.x) minX = p.x;
            if (maxX < p.x) maxX = p.x;
            if (minY > p.y) minY = p.y;
            if (maxY < p.y) maxY = p.y;
        }
        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }
}
