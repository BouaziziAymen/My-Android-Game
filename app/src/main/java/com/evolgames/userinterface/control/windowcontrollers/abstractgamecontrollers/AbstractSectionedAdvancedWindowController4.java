package com.evolgames.userinterface.control.windowcontrollers.abstractgamecontrollers;

import com.evolgames.userinterface.view.basics.Element;
import com.evolgames.userinterface.view.windows.AbstractLinearLayoutAdvancedWindow;

public abstract class AbstractSectionedAdvancedWindowController4<
        W extends AbstractLinearLayoutAdvancedWindow<?>,
        Primary extends Element,
        Secondary extends Element,
        Tertiary extends Element,
        Quaternary extends Element>
    extends AbstractSectionedAdvancedWindowController3<W, Primary, Secondary, Tertiary> {

  public abstract void onQuaternaryButtonClicked(Quaternary tertiary);

  public abstract void onQuaternaryButtonReleased(Quaternary tertiary);

  public abstract void onQuaternaryAdded(Quaternary tertiary);
}
