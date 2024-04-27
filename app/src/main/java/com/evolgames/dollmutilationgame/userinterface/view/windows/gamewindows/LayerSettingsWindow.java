package com.evolgames.dollmutilationgame.userinterface.view.windows.gamewindows;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.LayerSettingsWindowController;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.SimpleTitleField;
import com.evolgames.gameengine.R;

public class LayerSettingsWindow extends SettingsWindow {

    public LayerSettingsWindow(float pX, float pY, LayerSettingsWindowController controller) {
        super(pX, pY, 10, 9, controller);
        SimpleTitleField titleField = new SimpleTitleField(ResourceManager.getInstance().getString(R.string.layer_settings_title));
        titleField.setPadding(5);
        addPrimary(titleField);
        controller.init();

        createPanel(controller);
    }

}
