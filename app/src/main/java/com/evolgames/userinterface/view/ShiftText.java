package com.evolgames.userinterface.view;

import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.behaviors.ShiftTextBehavior;
import com.evolgames.userinterface.control.windowcontrollers.AdvancedWindowController;
import com.evolgames.userinterface.view.basics.Text;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.inputs.TypeABoard;

public class ShiftText<C extends AdvancedWindowController<?>> extends TypeABoard implements Temporal{

    private final Text movingText;
    private String text = "";
    private final ShiftTextBehavior<C> shiftTextBehavior;

    public ShiftText(float pX, float pY,int length, C controller) {
        super(pX, pY, length, true);
        Button<C> infoButton = new Button<>(ResourceManager.getInstance().infoBlueButton, Button.ButtonType.Selector,true);
        addElement(infoButton);
        infoButton.setPosition(5,4);
        infoButton.setBehavior(new ButtonBehavior<C>(controller,infoButton) {
            @Override
            public void informControllerButtonClicked() {

            }

            @Override
            public void informControllerButtonReleased() {

            }
        });

         movingText = new Text(text,2);

        addElement(movingText);
        movingText.setPosition(30,14);
        shiftTextBehavior = new ShiftTextBehavior<>(this, controller);
    }



    public void setText(String text){
        this.text = text;
        this.shiftTextBehavior.setShiftedText(text);
    }

    public String getText() {
        return text;
    }

    @Override
    public void onStep() {
        shiftTextBehavior.onStep();
    }

    public void update(String textString) {
        movingText.updateText(textString);
    }
}
