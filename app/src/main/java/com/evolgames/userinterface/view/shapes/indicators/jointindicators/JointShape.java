package com.evolgames.userinterface.view.shapes.indicators.jointindicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.scenes.EditorScene;
import com.evolgames.userinterface.model.jointmodels.JointModel;
import com.evolgames.userinterface.view.EditorUserInterface;
import com.evolgames.userinterface.view.basics.Container;
import com.evolgames.userinterface.view.shapes.indicators.LineShape;
import com.evolgames.userinterface.view.shapes.indicators.MovablesContainer;
import com.evolgames.userinterface.view.shapes.points.JointPointImage;
import com.evolgames.userinterface.view.shapes.points.PointImage;

import org.andengine.opengl.texture.region.TextureRegion;

import java.util.ArrayList;
import java.util.List;

public class JointShape extends LineShape implements MovablesContainer {
    final JointPointImage endPoint;
    final JointPointImage beginPoint;
    private final EditorUserInterface editorUserInterface;
    protected Container container;
    protected JointModel model;

    public JointShape(
            EditorUserInterface editorUserInterface,
            Vector2 begin,
            EditorScene scene,
            TextureRegion textureRegion) {
        super(begin, scene);
        beginPoint = new JointPointImage(this, textureRegion, this.begin);
        endPoint = new JointPointImage(this, textureRegion, this.end);

        this.beginPoint.setMoveAction(
                () -> {
                    Vector2 point = beginPoint.getPoint();
                    JointShape.this.onBeginPointMoved(point.x, point.y);
                });
        this.endPoint.setMoveAction(
                () -> {
                    Vector2 point = endPoint.getPoint();
                    JointShape.this.onEndPointMoved(point.x, point.y);
                });

        container = new Container();
        container.addElement(beginPoint);
        container.addElement(endPoint);
        release();
        this.editorUserInterface = editorUserInterface;
        this.editorUserInterface.addElement(container);
    }

    @Override
    public void setColor(float r, float g, float b) {
        super.setColor(r, g, b);
        endPoint.setColor(r, g, b);
        beginPoint.setColor(r, g, b);
    }

    public void select() {
        selected = true;
        setColor(0, 1, 0);
        container.setUpdated(true);
    }

    public void release() {
        selected = false;
        setColor(1, 1, 1);
        container.setUpdated(true);
    }

    @Override
    public void updateEnd(float x, float y) {
        super.updateEnd(x, y);
        endPoint.setPosition(end.x, end.y);
        if (selected) {
            select();
        } else {
            release();
        }
        onEndPointMoved(x, y);
    }

    public Vector2 getBegin() {
        return begin;
    }

    public Vector2 getEnd() {
        return end;
    }

    public List<PointImage> getMovables(boolean moveLimits) {
        ArrayList<PointImage> result = new ArrayList<>();
        result.add(beginPoint);
        result.add(endPoint);
        return result;
    }

    public void detach() {
        super.detach();
        editorUserInterface.removeElement(container);
    }

    void onEndPointMoved(float x, float y) {
        this.model.getLocalAnchorB().set(x, y);
    }

    public void onBeginPointMoved(float x, float y) {
        this.model.getLocalAnchorA().set(x, y);
    }
}
