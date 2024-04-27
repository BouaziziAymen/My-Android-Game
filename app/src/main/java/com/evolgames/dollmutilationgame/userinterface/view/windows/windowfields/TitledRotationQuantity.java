package com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields;

import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.AdvancedWindowController;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.RotationQuantity;

public class TitledRotationQuantity<C extends AdvancedWindowController<?>>
        extends TitledField<RotationQuantity<C>> {

    public TitledRotationQuantity(
            String titleString, int length, String key, float margin, float minX, C controller) {
        super(titleString, new RotationQuantity<>(0, 0, length, key, controller), margin, minX);
    }

    public void setRatio(float ratio) {
        getAttachment().updateRatio(ratio);
    }
}
