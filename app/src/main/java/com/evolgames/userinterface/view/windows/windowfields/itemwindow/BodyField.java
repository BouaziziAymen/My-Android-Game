package com.evolgames.userinterface.view.windows.windowfields.itemwindow;

import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ItemWindowController;
import com.evolgames.userinterface.sections.basic.PrimaryButtonField;
import com.evolgames.userinterface.sections.basic.PrimaryLinearLayout;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.ButtonWithText;
import com.evolgames.userinterface.view.windows.WindowPartIdentifier;

public class BodyField extends PrimaryButtonField {

    public BodyField(int bodyFieldKey, ItemWindowController controller) {
        super(bodyFieldKey, controller);
        setWindowPartIdentifier(WindowPartIdentifier.WINDOW_BODY);

        setPadding(5f);
    }

}
