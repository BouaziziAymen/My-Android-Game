package com.evolgames.userinterface.control.buttonboardcontrollers;


import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.SettingsType;
import com.evolgames.userinterface.view.Screen;
import com.evolgames.userinterface.view.UserInterface;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.layouts.ButtonBoard;

public class MainButtonBoardController extends ButtonBoardController {
    private final UserInterface userInterface;

    public MainButtonBoardController(ButtonBoard buttonBoard, UserInterface userInterface) {
        super(buttonBoard);
        this.userInterface = userInterface;
    }


    public void onDrawOptionClicked(Button<MainButtonBoardController> button) {
        onButtonClicked(button);

        userInterface.getDrawButtonBoardController().openBoard();
        userInterface.getImageButtonBoardController().closeBoard();
        userInterface.getJointButtonBoardController().closeBoard();
        userInterface.getItemButtonBoardController().closeBoard();

        userInterface.getLayersWindowController().openWindow();
        userInterface.getJointsWindowController().closeWindow();
        userInterface.getItemWindowController().closeWindow();
        userInterface.getItemSaveWindowController().closeWindow();
        userInterface.getJointSettingsWindowController().closeWindow();

        userInterface.changeSelectedScreen(Screen.DRAW_SCREEN);
    }

    public void onDrawOptionReleased(Button<MainButtonBoardController> button) {
        onButtonReleased(button);

        userInterface.getDrawButtonBoardController().closeBoard();
        userInterface.getLayersWindowController().closeWindow();

        userInterface.changeSelectedScreen(Screen.NONE);
    }

    public void onImageOptionClicked(Button<MainButtonBoardController> button) {
        onButtonClicked(button);

        userInterface.getDrawButtonBoardController().closeBoard();
        userInterface.getImageButtonBoardController().openBoard();
        userInterface.getJointButtonBoardController().closeBoard();
        userInterface.getItemButtonBoardController().closeBoard();

        userInterface.getLayersWindowController().closeWindow();
        userInterface.getJointsWindowController().closeWindow();
        userInterface.getItemWindowController().closeWindow();
        userInterface.getItemSaveWindowController().closeWindow();
        userInterface.getJointSettingsWindowController().closeWindow();

        userInterface.updateOptionsWindow(SettingsType.IMAGE_SETTINGS);
        userInterface.changeSelectedScreen(Screen.IMAGE_SCREEN);
    }

    public void onImageOptionReleased(Button<MainButtonBoardController> button) {
        onButtonReleased(button);

        userInterface.getImageButtonBoardController().closeBoard();
        userInterface.changeSelectedScreen(Screen.NONE);
    }


    public void onJointOptionClicked(Button<MainButtonBoardController> button) {
        onButtonClicked(button);

        userInterface.getDrawButtonBoardController().closeBoard();
        userInterface.getImageButtonBoardController().closeBoard();
        userInterface.getJointButtonBoardController().openBoard();
        userInterface.getItemButtonBoardController().closeBoard();

        userInterface.getLayersWindowController().closeWindow();
        userInterface.getJointsWindowController().openWindow();
        userInterface.getItemWindowController().closeWindow();
        userInterface.getItemSaveWindowController().closeWindow();
        userInterface.getJointSettingsWindowController().closeWindow();

        userInterface.changeSelectedScreen(Screen.JOINTS_SCREEN);
    }

    public void onJointOptionReleased(Button<MainButtonBoardController> button) {
        onButtonReleased(button);

        userInterface.getJointButtonBoardController().closeBoard();

        userInterface.getJointsWindowController().closeWindow();
        userInterface.changeSelectedScreen(Screen.NONE);
    }

    public void onToolOptionClicked(Button<MainButtonBoardController> button) {
        onButtonClicked(button);
        userInterface.getDrawButtonBoardController().closeBoard();
        userInterface.getImageButtonBoardController().closeBoard();
        userInterface.getJointButtonBoardController().closeBoard();
        userInterface.getItemButtonBoardController().openBoard();

        userInterface.getLayersWindowController().closeWindow();
        userInterface.getJointsWindowController().closeWindow();
        userInterface.getItemWindowController().openWindow();
        userInterface.getItemSaveWindowController().closeWindow();
        userInterface.getJointSettingsWindowController().closeWindow();
        userInterface.changeSelectedScreen(Screen.ITEMS_SCREEN);
    }

    public void onToolOptionReleased(Button<MainButtonBoardController> button) {
        onButtonReleased(button);
        userInterface.getItemButtonBoardController().closeBoard();
        userInterface.changeSelectedScreen(Screen.NONE);
    }

    public void onSaveOptionClicked(Button<MainButtonBoardController> button) {
        onButtonClicked(button);
        userInterface.updateOptionsWindow(SettingsType.TOOL_SAVE_SETTINGS);
        userInterface.getDrawButtonBoardController().closeBoard();
        userInterface.getImageButtonBoardController().closeBoard();
        userInterface.getJointButtonBoardController().closeBoard();
        userInterface.getItemButtonBoardController().closeBoard();

        userInterface.getLayersWindowController().closeWindow();
        userInterface.getJointsWindowController().closeWindow();
        userInterface.getItemWindowController().closeWindow();
        userInterface.getItemSaveWindowController().openWindow();
        userInterface.getJointSettingsWindowController().closeWindow();

        userInterface.changeSelectedScreen(Screen.SAVE_SCREEN);
    }

    public void onSaveOptionReleased(Button<MainButtonBoardController> button) {
        onButtonReleased(button);
        userInterface.changeSelectedScreen(Screen.NONE);
        // userInterface.setItemButtonBoardVisible(false);
    }
}
