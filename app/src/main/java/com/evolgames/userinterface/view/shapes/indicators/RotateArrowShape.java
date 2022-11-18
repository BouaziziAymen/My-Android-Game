package com.evolgames.userinterface.view.shapes.indicators;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.scenes.GameScene;
import com.evolgames.userinterface.model.PointsModel;

import java.util.ArrayList;

public class RotateArrowShape extends FixedLengthArrowShape {
    private final ArrayList<Vector2> points;
    private final Vector2 center;
    private final PointsModel<?> shapePointsModel;
    public RotateArrowShape(Vector2 begin, PointsModel<?> shapePointsModel, GameScene scene, float length) {
        super(begin, scene, length);
        points = new ArrayList<>();
        for(Vector2 p: shapePointsModel.getModelPoints()) {
            points.add(p.cpy());
        }

            center = begin.cpy();

        Log.e("mother",""+center);
        this.shapePointsModel = shapePointsModel;
        if(shapePointsModel.getCenter()!=null)centerOfShape= shapePointsModel.getCenter().cpy();
    }
    Vector2 centerOfShape;

    @Override
    public void updateEnd(float x, float y) {
        super.updateEnd(x, y);
        float angle = (float) Math.atan2(direction.y, direction.x);
        ArrayList<Vector2> newPoints = new ArrayList<>();

        if(shapePointsModel.getCenter()!=null) {
            Vector2 center = centerOfShape.cpy();
            GeometryUtils.rotate(center, angle,this.center);
            shapePointsModel.setCenter(center);
            shapePointsModel.getPointsShape().getCenterPointImage().setPosition(center.x,center.y);
        }


        for (Vector2 p : points) {
            Vector2 pp = p.cpy();
            GeometryUtils.rotate(pp, angle,center);
            newPoints.add(pp);
        }

        if(!shapePointsModel.test(newPoints))return;


        shapePointsModel.getPointsShape().detachPointImages();
        shapePointsModel.setPoints(newPoints);
        shapePointsModel.getPointsShape().onModelUpdated();
    }



}

