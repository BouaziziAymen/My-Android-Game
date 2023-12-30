package com.evolgames.userinterface.view.windows.gamewindows;

import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.OptionsWindowController;
import com.evolgames.userinterface.view.sections.basic.SimplePrimary;
import com.evolgames.userinterface.view.sections.basic.SimpleSecondary;
import com.evolgames.userinterface.view.sections.basic.SimpleTertiary;
import com.evolgames.userinterface.view.ShiftText;
import com.evolgames.userinterface.view.basics.Text;
import com.evolgames.userinterface.view.layouts.LinearLayout;
import com.evolgames.userinterface.view.layouts.TwoLevelSectionLayout;
import com.evolgames.userinterface.view.windows.AbstractTwoLevelSectionedAdvancedWindow;
import com.evolgames.userinterface.view.windows.WindowPartIdentifier;

public class OptionsWindow
    extends AbstractTwoLevelSectionedAdvancedWindow<
        SimplePrimary<?>, SimpleSecondary<?>, SimpleTertiary<?>> {

  public OptionsWindow(float pX, float pY, OptionsWindowController controller) {
    super(pX, pY, 5, 6, false, controller);
    Text text = new Text("Settings:", 2);
    LinearLayout mainField = new LinearLayout(LinearLayout.Direction.Horizontal);
    mainField.addToLayout(text);
    text.setPadding(20);
    layout.addDummySection(mainField);
    mainField.setPadding(5);

    addShiftingText(controller);

    controller.init();
  }

  private void addShiftingText(OptionsWindowController controller) {
    ShiftText<OptionsWindowController> shiftingText =
        new ShiftText<>(8, 32 * 5 - 12 - 5, 6, controller);
    addElement(shiftingText);
    shiftingText.setDepth(3);
    shiftingText.setWindowPartIdentifier(WindowPartIdentifier.WINDOW_BODY);
    shiftingText.setText(
        "Hi humans of the underworld! Today I would like to tell you about an experience. THIS IS IMPORTANT!!");
  }

  @Override
  protected TwoLevelSectionLayout<SimplePrimary<?>, SimpleSecondary<?>, SimpleTertiary<?>>
      createLayout() {
    return new TwoLevelSectionLayout<>(
        12, getLocalVisibilitySup(), LinearLayout.Direction.Vertical);
  }

  @Override
  public SimplePrimary createPrimary(int primaryKey) {
    return null;
  }

  @Override
  public SimpleSecondary createSecondary(int primaryKey, int secondaryKey) {
    return null;
  }

  @Override
  public SimpleTertiary createTertiary(int primaryKey, int secondaryKey, int tertiaryKey) {
    return null;
  }

  public void addPrimary(SimplePrimary primaryInterface) {
    getLayout().addPrimary(primaryInterface.getPrimaryKey(), primaryInterface, false);
  }

  public void removePrimary(int primaryKey) {
    getLayout().removePrimary(primaryKey);
  }

  public void addSecondary(SimpleSecondary secondary) {
    getLayout().addSecondary(secondary.getPrimaryKey(), secondary.getSecondaryKey(), secondary);
  }
}
