package com.evolgames.userinterface.view.windows.gamewindows;

import com.evolgames.userinterface.control.windowcontrollers.LinearLayoutAdvancedWindowController;
import com.evolgames.userinterface.sections.basic.SimplePrimary;
import com.evolgames.userinterface.sections.basic.SimpleQuaternary;
import com.evolgames.userinterface.sections.basic.SimpleSecondary;
import com.evolgames.userinterface.sections.basic.SimpleTertiary;
import com.evolgames.userinterface.view.layouts.LinearLayout;
import com.evolgames.userinterface.view.layouts.ThreeLevelSectionLayout;
import com.evolgames.userinterface.view.windows.AbstractThreeLevelSectionedAdvancedWindow;

public abstract class SettingsWindow
    extends AbstractThreeLevelSectionedAdvancedWindow<
        SimplePrimary<?>, SimpleSecondary<?>, SimpleTertiary<?>, SimpleQuaternary<?>> {

  public SettingsWindow(
      float pX,
      float pY,
      int rows,
      int columns,
      LinearLayoutAdvancedWindowController<?> controller) {
    super(pX, pY, rows, columns, false, controller);
  }

  public void addPrimary(SimplePrimary<?> primaryInterface) {
    getLayout().addPrimary(primaryInterface.getPrimaryKey(), primaryInterface, false);
  }

  public void addSecondary(SimpleSecondary<?> secondaryElement) {
    getLayout()
        .addSecondary(
            secondaryElement.getPrimaryKey(), secondaryElement.getSecondaryKey(), secondaryElement);
  }

  public void addTertiary(SimpleTertiary<?> tertiaryElement) {
    getLayout()
        .addTertiary(
            tertiaryElement.getPrimaryKey(),
            tertiaryElement.getSecondaryKey(),
            tertiaryElement.getTertiaryKey(),
            tertiaryElement);
  }

  public void addQuaternary(SimpleQuaternary<?> quaternaryElement) {
    getLayout()
        .addQuaternary(
            quaternaryElement.getPrimaryKey(),
            quaternaryElement.getSecondaryKey(),
            quaternaryElement.getTertiaryKey(),
            quaternaryElement.getQuaternaryKey(),
            quaternaryElement);
  }

  @Override
  protected ThreeLevelSectionLayout<
          SimplePrimary<?>, SimpleSecondary<?>, SimpleTertiary<?>, SimpleQuaternary<?>>
      createLayout() {
    return new ThreeLevelSectionLayout<>(
        8, getLocalVisibilitySup(), LinearLayout.Direction.Vertical);
  }

  @Override
  public SimplePrimary<?> createPrimary(int primaryKey) {
    throw new UnsupportedOperationException();
  }

  @Override
  public SimpleSecondary<?> createSecondary(int primaryKey, int secondaryKey) {
    throw new UnsupportedOperationException();
  }

  @Override
  public SimpleTertiary<?> createTertiary(int primaryKey, int secondaryKey, int tertiaryKey) {
    throw new UnsupportedOperationException();
  }

  @Override
  public SimpleQuaternary<?> createQuaternary(
      int primaryKey, int secondaryKey, int tertiaryKey, int quaternaryKey) {
    throw new UnsupportedOperationException();
  }
}
