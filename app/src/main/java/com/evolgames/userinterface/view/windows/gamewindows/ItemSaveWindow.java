package com.evolgames.userinterface.view.windows.gamewindows;


import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ItemSaveWindowController;
import com.evolgames.userinterface.view.windows.windowfields.SimpleTitleField;

public class ItemSaveWindow extends SettingsWindow {


    public ItemSaveWindow(float pX, float pY, ItemSaveWindowController controller) {
        super(pX, pY, 6, 8, controller);
        SimpleTitleField titleField = new SimpleTitleField("Saving Settings:");
        titleField.setPadding(5);
        addPrimary(titleField);
        controller.init();

        createPanel(controller);
        setVisible(false);
    }
}
