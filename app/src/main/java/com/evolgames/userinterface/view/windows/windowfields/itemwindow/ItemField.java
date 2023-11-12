package com.evolgames.userinterface.view.windows.windowfields.itemwindow;

import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ItemWindowController;
import com.evolgames.userinterface.sections.basic.SecondaryButtonField;
import com.evolgames.userinterface.view.windows.WindowPartIdentifier;

public class ItemField extends SecondaryButtonField {
  private final int modelId;
  private boolean visibleFields;

  public ItemField(int primaryKey, int modelId, ItemWindowController controller) {
    super(primaryKey, controller.getItemCounter().getAndIncrement(), controller);
    this.modelId = modelId;
    setWindowPartIdentifier(WindowPartIdentifier.WINDOW_BODY);
  }

  public void showFields() {
    this.visibleFields = true;
  }

  public void hideFields() {
    this.visibleFields = false;
  }

  @Override
  public void setVisible(boolean visible) {
    super.setVisible(visible);
    if (visible) {
      if (visibleFields) {
        this.showFields();
      } else {
        this.hideFields();
      }
    }
  }

  public int getModelId() {
    return modelId;
  }
}
