package com.evolgames.userinterface.control.windowcontrollers.abstractgamecontrollers;

import com.evolgames.userinterface.view.basics.Element;
import com.evolgames.userinterface.view.windows.AbstractLinearLayoutAdvancedWindow;

public abstract class AbstractSectionedAdvancedWindowController2<
        W extends AbstractLinearLayoutAdvancedWindow<?>,
        Primary extends Element,
        Secondary extends Element>
        extends AbstractSectionedAdvancedWindowController1<W, Primary> {

    public abstract void onSecondaryButtonClicked(Secondary secondary);

    public abstract void onSecondaryButtonReleased(Secondary secondary);

    public abstract void onSecondaryAdded(Secondary secondary);
}
