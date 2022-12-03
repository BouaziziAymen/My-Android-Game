package com.evolgames.userinterface.control;

import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ItemWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.JointSettingsWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.JointWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.LayerWindowController;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.DecorationModel;
import com.evolgames.userinterface.model.LayerModel;
import com.evolgames.userinterface.model.ToolModel;
import com.evolgames.userinterface.view.Color;
import com.evolgames.userinterface.view.Colors;
import com.evolgames.userinterface.view.Screen;
import com.evolgames.userinterface.view.UserInterface;
import com.evolgames.userinterface.view.shapes.PointsShape;

public class OutlineController extends Controller {
    private UserInterface userInterface;

    @Override
    public void init() {

    }

    private void resetAll() {
        for (BodyModel bodyModel : userInterface.getToolModel().getBodies()) {
            for (LayerModel layerModel : bodyModel.getLayers()) {
                PointsShape layerShape = layerModel.getPointsShape();
                layerShape.setLineLoopColor(Colors.white);
                layerShape.setVisible(false);
                for (DecorationModel decorationModel : layerModel.getDecorations()) {
                    PointsShape decorationShape = decorationModel.getPointsShape();
                    decorationShape.setLineLoopColor(Colors.white);
                    decorationShape.setVisible(false);
                }
            }
        }
    }

    public void onSelectionUpdated(BodyModel selectedBodyModel, LayerModel selectedLayerModel, DecorationModel selectedDecorationModel) {
        this.resetAll();
        if (selectedDecorationModel != null) {
            selectBodyModel(selectedBodyModel,Colors.white);
            selectedDecorationModel.getPointsShape().setPointsVisible(true);
            selectedDecorationModel.getPointsShape().setOutlineVisible(true);
            selectedDecorationModel.getPointsShape().setLineLoopColor(Colors.palette1_green);
            selectedLayerModel.getPointsShape().setLineLoopColor(Colors.palette1_blue);
            selectedLayerModel.getPointsShape().setOutlineVisible(true);
        } else if (selectedLayerModel != null) {
            selectBodyModel(selectedBodyModel,Colors.white);
            selectedLayerModel.getPointsShape().setLineLoopColor(Colors.palette1_green);
            selectedLayerModel.getPointsShape().setOutlineVisible(true);
            selectedLayerModel.getPointsShape().setPointsVisible(true);
        } else if (selectedBodyModel != null) {
            this.selectBodyModel(selectedBodyModel,Colors.white);
        } else {
            this.deselectBodyModels();
        }

    }

    private void deselectBodyModels() {
        for (BodyModel bodyModel : userInterface.getToolModel().getBodies()) {
            bodyModel.getLayers().forEach(layerModel -> {
                layerModel.getPointsShape().setVisible(false);
                layerModel.getPointsShape().setLineLoopColor(Colors.white);
            });
        }
    }

    private void selectBodyModel(BodyModel bodyModel, Color color) {
        bodyModel.getLayers().forEach(layerModel -> {
            layerModel.getPointsShape().setOutlineVisible(true);
            layerModel.getPointsShape().setLineLoopColor(color);
        });
    }

    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void onScreenChanged(Screen selectedScreen) {
            resetAll();
            switch (selectedScreen){
                case DRAW_SCREEN:
                    LayerWindowController layerController = userInterface.getLayersWindowController();
                    this.onSelectionUpdated(layerController.getSelectedBodyModel(),layerController.getSelectedLayerModel(),layerController.getSelectedDecorationModel() );
                    break;
                case JOINTS_SCREEN:
                    JointSettingsWindowController jointSettingsWindowController = userInterface.getJointSettingsWindowController();
                    this.onJointBodySelectionUpdated(jointSettingsWindowController.getBodyModelA(),jointSettingsWindowController.getBodyModelB());
                    break;
                case ITEMS_SCREEN:
                    ItemWindowController itemWindowController = userInterface.getItemWindowController();
                    this.onSelectionUpdated(itemWindowController.getSelectedBodyModel(),null,null );
                    break;
                case IMAGE_SCREEN:
                    break;
                case SAVE_SCREEN:
                    break;
                case NONE:
                    break;
            }
    }

    public void onJointBodySelectionUpdated(BodyModel bodyModelA, BodyModel bodyModelB) {
        resetAll();
        if(bodyModelA!=null){
            selectBodyModel(bodyModelA,Colors.palette1_joint_a_color);
        }
        if(bodyModelB!=null){
            selectBodyModel(bodyModelB,Colors.palette1_joint_b_color);
        }
    }
}
