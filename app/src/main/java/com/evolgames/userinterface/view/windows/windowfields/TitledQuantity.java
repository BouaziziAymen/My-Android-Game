package com.evolgames.userinterface.view.windows.windowfields;

import com.evolgames.activity.ResourceManager;
import com.evolgames.userinterface.control.windowcontrollers.AdvancedWindowController;
import com.evolgames.userinterface.view.inputs.Quantity;

public class TitledQuantity<C extends AdvancedWindowController<?>>
        extends TitledField<Quantity<C>> {

    public TitledQuantity(String titleString, int length, String key, float margin, boolean flag) {
        super(titleString, new Quantity<>(length, key), margin, ResourceManager.getInstance().getFontWidth(2,titleString));
    }

    public TitledQuantity(String titleString, int length, String key, float margin) {
        super(titleString, new Quantity<>(length, key), margin);
    }

    public void setRatio(float ratio) {
        getAttachment().updateRatio(ratio);
    }
}
