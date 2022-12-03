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
        //make draw button board visible
        userInterface.setDrawButtonBoardVisible(true);
        userInterface.setJointButtonBoardVisible(false);
        userInterface.setImageButtonBoardVisible(false);
        userInterface.setItemButtonBoardVisible(false);
        userInterface.setItemWindowVisible(false);
        userInterface.setSaveWindowVisible(false);
        userInterface.setLayersWindowVisible(true);
        userInterface.setJointsWindowVisible(false);
        userInterface.setSelectedScreen(Screen.DRAW_SCREEN);
    }

    public void onDrawOptionReleased(Button<MainButtonBoardController> button) {
        onButtonReleased(button);
        userInterface.setDrawButtonBoardVisible(false);
        userInterface.setLayersWindowVisible(false);
        userInterface.setSelectedScreen(Screen.NONE);
    }

    public void onImageOptionClicked(Button<MainButtonBoardController> button) {
        onButtonClicked(button);
        //make draw button board visible
        userInterface.setImageButtonBoardVisible(true);
        userInterface.setDrawButtonBoardVisible(false);
        userInterface.setJointButtonBoardVisible(false);
        userInterface.setLayersWindowVisible(false);
        userInterface.setJointsWindowVisible(false);
        userInterface.setItemButtonBoardVisible(false);
        userInterface.setSaveWindowVisible(false);
        userInterface.setItemWindowVisible(false);
        userInterface.updateOptionsWindow(SettingsType.IMAGE_SETTINGS);
        userInterface.setSelectedScreen(Screen.IMAGE_SCREEN);
    }

    public void onImageOptionReleased(Button<MainButtonBoardController> button) {
        onButtonReleased(button);
        userInterface.setImageButtonBoardVisible(false);
        userInterface.setSelectedScreen(Screen.NONE);
    }


    public void onJointOptionClicked(Button<MainButtonBoardController> button) {
        onButtonClicked(button);
        userInterface.setDrawButtonBoardVisible(false);
        userInterface.setImageButtonBoardVisible(false);
        userInterface.setJointButtonBoardVisible(true);
        userInterface.setLayersWindowVisible(false);
        userInterface.setItemButtonBoardVisible(false);
        userInterface.setItemWindowVisible(false);
        userInterface.setSaveWindowVisible(false);
        userInterface.setJointsWindowVisible(true);
        userInterface.setSelectedScreen(Screen.JOINTS_SCREEN);
    }

    public void onJointOptionReleased(Button<MainButtonBoardController> button) {
        onButtonReleased(button);
        userInterface.setJointButtonBoardVisible(false);
        userInterface.setJointsWindowVisible(false);
        userInterface.setSelectedScreen(Screen.NONE);
    }

    public void onToolOptionClicked(Button<MainButtonBoardController> button) {
        onButtonClicked(button);
        userInterface.setDrawButtonBoardVisible(false);
        userInterface.setImageButtonBoardVisible(false);
        userInterface.setJointButtonBoardVisible(false);
        userInterface.setLayersWindowVisible(false);
        userInterface.setJointsWindowVisible(false);
        userInterface.setItemWindowVisible(true);
        userInterface.setSaveWindowVisible(false);
        userInterface.setItemButtonBoardVisible(true);
        userInterface.setSelectedScreen(Screen.ITEMS_SCREEN);
    }

    public void onToolOptionReleased(Button<MainButtonBoardController> button) {
        onButtonReleased(button);
        userInterface.setItemButtonBoardVisible(false);
        userInterface.setSelectedScreen(Screen.NONE);
    }

    public void onSaveOptionClicked(Button<MainButtonBoardController> button) {
        onButtonClicked(button);
        userInterface.updateOptionsWindow(SettingsType.TOOL_SAVE_SETTINGS);
        userInterface.setDrawButtonBoardVisible(false);
        userInterface.setImageButtonBoardVisible(false);
        userInterface.setJointButtonBoardVisible(false);
        userInterface.setLayersWindowVisible(false);
        userInterface.setJointsWindowVisible(false);
        userInterface.setItemWindowVisible(false);
        userInterface.setItemButtonBoardVisible(false);
        userInterface.setSelectedScreen(Screen.SAVE_SCREEN);
    }

    public void onSaveOptionReleased(Button<MainButtonBoardController> button) {
        onButtonReleased(button);
        userInterface.setSelectedScreen(Screen.NONE);
        // userInterface.setItemButtonBoardVisible(false);
    }
}
