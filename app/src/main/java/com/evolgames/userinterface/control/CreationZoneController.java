package com.evolgames.userinterface.control;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.evolgames.scenes.GameScene;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ItemWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.JointWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.LayerWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.OptionsWindowController;
import com.evolgames.userinterface.model.DecorationModel;
import com.evolgames.userinterface.model.LayerModel;
import com.evolgames.userinterface.model.PointsModel;
import com.evolgames.userinterface.model.jointmodels.JointModel;
import com.evolgames.userinterface.model.toolmodels.BombModel;
import com.evolgames.userinterface.model.toolmodels.CasingModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.view.UserInterface;
import com.evolgames.userinterface.view.inputs.controllers.ControlElement;
import com.evolgames.userinterface.view.inputs.controllers.ControllerAction;
import com.evolgames.userinterface.view.shapes.CreationZone;
import com.evolgames.userinterface.view.shapes.indicators.LineShape;
import com.evolgames.userinterface.view.shapes.indicators.MirrorArrowShape;
import com.evolgames.userinterface.view.shapes.indicators.PolygonArrowShape;
import com.evolgames.userinterface.view.shapes.indicators.RotateArrowShape;
import com.evolgames.userinterface.view.shapes.indicators.RotateImageShape;
import com.evolgames.userinterface.view.shapes.indicators.ScaleImageShape;
import com.evolgames.userinterface.view.shapes.indicators.ShiftArrowShape;
import com.evolgames.userinterface.view.shapes.indicators.ShiftImageShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.BombShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.CasingShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.HandShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.ProjectileShape;
import com.evolgames.userinterface.view.shapes.indicators.jointindicators.DistanceJointShape;
import com.evolgames.userinterface.view.shapes.indicators.jointindicators.JointShape;
import com.evolgames.userinterface.view.shapes.indicators.jointindicators.PrismaticJointShape;
import com.evolgames.userinterface.view.shapes.indicators.jointindicators.RevoluteJointShape;
import com.evolgames.userinterface.view.shapes.indicators.jointindicators.WeldJointShape;
import com.evolgames.userinterface.view.shapes.points.ModelPointImage;
import com.evolgames.userinterface.view.shapes.points.PointImage;
import com.evolgames.userinterface.view.windows.windowfields.layerwindow.DecorationField;
import com.evolgames.userinterface.view.windows.windowfields.layerwindow.LayerField;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CreationZoneController extends Controller {

    private final ItemWindowController itemWindowController;
    private final GameScene gameScene;
    private final LayerWindowController layerWindowController;
    private final JointWindowController jointWindowController;
    private CreationAction action = CreationAction.ADD_POINT;
    private UserInterface userInterface;
    private LineShape indicatorArrow;
    private CreationZone creationZone;
    private PointImage selectedPointImage = null;
    private boolean upLocked;
    private float radiusForPolygon = 32;
    private boolean fixedRadiusForPolygon;
    private boolean reference;
    private float movePointSpeed;
    private OptionsWindowController.Direction direction;
    private int numberOfPointsForPolygon = 3;
    private boolean magnet, magnetLines, magnetCenters;
    private boolean moveLimits;
    private boolean congruentAnchors;

    public CreationZoneController(GameScene gameScene, LayerWindowController layerWindowController, JointWindowController jointWindowController, OptionsWindowController optionsWindowController, ItemWindowController itemWindowController) {
        this.gameScene = gameScene;
        this.layerWindowController = layerWindowController;
        this.jointWindowController = jointWindowController;
        this.itemWindowController = itemWindowController;
        optionsWindowController.setCreationZoneController(this);
    }

    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public CreationAction getAction() {
        return action;
    }

    public void setAction(CreationAction action) {
        this.action = action;
        if (userInterface.getImageShape() != null)
            userInterface.getImageShape().setPipeCircleVisibility(action == CreationAction.PIPING);
        userInterface.onActionChanged(action);
        switch (action) {
            case MOVE_TOOL_POINT:
            case AMMO:
            case DISTANCE:
            case WELD:
            case PRISMATIC:
            case ROTATE:
            case MIRROR:
            case SHIFT:
            case REVOLUTE:
            case PROJECTILE:
            case NONE:
            case ADD_POLYGON:
            case SCALE_IMAGE:
            case ROTATE_IMAGE:
            case MOVE_IMAGE:
                gameScene.setScrollerEnabled(false);
                gameScene.setZoomEnabled(false);
                break;
            case MOVE_JOINT_POINT:
            case PIPING:
            case REMOVE_POINT:
            case MOVE_POINT:
            case ADD_POINT:
            case BOMB:
                gameScene.setScrollerEnabled(true);
                gameScene.setZoomEnabled(true);
                break;
        }
    }

    @Override
    public void init() {

    }

    public void onZoneActionUp(float x, float y) {
        if (upLocked) {
            return;
        }
        processAbortedIndicators();
        if (action == CreationAction.ADD_POINT) {
            processAddPoint(x, y);
        }
        if (action == CreationAction.REMOVE_POINT) {
            processRemovePoint(x, y);
        }

        if (indicatorArrow != null) {
            if (!(indicatorArrow instanceof JointShape || indicatorArrow instanceof ProjectileShape || indicatorArrow instanceof CasingShape || indicatorArrow instanceof HandShape)) {
                indicatorArrow.detach();
            }
            indicatorArrow = null;
            creationZone.setTouchLocked(false);
        }
        processMobilePoints(x, y);

        releaseBoardsButtons();
    }

    private void releaseBoardsButtons() {
        if (action == CreationAction.ADD_POLYGON || action == CreationAction.MIRROR) {
            userInterface.getDrawButtonBoardController().releaseButtons();
            action = CreationAction.NONE;
        }
        if (action == CreationAction.REVOLUTE || action == CreationAction.DISTANCE || action == CreationAction.PRISMATIC) {
            userInterface.getJointButtonBoardController().releaseButtons();
            action = CreationAction.NONE;
        }
        if (action == CreationAction.PIPING) {
            userInterface.getImageButtonBoardController().releaseButtons();
            action = CreationAction.NONE;
        }
        if (action == CreationAction.BOMB || action == CreationAction.PROJECTILE || action == CreationAction.AMMO) {
            userInterface.getItemButtonBoardController().releaseButtons();
            action = CreationAction.NONE;
        }
    }

    private void processRemovePoint(float x, float y) {
        if (layerWindowController.getSelectedPointsModel() != null) {
            float distance = 32;
            Vector2 point = null;
            for (Vector2 p : layerWindowController.getSelectedPointsModel().getPoints()) {
                float d = p.dst(x, y);
                if (d < distance) {
                    point = p;
                    distance = d;
                }
            }

            if (point != null) {
                layerWindowController.getSelectedPointsModel().remove(point);
                ModelPointImage p = layerWindowController.getSelectedPointsModel().getPointsShape().getPointImage(point);
                layerWindowController.getSelectedPointsModel().getPointsShape().removeElement(p);
                layerWindowController.getSelectedPointsModel().getPointsShape().onModelUpdated();
            }
        }
    }

    private void processAbortedIndicators() {
        if (indicatorArrow != null && indicatorArrow.isAborted()) {
            if (action == CreationAction.PROJECTILE) {
                itemWindowController.onProjectileAborted(((ProjectileShape) indicatorArrow).getModel());
            } else if (action == CreationAction.AMMO) {
                itemWindowController.onAmmoAborted(((CasingShape) indicatorArrow).getModel());
            }
        }
    }

    private void processAddPoint(float x, float y) {
        if (layerWindowController.getSelectedPointsModel() != null) {
            PointsModel<?> pointsModel = layerWindowController.getSelectedPointsModel();
            if (pointsModel.test(x, y)) {
                pointsModel.addPoint(new Vector2(x, y));
                pointsModel.getPointsShape().onModelUpdated();
            }
        }
    }

    private void processMobilePoints(float x, float y) {
        List<PointImage> movablePointImages = null;
        switch (action) {
            case MOVE_POINT:
                if (!this.isReferenceEnabled()) {
                    movablePointImages = layerWindowController.getModelMovables();
                } else {
                    movablePointImages = new ArrayList<>(creationZone.getReferencePointImageArrayList());
                }
                break;
            case MOVE_TOOL_POINT:
                if (itemWindowController.hasSelectedItem()) {
                    movablePointImages = itemWindowController.getSelectedModelMovables(moveLimits);
                }
                break;
            case MOVE_JOINT_POINT:
                movablePointImages = jointWindowController.getSelectedModelMovables(moveLimits);
                break;
        }
        if (movablePointImages != null) {
            float distance = 32;
            PointImage point = null;
            for (PointImage p : movablePointImages) {
                float d = p.getPoint().dst(x, y);
                if (d < distance) {
                    point = p;
                    distance = d;
                }
            }
            if (point != null) {
                onPointImageReleased();
                selectPointImage(point);
            }
        }

    }

    public void createReferencePoint() {
        if (selectedPointImage != null) {
            if (selectedPointImage.getPointsShape() != null) {
                if (selectedPointImage.getPointsShape().getReferencePointImage(selectedPointImage.getPoint()) == null) {
                    selectedPointImage.getPointsShape().createReferencePointImage(selectedPointImage.getPoint());
                }
            }
        }
    }

    public void selectPointImage(PointImage pointImage) {
        pointImage.doubleSelect();
        selectedPointImage = pointImage;
        userInterface.setMoveElementController(userInterface.getPanel().allocateController(800 - 64 / 2f, 64 / 2f, ControlElement.Type.AnalogController, new ControllerAction() {
            @Override
            public void controlMoved(float pX, float pY) {
                pointImage.onControllerMoved(pX, pY);
            }

            @Override
            public void controlClicked() {}

            @Override
            public void controlReleased() {

            }
        }));

    }

    public void onPointImageReleased() {
        if (selectedPointImage != null) {
            selectedPointImage.undoDoubleSelect();
            selectedPointImage.release();
            selectedPointImage = null;
        }
    }

    public int getNumberOfPointsForPolygon() {
        return numberOfPointsForPolygon;
    }

    public void setNumberOfPointsForPolygon(int numberOfPointsForPolygon) {
        this.numberOfPointsForPolygon = numberOfPointsForPolygon;
    }

    public void onZoneActionDown(float x, float y) {

        upLocked = false;

        if (action == CreationAction.NONE) {
            return;
        }

        if (action == CreationAction.ROTATE_IMAGE) {
            if (gameScene.getUserInterface().getImageShape() != null) {
                float X = gameScene.getUserInterface().getImageShape().getSprite().getX();
                float Y = gameScene.getUserInterface().getImageShape().getSprite().getY();

                indicatorArrow = new RotateImageShape(new Vector2(X, Y), gameScene.getUserInterface().getImageShape(), gameScene, 32);
            }
        }
        if (action == CreationAction.SCALE_IMAGE) {
            if (gameScene.getUserInterface().getImageShape() != null) {
                float X = gameScene.getUserInterface().getImageShape().getSprite().getX();
                float Y = gameScene.getUserInterface().getImageShape().getSprite().getY();
                indicatorArrow = new ScaleImageShape(new Vector2(X, Y), gameScene.getUserInterface().getImageShape(), gameScene);

            }
        }

        if (action == CreationAction.MOVE_IMAGE) {
            if (gameScene.getUserInterface().getImageShape() != null) {
                indicatorArrow = new ShiftImageShape(new Vector2(x, y), gameScene.getUserInterface().getImageShape(), gameScene);
            }
        }
        if (action == CreationAction.REVOLUTE) {
            indicatorArrow = new RevoluteJointShape(gameScene, new Vector2(x, y));
            JointModel jointModel = userInterface.getToolModel().createJointModel((JointShape) indicatorArrow, new RevoluteJointDef());
            jointWindowController.onJointAdded(jointModel);
            indicatorArrow.setCongruentEndpoints(getCongruentAnchors());
            return;
        }
        if (action == CreationAction.WELD) {
            indicatorArrow = new WeldJointShape(gameScene, new Vector2(x, y));
            JointModel jointModel = userInterface.getToolModel().createJointModel((JointShape) indicatorArrow, new WeldJointDef());
            jointWindowController.onJointAdded(jointModel);
            indicatorArrow.setCongruentEndpoints(getCongruentAnchors());
            return;
        }
        if (action == CreationAction.PRISMATIC) {
            indicatorArrow = new PrismaticJointShape(gameScene, new Vector2(x, y));
            JointModel jointModel = userInterface.getToolModel().createJointModel((JointShape) indicatorArrow, new PrismaticJointDef());
            jointWindowController.onJointAdded(jointModel);
            indicatorArrow.setCongruentEndpoints(getCongruentAnchors());
            return;
        }

        if (action == CreationAction.DISTANCE) {
            indicatorArrow = new DistanceJointShape(gameScene, new Vector2(x, y));
            JointModel jointModel = userInterface.getToolModel().createJointModel((JointShape) indicatorArrow, new DistanceJointDef());
            jointWindowController.onJointAdded(jointModel);
            indicatorArrow.setCongruentEndpoints(getCongruentAnchors());
            return;
        }
        if (action == CreationAction.PROJECTILE) {
            if (userInterface.getItemWindowController().getSelectedBodyId() != -1) {
                indicatorArrow = new ProjectileShape(new Vector2(x, y), gameScene);
                ProjectileModel projectileModel = userInterface.getToolModel().createNewProjectile((ProjectileShape) indicatorArrow, userInterface.getItemWindowController().getSelectedBodyId());
                projectileModel.getProperties().setProjectileOrigin(new Vector2(x, y));
                projectileModel.getProperties().setProjectileEnd(new Vector2(x, y));
                itemWindowController.onProjectileCreated(projectileModel);
                ((ProjectileShape) indicatorArrow).bindModel(projectileModel);
                return;
            }
        }
        if (action == CreationAction.BOMB) {
            if (userInterface.getItemWindowController().getSelectedBodyId() != -1) {
                BombShape bombShape = new BombShape(new Vector2(x, y), gameScene);
                BombModel bombModel = userInterface.getToolModel().createNewBomb(bombShape, userInterface.getItemWindowController().getSelectedBodyId());
                itemWindowController.onBombCreated(bombModel);
                bombShape.bindModel(bombModel);
            }
        }
        if (action == CreationAction.AMMO) {
            if (userInterface.getItemWindowController().getSelectedBodyId() != -1) {
                indicatorArrow = new CasingShape(new Vector2(x, y), gameScene);
                CasingModel ammoModel = userInterface.getToolModel().createNewAmmo((CasingShape) indicatorArrow, userInterface.getItemWindowController().getSelectedBodyId());
                if (ammoModel != null) {
                    itemWindowController.onCasingCreated(ammoModel);
                    ((CasingShape) indicatorArrow).bindModel(ammoModel);
                }
                return;
            }
        }

        if (action == CreationAction.SHIFT && layerWindowController.getSelectedPointsModel() != null) {
            indicatorArrow = new ShiftArrowShape(new Vector2(x, y), layerWindowController.getSelectedPointsModel(), gameScene);
            creationZone.setTouchLocked(true);
            return;
        }
        if (action == CreationAction.ROTATE && layerWindowController.getSelectedPointsModel() != null) {
            Log.e("indicator", "rotate");
            indicatorArrow = new RotateArrowShape(new Vector2(x, y), layerWindowController.getSelectedPointsModel(), gameScene, 64);
            creationZone.setTouchLocked(true);
            return;
        }

        if (action == CreationAction.ADD_POLYGON && layerWindowController.getSelectedPointsModel() != null) {

            PointsModel<?> selectedPointsModel = layerWindowController.getSelectedPointsModel();
            if (selectedPointsModel == null) {
                return;
            }
            Vector2 center = new Vector2(x, y);

            indicatorArrow = (fixedRadiusForPolygon) ? new PolygonArrowShape(center, layerWindowController.getSelectedPointsModel(), gameScene, numberOfPointsForPolygon, radiusForPolygon) : new PolygonArrowShape(center, layerWindowController.getSelectedPointsModel(), gameScene, numberOfPointsForPolygon);
            selectedPointsModel.getReferencePoints().add(center);
            creationZone.setTouchLocked(true);
            return;
        }

        if (action == CreationAction.MIRROR && layerWindowController.getSelectedPointsModel() != null) {
            PointsModel<?> selectedLayerPointsModel = layerWindowController.getSelectedPointsModel();
            PointsModel<?> shapePointsModel;
            if (selectedLayerPointsModel instanceof LayerModel) {
                LayerModel layerModel = (LayerModel)selectedLayerPointsModel;
                LayerField layer = layerWindowController.onAddLayerButtonCLicked(userInterface.getToolModel().getBodyModelById(layerModel.getBodyId()).getField());
                shapePointsModel = layerWindowController.getLayerModel(layer.getPrimaryKey(), layer.getSecondaryKey());
            } else {
                DecorationModel decorationModel = (DecorationModel) selectedLayerPointsModel;
                DecorationField decoration = layerWindowController.onLayerAddDecorationClicked(userInterface.getToolModel().getLayerModelById(decorationModel.getLayerModel().getBodyId(),decorationModel.getLayerModel().getLayerId()).getField());
                shapePointsModel = layerWindowController.getDecorationModel(decoration.getPrimaryKey(), decoration.getSecondaryKey(), decoration.getTertiaryKey());
            }

            shapePointsModel.setPoints(selectedLayerPointsModel.getPoints().stream().map(Vector2::new).collect(Collectors.toList()));
            shapePointsModel.setReferencePoints(selectedLayerPointsModel.getReferencePoints().stream().map(Vector2::new).collect(Collectors.toList()));
            indicatorArrow = new MirrorArrowShape(new Vector2(x, y), shapePointsModel, gameScene);
            creationZone.setTouchLocked(true);
        }
    }


    public void onZoneActionMove(float x, float y) {
        if (action == CreationAction.NONE) {
            return;
        }

        if (indicatorArrow != null && !indicatorArrow.isCongruentEndpoints()) {
            indicatorArrow.updateEnd(x, y);
        }

    }

    public void setUpLocked(boolean b) {
        upLocked = true;
    }

    public void resetScrollAndZoom() {
        setAction(action);
    }

    public float getRadiusForPolygon() {
        return radiusForPolygon;
    }

    public void setRadiusForPolygon(float radiusForPolygon) {
        this.radiusForPolygon = radiusForPolygon;
    }

    public void setFixedRadiusEnabled(boolean b) {
        fixedRadiusForPolygon = b;
    }

    public boolean isFixedRadiusForPolygonEnabled() {
        return fixedRadiusForPolygon;
    }

    public boolean isMagnet() {
        return magnet;
    }

    public void setMagnet(boolean magnet) {
        this.magnet = magnet;
    }

    public boolean isMagnetLines() {
        return magnetLines;
    }

    public void setMagnetLines(boolean magnetLines) {
        this.magnetLines = magnetLines;
    }

    public boolean isMagnetCenters() {
        return magnetCenters;
    }

    public void setMagnetCenters(boolean magnetCenters) {
        this.magnetCenters = magnetCenters;
    }

    public void setReference(boolean reference) {
        this.reference = reference;
    }

    public boolean isReferenceEnabled() {
        return reference;
    }

    public float getMovePointSpeed() {
        return movePointSpeed;
    }

    public void setMovePointSpeed(float movePointSpeed) {
        this.movePointSpeed = movePointSpeed;
    }

    public OptionsWindowController.Direction getDirection() {
        return direction;
    }

    public void setDirection(OptionsWindowController.Direction direction) {
        this.direction = direction;
    }

    public boolean getMoveLimits() {
        return moveLimits;
    }

    public void setMoveLimits(boolean moveLimits) {
        this.moveLimits = moveLimits;
    }

    public boolean getCongruentAnchors() {
        return congruentAnchors;
    }

    public void setCongruentAnchors(boolean congruentAnchors) {
        this.congruentAnchors = congruentAnchors;
    }


    public void setCreationZone(CreationZone creationZone) {
        this.creationZone = creationZone;
    }


    public enum CreationAction {
        ADD_POINT, MOVE_POINT, REMOVE_POINT, ADD_POLYGON, NONE, MIRROR, ROTATE, SHIFT, REVOLUTE, PRISMATIC, WELD, DISTANCE, MOVE_JOINT_POINT, MOVE_IMAGE, ROTATE_IMAGE, SCALE_IMAGE, PIPING, PROJECTILE, MOVE_TOOL_POINT, AMMO, BOMB
    }
}

