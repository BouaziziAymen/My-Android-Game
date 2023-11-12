package com.evolgames.userinterface.control.windowcontrollers.abstractgamecontrollers;

import com.evolgames.userinterface.control.windowcontrollers.LinearLayoutAdvancedWindowController;
import com.evolgames.userinterface.view.basics.Element;
import com.evolgames.userinterface.view.windows.AbstractLinearLayoutAdvancedWindow;

public abstract class AbstractSectionedAdvancedWindowController1<
        W extends AbstractLinearLayoutAdvancedWindow<?>, Primary extends Element>
    extends LinearLayoutAdvancedWindowController<W> {
  public abstract void onPrimaryButtonClicked(Primary primary);

  public abstract void onPrimaryButtonReleased(Primary primary);

  public abstract void onPrimaryAdded(Primary primaryField);
}
