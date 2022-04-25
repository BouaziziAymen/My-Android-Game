package com.evolgames.userinterface.control.validators;

import com.evolgames.userinterface.view.inputs.Keyboard;

public abstract class TextFieldValidator {
    protected Keyboard.KeyboardType inputType;
    protected int length;
    protected String errorMessage;

    public int getLength() {
        return length;
    }

    TextFieldValidator(int length) {
        this.length = length;
    }

    public abstract boolean checkText(String string);
    public abstract boolean validate(String string);
    public String getErrorMessage(){
      return errorMessage;
    }


}
