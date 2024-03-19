package com.evolgames.userinterface.control.buttonboardcontrollers;

import com.evolgames.scenes.entities.SceneType;
import com.evolgames.userinterface.control.CreationZoneController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.SettingsType;
import com.evolgames.userinterface.view.EditorUserInterface;
import com.evolgames.userinterface.view.Screen;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.layouts.ButtonBoard;

public class MainButtonBoardController extends ButtonBoardController {
    private final EditorUserInterface editorUserInterface;

    public MainButtonBoardController(
            ButtonBoard buttonBoard, EditorUserInterface editorUserInterface) {
        super(buttonBoard);
        this.editorUserInterface = editorUserInterface;
    }

    public void onDrawOptionClicked(Button<MainButtonBoardController> button) {
        onButtonClicked(button);

        editorUserInterface.getDrawButtonBoardController().openBoard();
        editorUserInterface.getImageButtonBoardController().closeBoard();
        editorUserInterface.getJointButtonBoardController().closeBoard();
        editorUserInterface.getItemButtonBoardController().closeBoard();

        editorUserInterface.getLayersWindowController().openWindow();
        editorUserInterface.getJointsWindowController().closeWindow();
        editorUserInterface.getItemWindowController().closeWindow();
        editorUserInterface.getItemSaveWindowController().closeWindow();
        editorUserInterface.getJointSettingsWindowController().closeWindow();

        editorUserInterface.changeSelectedScreen(Screen.DRAW_SCREEN);
    }

    public void onDrawOptionReleased(Button<MainButtonBoardController> button) {
        onButtonReleased(button);

        editorUserInterface.getDrawButtonBoardController().closeBoard();
        editorUserInterface.getLayersWindowController().closeWindow();

        editorUserInterface.getCreationZoneController().setAction(CreationZoneController.CreationAction.NONE);
        editorUserInterface.changeSelectedScreen(Screen.NONE);
    }

    public void onImageOptionClicked(Button<MainButtonBoardController> button) {
        onButtonClicked(button);

        editorUserInterface.getDrawButtonBoardController().closeBoard();
        editorUserInterface.getImageButtonBoardController().openBoard();
        editorUserInterface.getJointButtonBoardController().closeBoard();
        editorUserInterface.getItemButtonBoardController().closeBoard();

        editorUserInterface.getLayersWindowController().closeWindow();
        editorUserInterface.getJointsWindowController().closeWindow();
        editorUserInterface.getItemWindowController().closeWindow();
        editorUserInterface.getItemSaveWindowController().closeWindow();
        editorUserInterface.getJointSettingsWindowController().closeWindow();

        editorUserInterface.updateOptionsWindow(SettingsType.IMAGE_SETTINGS);
        editorUserInterface.changeSelectedScreen(Screen.IMAGE_SCREEN);
    }

    public void onImageOptionReleased(Button<MainButtonBoardController> button) {
        onButtonReleased(button);

        editorUserInterface.getCreationZoneController().setAction(CreationZoneController.CreationAction.NONE);
        editorUserInterface.getImageButtonBoardController().closeBoard();
        editorUserInterface.changeSelectedScreen(Screen.NONE);
    }

    public void onJointOptionClicked(Button<MainButtonBoardController> button) {
        onButtonClicked(button);

        editorUserInterface.getDrawButtonBoardController().closeBoard();
        editorUserInterface.getImageButtonBoardController().closeBoard();
        editorUserInterface.getJointButtonBoardController().openBoard();
        editorUserInterface.getItemButtonBoardController().closeBoard();

        editorUserInterface.getLayersWindowController().closeWindow();
        editorUserInterface.getJointsWindowController().openWindow();
        editorUserInterface.getItemWindowController().closeWindow();
        editorUserInterface.getItemSaveWindowController().closeWindow();
        editorUserInterface.getJointSettingsWindowController().closeWindow();

        editorUserInterface.changeSelectedScreen(Screen.JOINTS_SCREEN);
    }

    public void onJointOptionReleased(Button<MainButtonBoardController> button) {
        onButtonReleased(button);

        editorUserInterface.getJointButtonBoardController().closeBoard();

        editorUserInterface.getCreationZoneController().setAction(CreationZoneController.CreationAction.NONE);
        editorUserInterface.getJointsWindowController().closeWindow();
        editorUserInterface.changeSelectedScreen(Screen.NONE);
    }

    public void onToolOptionClicked(Button<MainButtonBoardController> button) {
        onButtonClicked(button);
        editorUserInterface.getDrawButtonBoardController().closeBoard();
        editorUserInterface.getImageButtonBoardController().closeBoard();
        editorUserInterface.getJointButtonBoardController().closeBoard();
        editorUserInterface.getItemButtonBoardController().openBoard();

        editorUserInterface.getLayersWindowController().closeWindow();
        editorUserInterface.getJointsWindowController().closeWindow();
        editorUserInterface.getItemWindowController().openWindow();
        editorUserInterface.getItemSaveWindowController().closeWindow();
        editorUserInterface.getJointSettingsWindowController().closeWindow();
        editorUserInterface.changeSelectedScreen(Screen.ITEMS_SCREEN);
    }

    public void onToolOptionReleased(Button<MainButtonBoardController> button) {
        onButtonReleased(button);

        editorUserInterface.getCreationZoneController().setAction(CreationZoneController.CreationAction.NONE);
        editorUserInterface.getItemButtonBoardController().closeBoard();
        editorUserInterface.changeSelectedScreen(Screen.NONE);
    }

    public void onSaveOptionClicked(Button<MainButtonBoardController> button) {
        onButtonClicked(button);
        editorUserInterface.updateOptionsWindow(SettingsType.TOOL_SAVE_SETTINGS);
        editorUserInterface.getDrawButtonBoardController().closeBoard();
        editorUserInterface.getImageButtonBoardController().closeBoard();
        editorUserInterface.getJointButtonBoardController().closeBoard();
        editorUserInterface.getItemButtonBoardController().closeBoard();

        editorUserInterface.getLayersWindowController().closeWindow();
        editorUserInterface.getJointsWindowController().closeWindow();
        editorUserInterface.getItemWindowController().closeWindow();
        editorUserInterface.getItemSaveWindowController().openWindow();
        editorUserInterface.getJointSettingsWindowController().closeWindow();

        editorUserInterface.changeSelectedScreen(Screen.SAVE_SCREEN);
    }

    public void onSaveOptionReleased(Button<MainButtonBoardController> button) {
        onButtonReleased(button);
        editorUserInterface.getCreationZoneController().setAction(CreationZoneController.CreationAction.NONE);
        editorUserInterface.changeSelectedScreen(Screen.NONE);
    }

    public void onHomeButtonClicked(Button<MainButtonBoardController> button) {
        onButtonClicked(button);
        editorUserInterface.updateOptionsWindow(SettingsType.NONE);
        editorUserInterface.getDrawButtonBoardController().closeBoard();
        editorUserInterface.getImageButtonBoardController().closeBoard();
        editorUserInterface.getJointButtonBoardController().closeBoard();
        editorUserInterface.getItemButtonBoardController().closeBoard();

        editorUserInterface.getLayersWindowController().closeWindow();
        editorUserInterface.getJointsWindowController().closeWindow();
        editorUserInterface.getItemWindowController().closeWindow();
        editorUserInterface.getItemSaveWindowController().closeWindow();
        editorUserInterface.getJointSettingsWindowController().closeWindow();

        editorUserInterface.changeSelectedScreen(Screen.NONE);
    }

    public void onHomeButtonReleased(Button<MainButtonBoardController> button) {
        onButtonReleased(button);
        editorUserInterface.doWithConfirm("Are you sure you want to quit?",()->{
            editorUserInterface.getScene().goToScene(SceneType.MENU);
        });
    }
}
