package com.evolgames.dollmutilationgame.userinterface.view.windows.gamewindows;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.LiquidSourceOptionController;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.SimpleTitleField;
import com.evolgames.gameengine.R;

public class LiquidSourceOptionWindow extends SettingsWindow {

    public LiquidSourceOptionWindow(float pX, float pY, LiquidSourceOptionController controller) {
        super(pX, pY, 6, 8, controller);
        SimpleTitleField titleField = new SimpleTitleField(ResourceManager.getInstance().getString(R.string.liquid_source_settings_title));
        titleField.setPadding(5);
        addPrimary(titleField);
        controller.init();

        createPanel(controller);
    }
}
