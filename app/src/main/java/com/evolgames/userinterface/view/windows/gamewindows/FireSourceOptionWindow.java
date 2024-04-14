package com.evolgames.userinterface.view.windows.gamewindows;

import com.evolgames.activity.ResourceManager;
import com.evolgames.gameengine.R;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.FireSourceOptionController;
import com.evolgames.userinterface.view.windows.windowfields.SimpleTitleField;

public class FireSourceOptionWindow extends SettingsWindow {

    public FireSourceOptionWindow(float pX, float pY, FireSourceOptionController controller) {
        super(pX, pY, 6, 8, controller);
        SimpleTitleField titleField = new SimpleTitleField(ResourceManager.getInstance().getString(R.string.fire_source_settings_title));
        titleField.setPadding(5);
        addPrimary(titleField);
        controller.init();

        createPanel(controller);
    }

}
