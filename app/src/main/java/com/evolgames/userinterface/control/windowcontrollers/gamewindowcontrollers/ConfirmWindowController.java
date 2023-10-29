package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;

import com.evolgames.userinterface.control.behaviors.actions.ConfirmableAction;
import com.evolgames.userinterface.control.windowcontrollers.LinearLayoutAdvancedWindowController;
import com.evolgames.userinterface.view.UserInterface;
import com.evolgames.userinterface.view.windows.gamewindows.ConfirmWindow;

public class ConfirmWindowController  extends LinearLayoutAdvancedWindowController<ConfirmWindow> {

    private final UserInterface userInterface;
    ConfirmableAction confirmableAction;
    public ConfirmWindowController(UserInterface userInterface){
        this.userInterface = userInterface;
    }
    @Override
    public void init() {
       this.closeWindow();
    }
    public void bindAction(ConfirmableAction confirmableAction){
        this.confirmableAction = confirmableAction;
        window.updateText(this.confirmableAction.getPrompt());
        this.openWindow();
        this.userInterface.shade(window);
    }

    @Override
    public void closeWindow() {
        this.userInterface.undoShade();
        super.closeWindow();
    }

    public void onCancel() {
       confirmableAction.onCancel();
       this.closeWindow();
    }

    public void onConfirm() {
        confirmableAction.onConfirm();
        this.closeWindow();
    }
}
