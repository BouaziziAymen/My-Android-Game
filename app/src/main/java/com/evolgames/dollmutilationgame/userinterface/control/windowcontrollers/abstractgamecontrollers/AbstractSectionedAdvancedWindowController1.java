package com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.abstractgamecontrollers;

import com.evolgames.dollmutilationgame.userinterface.view.basics.Element;
import com.evolgames.dollmutilationgame.userinterface.view.windows.AbstractLinearLayoutAdvancedWindow;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.LinearLayoutAdvancedWindowController;

public abstract class AbstractSectionedAdvancedWindowController1<
        W extends AbstractLinearLayoutAdvancedWindow<?>, Primary extends Element>
        extends LinearLayoutAdvancedWindowController<W> {
    public abstract void onPrimaryButtonClicked(Primary primary);

    public abstract void onPrimaryButtonReleased(Primary primary);

    public abstract void onPrimaryAdded(Primary primaryField);
}
