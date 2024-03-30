package com.evolgames.userinterface.control;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.evolgames.scenes.EditorScene;
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
import com.evolgames.userinterface.model.toolmodels.DragModel;
import com.evolgames.userinterface.model.toolmodels.FireSourceModel;
import com.evolgames.userinterface.model.toolmodels.LiquidSourceModel;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;
import com.evolgames.userinterface.model.toolmodels.SpecialPointModel;
import com.evolgames.userinterface.view.EditorUserInterface;
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
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.DragShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.FireSourceShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.LiquidSourceShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.ProjectileShape;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.SpecialPointShape;
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

    private final EditorScene editorScene;
    private LayerWindowController layerWindowController;
    private JointWindowController jointWindowController;
    private ItemWindowController itemWindowController;
    private CreationAction action = CreationAction.NONE;
    private EditorUserInterface editorUserInterface;
    private LineShape indicatorArrow;
    private CreationZone creationZone;
    private PointImage selectedPointImage = null;
    private float radiusForPolygon = 32;
    private boolean fixedRadiusForPolygon;
    private boolean reference;
    private float movePointSpeed;
    private int numberOfPointsForPolygon = 3;
    private boolean magnetCenters;
    private boolean moveLimits;
    private boolean congruentAnchors;
    private boolean sameShape;
    private boolean invertShape;
    private boolean imageFixedRatio = true;

    public CreationZoneController(EditorScene editorScene) {
        this.editorScene = editorScene;
    }

    public void setLayerWindowController(LayerWindowController layerWindowController) {
        this.layerWindowController = layerWindowController;
    }

    public void setJointWindowController(JointWindowController jointWindowController) {
        this.jointWindowController = jointWindowController;
    }

    public void setItemWindowController(ItemWindowController itemWindowController) {
        this.itemWindowController = itemWindowController;
    }

    public void setUserInterface(EditorUserInterface editorUserInterface) {
        this.editorUserInterface = editorUserInterface;
    }

    public CreationAction getAction() {
        return action;
    }

    public void setAction(CreationAction action) {
        this.action = action;
        if (editorUserInterface.getImageShape() != null)
            editorUserInterface.getImageShape().setPipeCircleVisibility(action == CreationAction.PIPING);
        editorUserInterface.onActionChanged(action);
        switch (action) {
            case AMMO:
            case DISTANCE:
            case WELD:
            case PRISMATIC:
            case ROTATE:
            case MIRROR:
            case SHIFT:
            case REVOLUTE:
            case PROJECTILE:
            case ADD_POLYGON:
            case SCALE_IMAGE:
            case ROTATE_IMAGE:
            case FIRE_SOURCE:
            case LIQUID_SOURCE:
            case DRAG:
            case MOVE_IMAGE:
                editorScene.setScrollerEnabled(false);
                editorScene.setZoomEnabled(false);
                break;
            case MOVE_JOINT_POINT:
            case PIPING:
            case REMOVE_POINT:
            case MOVE_POINT:
            case MOVE_TOOL_POINT:
            case ADD_POINT:
            case BOMB:
            case SPECIAL_POINT:
                editorScene.setScrollerEnabled(true);
                editorScene.setZoomEnabled(true);
                break;
            case NONE:
                if(selectedPointImage!=null){
                  releaseSelectedPointImage();
                }
                editorScene.setScrollerEnabled(true);
                editorScene.setZoomEnabled(true);
                break;
        }
    }

    @Override
    public void init() {
    }

    public void onZoneActionUp(float x, float y) {
        editorScene.setHudLocked(false);
        processAbortedIndicators();
        if (action == CreationAction.ADD_POINT) {
            processAddPoint(x, y);
        }
        if (action == CreationAction.REMOVE_POINT) {
            processRemovePoint(x, y);
        }

        if (indicatorArrow != null) {

            if (!(indicatorArrow instanceof JointShape || indicatorArrow instanceof ProjectileShape || indicatorArrow instanceof CasingShape || indicatorArrow instanceof FireSourceShape || indicatorArrow instanceof LiquidSourceShape || indicatorArrow instanceof DragShape)) {
                indicatorArrow.detach();
            }
            indicatorArrow = null;
        }
        processMobilePoints(x, y);

        releaseBoardsButtons();
    }

    private void releaseBoardsButtons() {
        if (action == CreationAction.ROTATE_IMAGE || action == CreationAction.SCALE_IMAGE|| action == CreationAction.MOVE_IMAGE) {
            editorUserInterface.getImageButtonBoardController().releaseButtons();
            setAction(CreationAction.NONE);
        }
        if (action == CreationAction.ADD_POLYGON || action == CreationAction.MIRROR || action == CreationAction.SHIFT || action == CreationAction.ROTATE) {
            editorUserInterface.getDrawButtonBoardController().releaseButtons();
            setAction(CreationAction.NONE);
        }
        if (action == CreationAction.REVOLUTE || action == CreationAction.DISTANCE || action == CreationAction.PRISMATIC) {
            editorUserInterface.getJointButtonBoardController().releaseButtons();
            setAction(CreationAction.NONE);
        }
        if (action == CreationAction.PIPING) {
            editorUserInterface.getImageButtonBoardController().releaseButtons();
            setAction(CreationAction.NONE);
        }
        if (action == CreationAction.BOMB ||action==CreationAction.SPECIAL_POINT|| action == CreationAction.PROJECTILE || action == CreationAction.AMMO || action == CreationAction.LIQUID_SOURCE || action == CreationAction.FIRE_SOURCE || action == CreationAction.DRAG) {
            editorUserInterface.getItemButtonBoardController().releaseButtons();
            setAction(CreationAction.NONE);
        }

    }

    private void processRemovePoint(float x, float y) {
        if (layerWindowController.getSelectedPointsModel() != null) {
            float distance = 32 / editorUserInterface.getZoomFactor();
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
            editorScene.setHudLocked(false);
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
            if (pointsModel.testAdd(x, y)) {
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
            float distance = 32f/editorUserInterface.getZoomFactor();
            PointImage point = null;
            for (PointImage p : movablePointImages) {
                float d = p.getPoint().dst(x, y);
                if (d < distance) {
                    point = p;
                    distance = d;
                }
            }
            if (point != null) {
                if (point == selectedPointImage) {
                    releaseSelectedPointImage();
                    editorUserInterface.hideMoveElementController();
                } else {
                    releaseSelectedPointImage();
                    selectPointImage(point);
                }
            }
        }
    }

    public void createReferencePoint() {
        if (selectedPointImage != null) {
            if (selectedPointImage.getPointsShape() != null) {
                    selectedPointImage.getPointsShape().createReferencePointImage(selectedPointImage.getPoint());
            }
        }
    }

    public void selectPointImage(PointImage pointImage) {
        pointImage.doubleSelect();
        selectedPointImage = pointImage;
        float moveSpeed = 1f / editorUserInterface.getZoomFactor();
        editorUserInterface.setMoveElementController(editorUserInterface.getPanel().allocateController(800 - 64 / 2f - 16f, 64 / 2f + 16f, ControlElement.Type.AnalogController, new ControllerAction() {
            @Override
            public void controlMoved(float pX, float pY) {
                pointImage.onControllerMoved(pX * moveSpeed, pY * moveSpeed);
            }

            @Override
            public void controlClicked() {
            }

            @Override
            public void controlReleased() {
            }
        }));
    }

    public void releaseSelectedPointImage() {
        if (selectedPointImage != null) {
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

        if (action == CreationAction.NONE) {
            return;
        }

        if (action == CreationAction.ROTATE_IMAGE) {
            if (editorScene.getUserInterface().getImageShape() != null) {
                float X = editorScene.getUserInterface().getImageShape().getX();
                float Y = editorScene.getUserInterface().getImageShape().getY();

                indicatorArrow = new RotateImageShape(new Vector2(X, Y), editorScene.getUserInterface().getImageShape(), editorScene, 32);
            editorScene.setHudLocked(true);
            }
        }
        if (action == CreationAction.SCALE_IMAGE) {
            if (editorScene.getUserInterface().getImageShape() != null) {
                float X = editorScene.getUserInterface().getImageShape().getX();
                float Y = editorScene.getUserInterface().getImageShape().getY();
                indicatorArrow = new ScaleImageShape(new Vector2(X, Y), editorScene.getUserInterface().getImageShape(), editorScene, imageFixedRatio);
                editorScene.setHudLocked(true);
            }
        }

        if (action == CreationAction.MOVE_IMAGE) {
            if (editorScene.getUserInterface().getImageShape() != null) {
                indicatorArrow = new ShiftImageShape(new Vector2(x, y), editorScene.getUserInterface().getImageShape(), editorScene);
                editorScene.setHudLocked(true);
            }
        }
        if (action == CreationAction.REVOLUTE) {
            RevoluteJointShape revoluteJointShape = new RevoluteJointShape(editorScene, new Vector2(x, y));
            JointModel jointModel = editorUserInterface.getToolModel().createJointModel((JointShape) indicatorArrow, JointDef.JointType.RevoluteJoint);
            jointModel.getProperties().getLocalAnchorA().set(x, y);
            jointModel.getProperties().getLocalAnchorB().set(x, y);
            revoluteJointShape.bindModel(jointModel);
            jointWindowController.onJointAdded(jointModel);
            revoluteJointShape.setCongruentEndpoints(getCongruentAnchors());
            this.indicatorArrow = revoluteJointShape;
            editorScene.setHudLocked(true);
            return;
        }
        if (action == CreationAction.WELD) {
            WeldJointShape weldJointShape = new WeldJointShape(editorScene, new Vector2(x, y));
            JointModel jointModel = editorUserInterface.getToolModel().createJointModel(weldJointShape, JointDef.JointType.WeldJoint);
            jointModel.getProperties().getLocalAnchorA().set(x, y);
            jointModel.getProperties().getLocalAnchorB().set(x, y);
            weldJointShape.bindModel(jointModel);
            jointWindowController.onJointAdded(jointModel);
            weldJointShape.setCongruentEndpoints(getCongruentAnchors());
            this.indicatorArrow = weldJointShape;
            editorScene.setHudLocked(true);
            return;
        }
        if (action == CreationAction.PRISMATIC) {
            PrismaticJointShape prismaticJointShape = new PrismaticJointShape(editorScene, new Vector2(x, y));
            JointModel jointModel = editorUserInterface.getToolModel().createJointModel(prismaticJointShape, JointDef.JointType.PrismaticJoint);
            jointModel.getProperties().getLocalAnchorA().set(x, y);
            jointModel.getProperties().getLocalAnchorB().set(x, y);
            prismaticJointShape.bindModel(jointModel);
            jointWindowController.onJointAdded(jointModel);
            prismaticJointShape.setCongruentEndpoints(getCongruentAnchors());
            this.indicatorArrow = prismaticJointShape;
            editorScene.setHudLocked(true);
            return;
        }
        if (action == CreationAction.DISTANCE) {
            DistanceJointShape distanceJointShape = new DistanceJointShape(editorScene, new Vector2(x, y));
            JointModel jointModel = editorUserInterface.getToolModel().createJointModel(distanceJointShape, JointDef.JointType.DistanceJoint);
            jointModel.getProperties().getLocalAnchorA().set(x, y);
            jointModel.getProperties().getLocalAnchorB().set(x, y);
            jointWindowController.onJointAdded(jointModel);
            distanceJointShape.bindModel(jointModel);
            distanceJointShape.setCongruentEndpoints(getCongruentAnchors());
            this.indicatorArrow = distanceJointShape;
            editorScene.setHudLocked(true);
            return;
        }
        if (action == CreationAction.PROJECTILE) {
            if (editorUserInterface.getItemWindowController().getSelectedBodyId() != -1) {
                ProjectileShape projectileShape = new ProjectileShape(new Vector2(x, y), editorScene);
                ProjectileModel projectileModel = editorUserInterface.getToolModel().createNewProjectile(projectileShape, editorUserInterface.getItemWindowController().getSelectedBodyId());
                projectileModel.getProperties().setProjectileOrigin(new Vector2(x, y));
                projectileModel.getProperties().setProjectileEnd(new Vector2(x, y));
                itemWindowController.onProjectileCreated(projectileModel);
                projectileShape.bindModel(projectileModel);
                this.indicatorArrow = projectileShape;
                editorScene.setHudLocked(true);
                return;
            }
        }
        if (action == CreationAction.DRAG) {
            if (editorUserInterface.getItemWindowController().getSelectedBodyId() != -1) {
                DragShape dragShape = new DragShape(new Vector2(x, y), editorScene);
                DragModel dragModel = editorUserInterface.getToolModel().createNewDrag(dragShape, editorUserInterface.getItemWindowController().getSelectedBodyId());
                itemWindowController.onDragCreated(dragModel);
                dragShape.bindModel(dragModel);
                this.indicatorArrow = dragShape;
                editorScene.setHudLocked(true);
            }
        }
        if (action == CreationAction.LIQUID_SOURCE) {
            if (editorUserInterface.getItemWindowController().getSelectedBodyId() != -1) {
                LiquidSourceShape liquidSourceShape = new LiquidSourceShape(new Vector2(x, y), editorScene);
                LiquidSourceModel liquidSourceModel = editorUserInterface.getToolModel().createNewLiquidSource(liquidSourceShape, editorUserInterface.getItemWindowController().getSelectedBodyId());
                itemWindowController.onLiquidSourceCreated(liquidSourceModel);
                liquidSourceShape.bindModel(liquidSourceModel);
                this.indicatorArrow = liquidSourceShape;
                editorScene.setHudLocked(true);
            }
        }
        if (action == CreationAction.BOMB) {
            if (editorUserInterface.getItemWindowController().getSelectedBodyId() != -1) {
                BombShape bombShape = new BombShape(new Vector2(x, y), editorScene);
                BombModel bombModel = editorUserInterface.getToolModel().createNewBomb(bombShape, editorUserInterface.getItemWindowController().getSelectedBodyId());
                itemWindowController.onBombCreated(bombModel);
                bombShape.bindModel(bombModel);
                editorScene.setHudLocked(true);
            }
        }
        if (action == CreationAction.SPECIAL_POINT) {
            if (editorUserInterface.getItemWindowController().getSelectedBodyId() != -1) {
                SpecialPointShape specialPointShape = new SpecialPointShape(new Vector2(x, y), editorScene);
                SpecialPointModel specialPointModel = editorUserInterface.getToolModel().createNewSpecialPoint(specialPointShape, editorUserInterface.getItemWindowController().getSelectedBodyId());
                itemWindowController.onSpecialPointCreated(specialPointModel);
                specialPointShape.bindModel(specialPointModel);
                editorScene.setHudLocked(true);
            }
        }
        if (action == CreationAction.AMMO) {
            if (editorUserInterface.getItemWindowController().getSelectedBodyId() != -1) {
                CasingShape casingShape = new CasingShape(new Vector2(x, y), editorScene);
                CasingModel ammoModel = editorUserInterface.getToolModel().createNewAmmo(casingShape, editorUserInterface.getItemWindowController().getSelectedBodyId());
                itemWindowController.onCasingCreated(ammoModel);
                casingShape.bindModel(ammoModel);
                this.indicatorArrow = casingShape;
                editorScene.setHudLocked(true);
                return;
            }
        }
        if (action == CreationAction.FIRE_SOURCE) {
            if (editorUserInterface.getItemWindowController().getSelectedBodyId() != -1) {
                FireSourceShape fireSourceShape = new FireSourceShape(new Vector2(x, y), editorScene);
                FireSourceModel fireSourceModel = editorUserInterface.getToolModel().createNewFireSource(fireSourceShape, editorUserInterface.getItemWindowController().getSelectedBodyId());
                itemWindowController.onFireSourceCreated(fireSourceModel);
                fireSourceShape.bindModel(fireSourceModel);
                this.indicatorArrow = fireSourceShape;
                editorScene.setHudLocked(true);
                return;
            }
        }
        if (action == CreationAction.SHIFT && layerWindowController.getSelectedPointsModel() != null) {
            indicatorArrow = new ShiftArrowShape(new Vector2(x, y), layerWindowController.getSelectedPointsModel(), editorScene);
            editorScene.setHudLocked(true);
            return;
        }
        if (action == CreationAction.ROTATE && layerWindowController.getSelectedPointsModel() != null) {
            indicatorArrow = new RotateArrowShape(new Vector2(x, y), layerWindowController.getSelectedPointsModel(), editorScene, 64);
            editorScene.setHudLocked(true);
            return;
        }

        if (action == CreationAction.ADD_POLYGON && layerWindowController.getSelectedPointsModel() != null) {

            PointsModel<?> selectedPointsModel = layerWindowController.getSelectedPointsModel();
            if (selectedPointsModel == null) {
                return;
            }
            Vector2 center = new Vector2(x, y);

            indicatorArrow = (fixedRadiusForPolygon) ? new PolygonArrowShape(center, layerWindowController.getSelectedPointsModel(), editorScene, numberOfPointsForPolygon, radiusForPolygon) : new PolygonArrowShape(center, layerWindowController.getSelectedPointsModel(), editorScene, numberOfPointsForPolygon);
            selectedPointsModel.getReferencePoints().add(center);
            editorScene.setHudLocked(true);
            return;
        }

        if (action == CreationAction.MIRROR && layerWindowController.getSelectedPointsModel() != null) {
            PointsModel<?> selectedLayerPointsModel = layerWindowController.getSelectedPointsModel();
            PointsModel<?> shapePointsModel;
            if (selectedLayerPointsModel instanceof LayerModel) {
                LayerModel layerModel = (LayerModel) selectedLayerPointsModel;
                if (!isSameShape()) {
                    LayerField layer = layerWindowController.onAddLayerButtonCLicked(editorUserInterface.getToolModel().getBodyModelById(layerModel.getBodyId()).getField());
                    shapePointsModel = layerWindowController.getLayerModel(layer.getPrimaryKey(), layer.getSecondaryKey());
                } else {
                    shapePointsModel = layerModel;
                }

            } else {
                DecorationModel decorationModel = (DecorationModel) selectedLayerPointsModel;
                if (!isSameShape()) {
                    DecorationField decoration = layerWindowController.onLayerAddDecorationClicked(editorUserInterface.getToolModel().getLayerModelById(decorationModel.getLayerModel().getBodyId(), decorationModel.getLayerModel().getLayerId()).getField());
                    shapePointsModel = layerWindowController.getDecorationModel(decoration.getPrimaryKey(), decoration.getSecondaryKey(), decoration.getTertiaryKey());
                } else {
                    shapePointsModel = decorationModel;
                }
            }

            shapePointsModel.setPoints(selectedLayerPointsModel.getPoints().stream().map(Vector2::new).collect(Collectors.toList()));
            shapePointsModel.setReferencePoints(selectedLayerPointsModel.getReferencePoints().stream().map(Vector2::new).collect(Collectors.toList()));
            indicatorArrow = new MirrorArrowShape(new Vector2(x, y), shapePointsModel, editorScene, isSameShape(), isInvertShape());
            editorScene.setHudLocked(true);
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

    public boolean isCenterMagnet() {
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

    public void setDirection(OptionsWindowController.Direction direction) {
    }

    public boolean isSameShape() {
        return sameShape;
    }

    public void setSameShape(boolean sameShape) {
        this.sameShape = sameShape;
    }

    public boolean isInvertShape() {
        return invertShape;
    }

    public void setInvertShape(boolean invertShape) {
        this.invertShape = invertShape;
    }

    public boolean isImageFixedRatio() {
        return imageFixedRatio;
    }

    public void setImageFixedRatio(boolean imageFixedRatio) {
        this.imageFixedRatio = imageFixedRatio;
    }

}
