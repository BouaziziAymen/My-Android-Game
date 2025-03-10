package com.evolgames.dollmutilationgame.userinterface.control.behaviors;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.AdvancedWindowController;
import com.evolgames.dollmutilationgame.userinterface.view.ShiftText;

public class ShiftTextBehavior<C extends AdvancedWindowController<?>> extends Behavior<C> {

    private final ShiftText<C> shiftText;
    private String shiftedText;
    private int step = 0;
    private boolean pause;

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
            if (!pause&&step % 10 == 0) {
                shiftedText = shiftedText.substring(1) + shiftedText.charAt(0);
                updateText();
            }
        }
    }
    public void setPause(boolean pause){
        this.pause = pause;
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
