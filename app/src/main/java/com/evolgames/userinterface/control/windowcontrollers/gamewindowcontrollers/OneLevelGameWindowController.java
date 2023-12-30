package com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers;


import com.evolgames.userinterface.control.windowcontrollers.OneLevelSectionedAdvancedWindowController;
import com.evolgames.userinterface.view.sections.basic.PrimaryButtonField;
import com.evolgames.userinterface.view.sections.basic.SecondaryButtonField;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.layouts.OneLevelSectionLayout;
import com.evolgames.userinterface.view.windows.AbstractSectionedAdvancedWindow;

public class OneLevelGameWindowController<
        W extends AbstractSectionedAdvancedWindow<OneLevelSectionLayout<Primary, Secondary>>,
        Primary extends PrimaryButtonField,
        Secondary extends SecondaryButtonField>
    extends OneLevelSectionedAdvancedWindowController<W, Primary, Secondary> {
  protected Primary selectedPrimaryField;
  protected Secondary selectedSecondaryField;

  @Override
  public void onPrimaryButtonClicked(Primary primary) {
    super.onPrimaryButtonClicked(primary);
    selectedPrimaryField = primary;
    for (int i = 0; i < window.getLayout().getPrimariesSize(); i++) {
      Primary otherPrimary = window.getLayout().getPrimaryByIndex(i);
      if (otherPrimary != null)
        if (otherPrimary != primary) {
          otherPrimary.getControl().updateState(Button.State.NORMAL);
          onPrimaryButtonReleased(otherPrimary);
          for (int j = 0;
              j < window.getLayout().getSecondariesSize(otherPrimary.getPrimaryKey());
              j++) {
            Secondary secondary =
                window.getLayout().getSecondaryByIndex(otherPrimary.getPrimaryKey(), j);
            secondary.getControl().updateState(Button.State.NORMAL);
            onSecondaryButtonReleased(secondary);
          }
        }
    }
  }

  @Override
  public void onPrimaryButtonReleased(Primary primary) {
    super.onPrimaryButtonReleased(primary);
    if (selectedPrimaryField == primary) {
      selectedPrimaryField = null;
    }
  }

  @Override
  public void onSecondaryButtonClicked(Secondary secondary) {
    super.onSecondaryButtonClicked(secondary);

    for (int i = 0; i < window.getLayout().getSecondariesSize(secondary.getPrimaryKey()); i++) {
      Secondary otherSecondary =
          window.getLayout().getSecondaryByIndex(secondary.getPrimaryKey(), i);
      if (otherSecondary != secondary) {
        otherSecondary.getControl().release();
        onSecondaryButtonReleased(otherSecondary);
      }
    }
    secondary.getControl().click();
  }

  @Override
  public void onSecondaryButtonReleased(Secondary secondary) {
    super.onSecondaryButtonReleased(secondary);
    if (secondary == selectedSecondaryField) {
      selectedSecondaryField = null;
      secondary.getControl().release();
    }
  }

  @Override
  public void onPrimaryAdded(Primary bodyField) {
    super.onPrimaryAdded(bodyField);
  }

  @Override
  public void onSecondaryAdded(Secondary secondary) {
    super.onSecondaryAdded(secondary);
  }
}
