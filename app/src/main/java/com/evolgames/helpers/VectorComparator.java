package com.evolgames.helpers;


import com.badlogic.gdx.math.Vector2;

import java.util.Comparator;

public class VectorComparator implements Comparator<Vector2> {
    @Override
    public int compare(Vector2 o1, Vector2 o2) {
        if(Math.atan2(o1.y, o1.x)<Math.atan2(o2.y, o2.x)) return -1;else return 1;
    }
}
