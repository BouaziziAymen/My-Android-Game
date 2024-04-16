package com.evolgames.userinterface.model;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.properties.Properties;
import com.evolgames.userinterface.view.shapes.PointsShape;
import com.evolgames.utilities.GeometryUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class PointsModel<T extends Properties> extends OutlineModel<T> {

    protected boolean show = true;
    private List<Vector2> points;

    private Vector2 center;

    PointsModel(String name) {
        super(name);
        this.points = new ArrayList<>();
    }

    public abstract boolean testMove(Vector2 movedPoint, float dx, float dy);

    public abstract boolean testAdd(float x, float y);

    public abstract boolean testPoints(List<Vector2> points);

    public PointsShape getPointsShape() {
        return (PointsShape) outlineShape;
    }

    public void setPointsShape(PointsShape outlineShape) {
        this.outlineShape = outlineShape;
        outlineShape.setOutlineModel(this);
    }

    public List<Vector2> getPoints() {
        return points;
    }

    public void setPoints(List<Vector2> points) {
        this.points = points;
        updateOutlinePoints();
    }

    public void updateOutlinePoints() {
        Vector2[] pointsArray = points.toArray(new Vector2[0]);
        if (pointsArray.length >= 3) {
            pointsArray = GeometryUtils.hullFinder.findConvexHull(pointsArray);
            setOutlinePoints(pointsArray);
        } else {
            setOutlinePoints(new Vector2[0]);
        }
    }

    public void addPoint(Vector2 vector2) {
        points.add(vector2);
        updateOutlinePoints();
    }

    public void remove(Vector2 point, boolean ref) {
        if(!ref) {
            points.remove(point);
            updateOutlinePoints();
        }
    }

    public Vector2 getCenter() {
        return center;
    }

    public void setCenter(Vector2 center) {
        this.center = center;
    }

    public void show() {
        this.show = true;
    }

    public void hide() {
        this.show = false;
    }

    public boolean isShow() {
        return show;
    }
}
