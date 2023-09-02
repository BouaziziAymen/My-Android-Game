package com.evolgames.userinterface.control.buttonboardcontrollers;

import android.util.Log;

import com.evolgames.userinterface.control.Controller;
import com.evolgames.userinterface.view.basics.Element;
import com.evolgames.userinterface.view.inputs.Button;
import com.evolgames.userinterface.view.layouts.ButtonBoard;

public class ButtonBoardController extends Controller {
    protected ButtonBoard buttonBoard;

    ButtonBoardController(ButtonBoard buttonBoard) {
        this.buttonBoard = buttonBoard;
    }

    public void openBoard(){
        buttonBoard.setVisible(true);
    }
    public void closeBoard(){
        buttonBoard.setVisible(false);
    }
    public void releaseButtons() {
        for (Element e : buttonBoard.getContents()) {
            Button otherButton = (Button) e;
            otherButton.updateState(Button.State.NORMAL);
        }
    }

    void onButtonClicked(Button button) {
        for (Element e : buttonBoard.getContents()) {

            Button otherButton = (Button) e;
            Log.e("onButtonClicked", "" + otherButton.getAbsoluteX() + "/" + otherButton.getAbsoluteY());
            if (button != otherButton) {

                otherButton.updateState(Button.State.NORMAL);
            }

        }
    }

    void onButtonReleased(Button button) {

    }

    @Override
    public void init() {

    }
}
