package com.evolgames.userinterface.view.windows.gamewindows;

import com.evolgames.activity.ResourceManager;
import com.evolgames.gameengine.R;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.DragOptionController;
import com.evolgames.userinterface.view.windows.windowfields.SimpleTitleField;

public class DragOptionWindow extends SettingsWindow {

    public DragOptionWindow(float pX, float pY, DragOptionController controller) {
        super(pX, pY, 6, 8, controller);
        SimpleTitleField titleField = new SimpleTitleField(ResourceManager.getInstance().getString(R.string.drag_settings_title));
        titleField.setPadding(5);
        addPrimary(titleField);
        controller.init();

        createPanel(controller);
    }
}
