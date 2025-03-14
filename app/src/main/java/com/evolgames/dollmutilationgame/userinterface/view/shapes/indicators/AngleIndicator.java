package com.evolgames.dollmutilationgame.userinterface.view.shapes.indicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.view.shapes.points.ControllerPointImage;
import com.evolgames.dollmutilationgame.scenes.EditorScene;
import com.evolgames.dollmutilationgame.utilities.MathUtils;

public abstract class AngleIndicator extends TurnAroundIndicator {
    private final ControllerPointImage limit;
    protected float angle = 0;

    public AngleIndicator(Vector2 begin, EditorScene scene, float length) {
        this(begin, scene, length, 0);
    }

    public AngleIndicator(Vector2 begin, EditorScene scene, float length, int size) {
        super(begin, scene, length, size);
        limit =
                new ControllerPointImage(ResourceManager.getInstance().diamondTextureRegion, end) {
                    @Override
                    protected void performControl(float dx, float dy) {
                        AngleIndicator.this.onControllerMoved(5 * dx, 5 * dy);
                    }
                };

        scene.getUserInterface().addElement(limit);
    }

    public void updateZoom(float zoom) {
        this.limit.updateZoom(zoom);
    }

    public ControllerPointImage getLimit() {
        return limit;
    }

    @Override
    public void updateEnd(float x, float y) {
        super.updateEnd(x, y);
        updateLimit(end.x, end.y);
        if(begin.dst(x,y)>5f){
            validated = true;
        }
    }

    public void updateDirection(Vector2 direction) {
        this.direction = direction;
        this.end = begin.cpy().add(direction.x * length, direction.y * length);
        super.updateEnd(end.x, end.y);
    }

    public void turnAround(float dA) {
        angle += dA;
        direction =
                MathUtils.getRotatedVectorByRadianAngle(
                        new Vector2(1, 0), angle * MathUtils.degreesToRadians);
        updateEnd(begin.x + direction.x * length, begin.y + direction.y * length);
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

    public void drawLimit() {
        updateLimit(end.x, end.y);
    }

    public float getAngleInDegrees() {
        return angle;
    }

    @Override
    public void onControllerMoved(float dx, float dy) {
        onTurnAroundCommand(dy);
    }

    public void updateLimit(float x, float y) {
        limit.setPosition(x, y);
        limit.setUpdated(true);
    }

    @Override
    public void detach() {
        super.detach();
        creationScene.getUserInterface().removeElement(limit);
    }
}
