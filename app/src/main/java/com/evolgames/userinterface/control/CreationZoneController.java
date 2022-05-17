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
import com.evolgames.userinterface.model.LayerPointsModel;
import com.evolgames.userinterface.model.PointsModel;
import com.evolgames.userinterface.model.jointmodels.JointModel;
import com.evolgames.userinterface.model.toolmodels.HandModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.view.UserInterface;
import com.evolgames.userinterface.view.shapes.CreationZone;
import com.evolgames.userinterface.view.shapes.indicators.LineShape;
import com.evolgames.userinterface.view.shapes.indicators.MirrorArrowShape;
import com.evolgames.userinterface.view.shapes.indicators.PolygonArrowShape;
import com.evolgames.userinterface.view.shapes.indicators.RotateArrowShape;
import com.evolgames.userinterface.view.shapes.indicators.RotateImageShape;
import com.evolgames.userinterface.view.shapes.indicators.ScaleImageShape;
import com.evolgames.userinterface.view.shapes.indicators.ShiftArrowShape;
import com.evolgames.userinterface.view.shapes.indicators.ShiftImageShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.HandShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.ProjectileShape;
import com.evolgames.userinterface.view.shapes.indicators.jointindicators.DistanceJointShape;
import com.evolgames.userinterface.view.shapes.indicators.jointindicators.JointShape;
import com.evolgames.userinterface.view.shapes.indicators.jointindicators.PrismaticJointShape;
import com.evolgames.userinterface.view.shapes.indicators.jointindicators.RevoluteJointShape;
import com.evolgames.userinterface.view.shapes.indicators.jointindicators.WeldJointShape;
import com.evolgames.userinterface.view.shapes.points.ModelPointImage;
import com.evolgames.userinterface.view.shapes.points.PointImage;
import com.evolgames.userinterface.view.shapes.points.ReferencePointImage;
import com.evolgames.userinterface.view.windows.windowfields.layerwindow.DecorationField1;
import com.evolgames.userinterface.view.windows.windowfields.layerwindow.LayerField1;

import org.andengine.extension.physics.box2d.util.Vector2Pool;

import java.util.ArrayList;

public class CreationZoneController extends Controller {

    private final ItemWindowController itemWindowController;
    private CreationAction action = CreationAction.ADD_POINT;
    private GameScene gameScene;
    private UserInterface userInterface;
    private LineShape indicatorArrow;
    private CreationZone creationZone;
    private LayerWindowController layerWindowController;
    private JointWindowController jointWindowController;
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
                gameScene.setScrollerEnabled(false);
                gameScene.setZoomEnabled(false);
                break;
            case MOVE_JOINT_POINT:
                gameScene.setScrollerEnabled(true);
                gameScene.setZoomEnabled(true);
                break;
            case MOVE_IMAGE:
                gameScene.setScrollerEnabled(false);
                gameScene.setZoomEnabled(false);
                break;

            case ROTATE_IMAGE:
                gameScene.setScrollerEnabled(false);
                gameScene.setZoomEnabled(false);
                break;
            case SCALE_IMAGE:
                gameScene.setScrollerEnabled(false);
                gameScene.setZoomEnabled(false);
                break;

            case ADD_POINT:

                gameScene.setScrollerEnabled(true);
                gameScene.setZoomEnabled(true);
                break;
            case MOVE_POINT:
                gameScene.setScrollerEnabled(true);
                gameScene.setZoomEnabled(true);
                break;
            case REMOVE_POINT:
                gameScene.setScrollerEnabled(true);
                gameScene.setZoomEnabled(true);
                break;
            case ADD_POLYGON:

                gameScene.setScrollerEnabled(false);
                gameScene.setZoomEnabled(false);
                break;
            case NONE:
                gameScene.setScrollerEnabled(false);
                gameScene.setZoomEnabled(false);
                break;
            case PROJECTILE:
                gameScene.setScrollerEnabled(false);
                gameScene.setZoomEnabled(false);
                break;
            case HAND:
                gameScene.setScrollerEnabled(false);
                gameScene.setZoomEnabled(false);
                break;
            case MIRROR:
                gameScene.setScrollerEnabled(false);
                gameScene.setZoomEnabled(false);
                break;
            case ROTATE:
                gameScene.setScrollerEnabled(false);
                gameScene.setZoomEnabled(false);
                break;
            case SHIFT:
                gameScene.setScrollerEnabled(false);
                gameScene.setZoomEnabled(false);
                break;
            case REVOLUTE:
                gameScene.setScrollerEnabled(false);
                gameScene.setZoomEnabled(false);
                break;
            case PRISMATIC:
                gameScene.setScrollerEnabled(false);
                gameScene.setZoomEnabled(false);
                break;
            case WELD:
                gameScene.setScrollerEnabled(false);
                gameScene.setZoomEnabled(false);
                break;
            case DISTANCE:
                gameScene.setScrollerEnabled(false);
                gameScene.setZoomEnabled(false);
                break;

            case PIPING:
                gameScene.setScrollerEnabled(true);
                gameScene.setZoomEnabled(true);
                break;
        }
    }

    @Override
    public void init() {

    }


    public void onZoneActionUp(float x, float y) {

        if (upLocked) return;

        if (action == CreationAction.ADD_POINT && layerWindowController.getSelectedShape() != null) {
            if (layerWindowController.getSelectedShape().test(x, y)) {
                layerWindowController.getSelectedShape().getPoints().add(new Vector2(x, y));
                layerWindowController.getSelectedShape().getPointsShape().onModelUpdated();
            }
        }
        if (action == CreationAction.PROJECTILE) {
            if (indicatorArrow != null && ((ProjectileShape) indicatorArrow).isAborted()) {
                itemWindowController.onTargetAborted(((ProjectileShape) indicatorArrow).getModel());
            }
        }
        if (action == CreationAction.HAND) {
            if (indicatorArrow != null && ((HandShape) indicatorArrow).isAborted()) {
                itemWindowController.onHandAborted(((HandShape) indicatorArrow).getModel());
            }
        }


        if (action == CreationAction.REMOVE_POINT && layerWindowController.getSelectedShape() != null) {
            float distance = 32;
            Vector2 point = null;
            for (Vector2 p : layerWindowController.getSelectedShape().getPoints()) {
                float d = p.dst(x, y);
                if (d < distance) {
                    point = p;
                    distance = d;
                }
            }
            if (point != null) {
                layerWindowController.getSelectedShape().getPoints().remove(point);
                ModelPointImage p = layerWindowController.getSelectedShape().getPointsShape().getPointImage(point);
                layerWindowController.getSelectedShape().getPointsShape().removeElement(p);
                layerWindowController.getSelectedShape().getPointsShape().onModelUpdated();
            }
        }

        if (indicatorArrow != null) {
            if (!(indicatorArrow instanceof JointShape || indicatorArrow instanceof ProjectileShape || indicatorArrow instanceof HandShape))
                indicatorArrow.detach();
            indicatorArrow = null;
            creationZone.setTouchLocked(false);
        }

        if (action == CreationAction.MOVE_POINT || action == CreationAction.MOVE_JOINT_POINT || action == CreationAction.MOVE_TOOL_POINT) {
            ArrayList<PointImage> movablePointImages;
            if (action == CreationAction.MOVE_POINT && layerWindowController.getSelectedShape() != null) {
                if (!isReferenceEnabled())
                    movablePointImages = layerWindowController.getSelectedShape().getPointsShape().getMovablePointImages();
                else
                    movablePointImages = new ArrayList<>(creationZone.referencePointImageArrayList);

            } else if (action == CreationAction.MOVE_TOOL_POINT) {
                System.out.println("MOVE_TOOL");
                movablePointImages = new ArrayList<>();
                ArrayList<ProjectileModel> list = userInterface.getToolModel().getBodyModelById(userInterface.getItemWindowController().getSelectedBodyId()).getProjectiles();
                for (ProjectileModel entry : list) {
                    if (entry.getProjectileShape().isSelected()) {
                        System.out.println("MOVE_TOOL_SELECTED");
                        movablePointImages.addAll(entry.getProjectileShape().getMovables(this.moveLimits));
                    }
                }
            } else {
                movablePointImages = new ArrayList<>();
                ArrayList<JointModel> list = userInterface.getToolModel().getJointModels();
                for (JointModel jointModel : list) {
                    if (jointModel.isSelected()) {
                        JointShape jointShape = jointModel.getJointShape();
                        ArrayList<PointImage> movables = jointShape.getMovables(moveLimits);
                        movablePointImages.addAll(movables);
                    }
                }
            }

            float distance = 32;
            PointImage point = null;
            for (PointImage p : movablePointImages) {
                float d = p.getPoint().dst(x, y);
                if (d < distance) {
                    point = p;
                    distance = d;
                }
            }
            // Log.e("move","point:"+point.getPoint());
            if (point != null) {
                onPointImageReleased(point);
                selectPointImage(point);
            }
        }

        if (action == CreationAction.ADD_POLYGON || action == CreationAction.MIRROR) {
            userInterface.getDrawButtonBoardController().releaseButtons();
            action = CreationAction.NONE;
        }
        if (action == CreationAction.REVOLUTE || action == CreationAction.DISTANCE || action == CreationAction.PRISMATIC) {
            userInterface.getJointButtonBoardController().releaseButtons();
            action = CreationAction.NONE;
        }
        if (action == CreationAction.PROJECTILE || action == CreationAction.HAND) {
            userInterface.getToolButtonBoardController().releaseButtons();
            action = CreationAction.NONE;
        }
        if (action == CreationAction.PIPING)
            userInterface.getImageButtonBoardController().releaseButtons();


    }

    public void selectPointImage(PointImage pointImage) {
        pointImage.doubleSelect();
        if (selectedPointImage != null)
            selectedPointImage.undoDoubleSelect();
        selectedPointImage = pointImage;
        gameScene.setMovable(pointImage);
    }

    public void onPointImageReleased(PointImage pointImage) {
        if (pointImage == selectedPointImage) {
            selectedPointImage.release();
            gameScene.setMovable(null);
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

        Log.e("indicator", "action down :" + upLocked);
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
            RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
            indicatorArrow = new RevoluteJointShape(revoluteJointDef, gameScene, new Vector2(x, y));
            JointModel jointModel = userInterface.getToolModel().createNewJoint((JointShape) indicatorArrow, revoluteJointDef);
            jointWindowController.onJointAdded(jointModel);
            indicatorArrow.setCongruentEndpoints(getCongruentAnchors());
            return;
        }
        if (action == CreationAction.WELD) {
            WeldJointDef weldJointDef = new WeldJointDef();
            indicatorArrow = new WeldJointShape(gameScene.getUserInterface(), new Vector2(x, y), gameScene);
            JointModel jointModel = userInterface.getToolModel().createNewJoint((JointShape) indicatorArrow, weldJointDef);
            jointWindowController.onJointAdded(jointModel);
            indicatorArrow.setCongruentEndpoints(getCongruentAnchors());
            return;
        }
        if (action == CreationAction.PROJECTILE) {
            if (userInterface.getItemWindowController().getSelectedBodyId() != -1) {
                indicatorArrow = new ProjectileShape(new Vector2(x, y), gameScene);

                ProjectileModel projectileModel = userInterface.getToolModel().createNewProjectile((ProjectileShape) indicatorArrow, userInterface.getItemWindowController().getSelectedBodyId());
                if (projectileModel != null) {
                    itemWindowController.onProjectileCreated(projectileModel);
                    ((ProjectileShape) indicatorArrow).setModel(projectileModel);
                }
                return;
            }
        }
        if (action == CreationAction.HAND) {
            if (userInterface.getItemWindowController().getSelectedBodyId() != -1) {
                indicatorArrow = new HandShape(new Vector2(x, y), gameScene);

                HandModel handModel = userInterface.getToolModel().createNewHand((HandShape) indicatorArrow, userInterface.getItemWindowController().getSelectedBodyId());
                itemWindowController.onNewHandCreated(handModel);
                ((HandShape) indicatorArrow).setModel(handModel);
                return;
            }
        }

        if (action == CreationAction.PRISMATIC) {
            PrismaticJointDef prismaticJointDef = new PrismaticJointDef();
            indicatorArrow = new PrismaticJointShape(prismaticJointDef, gameScene, new Vector2(x, y));
            JointModel jointModel = userInterface.getToolModel().createNewJoint((JointShape) indicatorArrow, prismaticJointDef);
            jointWindowController.onJointAdded(jointModel);
            indicatorArrow.setCongruentEndpoints(getCongruentAnchors());
            return;
        }

        if (action == CreationAction.DISTANCE) {
            DistanceJointDef distanceJointDef = new DistanceJointDef();
            indicatorArrow = new DistanceJointShape(gameScene.getUserInterface(), new Vector2(x, y), gameScene);
            JointModel jointModel = userInterface.getToolModel().createNewJoint((JointShape) indicatorArrow, distanceJointDef);
            jointWindowController.onJointAdded(jointModel);
            indicatorArrow.setCongruentEndpoints(getCongruentAnchors());
            return;
        }
        if (action == CreationAction.SHIFT) {


            indicatorArrow = new ShiftArrowShape(new Vector2(x, y), layerWindowController.getSelectedShape(), gameScene);
            creationZone.setTouchLocked(true);
            return;
        }
        if (action == CreationAction.ROTATE) {
            Log.e("indicator", "rotate");
            indicatorArrow = new RotateArrowShape(new Vector2(x, y), layerWindowController.getSelectedShape(), gameScene, 64);
            creationZone.setTouchLocked(true);
            return;
        }

        if (action == CreationAction.ADD_POLYGON) {

            PointsModel layerPointsModel = layerWindowController.getSelectedShape();
            if(layerPointsModel==null)return;
            Vector2 center = new Vector2(x, y);

            indicatorArrow = (fixedRadiusForPolygon) ? new PolygonArrowShape(center, layerWindowController.getSelectedShape(), gameScene, numberOfPointsForPolygon, radiusForPolygon) : new PolygonArrowShape(center, layerWindowController.getSelectedShape(), gameScene, numberOfPointsForPolygon);
            Vector2 centerCopy = Vector2Pool.obtain(center);
            ReferencePointImage centerPointImage = new ReferencePointImage(centerCopy.cpy());

            layerPointsModel.getPointsShape().addCenterPointImage(centerPointImage);
            layerPointsModel.setCenter(centerCopy);
            userInterface.addReferencePoint(centerPointImage);
            creationZone.setTouchLocked(true);
            return;
        }

        if (action == CreationAction.MIRROR) {
            PointsModel selectedLayerPointsModel = layerWindowController.getSelectedShape();
            ArrayList<Vector2> points = selectedLayerPointsModel.getPoints();

            PointsModel shapePointsModel;
            if (selectedLayerPointsModel instanceof LayerPointsModel) {
                LayerField1 layer = layerWindowController.onAddLayerButtonCLicked(layerWindowController.getSelectedBodyField());
                shapePointsModel = layerWindowController.getLayerModel(layer.getPrimaryKey(), layer.getSecondaryKey());
            } else {
                DecorationField1 decoration = layerWindowController.onLayerAddDecorationClicked(layerWindowController.getSelectedLayerField());
                shapePointsModel = layerWindowController.getDecorationModel(decoration.getPrimaryKey(), decoration.getSecondaryKey(), decoration.getTertiaryKey());
            }
            if (selectedLayerPointsModel.getCenter() != null) {
                shapePointsModel.setCenter(selectedLayerPointsModel.getCenter().cpy());
                ReferencePointImage centerPointImage = new ReferencePointImage(shapePointsModel.getCenter().cpy());
                shapePointsModel.getPointsShape().addCenterPointImage(centerPointImage);

                userInterface.addReferencePoint(centerPointImage);
            }
            indicatorArrow = new MirrorArrowShape(new Vector2(x, y), shapePointsModel, points, gameScene);


            creationZone.setTouchLocked(true);

        }
    }

    public void onZoneActionMove(float x, float y) {
        if (action == CreationAction.NONE) {
            return;
        }

        if (indicatorArrow != null && !indicatorArrow.isCongruentEndpoints()) {
            indicatorArrow.onUpdated(x, y);
        }

    }

    public void setCreationZone(CreationZone creationZone) {
        this.creationZone = creationZone;
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


    public enum CreationAction {
        ADD_POINT, MOVE_POINT, REMOVE_POINT, ADD_POLYGON, NONE, MIRROR, ROTATE, SHIFT, REVOLUTE, PRISMATIC, WELD, DISTANCE, MOVE_JOINT_POINT, MOVE_IMAGE, ROTATE_IMAGE, SCALE_IMAGE, PIPING, PROJECTILE, MOVE_TOOL_POINT, HAND;
    }


}

