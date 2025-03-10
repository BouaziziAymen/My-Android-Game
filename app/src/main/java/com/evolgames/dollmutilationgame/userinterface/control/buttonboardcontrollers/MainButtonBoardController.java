package com.evolgames.dollmutilationgame.userinterface.control.buttonboardcontrollers;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.scenes.MainScene;
import com.evolgames.dollmutilationgame.userinterface.control.CreationAction;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.SettingsType;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.Button;
import com.evolgames.dollmutilationgame.userinterface.view.layouts.ButtonBoard;
import com.evolgames.dollmutilationgame.R;
import com.evolgames.dollmutilationgame.scenes.entities.SceneType;
import com.evolgames.dollmutilationgame.userinterface.view.EditorUserInterface;
import com.evolgames.dollmutilationgame.userinterface.view.Screen;

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

        editorUserInterface.getCreationZoneController().setAction(CreationAction.NONE);
        editorUserInterface.getOptionsWindowController().selectSettingsType(SettingsType.NONE);
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

        editorUserInterface.getCreationZoneController().setAction(CreationAction.NONE);
        editorUserInterface.getImageButtonBoardController().closeBoard();
        editorUserInterface.getOptionsWindowController().selectSettingsType(SettingsType.NONE);
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

        editorUserInterface.getCreationZoneController().setAction(CreationAction.NONE);
        editorUserInterface.getJointsWindowController().closeWindow();
        editorUserInterface.getOptionsWindowController().selectSettingsType(SettingsType.NONE);
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

        editorUserInterface.getCreationZoneController().setAction(CreationAction.NONE);
        editorUserInterface.getItemButtonBoardController().closeBoard();
        editorUserInterface.getOptionsWindowController().selectSettingsType(SettingsType.NONE);
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
        editorUserInterface.getCreationZoneController().setAction(CreationAction.NONE);
        editorUserInterface.getOptionsWindowController().selectSettingsType(SettingsType.NONE);
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
        editorUserInterface.doWithConfirm(ResourceManager.getInstance().getString(R.string.quit_confirmation_message), () -> {
            ResourceManager.getInstance().activity.getUiController().goToScene(SceneType.MENU);
        });
    }

    public void onHelpButtonClicked(Button<MainButtonBoardController> button) {
        ResourceManager.getInstance().activity.runOnUiThread(()->{
            ResourceManager.getInstance().activity.showEditorHelpDialog();
        });
    }

    public void onHelpButtonReleased(Button<MainButtonBoardController> button) {

    }
}
