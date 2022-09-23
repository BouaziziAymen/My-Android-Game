package com.evolgames.userinterface.view.shapes.indicators.jointindicators;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.helpers.utilities.MathUtils;
import com.evolgames.scenes.GameScene;
import com.evolgames.userinterface.view.Colors;
import com.evolgames.userinterface.view.UserInterface;
import com.evolgames.userinterface.view.shapes.points.ControllerPointImage;
import com.evolgames.userinterface.view.shapes.points.PointImage;
import com.evolgames.userinterface.view.shapes.indicators.AngleIndicator;

import org.andengine.entity.primitive.Line;

import java.util.ArrayList;

public class PrismaticJointShape extends JointShape {


    private final AngleIndicator directionAngleIndicator;
    private final PrismaticJointDef jointDef;
    private final UserInterface userInterface;
    private final ControllerPointImage upperLimitPoint;
    private final ControllerPointImage lowerLimitPoint;
    private float upperLimit, lowerLimit;
    private Line limitLine;

    public float getLowerLimit() {
        return lowerLimit;
    }

    public float getUpperLimit() {
        return upperLimit;
    }

    public PrismaticJointShape(PrismaticJointDef prismaticJointDef, GameScene scene, Vector2 begin) {
        super(scene.getUserInterface(), begin, scene, ResourceManager.getInstance().doubleSquareTextureRegion);
        setIndicatorsVisible(true);
        this.jointDef = prismaticJointDef;
        this.userInterface = scene.getUserInterface();
        beginPoint.setMoveAction(() -> {
            Vector2 point = beginPoint.getPoint();
            PrismaticJointShape.this.onBeginPointMoved(point.x, point.y);
        });

        directionAngleIndicator = new AngleIndicator(begin, scene, 64) {
            @Override
            public void onTurnAroundCommand(float dA) {
                turnAround(dA);
                onDirectionIndicatorTurned();

            }
        };

        directionAngleIndicator.setColor(Colors.palette1_green);
        directionAngleIndicator.updateEnd(begin.x + 64, begin.y);
        upperLimitPoint = new ControllerPointImage(ResourceManager.getInstance().diskTextureRegion, begin.cpy()) {
            @Override
            protected void performControl(float dx, float dy) {
                if (upperLimit + dy > lowerLimit) {
                    upperLimit += dy;
                } else {
                    upperLimit = lowerLimit;
                }

                updateUpperLimitPosition();
            }
        };
        userInterface.addElement(upperLimitPoint);


        lowerLimitPoint = new ControllerPointImage(ResourceManager.getInstance().diskTextureRegion, begin.cpy()) {
            @Override
            protected void performControl(float dx, float dy) {
                if (lowerLimit + dy < upperLimit) {
                    lowerLimit += dy;
                } else {
                    lowerLimit = upperLimit;
                }

                updateLowerLimitPosition();
            }
        };
        userInterface.addElement(lowerLimitPoint);
        hideIndicators();

    }

    public void onBeginPointMoved(float x, float y) {
        directionAngleIndicator.updateBegin(x, y);
        directionAngleIndicator.drawSelf();
        updateLowerLimitPosition();
        updateUpperLimitPosition();

    }

    @Override
    public ArrayList<PointImage> getMovables(boolean moveLimits) {
        ArrayList<PointImage> result = new ArrayList<>();
        if (!moveLimits) {
            result.addAll(super.getMovables(false));
            result.add(directionAngleIndicator.getLimit());
        } else {
            if(isIndicatorsVisible()) {
                result.add(upperLimitPoint);
                result.add(lowerLimitPoint);
            }
        }
        return result;
    }

    private void updateLimitLine() {
        if (limitLine != null) limitLine.detachSelf();
        limitLine = new Line(lowerLimitPoint.getPoint().x, lowerLimitPoint.getPoint().y, upperLimitPoint.getPoint().x, upperLimitPoint.getPoint().y, ResourceManager.getInstance().vbom);
        creationScene.attachChild(limitLine);
        limitLine.setZIndex(10);
        limitLine.setColor(1, 0, 0);
        limitLine.setLineWidth(3);
        creationScene.sortChildren();
    }

    private void updateUpperLimitPosition() {
        Vector2 dir = new Vector2(1, 0);
        GeometryUtils.rotateVectorDeg(dir, directionAngleIndicator.getAngle());
        Vector2 position = begin.cpy().add(dir.x * upperLimit, dir.y * upperLimit);
        upperLimitPoint.setPosition(position.x, position.y);
        updateLimitLine();
    }

    private void updateLowerLimitPosition() {
        Vector2 dir = new Vector2(1, 0);
        GeometryUtils.rotateVectorDeg(dir, directionAngleIndicator.getAngle());
        Vector2 position = begin.cpy().add(dir.x * lowerLimit, dir.y * lowerLimit);
        lowerLimitPoint.setPosition(position.x, position.y);
        updateLimitLine();
    }

    private void onDirectionIndicatorTurned() {
        Vector2 direction = new Vector2(1, 0);
        MathUtils.rotateVectorByRadianAngle(direction, (float) (getDirectionAngle() * 2 * Math.PI / 360));
        jointDef.localAxis1.set(direction);
        userInterface.getJointSettingsWindowController().setPrismaticDirectionAngle(getDirectionAngle() / 360);
        updateUpperLimitPosition();
        updateLowerLimitPosition();
    }


    @Override
    public void detach() {
        super.detach();
        directionAngleIndicator.detach();
        creationScene.getUserInterface().removeElement(upperLimitPoint);
        creationScene.getUserInterface().removeElement(lowerLimitPoint);
        if(limitLine!=null)
        limitLine.detachSelf();
    }


    public float getDirectionAngle() {
        return directionAngleIndicator.getAngle();
    }

    public void updateDirectionAngleIndicator(float angle) {
        directionAngleIndicator.turnAround(angle - getDirectionAngle());
        updateLowerLimitPosition();
        updateUpperLimitPosition();
    }

    public void updateLowerLimit(float lowerTranslation) {
        this.lowerLimit = lowerTranslation;
        updateLowerLimitPosition();
    }

    public void updateUpperLimit(float upperTranslation) {
        this.upperLimit = upperTranslation;
        updateUpperLimitPosition();
    }


    @Override
    public void showIndicators() {
        super.showIndicators();
        if(limitLine!=null)
        limitLine.setVisible(true);
        lowerLimitPoint.setVisible(true);
        upperLimitPoint.setVisible(true);
    }
    @Override
    public void hideIndicators() {
        super.hideIndicators();
        if(limitLine!=null)
        limitLine.setVisible(false);
        lowerLimitPoint.setVisible(false);
        upperLimitPoint.setVisible(false);
        userInterface.getCreationZoneController().onPointImageReleased(lowerLimitPoint);
        userInterface.getCreationZoneController().onPointImageReleased(upperLimitPoint);
    }
}
