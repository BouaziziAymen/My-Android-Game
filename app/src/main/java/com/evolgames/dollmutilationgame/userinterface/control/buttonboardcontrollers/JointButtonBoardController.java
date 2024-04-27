package com.evolgames.dollmutilationgame.userinterface.control.buttonboardcontrollers;


import com.evolgames.dollmutilationgame.userinterface.control.CreationAction;
import com.evolgames.dollmutilationgame.userinterface.control.CreationZoneController;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.OptionsWindowController;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.SettingsType;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.Button;
import com.evolgames.dollmutilationgame.userinterface.view.layouts.ButtonBoard;

public class JointButtonBoardController extends ButtonBoardController {
    private final OptionsWindowController optionsWindowController;
    private final CreationZoneController creationZoneController;

    public JointButtonBoardController(
            ButtonBoard buttonBoard,
            CreationZoneController creationZoneController,
            OptionsWindowController optionsWindowController) {
        super(buttonBoard);
        this.creationZoneController = creationZoneController;
        this.optionsWindowController = optionsWindowController;
    }

    public void onRevoluteButtonClicked(Button button) {
        onButtonClicked(button);
        creationZoneController.setAction(CreationAction.REVOLUTE);
        optionsWindowController.selectSettingsType(SettingsType.REVOLUTE_JOINT_SETTINGS);
    }

    public void onRevoluteButtonReleased(Button button) {
        onButtonReleased(button);
        creationZoneController.setAction(CreationAction.NONE);
        optionsWindowController.selectSettingsType(SettingsType.NONE);
    }

    public void onPrismaticButtonClicked(Button button) {
        onButtonClicked(button);
        creationZoneController.setAction(CreationAction.PRISMATIC);
        optionsWindowController.selectSettingsType(SettingsType.PRISMATIC_JOINT_SETTINGS);
    }

    public void onPrismaticButtonReleased(Button button) {
        onButtonReleased(button);
        creationZoneController.setAction(CreationAction.NONE);
        optionsWindowController.selectSettingsType(SettingsType.NONE);
    }

    public void onDistanceButtonClicked(Button button) {
        onButtonClicked(button);
        creationZoneController.setAction(CreationAction.DISTANCE);
        optionsWindowController.selectSettingsType(SettingsType.DISTANCE_JOINT_SETTINGS);
    }

    public void onDistanceButtonReleased(Button button) {
        onButtonReleased(button);
        creationZoneController.setAction(CreationAction.NONE);
        optionsWindowController.selectSettingsType(SettingsType.NONE);
    }

    public void onWeldButtonClicked(Button button) {
        onButtonClicked(button);
        creationZoneController.setAction(CreationAction.WELD);
        optionsWindowController.selectSettingsType(SettingsType.WELD_JOINT_SETTINGS);
    }

    public void onWeldButtonReleased(Button button) {
        onButtonReleased(button);
        creationZoneController.setAction(CreationAction.NONE);
        optionsWindowController.selectSettingsType(SettingsType.NONE);
    }

    public void onMoveButtonClicked(Button button) {
        onButtonClicked(button);
        creationZoneController.setAction(CreationAction.MOVE_JOINT_POINT);
        optionsWindowController.selectSettingsType(SettingsType.MOVE_JOINT_POINT_SETTINGS);
    }

    public void onMoveButtonReleased(Button button) {
        onButtonReleased(button);
        creationZoneController.setAction(CreationAction.NONE);
        optionsWindowController.selectSettingsType(SettingsType.NONE);
    }
}
