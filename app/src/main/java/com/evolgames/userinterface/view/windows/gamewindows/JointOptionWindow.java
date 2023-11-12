package com.evolgames.userinterface.view.windows.gamewindows;

import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.windowcontrollers.AdvancedWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.JointSettingsWindowController;
import com.evolgames.userinterface.sections.basic.SimplePrimary;
import com.evolgames.userinterface.sections.basic.SimpleSecondary;
import com.evolgames.userinterface.view.basics.Panel;
import com.evolgames.userinterface.view.layouts.LinearLayout;
import com.evolgames.userinterface.view.layouts.OneLevelSectionLayout;
import com.evolgames.userinterface.view.windows.AbstractOneLevelSectionedAdvancedWindow;
import com.evolgames.userinterface.view.windows.windowfields.SimpleTitleField;

public class JointOptionWindow
    extends AbstractOneLevelSectionedAdvancedWindow<SimplePrimary<?>, SimpleSecondary<?>> {
  public JointOptionWindow(float pX, float pY, JointSettingsWindowController controller) {
    super(pX, pY, 10, 7, true, controller);
    SimpleTitleField titleField = new SimpleTitleField("Joint Settings:");
    titleField.setPadding(5);
    addPrimary(titleField);
    controller.init();

    Panel mPanel = new Panel(0, -64, 4, true, true);

    mPanel
        .getCloseButton()
        .setBehavior(
            new ButtonBehavior<AdvancedWindowController<?>>(controller, mPanel.getCloseButton()) {
              @Override
              public void informControllerButtonClicked() {}

              @Override
              public void informControllerButtonReleased() {
                controller.onCancelSettings();
              }
            });

    mPanel
        .getAcceptButton()
        .setBehavior(
            new ButtonBehavior<AdvancedWindowController<?>>(controller, mPanel.getAcceptButton()) {
              @Override
              public void informControllerButtonClicked() {}

              @Override
              public void informControllerButtonReleased() {
                controller.onSubmitSettings();
              }
            });
    mPanel.setLowerBottomX(getWidth() / 2 - mPanel.getWidth() / 2);
    addElement(mPanel);
    createScroller();
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
  protected OneLevelSectionLayout<SimplePrimary<?>, SimpleSecondary<?>> createLayout() {
    return new OneLevelSectionLayout<>(
        12, getLocalVisibilitySup(), LinearLayout.Direction.Vertical);
  }

  public void addPrimary(SimplePrimary<?> primaryInterface) {
    getLayout().addPrimary(primaryInterface.getPrimaryKey(), primaryInterface, false);
  }

  public void removePrimary(int primaryKey) {
    getLayout().removePrimary(primaryKey);
  }

  public void addSecondary(SimpleSecondary<?> secondary) {
    getLayout().addSecondary(secondary.getPrimaryKey(), secondary.getSecondaryKey(), secondary);
  }
}
