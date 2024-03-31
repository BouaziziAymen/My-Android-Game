package com.evolgames.userinterface.view.windows.gamewindows;

import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.LiquidSourceOptionController;
import com.evolgames.userinterface.view.windows.windowfields.SimpleTitleField;

public class LiquidSourceOptionWindow extends SettingsWindow {

    public LiquidSourceOptionWindow(float pX, float pY, LiquidSourceOptionController controller) {
        super(pX, pY, 6, 8, controller);
        SimpleTitleField titleField = new SimpleTitleField("Liquid Source Settings:");
        titleField.setPadding(5);
        addPrimary(titleField);
        controller.init();

        createPanel(controller);
    }
}
