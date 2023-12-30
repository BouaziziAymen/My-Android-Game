package com.evolgames.userinterface.view.windows;

import com.evolgames.userinterface.control.windowcontrollers.LinearLayoutAdvancedWindowController;
import com.evolgames.userinterface.view.basics.Element;
import com.evolgames.userinterface.view.layouts.ZeroLevelSectionLayout;

public abstract class AbstractZeroLevelSectionedAdvancedWindow<Primary extends Element>
    extends AbstractSectionedAdvancedWindow<ZeroLevelSectionLayout<Primary>> {
  public AbstractZeroLevelSectionedAdvancedWindow(
      float pX,
      float pY,
      int rows,
      int columns,
      boolean hasPadding,
      LinearLayoutAdvancedWindowController<?> controller) {
    super(pX, pY, rows, columns, hasPadding, controller);
  }

  public Primary addPrimary(int primaryKey, boolean isActive) {
    Primary primaryField = createPrimary(primaryKey);
    layout.addPrimary(primaryKey, primaryField, isActive);
    return primaryField;
  }

  public abstract Primary createPrimary(int primaryKey);
}
