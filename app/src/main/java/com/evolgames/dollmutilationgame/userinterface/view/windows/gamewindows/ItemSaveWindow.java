package com.evolgames.dollmutilationgame.userinterface.view.windows.gamewindows;


import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.gamewindowcontrollers.ItemSaveWindowController;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.SimpleTitleField;
import com.evolgames.gameengine.R;

public class ItemSaveWindow extends SettingsWindow {


    public ItemSaveWindow(float pX, float pY, ItemSaveWindowController controller) {
        super(pX, pY, 6, 8, controller);
        SimpleTitleField titleField = new SimpleTitleField(ResourceManager.getInstance().getString(R.string.saving_settings_title));
        titleField.setPadding(5);
        addPrimary(titleField);
        controller.init();

        createPanel(controller);
        setVisible(false);
    }
}
