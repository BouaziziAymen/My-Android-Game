package com.evolgames.userinterface.view.shapes.indicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.gameengine.ResourceManager;
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
    public void updateEnd(float x, float y) {
        super.updateEnd(x, y);
        updateLimit(end.x,end.y);
        angle = (float) (MathUtils.radiansToDegrees*Math.atan2(direction.y, direction.x));
    }

    public void updateDirection(Vector2 direction){
        this.direction = direction;
        end = begin.cpy().add(direction.x*length,direction.y*length);
        updateEnd(end.x,end.y);
    }

    public void turnAround(float dA) {
        angle += dA;
        direction = MathUtils.getRotatedVectorByRadianAngle(new Vector2(1,0),angle*MathUtils.degreesToRadians);
        updateEnd(begin.x+direction.x*length,begin.y+direction.y*length);
    }

    @Override
    public void updateBegin(float x, float y) {
        super.updateBegin(x, y);
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


    public float getAngleInDegrees() {
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
