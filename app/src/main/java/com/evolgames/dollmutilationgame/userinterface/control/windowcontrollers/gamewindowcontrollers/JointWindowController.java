package com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.dollmutilationgame.userinterface.control.OutlineController;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.Button;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.ZeroLevelSectionedAdvancedWindowController;
import com.evolgames.dollmutilationgame.userinterface.model.jointmodels.JointModel;
import com.evolgames.dollmutilationgame.userinterface.view.EditorUserInterface;
import com.evolgames.dollmutilationgame.userinterface.view.Strings;
import com.evolgames.dollmutilationgame.userinterface.view.shapes.indicators.jointindicators.JointShape;
import com.evolgames.dollmutilationgame.userinterface.view.shapes.points.PointImage;
import com.evolgames.dollmutilationgame.userinterface.view.windows.gamewindows.JointsWindow;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.jointswindow.JointField;

import java.util.ArrayList;
import java.util.List;

public class JointWindowController
        extends ZeroLevelSectionedAdvancedWindowController<JointsWindow, JointField> {

    private OutlineController outlineController;
    private EditorUserInterface editorUserInterface;
    private JointModel selectedJointModel;
    private JointSettingsWindowController jointSettingsWindowController;

    public void setOutlineController(OutlineController outlineController) {
        this.outlineController = outlineController;
    }

    public void setJointSettingsWindowController(
            JointSettingsWindowController jointSettingsWindowController) {
        this.jointSettingsWindowController = jointSettingsWindowController;
    }

    @Override
    public void init() {
        if (editorUserInterface.getToolModel() == null) {
            return;
        }
        ArrayList<JointModel> joints = editorUserInterface.getToolModel().getJoints();
        for (int i = 0; i < joints.size(); i++) {
            JointModel jointModel = joints.get(i);
            onJointAdded(jointModel);
        }
    }

    public void onResume() {
        fold();
    }

    public void onJointAdded(JointModel jointModel) {
        JointField jointField = window.addPrimary(jointModel.getJointId(), false);
        jointField.getJointControl().updateState(Button.State.PRESSED);
        onPrimaryButtonClicked(jointField);
        super.onPrimaryAdded(jointField);
    }

    @Override
    public void onPrimaryButtonClicked(JointField jointField) {
        super.onPrimaryButtonClicked(jointField);
        for (int i = 0; i < window.getLayout().getPrimariesSize(); i++) {
            JointField otherJointField = window.getLayout().getPrimaryByIndex(i);
            if (otherJointField != null)
                if (otherJointField != jointField) {
                    otherJointField.getJointControl().updateState(Button.State.NORMAL);
                    onPrimaryButtonReleased(otherJointField);
                }
        }
        JointModel jointModel =
                editorUserInterface.getToolModel().getJointById(jointField.getPrimaryKey());
        this.selectedJointModel = jointModel;
        jointSettingsWindowController.updateBodySelectionFields();
        jointSettingsWindowController.updateJointModel(jointModel);
        jointField.showFields();
        outlineController.onJointBodySelectionUpdated(jointModel);
    }

    @Override
    public void onPrimaryButtonReleased(JointField jointField) {
        super.onPrimaryButtonReleased(jointField);
        jointField.hideFields();
        outlineController.onJointBodySelectionUpdated(null);
    }

    public void onOptionButtonReleased(JointField jointField) {
        jointSettingsWindowController.openWindow();
        unfold();
        JointModel jointModel =
                editorUserInterface.getToolModel().getJointById(jointField.getPrimaryKey());
        jointSettingsWindowController.updateJointModel(jointModel);
    }

    public void onRemoveButtonReleased(JointField jointField) {
        JointModel jointModel =
                editorUserInterface.getToolModel().getJointById(jointField.getPrimaryKey());
        editorUserInterface.doWithConfirm(
                String.format(Strings.JOINT_DELETE_CONFIRM, jointModel.getModelName()),
                () -> {
                    editorUserInterface.getToolModel().removeJoint(jointField.getPrimaryKey());
                    window.getLayout().removePrimary(jointField.getPrimaryKey());
                    updateLayout();
                    JointShape jointShape = jointModel.getJointShape();
                    jointShape.detach();
                    updateLayout();
                });
    }

    public void setUserInterface(EditorUserInterface editorUserInterface) {
        this.editorUserInterface = editorUserInterface;
    }

    public List<PointImage> getSelectedModelMovables(boolean moveLimits) {
        if (selectedJointModel == null) {
            return null;
        }
        return selectedJointModel.getJointShape().getMovables(moveLimits);
    }
}
