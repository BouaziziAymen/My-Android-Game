package com.evolgames.userinterface.view.windows.gamewindows;

import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.LayerSettingsWindowController;
import com.evolgames.userinterface.view.windows.windowfields.SimpleTitleField;

public class LayerSettingsWindow extends SettingsWindow {

    public LayerSettingsWindow(float pX, float pY, LayerSettingsWindowController controller) {
        super(pX, pY, 10, 8, controller);
        SimpleTitleField titleField = new SimpleTitleField("Layer Settings:");
        titleField.setPadding(5);
        addPrimary(titleField);
        controller.init();

        createPanel(controller);
    }

}
