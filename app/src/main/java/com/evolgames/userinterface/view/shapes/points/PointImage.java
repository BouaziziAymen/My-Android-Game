package com.evolgames.userinterface.view.shapes.points;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.userinterface.view.Color;
import com.evolgames.userinterface.view.Colors;
import com.evolgames.userinterface.view.inputs.controllers.Movable;
import com.evolgames.userinterface.view.shapes.PointsShape;

import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;

public class PointImage extends SceneImage implements Movable {
    protected Vector2 point;
    protected boolean selected;
    private PointsShape pointsShape;

    public PointImage(ITextureRegion pTextureRegion, Vector2 point) {
        super(pTextureRegion);
        this.point = point;
        setPosition(point.x, point.y);
    }

    @Override
    public void setScale(float pScaleX, float pScaleY) {
        super.setScale(pScaleX, pScaleY);
        this.centerPosition();
    }

    private void centerPosition() {
        super.setPosition(point.x - scaleX * getWidth() / 2, point.y - scaleY * getHeight() / 2);
    }

    @Override
    public void setPosition(float pX, float pY) {
        point.set(pX, pY);
        centerPosition();
    }

    public Vector2 getPoint() {
        return point;
    }

    public void setPoint(Vector2 point) {
        this.point = point;
    }

    @Override
    public void onControllerMoved(float dx, float dy) {
        float x = point.x + dx;
        float y = point.y + dy;
        setPosition(x, y);
        setUpdated(true);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (!visible) {
            release();
        }
    }

    @Override
    public boolean onTouchHud(TouchEvent pTouchEvent) {
        return false;
    }

    public void select() {
        Color green = Colors.palette1_light_green;
        setColor(green);
        selected = true;
    }

    public void release() {
        selected = false;
        setColor(1, 1, 1);
    }

    public void undoDoubleSelect() {
        Color green = Colors.palette1_light_green;
        if (selected) setColor(green);
        else setColor(1, 1, 1);
    }

    public void doubleSelect() {
        Color blue = Colors.palette1_blue;
        setColor(blue);
    }

    @Override
    public void updateZoom(float pZoomFactor) {
        super.updateZoom(pZoomFactor);
        this.setScale(1f / pZoomFactor, 1f / pZoomFactor);
    }

    public PointsShape getPointsShape() {
        return pointsShape;
    }

    public void setPointsShape(PointsShape pointsShape) {
        this.pointsShape = pointsShape;
    }
}
