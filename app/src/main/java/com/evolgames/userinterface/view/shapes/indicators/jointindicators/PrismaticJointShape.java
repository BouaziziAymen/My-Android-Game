package com.evolgames.userinterface.view.shapes.indicators.jointindicators;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.activity.ResourceManager;
import com.evolgames.scenes.EditorScene;
import com.evolgames.userinterface.model.jointmodels.JointModel;
import com.evolgames.userinterface.view.Colors;
import com.evolgames.userinterface.view.EditorUserInterface;
import com.evolgames.userinterface.view.shapes.indicators.AngleIndicator;
import com.evolgames.userinterface.view.shapes.points.ControllerPointImage;
import com.evolgames.userinterface.view.shapes.points.PointImage;
import com.evolgames.utilities.GeometryUtils;

import org.andengine.entity.primitive.Line;

import java.util.ArrayList;
import java.util.List;

public class PrismaticJointShape extends JointShape {

    private final AngleIndicator directionAngleIndicator;
    private final EditorUserInterface editorUserInterface;
    private final ControllerPointImage upperLimitPoint;
    private final ControllerPointImage lowerLimitPoint;
    private float upperLimit, lowerLimit;
    private Line limitLine;

    public PrismaticJointShape(EditorScene scene, Vector2 begin) {
        super(
                scene.getUserInterface(),
                begin,
                scene,
                ResourceManager.getInstance().doubleSquareTextureRegion);
        this.editorUserInterface = scene.getUserInterface();
        beginPoint.setMoveAction(
                () -> {
                    Vector2 point = beginPoint.getPoint();
                    PrismaticJointShape.this.onBeginPointMoved(point.x, point.y);
                });

        directionAngleIndicator =
                new AngleIndicator(begin, scene, 64) {
                    @Override
                    public void onTurnAroundCommand(float dA) {
                        turnAround(dA);
                        onDirectionIndicatorTurned();
                    }
                };

        directionAngleIndicator.setColor(Colors.palette1_green);
        directionAngleIndicator.updateEnd(begin.x + 64, begin.y);
        upperLimitPoint =
                new ControllerPointImage(ResourceManager.getInstance().diskTextureRegion, begin.cpy()) {
                    @Override
                    protected void performControl(float dx, float dy) {
                        if (upperLimit + dy > lowerLimit) {
                            upperLimit += dy;
                        } else {
                            upperLimit = lowerLimit;
                        }
                        PrismaticJointShape.this.onUpperLimitUpdated();
                        updateUpperLimitPosition();
                    }
                };
        editorUserInterface.addElement(upperLimitPoint);

        lowerLimitPoint =
                new ControllerPointImage(ResourceManager.getInstance().diskTextureRegion, begin.cpy()) {
                    @Override
                    protected void performControl(float dx, float dy) {
                        if (lowerLimit + dy < upperLimit) {
                            lowerLimit += dy;
                        } else {
                            lowerLimit = upperLimit;
                        }
                        PrismaticJointShape.this.onLowerLimitUpdated();
                        updateLowerLimitPosition();
                    }
                };
        editorUserInterface.addElement(lowerLimitPoint);
        hideLimitsElements();
    }

    private void onLowerLimitUpdated() {
        model.getProperties().setLowerTranslation(lowerLimit / 32f);
    }

    private void onUpperLimitUpdated() {
        model.getProperties().setUpperTranslation(upperLimit / 32f);
    }


    public void onBeginPointMoved(float x, float y) {
        super.onBeginPointMoved(x, y);
        directionAngleIndicator.updateBegin(x, y);
        directionAngleIndicator.drawSelf();
        updateLowerLimitPosition();
        updateUpperLimitPosition();
    }

    @Override
    public List<PointImage> getMovables(boolean moveLimits) {
        ArrayList<PointImage> result = new ArrayList<>();
        if (!moveLimits) {
            result.addAll(super.getMovables(false));
        } else {
            if (limitsShown) {
                result.add(directionAngleIndicator.getLimit());
                result.add(upperLimitPoint);
                result.add(lowerLimitPoint);
            }
        }
        return result;
    }

    private void updateLimitLine() {
        if (limitLine != null) limitLine.detachSelf();
        limitLine =
                new Line(
                        lowerLimitPoint.getPoint().x,
                        lowerLimitPoint.getPoint().y,
                        upperLimitPoint.getPoint().x,
                        upperLimitPoint.getPoint().y,
                        ResourceManager.getInstance().vbom);
        creationScene.attachChild(limitLine);
        limitLine.setZIndex(10);
        limitLine.setColor(1, 0, 0);
        limitLine.setLineWidth(3);
        creationScene.sortChildren();
    }

    private void updateUpperLimitPosition() {
        Vector2 dir = new Vector2(1, 0);
        GeometryUtils.rotateVectorDeg(dir, directionAngleIndicator.getAngleInDegrees());
        Vector2 position = begin.cpy().add(dir.x * upperLimit, dir.y * upperLimit);
        upperLimitPoint.setPosition(position.x, position.y);
        updateLimitLine();
    }

    private void updateLowerLimitPosition() {
        Vector2 dir = new Vector2(1, 0);
        GeometryUtils.rotateVectorDeg(dir, directionAngleIndicator.getAngleInDegrees());
        Vector2 position = begin.cpy().add(dir.x * lowerLimit, dir.y * lowerLimit);
        lowerLimitPoint.setPosition(position.x, position.y);
        updateLimitLine();
    }

    private void onDirectionIndicatorTurned() {
        editorUserInterface
                .getJointSettingsWindowController()
                .setPrismaticDirectionAngle(getDirectionAngleDegrees());
        updateUpperLimitPosition();
        updateLowerLimitPosition();
    }

    @Override
    public void detach() {
        super.detach();
        directionAngleIndicator.detach();
        creationScene.getUserInterface().removeElement(upperLimitPoint);
        creationScene.getUserInterface().removeElement(lowerLimitPoint);
        if (limitLine != null) {
            limitLine.detachSelf();
        }
    }

    public float getDirectionAngleDegrees() {
        return directionAngleIndicator.getAngleInDegrees();
    }

    public void updateDirectionAngleIndicator(float angle) {
        directionAngleIndicator.turnAround(angle - getDirectionAngleDegrees());
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
    public void showLimitsElements() {
        super.showLimitsElements();
        if (limitLine != null) {
            limitLine.setVisible(true);
        }
        directionAngleIndicator.setVisible(true);
        lowerLimitPoint.setVisible(true);
        upperLimitPoint.setVisible(true);
    }

    @Override
    public void hideLimitsElements() {
        super.hideLimitsElements();
        if (limitLine != null) {
            limitLine.setVisible(false);
        }
        directionAngleIndicator.setVisible(false);
        lowerLimitPoint.setVisible(false);
        upperLimitPoint.setVisible(false);
        editorUserInterface.getCreationZoneController().releaseSelectedPointImage();
        editorUserInterface.getCreationZoneController().releaseSelectedPointImage();
    }

    public void bindModel(JointModel model) {
        this.model = model;
        Vector2 modelEnd = model.getProperties().getLocalAnchorB();
        model.setJointShape(this);
        this.updateEnd(modelEnd.x, modelEnd.y);
        this.updateEnd(end.x, end.y);
        if (model.getProperties().isEnableLimit()) {
            this.showLimitsElements();
        }
        this.updateLowerLimit(model.getProperties().getLowerTranslation() * 32f);
        this.updateUpperLimit(model.getProperties().getUpperTranslation() * 32f);
        float angle = (float) Math.toDegrees(Math.atan2(model.getProperties().getLocalAxis1().y, model.getProperties().getLocalAxis1().x));
        this.updateDirectionAngleIndicator(angle);
    }
}
