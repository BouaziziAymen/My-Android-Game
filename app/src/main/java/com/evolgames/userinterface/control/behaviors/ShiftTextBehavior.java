package com.evolgames.userinterface.control.behaviors;

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
        shiftText.update(shiftedText);
    }
    public void onStep(){
        if(shiftedText!=null && !shiftedText.isEmpty()) {
            step++;
            if (step % 30 == 0) {
                shiftedText = shiftedText.substring(1) + shiftedText.charAt(0);
                updateText();
            }
        }
    }

    public void setShiftedText(String text) {
        shiftedText = text;
    }
}
