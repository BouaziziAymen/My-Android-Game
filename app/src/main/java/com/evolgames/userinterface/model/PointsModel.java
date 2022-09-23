package com.evolgames.userinterface.model;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.properties.Properties;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.userinterface.view.Color;
import com.evolgames.userinterface.view.Colors;
import com.evolgames.userinterface.view.shapes.PointsShape;

import java.util.ArrayList;
import java.util.List;

public abstract class PointsModel<T extends Properties> extends ProperModel<T> {

    private List<Vector2> points;
    private Vector2[] outlinePoints;
    private PointsShape pointsShape;
    private Vector2 center;

    PointsModel(String name) {
        super(name);
        this.points = new ArrayList<>();
    }

    public Vector2 getCenter() {
        return center;
    }

    public void setCenter(Vector2 center) {
        this.center = center;
    }

    public abstract boolean test(Vector2 movedPoint, float dx, float dy);

    public abstract boolean test(float x, float y);

    public abstract boolean test(List<Vector2> points);


    public void select() {
        if (pointsShape != null) {
            pointsShape.setLineLoopColor(Colors.palette1_light_green);
            pointsShape.bringToFront();
        }
    }
    public void partialSelect() {
        if (pointsShape != null) {
            pointsShape.setLineLoopColor(Colors.palette1_light_green);
            pointsShape.bringToBackground();
        }
    }


    public void deselect() {
        if (pointsShape != null) {
            pointsShape.setLineLoopColor(1, 1, 1);
        }
    }

    public PointsShape getPointsShape() {
        return pointsShape;
    }

    public void setPointsShape(PointsShape pointsShape) {
        this.pointsShape = pointsShape;
        pointsShape.setShapePointsModel(this);
    }

    public List<Vector2> getPoints() {
        return points;
    }

    public void setPoints(List<Vector2> points) {
        this.points = points;
    }

    public Vector2[] getOutlinePoints() {
        return outlinePoints;
    }

    public void setOutlinePoints(Vector2[] points) {
        this.outlinePoints = points;
    }

    public void onShapeUpdated() {
        updateOutlinePoints();

    }

    private void updateOutlinePoints() {
        Vector2[] pointsArray = points.toArray(new Vector2[0]);
        if(pointsArray.length>=2) {
            pointsArray = GeometryUtils.hullFinder.findConvexHull(pointsArray);
            setOutlinePoints(pointsArray);
        }
    }

}
