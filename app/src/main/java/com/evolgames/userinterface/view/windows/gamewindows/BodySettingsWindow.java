package com.evolgames.userinterface.view.windows.gamewindows;

import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.BodySettingsWindowController;
import com.evolgames.userinterface.view.windows.windowfields.SimpleTitleField;

public class BodySettingsWindow extends SettingsWindow {

    public BodySettingsWindow(float pX, float pY, BodySettingsWindowController controller) {
        super(pX, pY, 10, 8, controller);
        SimpleTitleField titleField = new SimpleTitleField("Body Settings:");
        titleField.setPadding(5);
        addPrimary(titleField);
        controller.init();

        createPanel(controller);
    }

}
