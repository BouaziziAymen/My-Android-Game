package com.evolgames.userinterface.control.behaviors;

import com.evolgames.userinterface.control.behaviors.actions.Action;
import com.evolgames.userinterface.control.validators.TextFieldValidator;
import com.evolgames.userinterface.control.windowcontrollers.AdvancedWindowController;
import com.evolgames.userinterface.view.inputs.Keyboard;
import com.evolgames.userinterface.view.inputs.TextField;

import org.andengine.input.touch.TouchEvent;

public abstract class TextFieldBehavior<C extends AdvancedWindowController<?>> extends ErrorClickableBehavior<C> {
    private final TextField<C> textField;
    private String lastValidTextString = "";
    private int count = 0;
    private boolean showPrompt = true;
    private final Keyboard.KeyboardType keyboardType;
    private boolean selected = false;
    private TextFieldValidator validator;
    private String textString = "";
    private boolean resetText = false;
    private Action releaseAction;


    protected TextFieldBehavior(C controller, TextField<C> textField, Keyboard.KeyboardType keyboardType) {
        super(controller, textField);
        this.textField = textField;
        this.keyboardType = keyboardType;
    }

    protected TextFieldBehavior(C controller, TextField<C> textField, Keyboard.KeyboardType keyboardType, TextFieldValidator validator) {
        this(controller, textField, keyboardType);
        this.validator = validator;
    }

    protected TextFieldBehavior(C controller, TextField<C> textField, Keyboard.KeyboardType keyboardType, TextFieldValidator validator, boolean resetText) {
        this(controller, textField, keyboardType, validator);
        this.resetText = resetText;
    }

    public boolean isResetText() {
        return resetText;
    }

    public void setText(String textString) {
        if (validator == null || validator.checkText(textString)) {
            textField.getText().updateText(textString);
            this.textString = textString;
            onViewUpdated();
        } else {
            if (textField.getErrorDisplay() != null) {
                textField.getErrorDisplay().showError(validator.getErrorMessage());
                showError = true;
                updateWindowLayout();
            }
        }
    }

    public void setTextValidated(String textString) {
        if (validator == null || validator.checkText(textString)) {
            textField.getText().updateText(textString);
            this.textString = textString;
            setLastValidTextString(textString);
            onViewUpdated();
        }
    }


    public Keyboard.KeyboardType getKeyboardType() {
        return keyboardType;
    }

    protected abstract void informControllerTextFieldTapped();

    protected abstract void informControllerTextFieldReleased();

    public void release() {
        informControllerTextFieldReleased();
    }

    @Override
    public boolean processTouch(TouchEvent touchEvent, boolean touched) {
        if (textField.isInBounds(touchEvent.getX(), touchEvent.getY())) {
            if (touchEvent.isActionDown()) {
                if (!selected) {
                    informControllerTextFieldTapped();
                    selected = true;
                } else {
                    informControllerTextFieldReleased();
                    showPrompt = false;
                    selected = false;
                }
            }
            return true;
        }

        return false;
    }

    public String getLastValidTextString() {
        return lastValidTextString;
    }

    public void setLastValidTextString(String lastValidTextString) {
        this.lastValidTextString = lastValidTextString;
    }

    public TextFieldValidator getValidator() {
        return validator;
    }


    public void onStep() {
        super.onStep();
        if (!selected) return;
        count++;
        if (count == 30) {
            showPrompt = !showPrompt;
            setText();
            count = 0;
        }
    }


    private void setText() {
        if (showPrompt)
            textField.getText().updateText(textString + "-");
        else
            textField.getText().updateText(textString);
    }

    public String getTextString() {
        return textString;
    }

    public void setTextString(String s) {
        textString = s;
    }

    public void setSelected(boolean b) {
        if (!b) {
            showPrompt = false;
            setText();
        }
        this.selected = b;
    }

    public Action getReleaseAction() {
        return releaseAction;
    }

    public void setReleaseAction(Action action) {
        this.releaseAction = action;
    }

    public boolean validate() {
        String text = getTextString();
        boolean validation = validator.validate(text);
        if (textField.getErrorDisplay() != null) {
            if (!validation) {
                textField.getErrorDisplay().showError(validator.getErrorMessage());
                showError = true;
            } else {
                textField.getErrorDisplay().hideError();
                showError = false;
            }
            updateWindowLayout();
        }
        return validation;

    }

}
