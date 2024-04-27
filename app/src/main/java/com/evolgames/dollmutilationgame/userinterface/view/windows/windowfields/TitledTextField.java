package com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.AdvancedWindowController;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.TextField;

public class TitledTextField<C extends AdvancedWindowController<?>>
        extends TitledField<TextField<C>> {


    public TitledTextField(String titleString, int length, float margin) {
        super(titleString, new TextField<>(length), margin, ResourceManager.getInstance().getFontWidth(2,titleString));
    }
}
