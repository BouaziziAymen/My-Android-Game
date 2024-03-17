package com.evolgames.userinterface.view.shapes.points;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.userinterface.control.behaviors.actions.Action;
import com.evolgames.userinterface.view.shapes.indicators.jointindicators.JointShape;

import org.andengine.opengl.texture.region.ITextureRegion;

public class JointPointImage extends PointImage {

    private final JointShape jointShape;
    private Action moveAction;

    public JointPointImage(JointShape jointShape, ITextureRegion pTextureRegion, Vector2 p) {
        super(pTextureRegion, p);
        this.jointShape = jointShape;
    }

    public void setMoveAction(Action moveAction) {
        this.moveAction = moveAction;
    }

    @Override
    public void onControllerMoved(float dx, float dy) {
        super.onControllerMoved(dx, dy);
        jointShape.drawLine();
        if (moveAction != null) {
            moveAction.performAction();
        }
    }
}
