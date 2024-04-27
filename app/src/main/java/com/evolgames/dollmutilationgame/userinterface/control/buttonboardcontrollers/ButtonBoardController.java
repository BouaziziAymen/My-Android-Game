package com.evolgames.dollmutilationgame.userinterface.control.buttonboardcontrollers;

import android.util.Log;

import com.evolgames.dollmutilationgame.userinterface.view.basics.Element;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.Button;
import com.evolgames.dollmutilationgame.userinterface.view.layouts.ButtonBoard;
import com.evolgames.dollmutilationgame.userinterface.control.Controller;

public class ButtonBoardController extends Controller {
    protected ButtonBoard buttonBoard;
    private boolean active;

    ButtonBoardController(ButtonBoard buttonBoard) {
        this.buttonBoard = buttonBoard;
    }

    public void openBoard() {
        buttonBoard.setVisible(true);
    }

    public void closeBoard() {
        buttonBoard.setVisible(false);
    }

    public void releaseButtons() {
        for (Element e : buttonBoard.getContents()) {
            Button<?> otherButton = (Button<?>) e;
            otherButton.updateState(Button.State.NORMAL);
        }
    }

    void onButtonClicked(Button button) {
        for (Element e : buttonBoard.getContents()) {
            Button otherButton = (Button) e;
            Log.e("onButtonClicked", otherButton.getAbsoluteX() + "/" + otherButton.getAbsoluteY());
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

    public void setActive(boolean active) {
        this.active = active;
        if (!active) {
            for (int i = 0; i < buttonBoard.getSize(); i++) {
                Button<? extends Controller> button = buttonBoard.getButtonAtIndex(i);
                button.updateState(Button.State.DISABLED);
            }
        } else {
            for (int i = 0; i < buttonBoard.getSize(); i++) {
                Button<? extends Controller> button = buttonBoard.getButtonAtIndex(i);
                button.updateState(Button.State.NORMAL);
            }
        }
    }


    public void setTemporarilyActive(boolean active) {
        if (!active) {
            for (int i = 0; i < buttonBoard.getSize(); i++) {
                Button<? extends Controller> button = buttonBoard.getButtonAtIndex(i);
                button.updateState(Button.State.DISABLED);
            }
        } else {
            if (this.active) {
                for (int i = 0; i < buttonBoard.getSize(); i++) {
                    Button<? extends Controller> button = buttonBoard.getButtonAtIndex(i);
                    button.updateState(Button.State.NORMAL);
                }
            }
        }
    }
}
