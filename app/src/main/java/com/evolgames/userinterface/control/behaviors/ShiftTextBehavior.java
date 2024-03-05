package com.evolgames.userinterface.control.behaviors;

import com.evolgames.activity.ResourceManager;
import com.evolgames.userinterface.control.windowcontrollers.AdvancedWindowController;
import com.evolgames.userinterface.view.ShiftText;

public class ShiftTextBehavior<C extends AdvancedWindowController<?>> extends Behavior<C> {

  private final ShiftText<C> shiftText;
  private String shiftedText;
  private int step = 0;

  public ShiftTextBehavior(ShiftText<C> shiftText, C controller) {
    super(controller);
    this.shiftText = shiftText;
    this.shiftedText = shiftText.getText();
  }

  private void updateText() {
    shiftText.update(computeVisibleText());
  }

  public void onStep() {
    if (shiftedText != null && !shiftedText.isEmpty()) {
      step++;
      if (step % 30 == 0) {
        shiftedText = shiftedText.substring(1) + shiftedText.charAt(0);
        updateText();
      }
    }
  }

  private String computeVisibleText() {
    int i = 0;
    int textLength = shiftedText.length();
    StringBuilder visibleText = new StringBuilder();
    while (i < textLength) {
      visibleText.append(shiftedText.charAt(i));
      if (ResourceManager.getInstance().getFontWidth(2, visibleText.toString())
          > shiftText.getUsefulWidth()) {
        break;
      }
      i++;
    }
    return visibleText.toString();
  }

  public void setShiftedText(String text) {
    shiftedText = text;
  }
}
