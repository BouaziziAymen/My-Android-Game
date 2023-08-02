package com.evolgames.userinterface.control.behaviors;

import android.util.Log;

import com.evolgames.userinterface.control.Controller;
import com.evolgames.userinterface.control.behaviors.actions.Action;
import com.evolgames.userinterface.view.inputs.Button;

import org.andengine.input.touch.TouchEvent;

public abstract class ButtonBehavior<C extends Controller> extends ClickableBehavior<C> {
    private final Button<C> button;
    private Action pushAction;
    private Action releaseAction;

    protected ButtonBehavior(C controller, Button<C> button) {
        super(controller);
        this.button = button;
    }


    public void setPushAction(Action pushAction) {
        this.pushAction = pushAction;
    }

    public void setReleaseAction(Action releaseAction) {
        this.releaseAction = releaseAction;
    }

    public abstract void informControllerButtonClicked();

    public abstract void informControllerButtonReleased();


    @Override
    public boolean processTouch(TouchEvent touchEvent, boolean touched) {
        if (button.getState() == Button.State.DISABLED) {
            return false;
        }

        if (touched) {
            if (button.getType() == Button.ButtonType.OneClick) {
                if (touchEvent.getAction() == TouchEvent.ACTION_UP || touchEvent.getAction() == TouchEvent.ACTION_CANCEL || touchEvent.getAction() == TouchEvent.ACTION_OUTSIDE) {
                    if (button.getState() == Button.State.PRESSED) {
                        button.updateState(Button.State.NORMAL);
                        informControllerButtonReleased();
                        if (releaseAction != null) {
                            releaseAction.performAction();
                        }
                    }
                }
            }
            return false;
        }
        boolean isInBounds = button.isInBounds(touchEvent.getX(), touchEvent.getY());


        if (button.getType() == Button.ButtonType.OneClick) {
            if (touchEvent.getAction() == TouchEvent.ACTION_DOWN) {
                if (button.getState() == Button.State.NORMAL && isInBounds) {
                    button.updateState(Button.State.PRESSED);
                    informControllerButtonClicked();
                    if (pushAction != null) pushAction.performAction();
                }
            } else if (touchEvent.getAction() == TouchEvent.ACTION_UP || touchEvent.getAction() == TouchEvent.ACTION_CANCEL || touchEvent.getAction() == TouchEvent.ACTION_OUTSIDE) {
                if (button.getState() == Button.State.PRESSED) {
                    button.updateState(Button.State.NORMAL);
                    informControllerButtonReleased();
                    if (releaseAction != null) releaseAction.performAction();
                }
            } else if (touchEvent.getAction() == TouchEvent.ACTION_MOVE) {
                if (button.getState() == Button.State.PRESSED) {
                    isInBounds = true;
                }
            }
        } else if (button.getType() == Button.ButtonType.Selector && isInBounds) {
            if (touchEvent.getAction() == TouchEvent.ACTION_UP) {
                if (button.getState() == Button.State.NORMAL) {
                    button.updateState(Button.State.PRESSED);
                    informControllerButtonClicked();
                    if (pushAction != null) pushAction.performAction();
                } else if (button.getState() == Button.State.PRESSED) {
                    button.updateState(Button.State.NORMAL);
                    informControllerButtonReleased();
                    if (releaseAction != null) releaseAction.performAction();
                }
            }

        }


        return isInBounds;
    }
}
