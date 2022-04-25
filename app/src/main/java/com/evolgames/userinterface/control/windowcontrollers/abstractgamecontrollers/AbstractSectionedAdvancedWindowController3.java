package com.evolgames.userinterface.control.windowcontrollers.abstractgamecontrollers;

import com.evolgames.userinterface.view.basics.Element;
import com.evolgames.userinterface.view.windows.AbstractLinearLayoutAdvancedWindow;

public abstract class AbstractSectionedAdvancedWindowController3<W extends AbstractLinearLayoutAdvancedWindow<?>,Primary extends Element,Secondary extends Element,Tertiary extends Element>  extends AbstractSectionedAdvancedWindowController2<W,Primary,Secondary>  {

    public abstract void onTertiaryButtonClicked(Tertiary tertiary);
    public abstract void onTertiaryButtonReleased(Tertiary tertiary);
    public abstract void onTertiaryAdded(Tertiary tertiary);
}
