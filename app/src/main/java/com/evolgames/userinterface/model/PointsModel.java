package com.evolgames.userinterface.model;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.properties.Properties;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.userinterface.view.UserInterface;
import com.evolgames.userinterface.view.shapes.PointsShape;
import com.evolgames.userinterface.view.shapes.points.PointImage;
import com.evolgames.userinterface.view.shapes.points.ReferencePointImage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class PointsModel<T extends Properties> extends OutlineModel<T> {

    private List<Vector2> points;

    PointsModel(String name) {
        super(name);
        this.points = new ArrayList<>();
    }


    public abstract boolean test(Vector2 movedPoint, float dx, float dy);

    public abstract boolean test(float x, float y);

    public abstract boolean test(List<Vector2> points);

    public PointsShape getPointsShape() {
        return (PointsShape) outlineShape;
    }

    public void setPointsShape(PointsShape outlineShape) {
        this.outlineShape = outlineShape;
        outlineShape.setOutlineModel(this);
    }

    public List<Vector2> getModelPoints() {
        return points;
    }

    public void setPoints(List<Vector2> points) {
        this.points = points;
        updateOutlinePoints();
    }

    public void updateOutlinePoints() {
        Vector2[] pointsArray = points.toArray(new Vector2[0]);
        if (pointsArray.length >= 2) {
            pointsArray = GeometryUtils.hullFinder.findConvexHull(pointsArray);
            setOutlinePoints(pointsArray);
        }
    }

    public void addPoint(Vector2 vector2) {
        points.add(vector2);
        updateOutlinePoints();
    }

    public void remove(Vector2 point) {
        points.remove(point);
        updateOutlinePoints();
    }

    public List<Vector2> getReferencePoints() {
        return getPointsShape().getCenterPointImages().stream().map(PointImage::getPoint).collect(Collectors.toList());
    }

    public void addReferencePoint(Vector2 center, UserInterface userInterface) {
        ReferencePointImage centerPointImage = new ReferencePointImage(center);
       getPointsShape().getCenterPointImages().add(centerPointImage);
       userInterface.addReferencePoint(centerPointImage);
    }

}
