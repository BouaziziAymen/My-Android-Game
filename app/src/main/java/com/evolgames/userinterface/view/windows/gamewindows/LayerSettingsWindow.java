package com.evolgames.userinterface.view.windows.gamewindows;

import com.evolgames.activity.ResourceManager;
import com.evolgames.gameengine.R;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.LayerSettingsWindowController;
import com.evolgames.userinterface.view.windows.windowfields.SimpleTitleField;

public class LayerSettingsWindow extends SettingsWindow {

    public LayerSettingsWindow(float pX, float pY, LayerSettingsWindowController controller) {
        super(pX, pY, 10, 8, controller);
        SimpleTitleField titleField = new SimpleTitleField(ResourceManager.getInstance().getString(R.string.layer_settings_title));
        titleField.setPadding(5);
        addPrimary(titleField);
        controller.init();

        createPanel(controller);
    }

}
