package com.evolgames.helpers;

import com.badlogic.gdx.math.Vector2;

import org.andengine.extension.physics.box2d.util.hull.QuickHull;

import java.util.Arrays;

public class Hull extends QuickHull {
    public Vector2[] findConvexHull(Vector2[] vertices){
        computeHull(vertices);
        return Arrays.copyOfRange(mVertices,0,getHullPointsNumber());
    }
    private int getHullPointsNumber(){
        return mHullVertexCount;
    }
}
