package com.evolgames.userinterface.control.buttonboardcontrollers;

import com.evolgames.userinterface.control.CreationAction;
import com.evolgames.userinterface.control.CreationZoneController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.OptionsWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.SettingsType;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.layouts.ButtonBoard;

public class DrawButtonBoardController extends ButtonBoardController {
    private final OptionsWindowController optionsWindowController;
    private final CreationZoneController creationZoneController;

    public DrawButtonBoardController(
            ButtonBoard buttonBoard,
            CreationZoneController creationZoneController,
            OptionsWindowController optionsWindowController) {
        super(buttonBoard);
        this.creationZoneController = creationZoneController;
        this.optionsWindowController = optionsWindowController;
    }

    public void onPolygonCreationButtonClicked(Button button) {
        onButtonClicked(button);
        optionsWindowController.selectSettingsType(SettingsType.POLYGON_CREATION_SETTINGS);
        creationZoneController.setAction(CreationAction.ADD_POLYGON);
    }

    public void onPolygonCreationButtonReleased(Button button) {
        onButtonReleased(button);
        optionsWindowController.selectSettingsType(SettingsType.NONE);
        creationZoneController.setAction(CreationAction.NONE);
    }

    public void onAddPointButtonButtonClicked(Button button) {
        onButtonClicked(button);
        creationZoneController.setAction(CreationAction.ADD_POINT);
        optionsWindowController.selectSettingsType(SettingsType.INSERT_POINT_SETTINGS);
    }

    public void onAddPointButtonReleased(Button button) {
        onButtonReleased(button);
        creationZoneController.setAction(CreationAction.NONE);
        optionsWindowController.selectSettingsType(SettingsType.NONE);
    }

    public void onRemovePointButtonClicked(Button button) {
        onButtonClicked(button);
        creationZoneController.setAction(CreationAction.REMOVE_POINT);
        optionsWindowController.selectSettingsType(SettingsType.REMOVE_POINT_SETTINGS);
    }

    public void onRemovePointButtonReleased(Button button) {
        onButtonReleased(button);
        creationZoneController.setAction(CreationAction.NONE);
        optionsWindowController.selectSettingsType(SettingsType.NONE);
    }

    public void onMovePointButtonClicked(Button button) {
        onButtonClicked(button);
        creationZoneController.setAction(CreationAction.MOVE_POINT);
        optionsWindowController.selectSettingsType(SettingsType.MOVE_POINT_SETTINGS);
    }

    public void onMovePointButtonReleased(Button button) {
        onButtonReleased(button);
        creationZoneController.setAction(CreationAction.NONE);
        optionsWindowController.selectSettingsType(SettingsType.NONE);
    }

    public void onMirrorButtonClicked(Button button) {
        onButtonClicked(button);
        creationZoneController.setAction(CreationAction.MIRROR);
        optionsWindowController.selectSettingsType(SettingsType.MIRROR_SETTINGS);
    }

    public void onMirrorButtonReleased(Button button) {
        onButtonReleased(button);
        creationZoneController.setAction(CreationAction.NONE);
        optionsWindowController.selectSettingsType(SettingsType.NONE);
    }

    public void onRotateButtonClicked(Button<DrawButtonBoardController> button) {
        onButtonClicked(button);
        creationZoneController.setAction(CreationAction.ROTATE);
        optionsWindowController.selectSettingsType(SettingsType.ROTATE_SETTINGS);
    }

    public void onRotateButtonReleased(Button button) {
        onButtonReleased(button);
        creationZoneController.setAction(CreationAction.NONE);
        optionsWindowController.selectSettingsType(SettingsType.NONE);
    }

    public void onShiftButtonClicked(Button<DrawButtonBoardController> button) {
        onButtonClicked(button);
        creationZoneController.setAction(CreationAction.SHIFT);
        optionsWindowController.selectSettingsType(SettingsType.SHIFT_SETTINGS);
    }

    public void onShiftButtonReleased(Button button) {
        onButtonReleased(button);
        creationZoneController.setAction(CreationAction.NONE);
        optionsWindowController.selectSettingsType(SettingsType.NONE);
    }
}
