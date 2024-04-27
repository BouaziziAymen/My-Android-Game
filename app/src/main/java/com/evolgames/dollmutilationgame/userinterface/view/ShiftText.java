package com.evolgames.dollmutilationgame.userinterface.view;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.dollmutilationgame.userinterface.control.behaviors.ShiftTextBehavior;
import com.evolgames.dollmutilationgame.userinterface.control.windowcontrollers.AdvancedWindowController;
import com.evolgames.dollmutilationgame.userinterface.view.basics.Container;
import com.evolgames.dollmutilationgame.userinterface.view.basics.Text;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.Button;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.TypeABoard;

public class ShiftText<C extends AdvancedWindowController<?>> extends Container
        implements Temporal {

    private final Text movingText;
    private final TypeABoard board;
    private final ShiftTextBehavior<C> shiftTextBehavior;
    private String text = "";

    public ShiftText(float pX, float pY, int length, C controller) {
        super(pX, pY);
        board = new TypeABoard(0, 0, length, true);
        board.setBounds(null);
        Button<C> infoButton =
                new Button<>(
                        ResourceManager.getInstance().infoBlueButton, Button.ButtonType.Selector, true);
        infoButton.setPosition(5, 4f);
        infoButton.setBehavior(
                new ButtonBehavior<C>(controller, infoButton) {
                    @Override
                    public void informControllerButtonClicked() {
                        shiftTextBehavior.setPause(false);
                    }
                    @Override
                    public void informControllerButtonReleased() {
                        shiftTextBehavior.setPause(true);
                    }
                });
        infoButton.updateState(Button.State.PRESSED);
        addElement(infoButton);
        addElement(board);

        movingText = new Text(text, 2);
        addElement(movingText);
        movingText.setPosition(30, 14);
        shiftTextBehavior = new ShiftTextBehavior<>(this, controller);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        this.shiftTextBehavior.setShiftedText(text);
    }

    @Override
    public void onStep() {
        shiftTextBehavior.onStep();
    }

    public void update(String textString) {
        movingText.updateText(textString);
    }

    public float getUsefulWidth() {
        return board.getUsefulWidth();
    }
}
