package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.userinterface.control.OutlineController;
import com.evolgames.userinterface.control.windowcontrollers.ZeroLevelSectionedAdvancedWindowController;
import com.evolgames.userinterface.model.jointmodels.JointModel;
import com.evolgames.userinterface.view.UserInterface;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.shapes.indicators.jointindicators.JointShape;
import com.evolgames.userinterface.view.windows.gamewindows.JointsWindow;
import com.evolgames.userinterface.view.windows.windowfields.jointswindow.JointField;

import java.util.ArrayList;

public class JointWindowController extends ZeroLevelSectionedAdvancedWindowController<JointsWindow, JointField> {

    private final OutlineController outlineController;
    private UserInterface userInterface;

    public JointWindowController(JointSettingsWindowController jointSettingsWindowController, OutlineController outlineController) {
        this.jointSettingsWindowController = jointSettingsWindowController;
        jointSettingsWindowController.setJointWindowController(this);
        this.outlineController = outlineController;
    }

    @Override
    public void init() {
        if (userInterface.getToolModel() == null){
            return;
        }
        ArrayList<JointModel> joints = userInterface.getToolModel().getJoints();
        for (int i = 0; i < joints.size(); i++) {
            JointModel jointModel = joints.get(i);
            onJointAdded(jointModel);
        }
    }
    public void onResume() {
        fold();
    }
    private final JointSettingsWindowController jointSettingsWindowController;

    public void onJointAdded(JointModel jointModel) {
        JointField jointField = window.addPrimary(jointModel.getJointId(), false);
        jointField.getBodyControl().updateState(Button.State.PRESSED);
        onPrimaryButtonClicked(jointField);
        super.onPrimaryAdded(jointField);
    }


    @Override
    public void onPrimaryButtonClicked(JointField jointField) {
        super.onPrimaryButtonClicked(jointField);
        userInterface.getToolModel().selectJoint(jointField.getPrimaryKey());
        for (int i = 0; i < window.getLayout().getPrimariesSize(); i++) {
            JointField otherJointField = window.getLayout().getPrimaryByIndex(i);
            if (otherJointField != null)
                if (otherJointField != jointField) {
                    otherJointField.getBodyControl().updateState(Button.State.NORMAL);
                    onPrimaryButtonReleased(otherJointField);
                }
        }
        JointModel jointModel = userInterface.getToolModel().getJointById(jointField.getPrimaryKey());

        jointSettingsWindowController.updateBodySelectionFields();
        jointSettingsWindowController.updateJointModel(jointModel);
        outlineController.onJointBodySelectionUpdated(jointModel.getBodyModel1(),jointModel.getBodyModel2());
    }


    @Override
    public void onPrimaryButtonReleased(JointField jointField) {
        super.onPrimaryButtonReleased(jointField);
        userInterface.getToolModel().deselectJoint(jointField.getPrimaryKey());
        outlineController.onJointBodySelectionUpdated(null,null);
    }


    public void onOptionButtonReleased(JointField jointField) {
        jointSettingsWindowController.openWindow();
        unfold();
        JointModel jointModel = userInterface.getToolModel().getJointById(jointField.getPrimaryKey());
        jointSettingsWindowController.updateJointModel(jointModel);
    }

    public void onRemoveButtonReleased(JointField jointField) {
        window.getLayout().removePrimary(jointField.getPrimaryKey());
        window.getLayout().updateLayout();
        JointModel jointModel = userInterface.getToolModel().removeJoint(jointField.getPrimaryKey());
        JointShape jointShape = jointModel.getJointShape();
        jointShape.detach();
        updateLayout();
    }

    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }
}
