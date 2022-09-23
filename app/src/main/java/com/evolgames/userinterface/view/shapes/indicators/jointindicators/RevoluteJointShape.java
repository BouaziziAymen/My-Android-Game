package com.evolgames.userinterface.view.shapes.indicators.jointindicators;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.helpers.utilities.MathUtils;
import com.evolgames.scenes.GameScene;
import com.evolgames.userinterface.control.behaviors.actions.Action;
import com.evolgames.userinterface.view.Colors;
import com.evolgames.userinterface.view.UserInterface;
import com.evolgames.userinterface.view.shapes.points.PointImage;
import com.evolgames.userinterface.view.shapes.indicators.AngleIndicator;

import java.util.ArrayList;

public class RevoluteJointShape extends JointShape {


    private final AngleIndicator referenceAngleIndicator;
    private final AngleIndicator upperAngleIndicator;
    private final AngleIndicator lowerAngleIndicator;
    private final RevoluteJointDef jointDef;
    private final UserInterface userInterface;
    public RevoluteJointShape(RevoluteJointDef revoluteJointDef, GameScene scene, Vector2 begin) {
        super(scene.getUserInterface(), begin, scene, ResourceManager.getInstance().doubleDiskTextureRegion);
        this.jointDef = revoluteJointDef;
        this.userInterface = scene.getUserInterface();
        beginPoint.setMoveAction(new Action() {
            @Override
            public void performAction() {
                Vector2 point = beginPoint.getPoint();
                RevoluteJointShape.this.onBeginPointMoved(point.x, point.y);
            }
        });

        referenceAngleIndicator = new AngleIndicator(begin, scene, 48) {

            @Override
            public void onTurnAroundCommand(float dA) {
                turnAround(dA);
                RevoluteJointShape.this.onReferenceIndicatorTurned(dA);
            }
        };


        upperAngleIndicator = new AngleIndicator(begin, scene, 64) {

            @Override
            public void onTurnAroundCommand(float dA) {
                //upperAngle > lowerAngle
                if (dA > 0)
                    turnAround(dA);
                else {
                    float upperAngle = RevoluteJointShape.this.getUpperAngle();
                    float lowerAngle = RevoluteJointShape.this.getLowerAngle();
                    if (upperAngle + dA >= lowerAngle) super.turnAround(dA);
                    else {
                        turnAround(lowerAngle - upperAngle);
                    }
                }
                RevoluteJointShape.this.onUpperIndicatorTurned();
            }
        };

        lowerAngleIndicator = new AngleIndicator(begin, scene, 64) {
            @Override
            public void onTurnAroundCommand(float dA) {
                float upperAngle = RevoluteJointShape.this.getUpperAngle();
                float lowerAngle = RevoluteJointShape.this.getLowerAngle();
                if (dA < 0 || lowerAngle + dA <= upperAngle) turnAround(dA);
                else turnAround(upperAngle - lowerAngle);

                RevoluteJointShape.this.onLowerIndicatorTurned();
            }
        };


        referenceAngleIndicator.updateEnd(begin.x + 48, begin.y);
        upperAngleIndicator.setColor(Colors.palette1_red);
        lowerAngleIndicator.setColor(Colors.palette1_blue);
        upperAngleIndicator.updateEnd(begin.x + 64, begin.y);
        lowerAngleIndicator.updateEnd(begin.x + 64, begin.y);

        hideIndicators();

    }

    public void onBeginPointMoved(float x, float y) {
        referenceAngleIndicator.updateBegin(x, y);
        referenceAngleIndicator.drawSelf();
        upperAngleIndicator.updateBegin(x, y);
        upperAngleIndicator.drawSelf();
        lowerAngleIndicator.updateBegin(x, y);
        lowerAngleIndicator.drawSelf();
    }

    @Override
    public ArrayList<PointImage> getMovables(boolean moveLimits) {
        ArrayList<PointImage> result = super.getMovables(moveLimits);
        if (isIndicatorsVisible()) {
            result.add(referenceAngleIndicator.getLimit());
            result.add(lowerAngleIndicator.getLimit());
            result.add(upperAngleIndicator.getLimit());
        }
        return result;
    }

    public void onLowerIndicatorTurned() {
        float angle = (lowerAngleIndicator.getAngle() - referenceAngleIndicator.getAngle());
        jointDef.lowerAngle = angle * MathUtils.degreesToRadians;
        Log.e("revolute", "on lower indicator turned " + angle);
        userInterface.getJointSettingsWindowController().setRevoluteLowerAngle(angle / 360);
    }

    public void onUpperIndicatorTurned() {
        float angle = (upperAngleIndicator.getAngle() - referenceAngleIndicator.getAngle());
        jointDef.upperAngle = angle * MathUtils.degreesToRadians;
        userInterface.getJointSettingsWindowController().setRevoluteUpperAngle(angle / 360);
    }


    public void onReferenceIndicatorTurned(float dA) {
        if (dA < 0) {
            lowerAngleIndicator.turnAround(dA);
            upperAngleIndicator.turnAround(dA);
        } else {
            upperAngleIndicator.turnAround(dA);
            lowerAngleIndicator.turnAround(dA);
        }

        jointDef.referenceAngle = referenceAngleIndicator.getAngle() * MathUtils.degreesToRadians;
        userInterface.getJointSettingsWindowController().setRevoluteReferenceAngle(referenceAngleIndicator.getAngle() / 360);
        onLowerIndicatorTurned();
        onUpperIndicatorTurned();

    }

    public void turnIndicators(float dA) {
        if (dA < 0) {
            lowerAngleIndicator.turnAround(dA);
            upperAngleIndicator.turnAround(dA);
        } else {
            upperAngleIndicator.turnAround(dA);
            lowerAngleIndicator.turnAround(dA);
        }
    }

    public float getUpperAngleRelative() {
        return (upperAngleIndicator.getAngle() - referenceAngleIndicator.getAngle());
    }

    public float getLowerAngleRelative() {
        return (lowerAngleIndicator.getAngle() - referenceAngleIndicator.getAngle());
    }

    public float getReferenceAngle() {
        return referenceAngleIndicator.getAngle();
    }

    private float getUpperAngle() {
        return upperAngleIndicator.getAngle();
    }

    private float getLowerAngle() {
        return lowerAngleIndicator.getAngle();
    }


    @Override
    public void detach() {
        super.detach();
        referenceAngleIndicator.detach();
        lowerAngleIndicator.detach();
        upperAngleIndicator.detach();
    }

    @Override
    public void showIndicators() {
        super.showIndicators();
        referenceAngleIndicator.setVisible(true);
        lowerAngleIndicator.setVisible(true);
        upperAngleIndicator.setVisible(true);
    }

    private void hide(AngleIndicator indicator) {
        indicator.setVisible(false);
        userInterface.getCreationZoneController().onPointImageReleased(indicator.getLimit());
    }

    @Override
    public void hideIndicators() {
        super.hideIndicators();
        hide(referenceAngleIndicator);
        hide(upperAngleIndicator);
        hide(lowerAngleIndicator);

    }

    public void updateLowerAngleIndicator(float angle) {
        //target = current+dA
        lowerAngleIndicator.turnAround(angle - getLowerAngle());
    }

    public void updateUpperAngleIndicator(float angle) {
        upperAngleIndicator.turnAround(angle - getUpperAngle());
    }

    public void updateReferenceAngleIndicator(float angle) {
        float dA = angle - getReferenceAngle();
        referenceAngleIndicator.turnAround(dA);
        turnIndicators(dA);
    }


}
