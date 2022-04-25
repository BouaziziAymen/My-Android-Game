package com.evolgames.userinterface.view.shapes.indicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.helpers.utilities.MathUtils;
import com.evolgames.scenes.GameScene;
import com.evolgames.userinterface.view.shapes.points.ControllerPointImage;

public abstract class AngleIndicator extends TurnableIndicator {
    protected float angle = 0;
    public ControllerPointImage getLimit() {
        return limit;
    }

    private final ControllerPointImage limit;


    public AngleIndicator(Vector2 begin, GameScene scene, float length) {
   this(begin,scene,length,0);
    }
    public AngleIndicator(Vector2 begin, GameScene scene, float length,int size) {
        super(begin, scene, length,size);
        limit = new ControllerPointImage(ResourceManager.getInstance().diamondTextureRegion,end) {
            @Override
            protected void performControl(float dx, float dy) {
                AngleIndicator.this.onControllerMoved(dx,dy);
            }
        };

        scene.getUserInterface().addElement(limit);
    }


    @Override
    public void onUpdated(float x, float y) {
        super.onUpdated(x, y);
        updateLimit(end.x,end.y);
        angle = (float) (MathUtils.radiansToDegrees*Math.atan2(dir.y,dir.x));
    }


    public void turnAround(float dA) {
       dir.set(1,0);
        angle += dA;
        Vector2 newDir = MathUtils.getRotatedVectorByRadianAngle(dir,angle*MathUtils.degreesToRadians);
        onUpdated(begin.x+newDir.x*length,begin.y+newDir.y*length);

    }

    @Override
    public void setBegin(float x, float y) {
        super.setBegin(x, y);
        drawLimit();
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        limit.setVisible(b);
    }

    public void drawLimit(){
        updateLimit(end.x,end.y);
    }


    public float getAngle() {
        return angle;
    }

    @Override
    public void onControllerMoved(float dx, float dy) {
        onTurnAroundCommand(dy);
    }

   public void updateLimit(float x, float y){
        limit.setPosition(x,y);
        limit.setUpdated(true);
   }

    @Override
    public void detach() {
        super.detach();
        creationScene.getUserInterface().removeElement(limit);
    }


}
