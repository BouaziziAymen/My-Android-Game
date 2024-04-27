package com.evolgames.dollmutilationgame.userinterface.view.windows.gamewindows;


import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.DecorationSettingsWindowController;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.SimpleTitleField;
import com.evolgames.gameengine.R;

public class DecorationSettingsWindow extends SettingsWindow {

    public DecorationSettingsWindow(
            float pX, float pY, DecorationSettingsWindowController controller) {
        super(pX, pY, 8, 9, controller);
        SimpleTitleField titleField = new SimpleTitleField(ResourceManager.getInstance().getString(R.string.decoration_settings_title));
        titleField.setPadding(5);
        addPrimary(titleField);
        controller.init();

        createPanel(controller);
    }
}
