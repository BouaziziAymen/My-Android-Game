package com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.abstractgamecontrollers;

import com.evolgames.dollmutilationgame.userinterface.view.basics.Element;
import com.evolgames.dollmutilationgame.userinterface.view.windows.AbstractLinearLayoutAdvancedWindow;

public abstract class AbstractSectionedAdvancedWindowController2<
        W extends AbstractLinearLayoutAdvancedWindow<?>,
        Primary extends Element,
        Secondary extends Element>
        extends AbstractSectionedAdvancedWindowController1<W, Primary> {

    public abstract void onSecondaryButtonClicked(Secondary secondary);

    public abstract void onSecondaryButtonReleased(Secondary secondary);

    public abstract void onSecondaryAdded(Secondary secondary);
}
