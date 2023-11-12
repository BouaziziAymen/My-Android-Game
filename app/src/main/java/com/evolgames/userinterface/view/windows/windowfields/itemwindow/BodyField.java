package com.evolgames.userinterface.view.windows.windowfields.itemwindow;

import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ItemWindowController;
import com.evolgames.userinterface.sections.basic.PrimaryButtonField;
import com.evolgames.userinterface.view.windows.WindowPartIdentifier;

public class BodyField extends PrimaryButtonField {

  public BodyField(int bodyFieldKey, ItemWindowController controller) {
    super(bodyFieldKey, controller);
    setWindowPartIdentifier(WindowPartIdentifier.WINDOW_BODY);

    setPadding(5f);
  }
}
