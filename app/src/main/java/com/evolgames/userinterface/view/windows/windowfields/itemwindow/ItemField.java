package com.evolgames.userinterface.view.windows.windowfields.itemwindow;

import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ItemWindowController;
import com.evolgames.userinterface.sections.basic.SecondaryButtonField;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.windows.WindowPartIdentifier;

public class ItemField extends SecondaryButtonField {
    public ItemField(int primaryKey, int secondaryKey, ItemWindowController controller) {
        super(primaryKey, secondaryKey, controller);
        setWindowPartIdentifier(WindowPartIdentifier.WINDOW_BODY);
    }

}
