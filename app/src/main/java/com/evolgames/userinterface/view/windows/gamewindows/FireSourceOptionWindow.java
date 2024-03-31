package com.evolgames.userinterface.view.windows.gamewindows;

import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.FireSourceOptionController;
import com.evolgames.userinterface.view.windows.windowfields.SimpleTitleField;

public class FireSourceOptionWindow extends SettingsWindow {

    public FireSourceOptionWindow(float pX, float pY, FireSourceOptionController controller) {
        super(pX, pY, 6, 8, controller);
        SimpleTitleField titleField = new SimpleTitleField("Fire Source Settings:");
        titleField.setPadding(5);
        addPrimary(titleField);
        controller.init();

        createPanel(controller);
    }

}
