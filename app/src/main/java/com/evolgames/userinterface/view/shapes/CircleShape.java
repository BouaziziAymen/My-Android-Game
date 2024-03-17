package com.evolgames.userinterface.view.shapes;

import com.evolgames.activity.ResourceManager;

import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.LineLoop;

public class CircleShape {
    final float[][] circle_vertices;
    private final IEntity parent;
    LineLoop lineLoop;
    private int radius = 16;

    public CircleShape(IEntity parent) {
        this.parent = parent;
        circle_vertices = new float[16][2];
        for (int i = 0; i < 16; i++) {
            float theta = (float) (2 * Math.PI * (float) i / 16f); // get the current angle
            circle_vertices[i][0] = (float) (Math.cos(theta)); // calculate the x component
            circle_vertices[i][1] = (float) (Math.sin(theta)); // calculate the y component
        }
        onUpdated(0, 0);
    }

    public void setVisible(boolean visible) {
        lineLoop.setVisible(visible);
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void onUpdated(float x, float y) {
        if (lineLoop != null) lineLoop.detachSelf();
        lineLoop = new LineLoop(x, y, 120, ResourceManager.getInstance().vbom);
        lineLoop.setColor(1, 0, 0);
        lineLoop.setLineWidth(3f);
        parent.attachChild(lineLoop);
        for (int i = 0; i < 16; i++) {
            lineLoop.add(radius * circle_vertices[i][0], radius * circle_vertices[i][1]);
        }
    }
}
