package com.evolgames.userinterface.view.windows.gamewindows;


import com.evolgames.activity.ResourceManager;
import com.evolgames.gameengine.R;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.DecorationSettingsWindowController;
import com.evolgames.userinterface.view.windows.windowfields.SimpleTitleField;

public class DecorationSettingsWindow extends SettingsWindow {

    public DecorationSettingsWindow(
            float pX, float pY, DecorationSettingsWindowController controller) {
        super(pX, pY, 8, 8, controller);
        SimpleTitleField titleField = new SimpleTitleField(ResourceManager.getInstance().getString(R.string.decoration_settings_title));
        titleField.setPadding(5);
        addPrimary(titleField);
        controller.init();

        createPanel(controller);
    }
}
