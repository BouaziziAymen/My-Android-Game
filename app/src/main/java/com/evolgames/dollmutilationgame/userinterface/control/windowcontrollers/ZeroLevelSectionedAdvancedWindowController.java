package com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers;

import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.abstractgamecontrollers.AbstractSectionedAdvancedWindowController1;
import com.evolgames.dollmutilationgame.userinterface.view.basics.Element;
import com.evolgames.dollmutilationgame.userinterface.view.layouts.ZeroLevelSectionLayout;
import com.evolgames.dollmutilationgame.userinterface.view.windows.AbstractSectionedAdvancedWindow;

public class ZeroLevelSectionedAdvancedWindowController<
        W extends AbstractSectionedAdvancedWindow<ZeroLevelSectionLayout<Primary>>,
        Primary extends Element>
        extends AbstractSectionedAdvancedWindowController1<W, Primary> {
    @Override
    public void onPrimaryButtonClicked(Primary primary) {
        primary.getSection().setActive(true);
        updateLayout();
    }

    @Override
    public void onPrimaryButtonReleased(Primary primary) {
        primary.getSection().setActive(false);
        updateLayout();
    }

    @Override
    public void onPrimaryAdded(Primary primaryField) {
        updateLayout();
        onPrimaryButtonClicked(primaryField);
    }
}
