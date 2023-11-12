package com.evolgames.userinterface.control.buttonboardcontrollers;


import com.evolgames.userinterface.control.CreationZoneController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.OptionsWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.SettingsType;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.layouts.ButtonBoard;

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
    creationZoneController.setAction(CreationZoneController.CreationAction.REVOLUTE);
    optionsWindowController.selectSettingsType(SettingsType.REVOLUTE_JOINT_SETTINGS);
  }

  public void onRevoluteButtonReleased(Button button) {
    onButtonReleased(button);
    creationZoneController.setAction(CreationZoneController.CreationAction.NONE);
    optionsWindowController.selectSettingsType(SettingsType.NONE);
  }

  public void onPrismaticButtonClicked(Button button) {
    onButtonClicked(button);
    creationZoneController.setAction(CreationZoneController.CreationAction.PRISMATIC);
    optionsWindowController.selectSettingsType(SettingsType.PRISMATIC_JOINT_SETTINGS);
  }

  public void onPrismaticButtonReleased(Button button) {
    onButtonReleased(button);
    creationZoneController.setAction(CreationZoneController.CreationAction.NONE);
    optionsWindowController.selectSettingsType(SettingsType.NONE);
  }

  public void onDistanceButtonClicked(Button button) {
    onButtonClicked(button);
    creationZoneController.setAction(CreationZoneController.CreationAction.DISTANCE);
    optionsWindowController.selectSettingsType(SettingsType.DISTANCE_JOINT_SETTINGS);
  }

  public void onDistanceButtonReleased(Button button) {
    onButtonReleased(button);
    creationZoneController.setAction(CreationZoneController.CreationAction.NONE);
    optionsWindowController.selectSettingsType(SettingsType.NONE);
  }

  public void onWeldButtonClicked(Button button) {
    onButtonClicked(button);
    creationZoneController.setAction(CreationZoneController.CreationAction.WELD);
    optionsWindowController.selectSettingsType(SettingsType.WELD_JOINT_SETTINGS);
  }

  public void onWeldButtonReleased(Button button) {
    onButtonReleased(button);
    creationZoneController.setAction(CreationZoneController.CreationAction.NONE);
    optionsWindowController.selectSettingsType(SettingsType.NONE);
  }

  public void onMoveButtonClicked(Button button) {
    onButtonClicked(button);
    creationZoneController.setAction(CreationZoneController.CreationAction.MOVE_JOINT_POINT);
    optionsWindowController.selectSettingsType(SettingsType.MOVE_JOINT_POINT_SETTINGS);
  }

  public void onMoveButtonReleased(Button button) {
    onButtonReleased(button);
    creationZoneController.setAction(CreationZoneController.CreationAction.NONE);
    optionsWindowController.selectSettingsType(SettingsType.NONE);
  }
}
