package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.userinterface.control.OutlineController;
import com.evolgames.userinterface.control.windowcontrollers.ZeroLevelSectionedAdvancedWindowController;
import com.evolgames.userinterface.model.jointmodels.JointModel;
import com.evolgames.userinterface.view.Strings;
import com.evolgames.userinterface.view.UserInterface;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.shapes.indicators.jointindicators.JointShape;
import com.evolgames.userinterface.view.shapes.points.PointImage;
import com.evolgames.userinterface.view.windows.gamewindows.JointsWindow;
import com.evolgames.userinterface.view.windows.windowfields.jointswindow.JointField;

import java.util.ArrayList;
import java.util.List;

public class JointWindowController extends ZeroLevelSectionedAdvancedWindowController<JointsWindow, JointField> {

    private final OutlineController outlineController;
    private UserInterface userInterface;
    private JointModel selectedJointModel;

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
        jointField.getJointControl().updateState(Button.State.PRESSED);
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
                    otherJointField.getJointControl().updateState(Button.State.NORMAL);
                    onPrimaryButtonReleased(otherJointField);
                }
        }
        JointModel jointModel = userInterface.getToolModel().getJointById(jointField.getPrimaryKey());
        this.selectedJointModel = jointModel;
        jointSettingsWindowController.updateBodySelectionFields();
        jointSettingsWindowController.updateJointModel(jointModel);
        jointField.showFields();
        outlineController.onJointBodySelectionUpdated(jointModel.getBodyModel1(),jointModel.getBodyModel2());
    }


    @Override
    public void onPrimaryButtonReleased(JointField jointField) {
        super.onPrimaryButtonReleased(jointField);
        userInterface.getToolModel().deselectJoint(jointField.getPrimaryKey());
        jointField.hideFields();
        outlineController.onJointBodySelectionUpdated(null,null);
    }


    public void onOptionButtonReleased(JointField jointField) {
        jointSettingsWindowController.openWindow();
        unfold();
        JointModel jointModel = userInterface.getToolModel().getJointById(jointField.getPrimaryKey());
        jointSettingsWindowController.updateJointModel(jointModel);
    }

    public void onRemoveButtonReleased(JointField jointField) {
        JointModel jointModel = userInterface.getToolModel().getJointById(jointField.getPrimaryKey());
        userInterface.doWithConfirm(String.format(Strings.JOINT_DELETE_CONFIRM,jointModel.getJointName()),()-> {
            userInterface.getToolModel().removeJoint(jointField.getPrimaryKey());
            window.getLayout().removePrimary(jointField.getPrimaryKey());
            updateLayout();
            JointShape jointShape = jointModel.getJointShape();
            jointShape.detach();
            updateLayout();
        });
    }

    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public List<PointImage> getSelectedModelMovables(boolean moveLimits) {
        if(selectedJointModel==null){
            return null;
        }
        return selectedJointModel.getJointShape().getMovables(moveLimits);
    }
}
