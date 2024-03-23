package com.evolgames.userinterface.control.buttonboardcontrollers;

import com.evolgames.userinterface.control.CreationAction;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.SettingsType;
import com.evolgames.userinterface.view.EditorUserInterface;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.layouts.ButtonBoard;

public class ImageButtonBoardController extends ButtonBoardController {

    private final EditorUserInterface editorUserInterface;

    public ImageButtonBoardController(
            ButtonBoard buttonBoard, EditorUserInterface editorUserInterface) {
        super(buttonBoard);
        this.editorUserInterface = editorUserInterface;
    }

    public void onMoveImageButtonClicked(Button button) {
        onButtonClicked(button);
        editorUserInterface
                .getCreationZoneController()
                .setAction(CreationAction.MOVE_IMAGE);
        editorUserInterface.updateOptionsWindow(SettingsType.NONE);
    }

    public void onMoveImageButtonReleased(Button button) {
        onImageButtonReleased(button);
    }

    public void onScaleImageButtonClicked(Button<ImageButtonBoardController> button) {
        onButtonClicked(button);
        editorUserInterface
                .getCreationZoneController()
                .setAction(CreationAction.SCALE_IMAGE);
        editorUserInterface.updateOptionsWindow(SettingsType.SCALE_IMAGE_SETTINGS);
    }

    public void onScaleImageButtonReleased(Button<ImageButtonBoardController> button) {
        onImageButtonReleased(button);
    }

    public void onRotateImageButtonClicked(Button<ImageButtonBoardController> button) {
        onButtonClicked(button);
        editorUserInterface
                .getCreationZoneController()
                .setAction(CreationAction.ROTATE_IMAGE);
        editorUserInterface.updateOptionsWindow(SettingsType.NONE);
    }

    public void onRotateImageButtonReleased(Button<ImageButtonBoardController> button) {
        onImageButtonReleased(button);
    }

    public void onPipeButtonClicked(Button<ImageButtonBoardController> button) {
        onButtonClicked(button);
        editorUserInterface
                .getCreationZoneController()
                .setAction(CreationAction.PIPING);
        editorUserInterface.updateOptionsWindow(SettingsType.PIPE_SETTINGS);
    }

    public void onPipeButtonReleased(Button<ImageButtonBoardController> button) {
        onImageButtonReleased(button);
    }

    private void onImageButtonReleased(Button<ImageButtonBoardController> button) {
        onButtonReleased(button);
        editorUserInterface
                .getCreationZoneController()
                .setAction(CreationAction.NONE);
        editorUserInterface.updateOptionsWindow(SettingsType.IMAGE_SETTINGS);
    }
}
