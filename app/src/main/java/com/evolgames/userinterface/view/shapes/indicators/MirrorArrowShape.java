package com.evolgames.userinterface.view.shapes.indicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.scenes.GameScene;
import com.evolgames.userinterface.model.PointsModel;

import java.util.ArrayList;

public class MirrorArrowShape extends DoubleInvertedArrowsShape {
    private PointsModel shapePointsModel;
    private ArrayList<Vector2> points;
    private Vector2 centerOfShape;
    public MirrorArrowShape(Vector2 begin, PointsModel shapePointsModel, ArrayList<Vector2> points, GameScene scene) {
        super(begin, scene);
        this.shapePointsModel = shapePointsModel;
        this.points = points;
        if(shapePointsModel.getCenter()!=null){
            centerOfShape = shapePointsModel.getCenter();
        }
    }

    @Override
    public void onUpdated(float x, float y) {
        super.onUpdated(x,y);
        ArrayList<Vector2> pointsArray = GeometryUtils.mirrorPoints(points,begin,end);
        if(shapePointsModel.getCenter()!=null){
         Vector2 mirroredCenter = GeometryUtils.mirrorPoint(centerOfShape,begin,end);
         shapePointsModel.setCenter(mirroredCenter);
         shapePointsModel.getPointsShape().getCenterPointImage().setPosition(mirroredCenter.x,mirroredCenter.y);
        }
        if(shapePointsModel.test(pointsArray)) {
            shapePointsModel.getPointsShape().detachPointImages();
            shapePointsModel.setPoints(pointsArray);
            shapePointsModel.getPointsShape().onModelUpdated();
        }
    }



}
