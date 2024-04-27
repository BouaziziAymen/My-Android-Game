package com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.AdvancedWindowController;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.Quantity;

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
