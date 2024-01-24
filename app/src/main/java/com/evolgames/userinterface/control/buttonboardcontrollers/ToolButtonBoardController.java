package com.evolgames.userinterface.control.buttonboardcontrollers;

import com.evolgames.userinterface.control.CreationZoneController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.OptionsWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.SettingsType;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.layouts.ButtonBoard;

public class ToolButtonBoardController extends ButtonBoardController {
  private final CreationZoneController creationZoneController;
  private final OptionsWindowController optionsWindowController;

  public ToolButtonBoardController(
      ButtonBoard buttonBoard,
      CreationZoneController creationZoneController,
      OptionsWindowController optionsWindowController) {
    super(buttonBoard);
    this.creationZoneController = creationZoneController;
    this.optionsWindowController = optionsWindowController;
  }

  public void onTargetButtonClicked(Button<ToolButtonBoardController> button) {
    onButtonClicked(button);
    creationZoneController.setAction(CreationZoneController.CreationAction.PROJECTILE);
    optionsWindowController.selectSettingsType(SettingsType.PROJECTILE_SETTINGS);
  }

  public void onTargetButtonReleased(Button<ToolButtonBoardController> button) {
    onButtonReleased(button);
    creationZoneController.setAction(CreationZoneController.CreationAction.NONE);
    optionsWindowController.selectSettingsType(SettingsType.NONE);
  }

  public void onMovePointButtonClicked(Button<ToolButtonBoardController> button) {
    onButtonClicked(button);
    creationZoneController.setAction(CreationZoneController.CreationAction.MOVE_TOOL_POINT);
    optionsWindowController.selectSettingsType(SettingsType.MOVE_TOOL_POINT_SETTING);
  }

  public void onMovePointButtonReleased(Button<ToolButtonBoardController> button) {
    onButtonReleased(button);
    creationZoneController.setAction(CreationZoneController.CreationAction.NONE);
    optionsWindowController.selectSettingsType(SettingsType.NONE);
  }

  public void onAmmoButtonClicked(Button<ToolButtonBoardController> button) {
    onButtonClicked(button);
    creationZoneController.setAction(CreationZoneController.CreationAction.AMMO);
    optionsWindowController.selectSettingsType(SettingsType.AMMO_TOOL_POINT_SETTING);
  }

  public void onAmmoButtonReleased(Button<ToolButtonBoardController> button) {
    onButtonReleased(button);
    creationZoneController.setAction(CreationZoneController.CreationAction.NONE);
    optionsWindowController.selectSettingsType(SettingsType.NONE);
  }

  public void onBombButtonClicked(Button<ToolButtonBoardController> button) {
    onButtonClicked(button);
    creationZoneController.setAction(CreationZoneController.CreationAction.BOMB);
    optionsWindowController.selectSettingsType(SettingsType.BOMB_TOOL_POINT_SETTING);
  }

  public void onBombButtonReleased(Button<ToolButtonBoardController> button) {
    onButtonReleased(button);
    creationZoneController.setAction(CreationZoneController.CreationAction.NONE);
    optionsWindowController.selectSettingsType(SettingsType.NONE);
  }

  public void onDragButtonClicked(Button<ToolButtonBoardController> button) {
    onButtonClicked(button);
    creationZoneController.setAction(CreationZoneController.CreationAction.DRAG);
    // optionsWindowController.selectSettingsType(SettingsType.BOMB_TOOL_POINT_SETTING);
  }

  public void onDragButtonReleased(Button<ToolButtonBoardController> button) {
    onButtonReleased(button);
    creationZoneController.setAction(CreationZoneController.CreationAction.NONE);
    optionsWindowController.selectSettingsType(SettingsType.NONE);
  }

  public void onFireSourceButtonClicked(Button<ToolButtonBoardController> button) {
    onButtonClicked(button);
    creationZoneController.setAction(CreationZoneController.CreationAction.FIRE_SOURCE);
    // optionsWindowController.selectSettingsType(SettingsType.BOMB_TOOL_POINT_SETTING);
  }

  public void onFireSourceButtonReleased(Button<ToolButtonBoardController> button) {
    onButtonReleased(button);
    creationZoneController.setAction(CreationZoneController.CreationAction.NONE);
    optionsWindowController.selectSettingsType(SettingsType.NONE);
  }

  public void onLiquidButtonClicked(Button<ToolButtonBoardController> button) {
    onButtonClicked(button);
    creationZoneController.setAction(CreationZoneController.CreationAction.LIQUID_SOURCE);
  }

  public void onLiquidButtonReleased(Button<ToolButtonBoardController> button) {
    onButtonReleased(button);
    creationZoneController.setAction(CreationZoneController.CreationAction.NONE);
    optionsWindowController.selectSettingsType(SettingsType.NONE);
  }
}
