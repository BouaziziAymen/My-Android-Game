package com.evolgames.userinterface.view.shapes.indicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.factories.VerticesFactory;
import com.evolgames.scenes.GameScene;
import com.evolgames.userinterface.model.PointsModel;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import java.util.ArrayList;

public class PolygonArrowShape extends ArrowShape {

    private final boolean fixedRadius;
    private final float radius;
    private PointsModel shapePointsModel;
    private int n;
    public PolygonArrowShape(Vector2 begin, PointsModel shapePointsModel, GameScene scene, int n) {
        super(begin, scene);
        this.shapePointsModel = shapePointsModel;
        this.n = n;
this.fixedRadius = false;
radius = 0;
    }
    public PolygonArrowShape(Vector2 begin, PointsModel shapePointsModel, GameScene scene, int n, float radius) {
        super(begin, scene);
        this.shapePointsModel = shapePointsModel;
        this.n = n;
        this.fixedRadius = true;
        this.radius = radius;
    }


    @Override
    public void onUpdated(float x, float y) {

        float d = (fixedRadius)?radius:begin.dst(x,y);
        Vector2 dir = Vector2Pool.obtain(end).sub(begin);
        float angle = (float) Math.atan2(dir.y, dir.x);
        Vector2Pool.recycle(dir);

        ArrayList<Vector2> newPoints = VerticesFactory.createPolygon(begin.x, begin.y, angle, d, d, n);
        if(shapePointsModel.test(newPoints)) {
            detachPointImages();
            super.onUpdated(x, y);
            shapePointsModel.setPoints(newPoints);
            shapePointsModel.getPointsShape().onModelUpdated();
        }

    }
    private void detachPointImages(){
        shapePointsModel.getPointsShape().detachPointImages();
    }

}
