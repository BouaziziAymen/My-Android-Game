package com.evolgames.userinterface.view.windows;

import com.evolgames.userinterface.control.windowcontrollers.LinearLayoutAdvancedWindowController;
import com.evolgames.userinterface.view.basics.Element;
import com.evolgames.userinterface.view.layouts.TwoLevelSectionLayout;

public abstract class AbstractTwoLevelSectionedAdvancedWindow<
        Primary extends Element, Secondary extends Element, Tertiary extends Element>
    extends AbstractSectionedAdvancedWindow<TwoLevelSectionLayout<Primary, Secondary, Tertiary>> {
  public AbstractTwoLevelSectionedAdvancedWindow(
      float pX,
      float pY,
      int rows,
      int columns,
      boolean hasPadding,
      LinearLayoutAdvancedWindowController<?> controller) {
    super(pX, pY, rows, columns, hasPadding, controller);
  }

  protected Primary addPrimary(int primaryKey, boolean isActive) {
    Primary primaryField = createPrimary(primaryKey);
    layout.addPrimary(primaryKey, primaryField, isActive);
    getController().onLayoutChanged();
    return primaryField;
  }

  public abstract Primary createPrimary(int primaryKey);

  public abstract Secondary createSecondary(int primaryKey, int secondaryKey);

  public abstract Tertiary createTertiary(int primaryKey, int secondaryKey, int tertiaryKey);

  protected Secondary addSecondary(int primaryKey, int secondaryKey) {
    Secondary secondary = createSecondary(primaryKey, secondaryKey);
    layout.addSecondary(primaryKey, secondaryKey, secondary);
    return secondary;
  }

  protected Tertiary addTetiary(int primaryKey, int secondaryKey, int tertiaryKey) {
    Tertiary tertiary = createTertiary(primaryKey, secondaryKey, tertiaryKey);
    layout.addTertiary(primaryKey, secondaryKey, tertiaryKey, tertiary);
    return tertiary;
  }
}
