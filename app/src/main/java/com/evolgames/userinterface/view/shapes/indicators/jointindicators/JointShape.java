package com.evolgames.userinterface.view.shapes.indicators.jointindicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.scenes.GameScene;
import com.evolgames.userinterface.view.UserInterface;
import com.evolgames.userinterface.view.basics.Container;
import com.evolgames.userinterface.view.shapes.indicators.MovablesContainer;
import com.evolgames.userinterface.view.shapes.points.JointPointImage;
import com.evolgames.userinterface.view.shapes.points.PointImage;
import com.evolgames.userinterface.view.shapes.indicators.LineShape;

import org.andengine.opengl.texture.region.TextureRegion;

import java.util.ArrayList;
import java.util.List;

public class JointShape extends LineShape implements MovablesContainer {
    final JointPointImage endPoint;
    final JointPointImage beginPoint;
    private final UserInterface userInterface;
    protected Container container;


    public JointShape(UserInterface userInterface, Vector2 begin, GameScene scene, TextureRegion textureRegion) {
        super(begin, scene);
        beginPoint = new JointPointImage(this, textureRegion, this.begin);
        endPoint = new JointPointImage(this, textureRegion, this.end);

        container = new Container();
        container.addElement(beginPoint);
        container.addElement(endPoint);
        release();
        this.userInterface = userInterface;
        this.userInterface.addElement(container);
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
        if (selected) select();
        else release();
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
        userInterface.removeElement(container);
    }



}