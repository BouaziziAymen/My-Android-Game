package com.evolgames.userinterface.control.buttonboardcontrollers;

import com.evolgames.userinterface.control.Controller;
import com.evolgames.userinterface.control.CreationZoneController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.SettingsType;
import com.evolgames.userinterface.view.UserInterface;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.layouts.ButtonBoard;
import com.evolgames.userinterface.view.shapes.CreationZone;

public class ImageButtonBoardController extends ButtonBoardController {


    private final UserInterface userInterface;

    public ImageButtonBoardController(ButtonBoard buttonBoard, UserInterface userInterface) {
        super(buttonBoard);
        this.userInterface = userInterface;
    }
    public void disableButtons(){
        if(buttonBoard.getSize()<3)return;
        buttonBoard.getButtonAtIndex(0).updateState(Button.State.DISABLED);
        buttonBoard.getButtonAtIndex(1).updateState(Button.State.DISABLED);
        buttonBoard.getButtonAtIndex(2).updateState(Button.State.DISABLED);
        buttonBoard.getButtonAtIndex(3).updateState(Button.State.DISABLED);
    }
    public void enableButtons(){
        if(buttonBoard.getSize()<3)return;
        buttonBoard.getButtonAtIndex(0).updateState(Button.State.NORMAL);
        buttonBoard.getButtonAtIndex(1).updateState(Button.State.NORMAL);
        buttonBoard.getButtonAtIndex(2).updateState(Button.State.NORMAL);
        buttonBoard.getButtonAtIndex(3).updateState(Button.State.NORMAL);
    }
    public void onMoveImageButtonClicked(Button button){
        onButtonClicked(button);
        userInterface.getCreationZoneController().setAction(CreationZoneController.CreationAction.MOVE_IMAGE);
        userInterface.updateOptionsWindow(SettingsType.NONE);
    }

    public void onMoveImageButtonReleased(Button button){
        onImageButtonReleased(button);

    }
    public void onScaleImageButtonClicked(Button<ImageButtonBoardController> button) {
        onButtonClicked(button);
        userInterface.getCreationZoneController().setAction(CreationZoneController.CreationAction.SCALE_IMAGE);
        userInterface.updateOptionsWindow(SettingsType.NONE);
    }

    public void onScaleImageButtonReleased(Button<ImageButtonBoardController> button) {
        onImageButtonReleased(button);
    }

    public void onRotateImageButtonClicked(Button<ImageButtonBoardController> button) {
        onButtonClicked(button);
        userInterface.getCreationZoneController().setAction(CreationZoneController.CreationAction.ROTATE_IMAGE);
        userInterface.updateOptionsWindow(SettingsType.NONE);
    }

    public void onRotateImageButtonReleased(Button<ImageButtonBoardController> button) {
        onImageButtonReleased(button);
    }

    public void onPipeButtonClicked(Button<ImageButtonBoardController> button) {
        onButtonClicked(button);
        userInterface.getCreationZoneController().setAction(CreationZoneController.CreationAction.PIPING);
        userInterface.updateOptionsWindow(SettingsType.PIPE_SETTINGS);

    }

    public void onPipeButtonReleased(Button<ImageButtonBoardController> button) {
        onImageButtonReleased(button);
    }
    private void onImageButtonReleased(Button<ImageButtonBoardController> button){
        onButtonReleased(button);
        userInterface.getCreationZoneController().setAction(CreationZoneController.CreationAction.NONE);
        userInterface.updateOptionsWindow(SettingsType.IMAGE_SETTINGS);
    }

}
